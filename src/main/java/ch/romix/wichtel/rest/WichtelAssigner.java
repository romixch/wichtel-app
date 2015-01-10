package ch.romix.wichtel.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.romix.wichtel.model.Wichtel;
import ch.romix.wichtel.model.WichtelData;
import ch.romix.wichtel.model.WichtelEvent;

public class WichtelAssigner {

  public static void assign(WichtelEvent event) {
    List<Wichtel> wichtels = WichtelData.getWichtelListByEventResId(event.getResId());
    if (wichtels.size() <= 1) {
      throw new RuntimeException("WTF! You can't assign wichtels with only one or less wichtel. That's not fun! It does not make sense!");
    }
    if (!event.isCompleted()) {
      while (!isCorrectlyAssigned(event)) {
        assignWichtels(event);
      }
    }
  }

  private static boolean isCorrectlyAssigned(WichtelEvent event) {
    List<Wichtel> wichtels = WichtelData.getWichtelListByEventResId(event.getResId());
    Set<UUID> setOfWichtelIds = wichtels.stream().map(Wichtel::getResId).collect(Collectors.toSet());
    long goodEntries = wichtels.stream() //
        .filter(WichtelAssigner::doesNotWichtelToHimself) //
        .filter(w -> setOfWichtelIds.contains(w.getWichtelTo())) //
        .map(Wichtel::getWichtelTo) //
        .distinct() //
        .count();
    return goodEntries == wichtels.size();
  }

  private static boolean doesNotWichtelToHimself(Wichtel w) {
    return w.getResId() != w.getWichtelTo();
  }

  private static void assignWichtels(WichtelEvent event) {
    List<Wichtel> wichtels = WichtelData.getWichtelListByEventResId(event.getResId());
    List<UUID> availableWichtel = new ArrayList<>();
    wichtels.forEach(w -> availableWichtel.add(w.getResId()));
    wichtels.forEach(w -> {
      int wichtelToIndex = (int) (availableWichtel.size() * Math.random());
      UUID wichtelTo = availableWichtel.get(wichtelToIndex);
      w.setWichtelTo(wichtelTo);
      availableWichtel.remove(wichtelToIndex);
    });
  }
}
