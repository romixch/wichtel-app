package ch.romix.wichtel.model;


public class WichtelMailState {

  private Long wichtelResId;
  private boolean mailSent;
  private String sendError;

  public WichtelMailState(Wichtel wichtel) {
    wichtelResId = wichtel.getResId();
    mailSent = wichtel.isMailSent();
    sendError = wichtel.getSendError();
  }

  public Long getWichtelResId() {
    return wichtelResId;
  }

  public void setWichtelResId(Long wichtelResId) {
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
