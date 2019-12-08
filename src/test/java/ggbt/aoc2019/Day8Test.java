package ggbt.aoc2019;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class Day8Test {

  @Test
  public void part1() throws Exception {
    final int rows = 6;
    final int cols = 25;
    List<List<Integer>> layers = getEncodedLayers(rows, cols);
    
    int fewest0 = Integer.MAX_VALUE;
    int nr1Multinr2 = 0;
    
    for (List<Integer> layer : layers) {
      int nr0 = 0;
      int nr1 = 0;
      int nr2 = 0;

      for (Integer color : layer) {
        switch (color) {
        case 0:
          ++nr0;
          break;
        case 1:
          ++nr1;
          break;
        case 2:
          ++nr2;
          break;
        }
      }
      
      if (nr0 < fewest0) {
        fewest0 = nr0;
        nr1Multinr2 = nr1 * nr2;
      }
    }
    
    System.out.println(nr1Multinr2);
  }

  @Test
  public void part2() throws Exception {
    final int rows = 6;
    final int cols = 25;
    List<List<Integer>> layers = getEncodedLayers(rows, cols);
    
    List<Integer> decoded = new ArrayList<>(Collections.nCopies(rows * cols, 2));
    
    for (List<Integer> layer : layers) {
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          int color = layer.get(i * cols + j);
          
          if (color != 2 && decoded.get(i * cols + j).equals(2)) {
            decoded.set(i * cols + j, color);
          }
        }
      }
    }
    
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (decoded.get(i * cols + j).equals(1)) {
          System.out.print("O");
        } else {
          System.out.print("_");
        }
        
      }
      System.out.println();
    }
  }
  
  private List<List<Integer>> getEncodedLayers(int rows, int cols) throws IOException {
    Path inputPath = Paths.get("inputs", "day.8");
    String input = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    Matcher matcher = Pattern.compile("\\d").matcher(input);
    
    List<Integer> numbers = new ArrayList<>();
    while (matcher.find()) {
      numbers.add(Integer.parseInt(matcher.group()));
    }
    
    List<List<Integer>> layers = new ArrayList<>();
    
    Iterator<Integer> iterator = numbers.iterator();
    while (iterator.hasNext()) {
      List<Integer> layer = new ArrayList<>(rows * cols);
      
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          layer.add(iterator.next());
        }
      }
      
      layers.add(layer);
    }
    return layers;
  }
}
