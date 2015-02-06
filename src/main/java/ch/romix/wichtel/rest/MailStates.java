package ch.romix.wichtel.rest;

import java.util.List;

public class MailStates {
  private List<WichtelMailState> states;

  public MailStates(List<WichtelMailState> states) {
    this.states = states;
  }

  public List<WichtelMailState> getStates() {
    return states;
  }
}
