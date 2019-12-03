package ggbt.aoc2019;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;

public class Day3Test {

  
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.3");
    
    Pattern regexp = Pattern.compile("(U|D|L|R)(\\d+)");
    List<String> moves = Files.lines(inputPath).collect(Collectors.toList());
    
    int trailCounter = 0;
    List<List<Point>> trails = new ArrayList<>();
    trails.add(new ArrayList<>());
    trails.add(new ArrayList<>());
    
    for (String move : moves) {
      List<Point> currTrail = trails.get(trailCounter++);
      Point prev = new Point(0, 0);
      Matcher matcher = regexp.matcher(move);
      
      while (matcher.find()) {
        String dir = matcher.group(1);
        int dist = Integer.parseInt(matcher.group(2));
        Point after = new Point(prev);
        
        switch (dir) {
        case "U":
          while (dist > 0) {
            after.y++;
            dist--;
            currTrail.add(new Point(after));
          }
          break;
        case "D":
          while (dist > 0) {
            after.y--;
            dist--;
            currTrail.add(new Point(after));
          }
          break;
        case "L":
          while (dist > 0) {
            after.x--;
            dist--;
            currTrail.add(new Point(after));
          }
          break;
        case "R":
          while (dist > 0) {
            after.x++;
            dist--;
            currTrail.add(new Point(after));
          }
          break;
        }
        
        prev = after;
      }
    }
    
    List<Point> trail1 = trails.get(0);
    List<Point> trail2 = trails.get(1);
    
    int smallestDist = Integer.MAX_VALUE;
    Point origin = new Point(0, 0);
    
    for (Point p1 : trail1) {
      for (Point p2 : trail2) {
        if (p1.equals(p2)) {
          if (origin.distance(p1) < smallestDist) {
            smallestDist = origin.distance(p1);
          }
        }
      }
    }
    
    System.out.println(smallestDist);
  }

  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.3");
    
    Pattern regexp = Pattern.compile("(U|D|L|R)(\\d+)");
    List<String> moves = Files.lines(inputPath).collect(Collectors.toList());
    
    int trailCounter = 0;
    List<List<Point>> trails = new ArrayList<>();
    trails.add(new ArrayList<>());
    trails.add(new ArrayList<>());
    
    for (String move : moves) {
      List<Point> currTrail = trails.get(trailCounter++);
      Point prev = new Point(0, 0);
      Matcher matcher = regexp.matcher(move);
      
      while (matcher.find()) {
        String dir = matcher.group(1);
        int dist = Integer.parseInt(matcher.group(2));
        Point after = new Point(prev);
        
        switch (dir) {
        case "U":
          while (dist > 0) {
            after.y++;
            after.score++;
            dist--;
            currTrail.add(new Point(after));
          }
          break;
        case "D":
          while (dist > 0) {
            after.y--;
            after.score++;
            dist--;
            currTrail.add(new Point(after));
          }
          break;
        case "L":
          while (dist > 0) {
            after.x--;
            after.score++;
            dist--;
            currTrail.add(new Point(after));
          }
          break;
        case "R":
          while (dist > 0) {
            after.x++;
            after.score++;
            dist--;
            currTrail.add(new Point(after));
          }
          break;
        }
        
        prev = after;
      }
    }
    
    List<Point> trail1 = trails.get(0);
    List<Point> trail2 = trails.get(1);
    
    int smallestScore = Integer.MAX_VALUE;
    
    for (Point p1 : trail1) {
      for (Point p2 : trail2) {
        if (p1.equals(p2)) {
          if (smallestScore > p1.score + p2.score) {
            smallestScore = p1.score + p2.score;
          }
        }
      }
    }
    
    System.out.println(smallestScore);
  }
}
