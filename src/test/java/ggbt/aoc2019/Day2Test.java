package ggbt.aoc2019;

import static java.util.stream.Collectors.toList;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Day2Test {

  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.2");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Integer> program = Arrays.asList(programInput.split(",")).stream()
    .map(Integer::parseInt)
    .collect(toList());
    
    program.set(1, 12);
    program.set(2, 2);
    
    int instructionPointer = 0;
    while (instructionPointer < program.size()) {
      
      Integer opCode = program.get(instructionPointer);
      
      switch (opCode) {
      case 1:
        Integer pos1 = program.get(instructionPointer + 1);
        Integer pos2 = program.get(instructionPointer + 2);
        Integer destination = program.get(instructionPointer + 3);
        
        program.set(destination, program.get(pos1) + program.get(pos2));
        
        instructionPointer += 4;
        break;
      case 2:
        Integer p1 = program.get(instructionPointer + 1);
        Integer p2 = program.get(instructionPointer + 2);
        Integer d = program.get(instructionPointer + 3);
        
        program.set(d, program.get(p1) * program.get(p2));
        
        instructionPointer += 4;
        break;
      case 99:
        instructionPointer = program.size();
        break;
      default:
        throw new Error("Something went wrong");
      }
    }
    
    System.out.println(program.get(0));
  }
  
  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.2");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Integer> program = Arrays.asList(programInput.split(",")).stream()
    .map(Integer::parseInt)
    .collect(toList());
    
    root: for (int i = 0; i <= 99; ++i) {
      for (int j = 0; j <= 99; ++j) {
        List<Integer> p = new ArrayList<>(program);
        
        int result = executeProgram(p, i, j);
        
        if (result == 19690720) {
          System.out.println(100 * i + j);
          break root;
        }
      }
    }
  }
  
  private int executeProgram(List<Integer> program, int noun, int verb) {
    program.set(1, noun);
    program.set(2, verb);
    
    int instructionPointer = 0;
    while (instructionPointer < program.size()) {
      
      Integer opCode = program.get(instructionPointer);
      
      switch (opCode) {
      case 1:
        Integer pos1 = program.get(instructionPointer + 1);
        Integer pos2 = program.get(instructionPointer + 2);
        Integer destination = program.get(instructionPointer + 3);
        
        program.set(destination, program.get(pos1) + program.get(pos2));
        
        instructionPointer += 4;
        break;
      case 2:
        Integer p1 = program.get(instructionPointer + 1);
        Integer p2 = program.get(instructionPointer + 2);
        Integer d = program.get(instructionPointer + 3);
        
        program.set(d, program.get(p1) * program.get(p2));
        
        instructionPointer += 4;
        break;
      case 99:
        instructionPointer = program.size();
        break;
      default:
        throw new Error("Something went wrong");
      }
    }
    
    return program.get(0);
  }
}
