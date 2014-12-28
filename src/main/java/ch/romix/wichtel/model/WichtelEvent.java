package ch.romix.wichtel.model;

import org.springframework.hateoas.ResourceSupport;

public class WichtelEvent extends ResourceSupport {
  private long resId;
  private String name;
  private boolean completed;

  public long getResId() {
    return resId;
  }

  public void setResId(long resId) {
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
