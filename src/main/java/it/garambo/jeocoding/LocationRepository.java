package it.garambo.jeocoding;

import ch.randelshofer.fastdoubleparser.JavaDoubleParser;
import it.garambo.jeocoding.data.Location;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class LocationRepository {

  private final Map<String, Location> locationMap;

  public LocationRepository(File file) throws IOException {
    locationMap = new HashMap<>();
    load(file);
  }

  private void load(File file) throws IOException {

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      log.debug("Location Repository load started");
      long start = System.currentTimeMillis();
      while (null != (line = reader.readLine())) {
        String[] entry = line.split("\t");
        Location location = toLocation(entry);
        locationMap.put(location.name().toLowerCase(), location);
      }

      long end = System.currentTimeMillis();
      log.debug("Location Repository load ended, took " + (end - start) + " ms");

    } catch (IOException e) {
      log.error("Could not load data.", e);
      throw e;
    }
  }

  private Location toLocation(String[] entry) {
    return new Location(entry[1], fastParse(entry[4]), fastParse(entry[5]), entry[8]);
  }

  private double fastParse(String doubleString) {
    return JavaDoubleParser.parseDouble(doubleString);
  }
}
