package ch.romix.wichtel.integration;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import ch.romix.wichtel.WichtelApp;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WichtelApp.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class WebpageTest {

  @Value("${local.server.port}")
  private int port;

  @Test
  public void testIfIndexHtmlIsServed() throws Exception {
    RestTemplate template = new TestRestTemplate();
    ClientHttpRequest request = template.getRequestFactory().createRequest(new URI("http://localhost:" + port + "/"), HttpMethod.GET);
    ClientHttpResponse response = request.execute();
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
  }
}
