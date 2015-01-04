package ch.romix.wichtel.model;

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
