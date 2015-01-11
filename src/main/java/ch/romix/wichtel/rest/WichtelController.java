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
import ch.romix.wichtel.model.WichtelEntity;
import ch.romix.wichtel.model.WichtelEventEntity;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@Transactional
public class WichtelController {

  @Autowired
  private EntityManager em;

  @RequestMapping(value = "/rest/event/{eid}/wichtel", method = RequestMethod.POST)
  public HttpEntity<Void> addWichtel(@RequestBody Wichtel wichtel, @PathVariable("eid") UUID eventId) {
    WichtelEventEntity eventEntity = em.find(WichtelEventEntity.class, eventId);
    WichtelEntity wichtelEntity = new WichtelEntity();
    wichtelEntity.setId(UUID.randomUUID());
    wichtelEntity.setEvent(eventEntity);
    wichtelEntity.setName(wichtel.getName());
    wichtelEntity.setEmail(wichtel.getEmail());
    em.persist(wichtelEntity);
    URI location = linkTo(methodOn(getClass()).getWichtel(wichtelEntity.getEvent().getId(), wichtelEntity.getId())).toUri();
    return ResponseEntity.created(location).build();
  }

  @RequestMapping(value = "/rest/event/{eid}/wichtel", method = RequestMethod.GET)
  public HttpEntity<Collection<Link>> getWichtelLinks(@PathVariable("eid") UUID eventId) {
    ArrayList<Link> links = new ArrayList<>();
    WichtelEventEntity event = em.find(WichtelEventEntity.class, eventId);
    if (event.isCompleted()) {
      return new ResponseEntity<Collection<Link>>(HttpStatus.FORBIDDEN);
    }
    for (WichtelEntity wichtel : event.getWichtels()) {
      Link link = linkTo(methodOn(getClass()).getWichtel(eventId, wichtel.getId())).withRel("wichtel");
      links.add(link);
    }
    return new ResponseEntity<Collection<Link>>(links, HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/event/{eid}/wichtel/{id}", method = RequestMethod.GET)
  public HttpEntity<Wichtel> getWichtel(@PathVariable("eid") UUID eventId, @PathVariable("id") UUID wichtelId) {
    WichtelEventEntity event = em.find(WichtelEventEntity.class, eventId);
    if (event.isCompleted()) {
      return new ResponseEntity<Wichtel>(HttpStatus.FORBIDDEN);
    }
    WichtelEntity wichtelEntity = em.find(WichtelEntity.class, wichtelId);
    if (wichtelEntity == null) {
      return new ResponseEntity<Wichtel>(HttpStatus.NOT_FOUND);
    } else {
      if (!wichtelEntity.getEvent().equals(event)) {
        return new ResponseEntity<Wichtel>(HttpStatus.FORBIDDEN);
      }
      Wichtel wichtel = new Wichtel();
      wichtel.setResId(wichtelEntity.getId());
      wichtel.setName(wichtelEntity.getName());
      wichtel.setEmail(wichtelEntity.getEmail());
      wichtel.setMailSent(wichtelEntity.isMailSent());
      wichtel.setSendError(wichtelEntity.getSendError());
      if (wichtelEntity.getWichtelTo() != null) {
        wichtel.setWichtelTo(wichtelEntity.getWichtelTo().getId());
      }
      return ResponseEntity.ok(wichtel);
    }
  }
}
