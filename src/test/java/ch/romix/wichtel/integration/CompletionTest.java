package ch.romix.wichtel.integration;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.mock_javamail.Mailbox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ch.romix.wichtel.WichtelApp;
import ch.romix.wichtel.model.Wichtel;
import ch.romix.wichtel.model.WichtelEvent;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WichtelApp.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class CompletionTest {

  @Value("${local.server.port}")
  private int port;

  private RestTemplate restTemplate = new RestTemplate();

  @Before
  public void setUp() {
    Mailbox.clearAll();
  }

  @Test
  public void testCompleteWichtelEvent() throws MessagingException, IOException {
    WichtelEvent event = createEvent("MyEvent");
    Link wichtelLink = event.getLink("wichtel");
    postWichtel(wichtelLink, "w1@example.com", "w1");
    postWichtel(wichtelLink, "wichtel2@example.com", "Wichtel2");
    Link completeLink = event.getLink("completed");
    assertNotNull(completeLink);
    restTemplate.put(completeLink.getHref(), Boolean.TRUE);
    assertSentMail("wichtelapp.mailer@gmail.com", "w1@example.com", "Wichteln für MyEvent", "Wichtel2");
    assertSentMail("wichtelapp.mailer@gmail.com", "wichtel2@example.com", "Wichteln für MyEvent", "w1");
    assertNoMoreMails("w1@example.com");
    assertNoMoreMails("wichtel2@example.com");
    try {
      restTemplate.getForObject(wichtelLink.getHref(), List.class);
      fail("asking wichtels must be prohibitted after completing.");
    } catch (HttpClientErrorException e) {
      assertThat(e.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
    // PUT a second time must not result in an exception
    restTemplate.put(completeLink.getHref(), Boolean.TRUE);
  }

  private void assertSentMail(String from, String to, String subject, String... containedInContent) throws AddressException,
      MessagingException, IOException {
    Mailbox mailbox = Mailbox.get(to);
    Message message = mailbox.get(0);
    assertThat(message.getFrom()[0].toString(), is(from));
    assertThat(message.getSubject(), is(subject));
    Arrays.stream(containedInContent).forEach(test -> {
      try {
        assertThat(message.getContent().toString(), Matchers.containsString(test));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    mailbox.remove(0);
  }

  private void assertNoMoreMails(String emailAddress) throws AddressException {
    Mailbox mailbox = Mailbox.get(emailAddress);
    assertTrue(mailbox.isEmpty());
  }

  private WichtelEvent createEvent(String eventName) {
    WichtelEvent event = new WichtelEvent();
    event.setName(eventName);
    ResponseEntity<Void> eventResponse = restTemplate.postForEntity("http://localhost:" + port + "/rest/event/", event, Void.class);
    URI eventLocation = eventResponse.getHeaders().getLocation();
    WichtelEvent getEvent = restTemplate.getForObject(eventLocation, WichtelEvent.class);
    return getEvent;
  }

  private URI postWichtel(Link wichtelLink, String email, String name) {
    Wichtel wichtel = new Wichtel();
    wichtel.setEmail(email);
    wichtel.setName(name);
    ResponseEntity<Void> wichtelResponse = restTemplate.postForEntity(wichtelLink.getHref(), wichtel, Void.class);
    URI location = wichtelResponse.getHeaders().getLocation();
    return location;
  }
}
