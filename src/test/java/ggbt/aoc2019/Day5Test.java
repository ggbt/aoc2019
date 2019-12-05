package ggbt.aoc2019;

import static java.util.stream.Collectors.toList;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Day5Test {

  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.5");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Integer> program = Arrays.asList(programInput.split(",")).stream()
    .map(Integer::parseInt)
    .collect(toList());
    
    executeProgram(program, 1);
  }

  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.5");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Integer> program = Arrays.asList(programInput.split(",")).stream()
    .map(Integer::parseInt)
    .collect(toList());
    
    executeProgram(program, 5);
  }
  
  private int executeProgram(List<Integer> program, int input) {
    int instructionPointer = 0;
    while (instructionPointer < program.size()) {
      
      String opCodeParts = String.format("%05d" , program.get(instructionPointer));
      
      int opCodePointer = opCodeParts.length() - 1;
      
      int opCode = Integer.parseInt(opCodeParts.substring(opCodeParts.length() - 2));
      opCodePointer -= 2;

      boolean immediateMode;
      
      switch (opCode) {
      case 1: { // sum
        int param1 = program.get(instructionPointer + 1);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param1Value = immediateMode ? param1 : program.get(param1);
        
        int param2 = program.get(instructionPointer + 2);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param2Value = immediateMode ? param2 : program.get(param2);
        
        int output = program.get(instructionPointer + 3);
        program.set(output, param1Value + param2Value);
        
        instructionPointer += 4;
      } break;
      case 2: { // multiply
        int param1 = program.get(instructionPointer + 1);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param1Value = immediateMode ? param1 : program.get(param1);
        
        int param2 = program.get(instructionPointer + 2);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param2Value = immediateMode ? param2 : program.get(param2);
        
        int output = program.get(instructionPointer + 3);
        
        program.set(output, param1Value * param2Value);
        
        instructionPointer += 4;
      } break;
      case 3: { // save input at position
        int output = program.get(instructionPointer + 1);
        program.set(output, input);
        
        instructionPointer += 2;
      } break;
      case 4: { // log
        int param1 = program.get(instructionPointer + 1);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param1Value = immediateMode ? param1 : program.get(param1);
        
        System.out.println(param1Value);
        
        instructionPointer += 2;
      } break;
      case 5: { // jump-if-true
        int param1 = program.get(instructionPointer + 1);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param1Value = immediateMode ? param1 : program.get(param1);
        
        int param2 = program.get(instructionPointer + 2);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param2Value = immediateMode ? param2 : program.get(param2);
        
        if (param1Value != 0) {
          instructionPointer = param2Value;
        } else { // does nothing
          instructionPointer += 3;
        }
        
      } break;
      case 6: { // jump-if-false
        int param1 = program.get(instructionPointer + 1);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param1Value = immediateMode ? param1 : program.get(param1);
        
        int param2 = program.get(instructionPointer + 2);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param2Value = immediateMode ? param2 : program.get(param2);
        
        if (param1Value == 0) {
          instructionPointer = param2Value;
        } else { // does nothing
          instructionPointer += 3;
        }
      } break;
      case 7: { // less than
        int param1 = program.get(instructionPointer + 1);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param1Value = immediateMode ? param1 : program.get(param1);

        int param2 = program.get(instructionPointer + 2);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param2Value = immediateMode ? param2 : program.get(param2);
        
        int output = program.get(instructionPointer + 3);
        
        if (param1Value < param2Value) {
          program.set(output, 1);
        } else {
          program.set(output, 0);
        }
        
        instructionPointer += 4;
        
      } break;
      case 8: { // equals
        int param1 = program.get(instructionPointer + 1);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param1Value = immediateMode ? param1 : program.get(param1);

        int param2 = program.get(instructionPointer + 2);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param2Value = immediateMode ? param2 : program.get(param2);
        
        int output = program.get(instructionPointer + 3);
        
        if (param1Value == param2Value) {
          program.set(output, 1);
        } else {
          program.set(output, 0);
        }
        
        instructionPointer += 4;
        
      } break;
      case 99: { // exit
        instructionPointer = program.size();
      } break;
      default:
        throw new Error("Something went wrong");
      }
    }
    
    return program.get(0);
  }
}
