package ch.romix.wichtel.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

import ch.romix.wichtel.model.MailStates;
import ch.romix.wichtel.model.WichtelEntity;
import ch.romix.wichtel.model.WichtelEvent;
import ch.romix.wichtel.model.WichtelEventEntity;
import ch.romix.wichtel.model.WichtelMailState;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@Transactional
public class WichtelEventController {

  @Autowired
  private WichtelMailSender wichtelMailSender;
  @Autowired
  private EntityManager em;

  @RequestMapping(value = "/rest/event", method = RequestMethod.POST)
  public HttpEntity<Void> addEvent(@RequestBody WichtelEvent event) {
    WichtelEventEntity eventEntity = new WichtelEventEntity();
    eventEntity.setId(UUID.randomUUID());
    eventEntity.setName(event.getName());
    em.persist(eventEntity);
    URI uri = linkTo(methodOn(getClass()).getWichtelEvent(eventEntity.getId())).toUri();
    return ResponseEntity.created(uri).build();
  }

  @RequestMapping(value = "/rest/event", method = RequestMethod.GET)
  public Collection<Link> getWichtelEventLinks() {
    ArrayList<Link> links = new ArrayList<>();
    TypedQuery<WichtelEventEntity> query = em.createNamedQuery(WichtelEventEntity.ALL, WichtelEventEntity.class);
    List<WichtelEventEntity> events = query.getResultList();
    for (WichtelEventEntity event : events) {
      Link link = linkTo(methodOn(getClass()).getWichtelEvent(event.getId())).withRel("event");
      links.add(link);
    }
    return links;
  }

  @RequestMapping(value = "/rest/event/{resId}", method = RequestMethod.GET)
  public HttpEntity<WichtelEvent> getWichtelEvent(@PathVariable("resId") UUID resId) {
    WichtelEventEntity eventEntity = em.find(WichtelEventEntity.class, resId);
    Link linkToWichtels = linkTo(methodOn(WichtelController.class).addWichtel(null, eventEntity.getId())).withRel("wichtel");
    Link linkToCompleted = linkTo(methodOn(WichtelEventController.class).completeWichtelEvent(eventEntity.getId())).withRel("completed");

    WichtelEvent event = new WichtelEvent();
    event.setName(eventEntity.getName());
    event.setCompleted(eventEntity.isCompleted());
    event.add(linkToWichtels);
    event.add(linkToCompleted);
    return new ResponseEntity<WichtelEvent>(event, HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/event/{resId}/completed", method = RequestMethod.PUT)
  public HttpEntity<Void> completeWichtelEvent(@PathVariable("resId") UUID resId) {
    WichtelEventEntity eventEntity = em.find(WichtelEventEntity.class, resId);
    WichtelAssigner.assign(eventEntity);
    wichtelMailSender.sendWichtelMailsAndComplete(eventEntity.getId());
    Link completionState = linkTo(methodOn(WichtelEventController.class).getWichtelEventCompletionState(resId)).withSelfRel();
    return ResponseEntity.accepted().location(URI.create(completionState.getHref())).build();
  }

  @RequestMapping(value = "/rest/event/{resId}/completed", method = RequestMethod.GET)
  public HttpEntity<MailStates> getWichtelEventCompletionState(@PathVariable("resId") UUID resId) {
    WichtelEventEntity eventEntity = em.find(WichtelEventEntity.class, resId);
    Set<WichtelEntity> wichtelList = eventEntity.getWichtels();
    List<WichtelMailState> mailStates = wichtelList.stream().map(w -> new WichtelMailState(w)).collect(Collectors.toList());
    return new ResponseEntity<MailStates>(new MailStates(mailStates), HttpStatus.OK);
  }

}
