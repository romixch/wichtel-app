package ch.romix.wichtel.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.romix.wichtel.model.WichtelEvent;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class WichtelEventController {

  final static Map<Long, WichtelEvent> events = new ConcurrentHashMap<>();

  @RequestMapping(value = "/rest/event", method = RequestMethod.POST)
  public HttpEntity<Void> addEvent(@RequestBody WichtelEvent event) {
    long id = (long) (Math.random() * Long.MAX_VALUE);
    event.setResId(id);
    events.put(event.getResId(), event);
    URI uri = linkTo(methodOn(getClass()).getWichtelEvent(event.getResId())).toUri();
    return ResponseEntity.created(uri).build();
  }

  @RequestMapping(value = "/rest/event", method = RequestMethod.GET)
  public Collection<Link> getWichtelEventLinks() {
    ArrayList<Link> links = new ArrayList<>();
    for (WichtelEvent wichtel : events.values()) {
      Link link = linkTo(methodOn(getClass()).getWichtelEvent(wichtel.getResId())).withRel("event");
      links.add(link);
    }
    return links;
  }

  @RequestMapping(value = "/rest/event/{id}", method = RequestMethod.GET)
  public HttpEntity<WichtelEvent> getWichtelEvent(@PathVariable("id") Long id) {
    WichtelEvent event = events.get(id);
    Link link = linkTo(methodOn(WichtelController.class).addWichtel(null, event.getResId())).withRel("wichtel");
    event.removeLinks();
    event.add(link);
    return new ResponseEntity<WichtelEvent>(event, HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/event/{id}/completed", method = RequestMethod.PUT)
  public HttpEntity<Void> completeWichtelEvent(@PathVariable("id") Long id) {
    WichtelEvent event = events.get(id);
    complete(event);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  private void complete(WichtelEvent event) {
    if (!event.isCompleted()) {
      while (!isCorrectlyAssigned(event)) {
        assignWichtels(event);
      }
      event.setCompleted(true);
    }
    sendWichtelMails(event);
  }

  private boolean isCorrectlyAssigned(WichtelEvent event) {
    Set<Long> availableWichtel = new HashSet<Long>();
    event.getWichtels().forEach(w -> availableWichtel.add(w.getResId()));
    event.getWichtels().forEach(w -> availableWichtel.remove(w.getWichtelTo()));
    return availableWichtel.isEmpty();
  }

  private void assignWichtels(WichtelEvent event) {
    List<Long> availableWichtel = new ArrayList<>();
    event.getWichtels().forEach(w -> availableWichtel.add(w.getResId()));
    event.getWichtels().forEach(w -> {
      int wichtelTo = (int) Math.random() * availableWichtel.size();
      availableWichtel.get(wichtelTo);
      availableWichtel.remove(wichtelTo);
    });
  }

  private void sendWichtelMails(WichtelEvent event) {
    System.out.println("Send wichtel mails for " + event.getName() + " which has " + event.getWichtels().size() + " wichtels.");
  }
}
