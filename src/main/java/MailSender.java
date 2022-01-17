import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

	String deckLearnMail = " ";
	String password = " ";
	String userMail = " ", userName = " ";
	Session session;
	Transport transport;

	public MailSender() {
		try {
			loadProperties();
			session = getSession();
			transport = session.getTransport("smtp");
			transport.connect(deckLearnMail, password);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}

	private void loadProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("properties/mailsettings.properties")));
		deckLearnMail = properties.getProperty("MAIL");
		password = properties.getProperty("PASSWORD");
		//System.out.println(deckLearnMail + " " + password);
	}

	public void sendEmail(User user, List<Deck> decks) throws AddressException, MessagingException, UnsupportedEncodingException, FileNotFoundException {

		this.userMail = user.getMail();
		this.userName = user.getUsername();

		// System.out.println("Preparando el email a enviar");

		Message message = prepareMessage(session, decks);
		
		transport.sendMessage(message, message.getAllRecipients());
		
		//Transport.send(message);
		System.out.println("el mensage ha sido enviado");

	}

	private Message prepareMessage(Session session, List<Deck> decks)
			throws AddressException, MessagingException, UnsupportedEncodingException, FileNotFoundException {

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(deckLearnMail));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(userMail));

		BodyPart textPart = new MimeBodyPart();
		textPart.setContent(loadHTMLFile("files/DeckLearnMail.html", decks), "text/html");

		message.setSubject("It's time to study "+ userName +" !");
		
		MimeMultipart messageBody = new MimeMultipart();
		messageBody.addBodyPart(textPart);
		message.setContent(messageBody);
		return message;
	}

	private String loadHTMLFile(String path, List<Deck> decks) throws UnsupportedEncodingException, FileNotFoundException {
		StringBuilder sb = new StringBuilder();

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		
		try {
			String line = br.readLine();
			while (line != null) {
				sb.append(line).append("\n");
				//System.out.println(line);
				line = br.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String[] valores = sb.toString().split("[?]");

		StringBuilder sbli = new StringBuilder();
		for (Deck deck : decks) {
			sbli.append("<li style=\"margin:0 0 10px 30px;\">" + deck.getTitle() + ".</li> \n");
		}
		return (valores[0] + userName + valores[1] + sbli.toString()+ valores[2]);
	}

	private Session getSession() {

		Properties property = new Properties();
		property.setProperty("mail.smtp.auth", "true");
		property.setProperty("mail.smtp.starttls.enable", "true");
		property.setProperty("mail.smtp.host", "smtp.gmail.com");
		property.setProperty("mail.smtp.port", "587");
		property.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
		//property.setProperty("mail.debug", "true");

		Session session = Session.getInstance(property, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(deckLearnMail, password);
			}
		});

		//System.out.println("sesion creada");
		return session;
	}

    public void closeTransport() {
		try {
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
    }
}