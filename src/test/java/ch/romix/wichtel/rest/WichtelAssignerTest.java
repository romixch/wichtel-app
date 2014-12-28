package ch.romix.wichtel.rest;

import org.junit.Test;

import ch.romix.wichtel.model.Wichtel;
import ch.romix.wichtel.model.WichtelEvent;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

public class WichtelAssignerTest {

  @Test(expected = RuntimeException.class)
  public void testAssignmentWithOneWichtel() throws Exception {
    WichtelEvent event = new WichtelEvent();
    Wichtel wichtel = new Wichtel();
    wichtel.setResId(42);
    event.getWichtels().add(wichtel);
    WichtelAssigner.assign(event);
  }

  @Test
  public void testAssignmentWithTwoWichtels() throws Exception {
    WichtelEvent event = new WichtelEvent();
    Wichtel wichtel1 = new Wichtel();
    wichtel1.setResId(42);
    event.getWichtels().add(wichtel1);
    Wichtel wichtel2 = new Wichtel();
    wichtel2.setResId(84);
    event.getWichtels().add(wichtel2);
    WichtelAssigner.assign(event);
    assertThat(wichtel1.getWichtelTo(), is(wichtel2.getResId()));
    assertThat(wichtel2.getWichtelTo(), is(wichtel1.getResId()));
  }
}
