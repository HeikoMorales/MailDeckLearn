import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

	final static String DeckLearnMail = "decklearning@gmail.com"; // to address. It can be any like gmail, hotmail etc.
	final static String password = "Lh7aAdk$$L8yv^#"; // password for from mail address.
	String userMail = " ", userName = " "; // from address. As this is using Gmail SMTP.
	
	public MailSender() {
		
	}
	
	public void sendEmail(User user, List<Deck> decks) throws AddressException, MessagingException {
		
		this.userMail = user.getMail();
		this.userName = user.getUsername();
		
		System.out.println("Preparando el email a enviar");
		Message message = prepareMessage(getSession(), decks);
		Transport.send(message);

		System.out.println("el mensage ha sido enviado");

	}

	private Message prepareMessage(Session session, List<Deck> decks)
			throws AddressException, MessagingException {

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(DeckLearnMail));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(userMail));
		
		message.setSubject("It's time to study!");
		message.setText(createBodyMessage(decks));
		return message;
	}

	private String createBodyMessage(List<Deck> decks) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Deck deck : decks) {
			stringBuilder.append(deck.getTitle() + "\n");
			stringBuilder.append("\t"+deck.getDescription() + "\n");
		}

		return stringBuilder.toString();
	}

	private Session getSession() {

		Properties property = new Properties();
		property.setProperty("mail.smtp.auth", "true");
		property.setProperty("mail.smtp.starttls.enable", "true");
		property.setProperty("mail.smtp.host", "smtp.gmail.com");
		property.setProperty("mail.smtp.port", "587");
		property.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
		
		Session session = Session.getInstance(property, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(DeckLearnMail, password);
			}
		});
		
		System.out.println("sesion creada");
		return session;
	}

	
	
}