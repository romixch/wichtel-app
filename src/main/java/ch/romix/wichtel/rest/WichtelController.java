package ch.romix.wichtel.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
import ch.romix.wichtel.model.WichtelData;
import ch.romix.wichtel.model.WichtelEntity;
import ch.romix.wichtel.model.WichtelEvent;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@Transactional
public class WichtelController {

  @Autowired
  private EntityManager em;

  @RequestMapping(value = "/rest/event/{eid}/wichtel", method = RequestMethod.POST)
  public HttpEntity<Void> addWichtel(@RequestBody Wichtel wichtel, @PathVariable("eid") Long eventId) {
    WichtelEntity wichtelEntity = new WichtelEntity();
    wichtelEntity.setId(UUID.randomUUID());
    wichtelEntity.setEvent(null);
    wichtelEntity.setName(wichtel.getName());
    wichtelEntity.setEmail(wichtel.getEmail());
    em.persist(wichtelEntity);
    WichtelData.addWichtelToEvent(eventId, wichtel);
    URI location = linkTo(methodOn(getClass()).getWichtel(eventId, wichtel.getResId())).toUri();
    return ResponseEntity.created(location).build();
  }

  @RequestMapping(value = "/rest/event/{eid}/wichtel", method = RequestMethod.GET)
  public HttpEntity<Collection<Link>> getWichtelLinks(@PathVariable("eid") Long eventId) {
    ArrayList<Link> links = new ArrayList<>();
    WichtelEvent event = WichtelData.getEventByResId(eventId);
    if (event.isCompleted()) {
      return new ResponseEntity<Collection<Link>>(HttpStatus.FORBIDDEN);
    }
    for (Wichtel wichtel : WichtelData.getWichtelListByEventResId(eventId)) {
      Link link = linkTo(methodOn(getClass()).getWichtel(eventId, wichtel.getResId())).withRel("wichtel");
      links.add(link);
    }
    return new ResponseEntity<Collection<Link>>(links, HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/event/{eid}/wichtel/{id}", method = RequestMethod.GET)
  public HttpEntity<Wichtel> getWichtel(@PathVariable("eid") Long eventId, @PathVariable("id") UUID wichtelId) {
    WichtelEvent event = WichtelData.getEventByResId(eventId);
    if (event.isCompleted()) {
      return new ResponseEntity<Wichtel>(HttpStatus.FORBIDDEN);
    }
    Wichtel wichtel = WichtelData.getWichtelByEventAndWichtelResId(eventId, wichtelId);
    if (wichtel == null) {
      return new ResponseEntity<Wichtel>(HttpStatus.NOT_FOUND);
    } else {
      return ResponseEntity.ok(wichtel);
    }
  }
}
