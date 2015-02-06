package ch.romix.wichtel.integration;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import ch.romix.wichtel.WichtelApp;
import ch.romix.wichtel.rest.WichtelEvent;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WichtelApp.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class EventResourceTest {

  @Value("${local.server.port}")
  private int port;

  private RestTemplate restTemplate = new RestTemplate();

  @Test
  public void testAddingAndReadingWichtelEntry() {
    WichtelEvent eventToPost = new WichtelEvent();
    eventToPost.setName("Weihnachten 2014");
    ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/rest/event/", eventToPost, Void.class);
    assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    URI locationOfNewEvent = response.getHeaders().getLocation();
    assertNotNull(locationOfNewEvent);
    WichtelEvent eventRead = restTemplate.getForObject(locationOfNewEvent, WichtelEvent.class);
    assertThat(eventRead.getName(), is(eventToPost.getName()));
  }
}
