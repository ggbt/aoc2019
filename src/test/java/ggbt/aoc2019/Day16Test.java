package ggbt.aoc2019;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class Day16Test {
  
  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.16");
    String input = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    Matcher digitMatcher = Pattern.compile("\\d").matcher(input);
    
    List<Integer> digits = new ArrayList<>(input.length());
    
    while (digitMatcher.find()) {
      int digit = Integer.parseInt(digitMatcher.group());
      digits.add(digit);
    }
    
    for (int phase = 0; phase < 100; ++phase) {
      List<Integer> newDigits = new ArrayList<>(digits.size());
      
      for (int i = 0; i < digits.size(); ++i) {
        int sum = 0;
        for (int j = 0; j < digits.size(); ++j) {
          sum += digits.get(j) * getPattern(j, i + 1);
        }
        
        newDigits.add(Math.abs(sum % 10));
      }
      
      digits = newDigits;
    }
    
    for (int i = 0; i < 8; ++i) {
      System.out.print(digits.get(i));
    }
    System.out.println();
  }
  
  private int getPattern(int n, int size) {
    List<Integer> basePattern = Arrays.asList(0, 1, 0, -1);
    
    return basePattern.get(Math.floorDiv(n + 1, size) % 4);
  }
  
  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.16");
    String input = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    Matcher digitMatcher = Pattern.compile("\\d").matcher(input);
    
    List<Integer> digits = new ArrayList<>(input.length());
    
    while (digitMatcher.find()) {
      int digit = Integer.parseInt(digitMatcher.group());
      digits.add(digit);
    }
    
    int messageOffset = 0;
    for (int i = 0; i < 7; i++) {
      messageOffset = messageOffset * 10 + digits.get(i);
    }
    
    Function<Integer, Integer> digitAtIndex = n -> digits.get(n % digits.size());
    
    int lastRelevantDigitsSize = digits.size() * 10000 - messageOffset;
    List<Integer> relevantDigits = new ArrayList<>(lastRelevantDigitsSize);

    for (int i = 0; i < lastRelevantDigitsSize; ++i) {
      relevantDigits.add(digitAtIndex.apply(messageOffset + i));
    }
    
    for (int phase = 0; phase < 100; ++phase) {
      int sum = relevantDigits.parallelStream().reduce(0, Integer::sum);
      
      for (int i = 0; i < relevantDigits.size(); ++i) {
        int currentValueAtI = relevantDigits.get(i);
        relevantDigits.set(i, Math.abs(sum % 10));
        sum -= currentValueAtI;
      }
    }
    
    for (int i = 0; i < 8; ++i) {
      System.out.print(relevantDigits.get(i));
    }
    System.out.println();
  }
}
