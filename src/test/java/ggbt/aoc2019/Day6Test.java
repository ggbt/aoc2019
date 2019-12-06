package ggbt.aoc2019;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

public class Day6Test {

  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.6");
    
    MutableGraph<String> orbiters = GraphBuilder.directed().build();
    
    Files.lines(inputPath)
    .map(line -> line.split("\\)"))
    .forEach(split -> {
      orbiters.addNode(split[0]);
      orbiters.addNode(split[1]);
      
      orbiters.putEdge(split[1], split[0]);
    });
    
    AtomicInteger nrOrbits = new AtomicInteger(0);
    
    orbiters.nodes().forEach(node -> {
      try {
        while (true) {
          node = orbiters.successors(node).iterator().next();
          nrOrbits.incrementAndGet();
        }
      } catch (NoSuchElementException e) {}
    });
    
    System.out.println(nrOrbits);
  }
  
  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.6");
    
    MutableGraph<String> orbiters = GraphBuilder.directed().build();
    
    Files.lines(inputPath)
    .map(line -> line.split("\\)"))
    .forEach(split -> {
      orbiters.addNode(split[0]);
      orbiters.addNode(split[1]);
      
      orbiters.putEdge(split[1], split[0]);
    });
    
    String currentLocation = orbiters.successors("YOU").iterator().next();
    LinkedHashSet<String> yourPath = new LinkedHashSet<>();
    
    try {
      while (true) {
        currentLocation = orbiters.successors(currentLocation).iterator().next();
        yourPath.add(currentLocation);
      }
    } catch (NoSuchElementException e) {}

    currentLocation = orbiters.successors("SAN").iterator().next();
    LinkedHashSet<String> sansPath = new LinkedHashSet<>();
    
    sansPath.add(currentLocation);
    
    try {
      while (true) {
        currentLocation = orbiters.successors(currentLocation).iterator().next();
        sansPath.add(currentLocation);
      }
    } catch (NoSuchElementException e) {}
    
    System.out.println(Sets.difference(yourPath, sansPath).size() + Sets.difference(sansPath, yourPath).size() + 1);
  }
}
