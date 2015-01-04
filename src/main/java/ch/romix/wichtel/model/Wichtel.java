package ch.romix.wichtel.model;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @author roman
 *
 */
public class Wichtel extends ResourceSupport {

  private long resId;
  private String name;
  private String email;
  private long wichtelTo;
  private boolean mailSent;
  private String sendError;

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

  @JsonIgnore
  public long getWichtelTo() {
    return wichtelTo;
  }

  public void setWichtelTo(long wichtelTo) {
    this.wichtelTo = wichtelTo;
  }

  public boolean isMailSent() {
    return mailSent;
  }

  public void setMailSent(boolean mailSent) {
    this.mailSent = mailSent;
  }

  public boolean isError() {
    return sendError != null;
  }

  public void setSendError(String sendError) {
    this.sendError = sendError;
  }

  public String getSendError() {
    return sendError;
  }
}
