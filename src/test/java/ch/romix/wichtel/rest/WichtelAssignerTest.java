package ch.romix.wichtel.rest;

import java.util.UUID;

import org.junit.Test;

import ch.romix.wichtel.model.WichtelEntity;
import ch.romix.wichtel.model.WichtelEventEntity;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

public class WichtelAssignerTest {

  @Test(expected = RuntimeException.class)
  public void testAssignmentWithOneWichtel() throws Exception {
    WichtelEventEntity event = new WichtelEventEntity();
    event.setId(UUID.randomUUID());
    WichtelEntity wichtel = new WichtelEntity();
    wichtel.setId(UUID.randomUUID());
    event.getWichtels().add(wichtel);
    wichtel.setEvent(event);
    WichtelAssigner.assign(event);
  }

  @Test
  public void testAssignmentWithTwoWichtels() throws Exception {
    WichtelEventEntity event = new WichtelEventEntity();
    event.setId(UUID.randomUUID());
    WichtelEntity wichtel1 = new WichtelEntity();
    wichtel1.setId(UUID.randomUUID());
    event.getWichtels().add(wichtel1);
    wichtel1.setEvent(event);
    WichtelEntity wichtel2 = new WichtelEntity();
    wichtel2.setId(UUID.randomUUID());
    event.getWichtels().add(wichtel2);
    wichtel2.setEvent(event);
    WichtelAssigner.assign(event);
    assertThat(wichtel1.getWichtelTo().getId(), is(wichtel2.getId()));
    assertThat(wichtel2.getWichtelTo().getId(), is(wichtel1.getId()));
  }
}
