package ch.romix.wichtel.integration;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import ch.romix.wichtel.WichtelApp;
import ch.romix.wichtel.model.Wichtel;
import ch.romix.wichtel.model.WichtelEvent;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WichtelApp.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class WichtelResourceTest {

  @Value("${local.server.port}")
  private int port;

  private RestTemplate restTemplate = new RestTemplate();

  @Test
  public void testAddingWichtels() {
    WichtelEvent event = new WichtelEvent();
    event.setName("MyEvent");
    ResponseEntity<Void> eventResponse = restTemplate.postForEntity("http://localhost:" + port + "/rest/event/", event, Void.class);
    URI eventLocation = eventResponse.getHeaders().getLocation();
    WichtelEvent getEvent = restTemplate.getForObject(eventLocation, WichtelEvent.class);
    Link wichtelLink = getEvent.getLink("wichtel");
    assertNotNull(wichtelLink);
    URI wichtel1Location = postWichtel(wichtelLink, "w1@example.com", "w1");
    assertThat(wichtel1Location.getPath(), containsString("wichtel"));
    URI wichtel2Location = postWichtel(wichtelLink, "wichtel2@example.com", "Wichtel2");
    assertThat(wichtel2Location.getPath(), containsString("wichtel"));
    @SuppressWarnings("unchecked")
    List<Map<String, String>> wichtelLinks = restTemplate.getForObject(wichtelLink.getHref(), List.class);
    assertThat(wichtelLinks.size(), is(2));
    HashSet<Object> expectedLocations = new HashSet<>();
    expectedLocations.add(wichtel1Location.toString());
    expectedLocations.add(wichtel2Location.toString());
    assertTrue(expectedLocations.contains(wichtelLinks.get(0).get("href")));
    assertTrue(expectedLocations.contains(wichtelLinks.get(1).get("href")));
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
