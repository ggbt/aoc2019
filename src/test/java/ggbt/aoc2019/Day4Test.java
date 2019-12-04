package ggbt.aoc2019;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.junit.Test;

public class Day4Test {

  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.4");
    String input = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    Matcher matcher = Pattern.compile("(\\d+)-(\\d+)").matcher(input);
    matcher.find();
    
    int start = Integer.parseInt(matcher.group(1));
    int end = Integer.parseInt(matcher.group(2));
    
    long count = IntStream.range(start, end)
    .parallel()
    .filter(this::digitsNeverDecrease)
    .filter(this::has2AdjacentSameDigits)
    .count();
    
    System.out.println(count);
  }
  
  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.4");
    String input = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    Matcher matcher = Pattern.compile("(\\d+)-(\\d+)").matcher(input);
    matcher.find();
    
    int start = Integer.parseInt(matcher.group(1));
    int end = Integer.parseInt(matcher.group(2));
    
    long count = IntStream.range(start, end)
    .parallel()
    .filter(this::digitsNeverDecrease)
    .filter(this::has2AdjacentSameDigitsNotPartOfLargerGroup)
    .count();
    
    System.out.println(count);
  }
  
  public boolean digitsNeverDecrease(int number) {
    Integer prev = null;
    
    while (number > 0) {
      int digit = number % 10;
      if (prev != null && (digit > prev)) {
        return false;
      }
      
      number /= 10;
      prev = digit;
    }
    
    return true;
  }

  public boolean has2AdjacentSameDigits(int number) {
    Integer prev = null;
    
    while (number > 0) {
      int digit = number % 10;
      if (prev != null && (prev == digit)) {
        return true;
      }
      
      number /= 10;
      prev = digit;
    }
    
    return false;
  }
  
  public boolean has2AdjacentSameDigitsNotPartOfLargerGroup(int number) {
    int nrSameAdjacent = 0;
    Integer prev = null;
    
    while (number > 0) {
      int digit = number % 10;
      
      if (prev != null) {
        if (prev == digit) {
          nrSameAdjacent++;
          
          if (nrSameAdjacent == 1 && number == digit) {
            return true;
          }
        } else if (nrSameAdjacent == 1) {
          return true;
        } else {
          nrSameAdjacent = 0;
        }
      }
      
      number /= 10;
      prev = digit;
    }
    
    return false;
  }
}
