package ggbt.aoc2019;

import static java.util.stream.Collectors.toList;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import ggbt.aoc2019.common.Point;
import ggbt.aoc2019.hullpainting.Color;
import ggbt.aoc2019.hullpainting.HullPaintingRobot;
import ggbt.aoc2019.intcode.IntCode;

public class Day11Test {

  @Test
  public void part1() throws Throwable {
    Path inputPath = Paths.get("inputs", "day.11");
    String input = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Long> program = Arrays.asList(input.split(",")).stream()
    .map(Long::parseLong)
    .collect(toList());
    
    IntCode brain = new IntCode(program);
    
    HashMap<Point, Color> colorMap = new HashMap<>();
    HashMap<Point, Boolean> visitationMap = new HashMap<>();
    
    new HullPaintingRobot(brain, colorMap, visitationMap).run();
    
    System.out.println(visitationMap.values().size());
  }

  @Test
  public void part2() throws Throwable {
    Path inputPath = Paths.get("inputs", "day.11");
    String input = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Long> program = Arrays.asList(input.split(",")).stream()
    .map(Long::parseLong)
    .collect(toList());
    
    IntCode brain = new IntCode(program);
    
    HashMap<Point, Color> colorMap = new HashMap<>();
    HashMap<Point, Boolean> visitationMap = new HashMap<>();
    
    // Set start position to WHITE
    colorMap.put(new Point(0, 0),  Color.WHITE);
    
    new HullPaintingRobot(brain, colorMap, visitationMap).run();
    
    showColoredPanels(colorMap);
  }

  private void showColoredPanels(HashMap<Point, Color> colorMap) {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;

    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    
    for (Point point : colorMap.keySet()) {
      if (point.x < minX) {
        minX = point.x;
      }
      if (point.y < minY) {
        minY = point.y;
      }
      if (point.x > maxX) {
        maxX = point.x;
      }
      if (point.y > maxY) {
        maxY = point.y;
      }
    }
    
    for (int y = minY; y <= maxY; ++y) {
      for (int x = minX; x <= maxX; ++x) {
        Color color = colorMap.computeIfAbsent(new Point(x, y), k -> Color.BLACK);
        
        if (color == Color.BLACK) {
          System.out.print(" ");
        } else {
          System.out.print("#");
        }
        
      }
      System.out.println();
    }
  }
}
