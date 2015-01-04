package ch.romix.wichtel.rest;

import java.util.List;
import java.util.concurrent.Future;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import ch.romix.wichtel.model.Wichtel;
import ch.romix.wichtel.model.WichtelData;
import ch.romix.wichtel.model.WichtelEvent;

@Service
public class WichtelMailSender {

  @Autowired
  private JavaMailSender mailSender;

  @Async
  public Future<Void> sendWichtelMailsAndComplete(WichtelEvent event) {
    System.out.println("Send wichtel mails for " + event.getName() + " which has "
        + WichtelData.getWichtelListByEventResId(event.getResId()).size() + " wichtels:");
    List<Wichtel> wichtelList = WichtelData.getWichtelListByEventResId(event.getResId());
    wichtelList.forEach(w -> w.setMailSent(false));
    for (Wichtel wichtel : wichtelList) {
      Wichtel wichtelTo = WichtelData.getWichtelByEventAndWichtelResId(event.getResId(), wichtel.getWichtelTo());
      System.out.println(event.getName() + ": " + wichtel.getName() + " with E-Mail " + wichtel.getEmail() + " wichtels to "
          + wichtelTo.getName() + " (" + wichtelTo.getEmail() + ")");
      send(event, wichtel, wichtelTo);
    }
    event.setCompleted(true);
    return new AsyncResult<Void>(null);
  }

  private void send(WichtelEvent event, Wichtel mailReceiver, Wichtel toBeWichteled) {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    mailReceiver.setSendError(null);
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
      mailSender.send(message);
    } catch (Exception e) {
      mailReceiver.setSendError(e.getLocalizedMessage());
    }
    mailReceiver.setMailSent(true);
  }
}
