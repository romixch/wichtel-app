package ch.romix.wichtel.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "WichtelEvent")
@NamedQueries({@NamedQuery(name = WichtelEventEntity.ALL, query = "SELECT w FROM WichtelEventEntity w")})
public class WichtelEventEntity {

  public static final String ALL = "WichtelEventEntity.All";
  @Id
  @Type(type = "pg-uuid")
  private UUID id;
  @Column(nullable = false)
  private String name;
  private boolean completed;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
  private Set<WichtelEntity> wichtels;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public boolean isCompleted() {
    return completed;
  }

  public Set<WichtelEntity> getWichtels() {
    if (wichtels == null) {
      wichtels = new HashSet<WichtelEntity>();
    }
    return wichtels;
  }
}
