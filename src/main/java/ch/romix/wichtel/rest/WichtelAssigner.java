package ch.romix.wichtel.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.romix.wichtel.model.Wichtel;
import ch.romix.wichtel.model.WichtelEvent;

public class WichtelAssigner {

  public static void assign(WichtelEvent event) {
    if (event.getWichtels().size() <= 1) {
      throw new RuntimeException("WTF! You can't assign wichtels with only one or less wichtel. That's not fun! It does not make sense!");
    }
    if (!event.isCompleted()) {
      while (!isCorrectlyAssigned(event)) {
        assignWichtels(event);
      }
      event.setCompleted(true);
    }
  }

  private static boolean isCorrectlyAssigned(WichtelEvent event) {
    List<Wichtel> wichtels = event.getWichtels();
    Set<Long> setOfWichtelIds = wichtels.stream().map(Wichtel::getResId).collect(Collectors.toSet());
    long goodEntries = wichtels.stream() //
        .filter(WichtelAssigner::doesNotWichtelToHimself) //
        .filter(w -> setOfWichtelIds.contains(w.getWichtelTo())) //
        .mapToLong(Wichtel::getWichtelTo) //
        .distinct() //
        .count();
    return goodEntries == wichtels.size();
  }

  private static boolean doesNotWichtelToHimself(Wichtel w) {
    return w.getResId() != w.getWichtelTo();
  }

  private static void assignWichtels(WichtelEvent event) {
    List<Long> availableWichtel = new ArrayList<>();
    event.getWichtels().forEach(w -> availableWichtel.add(w.getResId()));
    event.getWichtels().forEach(w -> {
      int wichtelToIndex = (int) (availableWichtel.size() * Math.random());
      Long wichtelTo = availableWichtel.get(wichtelToIndex);
      w.setWichtelTo(wichtelTo);
      availableWichtel.remove(wichtelToIndex);
    });
  }
}
