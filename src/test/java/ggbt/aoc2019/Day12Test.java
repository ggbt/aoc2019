package ggbt.aoc2019;

import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


import ggbt.aoc2019.common.Point3;

public class Day12Test {

  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.12");
    Pattern coordinatesPattern = Pattern.compile("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>");
    
    List<List<Point3>> moons = Files.lines(inputPath)
    .map(line -> {
      Matcher matcher = coordinatesPattern.matcher(line);
      matcher.find();
      
      Long x = Long.parseLong(matcher.group(1));
      Long y = Long.parseLong(matcher.group(2));
      Long z = Long.parseLong(matcher.group(3));
      
      return Arrays.asList(new Point3(x, y, z), new Point3(0, 0, 0));
    }).collect(toList());
    
    long totalEnergy = 0;
    
    int count = 1000;
    while (count-- > 0) {
      totalEnergy = tick(moons);
    }
    
    System.out.println(totalEnergy);
  }
  
  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.12");
    Pattern coordinatesPattern = Pattern.compile("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>");
    
    List<List<Point3>> moons = Files.lines(inputPath)
    .map(line -> {
      Matcher matcher = coordinatesPattern.matcher(line);
      matcher.find();
      
      Long x = Long.parseLong(matcher.group(1));
      Long y = Long.parseLong(matcher.group(2));
      Long z = Long.parseLong(matcher.group(3));
      
      return Arrays.asList(new Point3(x, y, z), new Point3(0, 0, 0));
    }).collect(toList());
    
    List<List<Point3>> initialMoons = moons.stream().map(posAndVel -> {
      return Arrays.asList(
        new Point3(posAndVel.get(0)),
        new Point3(posAndVel.get(1))
      );
    }).collect(toList());

    Long xFirstCycle = null;
    Long yFirstCycle = null;
    Long zFirstCycle = null;
    
    long index = 1;
    
    while (true) {
      tick(moons);
      ++index;
      
      boolean foundX = true;
      boolean foundY = true;
      boolean foundZ = true;
      for (int i = 0; i < moons.size(); i++) {
        List<Point3> posAndVel = moons.get(i);
        List<Point3> initialPosAndVel = initialMoons.get(i);

        if (posAndVel.get(0).x != initialPosAndVel.get(0).x) {
          foundX = false;
        }
        if (posAndVel.get(0).y != initialPosAndVel.get(0).y) {
          foundY = false;
        }
        if (posAndVel.get(0).z != initialPosAndVel.get(0).z) {
          foundZ = false;
        }
      }

      if (xFirstCycle == null && foundX) {
        xFirstCycle = index;
      }
      if (yFirstCycle == null && foundY) {
        yFirstCycle = index;
      }
      if (zFirstCycle == null && foundZ) {
        zFirstCycle = index;
      }
      
      if (xFirstCycle != null && yFirstCycle != null && zFirstCycle != null) {
        break;
      }
    }
    
    System.out.println(leastCommonMultiple(xFirstCycle, leastCommonMultiple(yFirstCycle, zFirstCycle)));
  }

  private long tick(List<List<Point3>> planets) {
    
    for (int i = 0; i < planets.size() - 1; i++) {
      for (int j = i + 1; j < planets.size(); j++) {
        List<Point3> posAndVel1 = planets.get(i);
        List<Point3> posAndVel2 = planets.get(j);
        applyGravity(posAndVel1, posAndVel2);
      }
    }
    
    for (List<Point3> posAndVel : planets) {
      applyVelocity(posAndVel);
    }
    
    long energy = 0;
    for (List<Point3> posAndVel : planets) {
      energy += calculateEnergy(posAndVel);
    }
    
    return energy;
  }

  private void applyGravity(List<Point3> posAndVel1, List<Point3> posAndVel2) {
    Point3 ganymedePos = posAndVel1.get(0);
    Point3 ganymedeVel = posAndVel1.get(1);
    
    Point3 callistoPos = posAndVel2.get(0);
    Point3 callistoVel = posAndVel2.get(1);
    
    if (ganymedePos.x != callistoPos.x) {
      long bigger = Math.max(callistoPos.x, ganymedePos.x);
      
      if (bigger == callistoPos.x) {
        callistoVel.x--;
      } else {
        callistoVel.x++;
      }

      if (bigger == ganymedePos.x) {
        ganymedeVel.x--;
      } else {
        ganymedeVel.x++;
      }
    }

    if (ganymedePos.y != callistoPos.y) {
      long bigger = Math.max(callistoPos.y, ganymedePos.y);
      
      if (bigger == callistoPos.y) {
        callistoVel.y--;
      } else {
        callistoVel.y++;
      }
      
      if (bigger == ganymedePos.y) {
        ganymedeVel.y--;
      } else {
        ganymedeVel.y++;
      }
    }

    if (ganymedePos.z != callistoPos.z) {
      long bigger = Math.max(callistoPos.z, ganymedePos.z);
      
      if (bigger == callistoPos.z) {
        callistoVel.z--;
      } else {
        callistoVel.z++;
      }
      
      if (bigger == ganymedePos.z) {
        ganymedeVel.z--;
      } else {
        ganymedeVel.z++;
      }
    }
  }

  private void applyVelocity(List<Point3> posAndVel) {
    Point3 pos = posAndVel.get(0);
    Point3 vel = posAndVel.get(1);
    
    pos.x += vel.x;
    pos.y += vel.y;
    pos.z += vel.z;
  }
  
  private long calculateEnergy(List<Point3> posAndVel) {
    Point3 pos = posAndVel.get(0);
    Point3 vel = posAndVel.get(1);
    
    long potentialEnergy = Math.abs(pos.x) + Math.abs(pos.y) + Math.abs(pos.z);
    long kineticEnergy = Math.abs(vel.x) + Math.abs(vel.y) + Math.abs(vel.z);
    
    return potentialEnergy * kineticEnergy;
  }
  
  private long leastCommonMultiple(long number1, long number2) {
    if (number1 == 0 || number2 == 0) {
      return 0;
    }
    long absNumber1 = Math.abs(number1);
    long absNumber2 = Math.abs(number2);
    
    long absHigherNumber = Math.max(absNumber1, absNumber2);
    long absLowerNumber = Math.min(absNumber1, absNumber2);
    
    long leastCommonMultiple = absHigherNumber;
    
    while (leastCommonMultiple % absLowerNumber != 0) {
      leastCommonMultiple += absHigherNumber;
    }
    
    return leastCommonMultiple;
  }
}
