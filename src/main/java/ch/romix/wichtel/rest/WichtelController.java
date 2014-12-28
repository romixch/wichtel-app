package ch.romix.wichtel.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.romix.wichtel.model.Wichtel;
import ch.romix.wichtel.model.WichtelEvent;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class WichtelController {

  @RequestMapping(value = "/rest/event/{eid}/wichtel", method = RequestMethod.POST)
  public HttpEntity<Void> addWichtel(@RequestBody Wichtel wichtel, @PathVariable("eid") Long eventId) {
    long id = (long) (Math.random() * Long.MAX_VALUE);
    wichtel.setResId(id);
    WichtelEvent event = WichtelEventController.events.get(eventId);
    event.getWichtels().add(wichtel);
    URI location = linkTo(methodOn(getClass()).getWichtel(eventId, wichtel.getResId())).toUri();
    return ResponseEntity.created(location).build();
  }

  @RequestMapping(value = "/rest/event/{eid}/wichtel", method = RequestMethod.GET)
  public HttpEntity<Collection<Link>> getWichtelLinks(@PathVariable("eid") Long eventId) {
    ArrayList<Link> links = new ArrayList<>();
    WichtelEvent event = WichtelEventController.events.get(eventId);
    for (Wichtel wichtel : event.getWichtels()) {
      Link link = linkTo(methodOn(getClass()).getWichtel(eventId, wichtel.getResId())).withRel("wichtel");
      links.add(link);
    }
    return new ResponseEntity<Collection<Link>>(links, HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/event/{eid}/wichtel/{id}", method = RequestMethod.GET)
  public HttpEntity<Wichtel> getWichtel(@PathVariable("eid") Long eventId, @PathVariable("id") Long wichtelId) {
    WichtelEvent event = WichtelEventController.events.get(eventId);
    for (Wichtel wichtel : event.getWichtels()) {
      if (wichtel.getId().equals(wichtelId)) {
        return ResponseEntity.ok(wichtel);
      }
    }
    return new ResponseEntity<Wichtel>(HttpStatus.NOT_FOUND);
  }
}
