package ggbt.aoc2019;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.Test;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

public class Day14Test {

  private HashMap<String, Long> outputQuantities;
  private MutableValueGraph<String, Long> reactions;
  private HashMap<String, Long> inventory;
  
  @Test
  public void part1() throws Exception {
    initChemicalReactions();

    System.out.println(priceInOre("FUEL", 1L));
  }

  @Test
  public void part2() throws Exception {
    initChemicalReactions();
    
    // Take a random number that gives a value bigger than 1 trillion and brute
    // force search until I find the maximum amount of FUEL I can produce with 1 trillion ORE
    long number = 1125000;
    long priceInOre = Long.MAX_VALUE;
    
    while (priceInOre > 1_000_000_000_000L) {
      priceInOre = priceInOre("FUEL", --number);
    }
    
    System.out.println(number);
  }

  private void initChemicalReactions() throws Exception {
    Path inputPath = Paths.get("inputs", "day.14");
    Pattern reactionPattern = Pattern.compile("(\\d+) ([A-Z]+)");
    
    outputQuantities = new HashMap<>();
    reactions = ValueGraphBuilder.directed().build();
    inventory = new HashMap<>();
    
    Files.lines(inputPath)
    .map(line -> line.split("=>"))
    .forEach(split -> {
      
      Matcher outputMatcher = reactionPattern.matcher(split[1]);
      outputMatcher.find();
      
      long outputQuantity = Long.parseLong(outputMatcher.group(1));
      String output = outputMatcher.group(2);
      
      reactions.addNode(output);
      outputQuantities.put(output, outputQuantity);
      
      Matcher inputMatcher = reactionPattern.matcher(split[0]);
      while (inputMatcher.find()) {
        long inputQuantity = Long.parseLong(inputMatcher.group(1));
        String input = inputMatcher.group(2);
        
        reactions.addNode(input);
        reactions.putEdgeValue(output, input, inputQuantity);
      }
    });
  }
  
  private long priceInOre(String chemical, long quantity) {
    if ("ORE".equals(chemical)) {
      return quantity;
    }
    
    long quantityAvailable = inventory.getOrDefault(chemical, 0L);
    inventory.put(chemical, Math.max(0, quantityAvailable - quantity));
    long quantityNeeded = quantity - quantityAvailable;
    
    long priceInOre = 0;
    
    if (quantityNeeded > 0) {
      long outputQuantity = outputQuantities.get(chemical);
      long requestQuantity = calculateRequestQuantity(quantityNeeded, outputQuantity);
      
      Stream<String> prerequisites = reactions.successors(chemical).stream();
      
      priceInOre = prerequisites.mapToLong(prerequisite -> {
        Long inputQuantity = reactions.edgeValue(chemical, prerequisite).get();
        long nrBatches = requestQuantity / outputQuantity;
        
        return priceInOre(prerequisite, inputQuantity * nrBatches);
      }).sum();
      
      // Put excess in inventory.
      inventory.merge(chemical, requestQuantity - quantityNeeded, Long::sum);
    }
    
    return priceInOre;
  }
  
  private long calculateRequestQuantity(long requestQuantity, long batchQuantity) {
    if (batchQuantity >= requestQuantity) {
      requestQuantity = batchQuantity;
    } else {
      if (requestQuantity % batchQuantity != 0) {
        requestQuantity = Math.floorDiv(requestQuantity, batchQuantity) * batchQuantity + batchQuantity;
      } else {
        requestQuantity = Math.floorDiv(requestQuantity, batchQuantity) * batchQuantity;
      }
    }
    
    return requestQuantity;
  }
}
