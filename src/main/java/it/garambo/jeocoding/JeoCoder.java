package it.garambo.jeocoding;

import it.garambo.jeocoding.data.Location;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

@Slf4j
public class JeoCoder {

  private final LocationRepository locationRepository;
  private final JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();

  public JeoCoder(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
  }

  public Location getLocation(String locationName) {
    String lowercaseLocationName = locationName.toLowerCase();
    Optional<String> searchTerm =
        findExactMatch(lowercaseLocationName).or(() -> (findClosestMatch(lowercaseLocationName)));

    return searchTerm
        .map(s -> locationRepository.getLocationMap().get(s.toLowerCase()))
        .orElse(null);
  }

  private Optional<String> findExactMatch(String word) {
    String searchTerm = locationRepository.getLocationMap().containsKey(word) ? word : null;
    return Optional.ofNullable(searchTerm);
  }

  private Optional<String> findClosestMatch(String word) {
    long start = System.currentTimeMillis();
    log.debug("Using findClosestMatch JaroWinkler distance algorithm, searching: {} ", word);

    Map<String, Double> cache = new ConcurrentHashMap<>();
    Optional<String> closest =
        locationRepository.getLocationMap().keySet().parallelStream()
            .map(String::trim)
            .max(
                (firstSimilarity, secondSimilarity) ->
                    Double.compare(
                        cache.computeIfAbsent(
                            firstSimilarity,
                            otherWord -> jaroWinklerSimilarity.apply(word, otherWord)),
                        cache.computeIfAbsent(
                            secondSimilarity,
                            otherWord -> jaroWinklerSimilarity.apply(word, otherWord))));
    log.debug(
        "Found closest match for query {} in {} ms -> {}",
        word,
        (System.currentTimeMillis() - start),
        closest);
    return closest;
  }
}
