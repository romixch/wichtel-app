package ch.romix.wichtel.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.romix.wichtel.model.WichtelEntity;
import ch.romix.wichtel.model.WichtelEventEntity;

public class WichtelAssigner {

  public static void assign(WichtelEventEntity event) {
    Set<WichtelEntity> wichtels = event.getWichtels();
    if (wichtels.size() <= 1) {
      throw new RuntimeException("WTF! You can't assign wichtels with only one or less wichtel. That's not fun! It does not make sense!");
    }
    if (!event.isCompleted()) {
      while (!isCorrectlyAssigned(event)) {
        assignWichtels(event);
      }
    }
  }

  private static boolean isCorrectlyAssigned(WichtelEventEntity event) {
    Set<WichtelEntity> wichtels = event.getWichtels();
    Set<UUID> setOfWichtelIds = wichtels.stream().map(WichtelEntity::getId).collect(Collectors.toSet());
    long goodEntries = wichtels.stream() //
        .filter(WichtelAssigner::doesNotWichtelToHimself) //
        .filter(w -> setOfWichtelIds.contains(w.getWichtelTo().getId())) //
        .map(WichtelEntity::getWichtelTo) //
        .distinct() //
        .count();
    return goodEntries == wichtels.size();
  }

  private static boolean doesNotWichtelToHimself(WichtelEntity w) {
    return w.getWichtelTo() != null && !w.getId().equals(w.getWichtelTo().getId());
  }

  private static void assignWichtels(WichtelEventEntity event) {
    Set<WichtelEntity> wichtels = event.getWichtels();
    List<WichtelEntity> availableWichtel = new ArrayList<>(wichtels);
    wichtels.forEach(w -> {
      int wichtelToIndex = (int) (availableWichtel.size() * Math.random());
      WichtelEntity wichtelTo = availableWichtel.get(wichtelToIndex);
      w.setWichtelTo(wichtelTo);
      availableWichtel.remove(wichtelToIndex);
    });
  }
}
