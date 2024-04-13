package it.garambo.jeocoding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import it.garambo.jeocoding.data.Location;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LocationRepositoryTest {

  @Test
  void load() {
    try {
      LocationRepository repository = new LocationRepository(getTestFile());
      Map<String, Location> locationMap = repository.getLocationMap();
      assertNotNull(locationMap);
      assertEquals(26471, locationMap.size());
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  void failedLoad() {
    IOException e =
        assertThrows(IOException.class, () -> new LocationRepository(new File("fake/file.txt")));
    assertEquals("fake/file.txt (No such file or directory)", e.getMessage());
  }

  File getTestFile() {
    return new File("src/test/resources/cities15000.txt");
  }
}
