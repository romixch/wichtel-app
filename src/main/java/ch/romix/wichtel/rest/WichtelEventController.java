package ch.romix.wichtel.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
import ch.romix.wichtel.model.Wichtel;
import ch.romix.wichtel.model.WichtelData;
import ch.romix.wichtel.model.WichtelEvent;
import ch.romix.wichtel.model.WichtelMailState;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class WichtelEventController {

  @Autowired
  private WichtelMailSender wichtelMailSender;

  @RequestMapping(value = "/rest/event", method = RequestMethod.POST)
  public HttpEntity<Void> addEvent(@RequestBody WichtelEvent event) {
    WichtelData.addEvent(event);
    URI uri = linkTo(methodOn(getClass()).getWichtelEvent(event.getResId())).toUri();
    return ResponseEntity.created(uri).build();
  }

  @RequestMapping(value = "/rest/event", method = RequestMethod.GET)
  public Collection<Link> getWichtelEventLinks() {
    ArrayList<Link> links = new ArrayList<>();
    for (WichtelEvent wichtel : WichtelData.getAllEvents()) {
      Link link = linkTo(methodOn(getClass()).getWichtelEvent(wichtel.getResId())).withRel("event");
      links.add(link);
    }
    return links;
  }

  @RequestMapping(value = "/rest/event/{resId}", method = RequestMethod.GET)
  public HttpEntity<WichtelEvent> getWichtelEvent(@PathVariable("resId") Long resId) {
    WichtelEvent event = WichtelData.getEventByResId(resId);
    Link linkToWichtels = linkTo(methodOn(WichtelController.class).addWichtel(null, event.getResId())).withRel("wichtel");
    Link linkToCompleted = linkTo(methodOn(WichtelEventController.class).completeWichtelEvent(event.getResId())).withRel("completed");
    event.removeLinks();
    event.add(linkToWichtels);
    event.add(linkToCompleted);
    return new ResponseEntity<WichtelEvent>(event, HttpStatus.OK);
  }

  @RequestMapping(value = "/rest/event/{resId}/completed", method = RequestMethod.PUT)
  public HttpEntity<Void> completeWichtelEvent(@PathVariable("resId") Long resId) {
    WichtelEvent event = WichtelData.getEventByResId(resId);
    WichtelAssigner.assign(event);
    wichtelMailSender.sendWichtelMailsAndComplete(event);
    Link completionState = linkTo(methodOn(WichtelEventController.class).getWichtelEventCompletionState(resId)).withSelfRel();
    return ResponseEntity.accepted().location(URI.create(completionState.getHref())).build();
  }

  @RequestMapping(value = "/rest/event/{resId}/completed", method = RequestMethod.GET)
  public HttpEntity<MailStates> getWichtelEventCompletionState(@PathVariable("resId") Long resId) {
    List<Wichtel> wichtelList = WichtelData.getWichtelListByEventResId(resId);
    List<WichtelMailState> mailStates = wichtelList.stream().map(w -> new WichtelMailState(w)).collect(Collectors.toList());
    return new ResponseEntity<MailStates>(new MailStates(mailStates), HttpStatus.OK);
  }

}
