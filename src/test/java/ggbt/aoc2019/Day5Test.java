package ggbt.aoc2019;

import static java.util.stream.Collectors.toList;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import ggbt.aoc2019.intcode.IntCode;

public class Day5Test {

  @Test
  public void part1() throws Throwable {
    Path inputPath = Paths.get("inputs", "day.5");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Long> program = Arrays.asList(programInput.split(",")).stream()
    .map(Long::parseLong)
    .collect(toList());
    
    AtomicLong output = new AtomicLong();
    new IntCode(program, () -> 1L, output::set).run();
    
    System.out.println(output);
  }

  @Test
  public void part2() throws Throwable {
    Path inputPath = Paths.get("inputs", "day.5");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Long> program = Arrays.asList(programInput.split(",")).stream()
    .map(Long::parseLong)
    .collect(toList());
    
    new IntCode(program, () -> 5L, System.out::println).run();
  }
}
