package ch.romix.wichtel.rest;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Future;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import ch.romix.wichtel.model.WichtelEntity;
import ch.romix.wichtel.model.WichtelEventEntity;

@Service
@Transactional
public class WichtelMailSender {

  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private EntityManager em;

  @Async
  public Future<Void> sendWichtelMailsAndComplete(UUID eventId) {
    WichtelEventEntity event = em.find(WichtelEventEntity.class, eventId);
    Set<WichtelEntity> wichtelList = event.getWichtels();
    wichtelList.forEach(w -> w.setMailSent(false));
    for (WichtelEntity wichtel : wichtelList) {
      send(wichtel);
    }
    event.setCompleted(true);
    em.persist(event);
    return new AsyncResult<Void>(null);
  }

  private void send(WichtelEntity wichtel) {
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
      text.append("Ist dir nicht ganz klar, was das soll? Antworte einfach auf dieses Mail und stell deine Frage.\n\n");
      text.append("Liebe Grüsse\nDeine Wichtel-App");
      helper.setText(text.toString());
      mailSender.send(message);
    } catch (Exception e) {
      wichtel.setSendError(e.getLocalizedMessage());
    }
    wichtel.setMailSent(true);
  }
}
