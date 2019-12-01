package ggbt.aoc2019;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class Day1Test {

  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.1");
    
    Files.lines(inputPath)
    .map(Long::parseLong)
    .map(this::calculateFuelRequired)
    .reduce(Long::sum)
    .ifPresent(System.out::println);
  }
  
  private long calculateFuelRequired(long mass) {
    return mass / 3 - 2;
  }
  
  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.1");
    
    Files.lines(inputPath)
    .map(Long::parseLong)
    .map(this::calculateFuelRequired2)
    .reduce(Long::sum)
    .ifPresent(System.out::println);
  }
  
  private long calculateFuelRequired2(long mass) {
    long fuelRequired = calculateFuelRequired(mass);
    long fuelRequiredWithFuel = fuelRequired;
    
    while (fuelRequired / 3 > 0) {
      fuelRequiredWithFuel += Math.max(0, fuelRequired / 3 - 2);
      fuelRequired = fuelRequired / 3 - 2;
    }
    
    return fuelRequiredWithFuel;
  }
}
