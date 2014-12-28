package ch.romix.wichtel.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class WichtelData {

  final static ConcurrentHashMap<Long, WichtelEvent> events = new ConcurrentHashMap<>();
  final static ConcurrentHashMap<Long, List<Wichtel>> wichtels = new ConcurrentHashMap<>();

  public static void addEvent(WichtelEvent event) {
    long id = (long) (Math.random() * Long.MAX_VALUE);
    event.setResId(id);
    events.put(event.getResId(), event);
  }

  public static Collection<WichtelEvent> getAllEvents() {
    return events.values();
  }

  public static WichtelEvent getEventByResId(Long resId) {
    return events.get(resId);
  }

  public static void addWichtelToEvent(Long eventResId, Wichtel wichtel) {
    long id = (long) (Math.random() * Long.MAX_VALUE);
    wichtel.setResId(id);
    List<Wichtel> wichtelList = wichtels.computeIfAbsent(eventResId, resId -> new CopyOnWriteArrayList<Wichtel>());
    wichtelList.add(wichtel);
  }

  public static List<Wichtel> getWichtelListByEventResId(Long eventResId) {
    List<Wichtel> wichtelList = wichtels.getOrDefault(eventResId, Collections.emptyList());
    return wichtelList;
  }

  public static Wichtel getWichtelByEventAndWichtelResId(Long eventResId, Long wichtelResId) {
    List<Wichtel> wichtelList = wichtels.getOrDefault(eventResId, Collections.emptyList());
    Optional<Wichtel> optionalWichtel = wichtelList.stream().filter(w -> w.getResId() == eventResId.longValue()).findAny();
    return optionalWichtel.orElse(null);
  }
}
