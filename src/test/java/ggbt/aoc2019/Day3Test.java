package ggbt.aoc2019;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import ggbt.aoc2019.common.Point;

public class Day3Test {

  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.3");
    List<String> moves = Files.lines(inputPath).collect(toList());
    
    List<LinkedHashSet<Point>> trails = new ArrayList<>();
    followMoves(moves, trails);
    
    LinkedHashSet<Point> trail1 = trails.get(0);
    LinkedHashSet<Point> trail2 = trails.get(1);

    SetView<Point> intersection = Sets.intersection(trail1, trail2);
    
    long smallestDistance = Long.MAX_VALUE;
    Point origin = new Point(0, 0);
    
    for (Point p1 : intersection) {
      if (origin.distance(p1) < smallestDistance) {
        smallestDistance = origin.distance(p1);
      }
    }
    
    System.out.println(smallestDistance);
  }

  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.3");
    List<String> moves = Files.lines(inputPath).collect(toList());
    
    List<LinkedHashSet<Point>> trails = new ArrayList<>();
    followMoves(moves, trails);
    
    LinkedHashSet<Point> trail1 = trails.get(0);
    LinkedHashSet<Point> trail2 = trails.get(1);

    List<Point> trail1Points = new ArrayList<>(trail1);
    List<Point> trail2Points = new ArrayList<>(trail2);
    
    AtomicInteger smallestScore = new AtomicInteger(Integer.MAX_VALUE);
    SetView<Point> intersection = Sets.intersection(trail1, trail2);
    
    intersection.forEach(point -> {
      int p1Score = trail1Points.indexOf(point) + 1;
      int p2Score = trail2Points.indexOf(point) + 1;
      
      int score = p1Score + p2Score;

      if (score < smallestScore.get()) {
        smallestScore.set(score);
      }
    });
    
    System.out.println(smallestScore);
  }
  
  private void followMoves(List<String> moves, List<LinkedHashSet<Point>> trails) throws IOException {
    Pattern movesPattern = Pattern.compile("(U|D|L|R)(\\d+)");
    
    for (String move : moves) {
      LinkedHashSet<Point> trail = new LinkedHashSet<>();
      trails.add(trail);

      Point prev = new Point(0, 0);
      Matcher moveMatcher = movesPattern.matcher(move);
      
      while (moveMatcher.find()) {
        String dir = moveMatcher.group(1);
        int dist = Integer.parseInt(moveMatcher.group(2));
        
        Point curr = new Point(prev);
        
        switch (dir) {
        case "U":
          while (dist-- > 0) {
            curr.y++;
            trail.add(new Point(curr));
          }
          break;
        case "D":
          while (dist-- > 0) {
            curr.y--;
            trail.add(new Point(curr));
          }
          break;
        case "L":
          while (dist-- > 0) {
            curr.x--;
            trail.add(new Point(curr));
          }
          break;
        case "R":
          while (dist-- > 0) {
            curr.x++;
            trail.add(new Point(curr));
          }
          break;
        }
        
        prev = curr;
      }
    }
  }
}
