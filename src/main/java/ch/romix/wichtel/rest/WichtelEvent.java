package ch.romix.wichtel.rest;

import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;

public class WichtelEvent extends ResourceSupport {
  private UUID resId;
  private String name;
  private boolean completed;

  public UUID getResId() {
    return resId;
  }

  public void setResId(UUID resId) {
    this.resId = resId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }
}
