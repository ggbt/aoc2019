package ggbt.aoc2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import ggbt.aoc2019.common.Point;

public class Day10Test {

  @Test
  public void part1() throws Exception {
    Map<Point, List<List<Point>>> visibilityMap = calculateStationsVisibility();
    
    int max = 0;
    for (Entry<Point, List<List<Point>>> entry : visibilityMap.entrySet()) {
      int nrOfVisible = entry.getValue().size();
      if (nrOfVisible > max) {
        max = nrOfVisible;
      }
    }
    
    System.out.println(max);
  }
  
  @Test
  public void part2() throws Exception {
    Map<Point, List<List<Point>>> visibilityMap = calculateStationsVisibility();
    
    Point bestStation = null;
    int max = 0;
    for (Entry<Point, List<List<Point>>> entry : visibilityMap.entrySet()) {
      
      int nrOfVisible = entry.getValue().size();
      if (nrOfVisible > max) {
        max = nrOfVisible;
        bestStation = entry.getKey();
      }
    }
    
    List<List<Point>> laserTargets = visibilityMap.get(bestStation);
    
    Point theBestStation = bestStation;
    Point origin = new Point(-theBestStation.x, theBestStation.y);
    
    laserTargets.forEach(target -> {
      // Move the origin of all asteroids for easier angle calculations.
      target.forEach(asteroid -> {
        asteroid.moveOrigin(theBestStation);
      });

      // Sort the asteroid on the same laser line: closest to station last.
      target.sort((a1, a2) -> {
        return (int) (theBestStation.distance(a1) - theBestStation.distance(a2));
      });
    });
    
    laserTargets.sort((l1, l2) -> {
      double angle1 = angle(l1.get(0));
      double angle2 = angle(l2.get(0));
      
      return Double.compare(angle1, angle2);
    });
    
    int index = 0;
    int nrIntersections = laserTargets.size();
    int currIntersection = 0;
    
    while (index < 200) {
      
      List<Point> list = laserTargets.get(currIntersection);
      
      if (!list.isEmpty()) {
        Point removed = list.remove(list.size() - 1);

        if (index == 199) {
          removed.moveOrigin(origin);
          System.out.println(removed.x * 100 + removed.y);
          break;
        }
        
        ++index;
      }
      
      currIntersection = ++currIntersection % nrIntersections;
    }
  }

  private Map<Point, List<List<Point>>> calculateStationsVisibility() throws IOException {
    Path inputPath = Paths.get("inputs", "day.10");
    
    Pattern asteroidsPattern = Pattern.compile("#|\\.");
    
    List<Point> asteroids = new ArrayList<>();
    
    int[] y = { 0 };
    Files.lines(inputPath).forEach(line -> {
      Matcher matcher = asteroidsPattern.matcher(line);
      
      int x = 0;
      while (matcher.find()) {
        if ("#".equals(matcher.group())) {
          asteroids.add(new Point(x, y[0]));
        }
        ++x;
      }
      
      ++y[0];
    });
    
    Map<Point, List<List<Point>>> visibilityMap = new HashMap<>();
    
    for (Point currentAsteroid : asteroids) {
      List<List<Point>> targets = new ArrayList<>();
      
      for (Point otherAsteroid : asteroids) {
        if (!currentAsteroid.equals(otherAsteroid)) {
          if (targets.isEmpty()) {
            ArrayList<Point> i = new ArrayList<>();
            i.add(otherAsteroid);
            targets.add(i);
          } else {
            Optional<List<Point>> l = targets.stream()
            .filter(list -> {
              Point intersectedAsteroid = list.get(0);
              
              boolean currentIsInMiddle = intersectedAsteroid.distance(otherAsteroid) == 
                  currentAsteroid.distance(intersectedAsteroid) + currentAsteroid.distance(otherAsteroid);

              return pointsAreOnSameLine(currentAsteroid, list.get(0), otherAsteroid) && !currentIsInMiddle;
            })
            .findFirst();
            
            if (l.isPresent()) {
              l.get().add(otherAsteroid);
            } else {
              ArrayList<Point> i = new ArrayList<>();
              i.add(otherAsteroid);
              targets.add(i);
            }
          }
        }
      }
      
      visibilityMap.put(currentAsteroid, targets);
    }
    return visibilityMap;
  }
  
  private boolean pointsAreOnSameLine(Point a, Point b, Point c) {
    return (a.x * (b.y - c.y) +
            b.x * (c.y - a.y) +
            c.x * (a.y - b.y)) == 0;
  }
  
  private double angle(Point point) {
    if (point.x == 0) {
      return point.y >= 0 ? (double) 0 : (double) 180;
    }

    double tan = (double) point.y / (double) point.x;
    double degrees = Math.toDegrees(Math.atan(tan));
    
    // Quadrant 1
    if (point.x >= 0 && point.y >= 0) {
      return 90 - degrees;
    }
    
    // Quadrant 2
    if (point.x < 0 && point.y >= 0) {
      return 270 - degrees;
    }
    
    // Quadrant 3
    if (point.x < 0 && point.y < 0) {
      return 180 + (90 - degrees);
    }
    
    // Quadrant 4
    if (point.x >= 0 && point.y < 0) {
      return 90 - degrees;
    }
    
    return degrees;
  }
}
