package it.garambo.jeocoding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import it.garambo.jeocoding.data.Location;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JeoCoderTest {

  @Mock LocationRepository repository;

  @InjectMocks JeoCoder jeoCoder;

  Map<String, Location> locationMap =
      Map.of(
          "dublin", new Location("Dublin", 53.33306, -6.24889, "IE"),
          "rome", new Location("Rome", 41.89193, 12.51133, "IT"),
          "new york city", new Location("New York City", 40.71427, -74.00597, "US"));

  @BeforeEach
  void setup() {
    when(repository.getLocationMap()).thenReturn(locationMap);
  }

  @Test
  void exactMatch() {
    Location testLocation = new Location("Dublin", 53.33306, -6.24889, "IE");

    assertEquals(testLocation, jeoCoder.getLocation("dublin"));
    assertEquals(testLocation, jeoCoder.getLocation("DUBLIN"));
  }

  @Test
  void closestMatch() {
    Location dublin = new Location("Dublin", 53.33306, -6.24889, "IE");
    Location rome = new Location("Rome", 41.89193, 12.51133, "IT");
    Location nyc = new Location("New York City", 40.71427, -74.00597, "US");

    // with typo
    assertEquals(dublin, jeoCoder.getLocation("dulbin"));

    // with different letter
    assertEquals(rome, jeoCoder.getLocation("roma"));

    // with missing word
    assertEquals(nyc, jeoCoder.getLocation("new york"));
  }
}
