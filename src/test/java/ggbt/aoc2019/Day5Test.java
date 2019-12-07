package ggbt.aoc2019;

import static java.util.stream.Collectors.toList;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import ggbt.aoc2019.intcode.IntCode;

public class Day5Test {

  @Test
  public void part1() throws Throwable {
    Path inputPath = Paths.get("inputs", "day.5");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Integer> program = Arrays.asList(programInput.split(",")).stream()
    .map(Integer::parseInt)
    .collect(toList());
    
    AtomicInteger output = new AtomicInteger();
    new IntCode(program, () -> 1, output::set).run();
    
    System.out.println(output);
  }

  @Test
  public void part2() throws Throwable {
    Path inputPath = Paths.get("inputs", "day.5");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Integer> program = Arrays.asList(programInput.split(",")).stream()
    .map(Integer::parseInt)
    .collect(toList());
    
    new IntCode(program, () -> 5, System.out::println).run();
  }
}
