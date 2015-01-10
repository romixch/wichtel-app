package ch.romix.wichtel.model;

import java.util.UUID;


public class WichtelMailState {

  private UUID wichtelResId;
  private boolean mailSent;
  private String sendError;

  public WichtelMailState(Wichtel wichtel) {
    wichtelResId = wichtel.getResId();
    mailSent = wichtel.isMailSent();
    sendError = wichtel.getSendError();
  }

  public UUID getWichtelResId() {
    return wichtelResId;
  }

  public void setWichtelResId(UUID wichtelResId) {
    this.wichtelResId = wichtelResId;
  }

  public void setMailSent(boolean mailSent) {
    this.mailSent = mailSent;
  }

  public boolean isMailSent() {
    return mailSent;
  }

  public boolean isError() {
    return sendError != null;
  }

  public String getSendError() {
    return sendError;
  }

  public void setSendError(String sendError) {
    this.sendError = sendError;
  }
}
