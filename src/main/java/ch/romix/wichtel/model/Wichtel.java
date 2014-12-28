package ch.romix.wichtel.model;

import org.springframework.hateoas.ResourceSupport;


public class Wichtel extends ResourceSupport {

  private long resId;
  private String name;
  private String email;
  private long wichtelTo;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public long getWichtelTo() {
    return wichtelTo;
  }

  public void setWichtelTo(long wichtelTo) {
    this.wichtelTo = wichtelTo;
  }


}
