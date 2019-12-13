package ggbt.aoc2019;

import static java.util.stream.Collectors.toList;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import ggbt.aoc2019.common.Point;
import ggbt.aoc2019.intcode.IntCode;

public class Day13Test {

  @Test
  public void part1() throws Throwable {
    Path inputPath = Paths.get("inputs", "day.13");
    String input = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Long> program = Arrays.asList(input.split(",")).stream()
    .map(Long::parseLong)
    .collect(toList());
    
    HashMap<Point, Long> gameMap = new HashMap<>();
    
    AtomicReference<Point> coordinate = new AtomicReference<>();
    AtomicInteger instructionCounter = new AtomicInteger();;
    IntCode arcade = new IntCode(program, () -> 0L, output -> {
      int currInstruction = instructionCounter.get();
      
      switch (currInstruction) {
      case 0:
        coordinate.set(new Point(output, 0));
        break;
      case 1:
        coordinate.get().y = output;
        break;
      case 2:
        gameMap.put(coordinate.get(), output);
        break;
      }
      
      currInstruction = ++currInstruction % 3;
      instructionCounter.set(currInstruction);
    });

    arcade.run();
    
    long nrBlockTiles = gameMap.values().stream().filter(obj -> obj == 2).count();
    
    System.out.println(nrBlockTiles);
  }

  @Test
  public void part2() throws Throwable {
    Path inputPath = Paths.get("inputs", "day.13");
    String input = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Long> program = Arrays.asList(input.split(",")).stream()
    .map(Long::parseLong)
    .collect(toList());
    
    // Play for free
    program.set(0, 2L);
    
    HashMap<Point, Long> gameMap = new HashMap<>();
    
    AtomicReference<Point> coordinate = new AtomicReference<>();
    AtomicInteger instructionCounter = new AtomicInteger();
    
    AtomicLong currentScore = new AtomicLong(0);
    
    IntCode arcade = new IntCode(program, () -> {
      
      Point ball = null;
      
      for (Entry<Point, Long> gameEntry : gameMap.entrySet()) {
        if (gameEntry.getValue() == 4) { // Ball
          ball = gameEntry.getKey();
          break;
        }
      }
      
      Point paddle = null;
      for (Entry<Point, Long> gameEntry : gameMap.entrySet()) {

        if (gameEntry.getValue() == 3) { // Horizontal Paddle
          paddle = gameEntry.getKey();
          break;
        }
      }

      if (paddle.x < ball.x) {
        return 1L;
      } else if (ball.x < paddle.x) {
        return -1L;
      } else {
        return 0L;
      }
    }, output -> {
      int currInstruction = instructionCounter.get();
      
      switch (currInstruction) {
      case 0:
        coordinate.set(new Point(output, 0));
        break;
      case 1:
        coordinate.get().y = output;
        break;
      case 2:
        Point coord = coordinate.get();
        
        if (coord.x == -1 && coord.y == 0) {
          currentScore.set(output);
        } else {
          gameMap.put(coord, output);
        }
        
        break;
      }
      
      currInstruction = ++currInstruction % 3;
      instructionCounter.set(currInstruction);
    });

    arcade.run();
    
    System.out.println(currentScore);
  }
}
