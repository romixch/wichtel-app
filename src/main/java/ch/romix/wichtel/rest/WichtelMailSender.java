package ch.romix.wichtel.rest;

import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ch.romix.wichtel.model.WichtelEntity;

@Service
@Transactional
public class WichtelMailSender {

  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private EntityManager em;

  @Async
  public void sendMail(UUID wichtelId) {
    WichtelEntity wichtel = em.find(WichtelEntity.class, wichtelId);
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    wichtel.setSendError(null);
    try {
      helper.setFrom("wichtelapp.mailer@gmail.com");
      helper.setTo(wichtel.getEmail());
      helper.setSubject("Wichteln für " + wichtel.getEvent().getName());
      StringBuilder text = new StringBuilder();
      text.append("Hallo ").append(wichtel.getName()).append("\n\n");
      text.append("Hier schreibt deine Wichtel-App von https://wichtel-app.herokuapp.com/. Jemand hat dich zum Wichteln eingeladen.\n\n");
      text.append("Du darfst für ").append(wichtel.getWichtelTo().getName()).append(" wichteln.\n\n");
      text.append("Noch Fragen? Antworte einfach auf dieses Mail und stell deine Frage.\n\n");
      text.append("Liebe Grüsse\nDeine Wichtel-App");
      helper.setText(text.toString());
      mailSender.send(message);
    } catch (Exception e) {
      String msg = e.getLocalizedMessage();
      if (msg == null) {
        msg = e.getMessage();
      }
      if (msg == null) {
        msg = e.toString();
      }
      wichtel.setSendError(msg);
    }
    wichtel.setMailSent(true);
    em.persist(wichtel);
  }
}
