package ch.romix.wichtel.rest;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import ch.romix.wichtel.model.Wichtel;
import ch.romix.wichtel.model.WichtelEvent;

public class WichtelMailSender {

  @Autowired
  private JavaMailSender mailSender;

  public void send(WichtelEvent event, Wichtel mailReceiver, Wichtel toBeWichteled) {

    System.out.println("mailSender: " + mailSender);

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    try {
      helper.setFrom("wichtelapp.mailer@gmail.com");
      helper.setTo(mailReceiver.getEmail());
      helper.setSubject("Wichteln für " + event.getName());
      StringBuilder text = new StringBuilder();
      text.append("Hallo ").append(mailReceiver.getName()).append("\n\n");
      text.append("Hier schreibt deine Wichtel-App von https://wichtel-app.herokuapp.com/. Jemand hat dich zum Wichteln eingeladen.\n\n");
      text.append("Du darfst für ").append(toBeWichteled.getName()).append(" wichteln.\n\n");
      text.append("Ist dir nicht ganz klar, was das soll? Antworte einfach auf dieses Mail und stell deine Frage.\n\n");
      text.append("Liebe Grüsse\nDeine Wichtel-App");
      helper.setText(text.toString());
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    mailSender.send(message);
  }
}
