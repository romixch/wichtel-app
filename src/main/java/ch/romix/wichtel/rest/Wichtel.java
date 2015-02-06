package ch.romix.wichtel.rest;

import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @author roman
 *
 */
public class Wichtel extends ResourceSupport {

  private UUID resId;
  private String name;
  private String email;
  private UUID wichtelTo;
  private boolean mailSent;
  private String sendError;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @JsonIgnore
  public UUID getWichtelTo() {
    return wichtelTo;
  }

  public void setWichtelTo(UUID wichtelTo) {
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
