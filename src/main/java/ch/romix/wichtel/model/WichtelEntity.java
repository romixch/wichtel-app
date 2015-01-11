package ch.romix.wichtel.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "Wichtel")
public class WichtelEntity {

  @Id
  @Type(type = "pg-uuid")
  private UUID id;
  @ManyToOne(optional = false)
  private WichtelEventEntity event;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String email;
  private boolean mailSent;
  private String sendError;

  @OneToOne
  private WichtelEntity wichtelTo;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public WichtelEventEntity getEvent() {
    return event;
  }

  public void setEvent(WichtelEventEntity event) {
    this.event = event;
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

  public WichtelEntity getWichtelTo() {
    return wichtelTo;
  }

  public void setWichtelTo(WichtelEntity wichtelTo) {
    this.wichtelTo = wichtelTo;
  }

  public boolean isMailSent() {
    return mailSent;
  }

  public void setMailSent(boolean mailSent) {
    this.mailSent = mailSent;
  }

  public String getSendError() {
    return sendError;
  }

  public void setSendError(String sendError) {
    this.sendError = sendError;
  }
}
