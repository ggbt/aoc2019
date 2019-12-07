package ggbt.aoc2019;

import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

public class Day7Test {

  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.7");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Integer> program = Arrays.asList(programInput.split(",")).stream()
    .map(Integer::parseInt)
    .collect(toList());

    ExecutorService exec = Executors.newFixedThreadPool(5);
    
    Collections2.permutations(ImmutableList.of(0, 1, 2, 3, 4)).stream()
    .map(phaseSettings -> {
      try {
        List<List<BlockingQueue<Integer>>> amplifiersIO = new ArrayList<>();
        
        for (int i = 0 ; i < phaseSettings.size(); ++i) {
          int phase = phaseSettings.get(i);
          
          BlockingQueue<Integer> input;
          if (i == 0) {
            input = new ArrayBlockingQueue<>(2);
            input.add(phase);
            input.add(0); // The first amplifier receives input 0.
          } else {
            // The output from the previous amplifier is sent to the current amplifier as input.
            input = amplifiersIO.get(i - 1).get(1);
            input.add(phase);
          }
          
          BlockingQueue<Integer> output = new ArrayBlockingQueue<>(2);

          amplifiersIO.add(ImmutableList.of(input, output));
        }
        
        for (List<BlockingQueue<Integer>> io : amplifiersIO) {
          BlockingQueue<Integer> input = io.get(0);
          BlockingQueue<Integer> output = io.get(1);
          
          exec.submit(() -> {
            try {
              executeProgram(new ArrayList<>(program), input, output);
            } catch (InterruptedException e) {
              e.printStackTrace();
              System.exit(1);
            }
          });
        }

        BlockingQueue<Integer> lastAmplifierOutput = amplifiersIO.get(amplifiersIO.size() - 1).get(1);
        return lastAmplifierOutput.take();
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.exit(1);
        return -1;
      }
    })
    .max(naturalOrder())
    .ifPresent(System.out::println);
  }
  
  @Test
  public void part2() throws Exception {
    Path inputPath = Paths.get("inputs", "day.7");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Integer> program = Arrays.asList(programInput.split(",")).stream()
    .map(Integer::parseInt)
    .collect(toList());

    ExecutorService exec = Executors.newFixedThreadPool(5);
    
    Collections2.permutations(ImmutableList.of(5, 6, 7, 8, 9)).stream()
    .map(phaseSettings -> {
      try {
        List<List<BlockingQueue<Integer>>> amplifiersIO = new ArrayList<>();
        
        for (int i = 0 ; i < phaseSettings.size(); ++i) {
          int phase = phaseSettings.get(i);
          
          BlockingQueue<Integer> input;
          if (i == 0) {
            input = new ArrayBlockingQueue<>(2);
            input.add(phase);
            input.add(0); // The first amplifier receives input 0.
          } else {
            // The output from the previous amplifier is sent to the current amplifier as input.
            input = amplifiersIO.get(i - 1).get(1);
            input.add(phase);
          }
          
          BlockingQueue<Integer> output;
          if (i == phaseSettings.size() - 1) {
            // The last amplifier sends its output to the first amplifier's input forming a loop.
            output = amplifiersIO.get(0).get(0);
          } else {
            output = new ArrayBlockingQueue<>(2);
          }

          amplifiersIO.add(ImmutableList.of(input, output));
        }
        
        CountDownLatch done = new CountDownLatch(5);
        
        for (List<BlockingQueue<Integer>> io : amplifiersIO) {
          BlockingQueue<Integer> input = io.get(0);
          BlockingQueue<Integer> output = io.get(1);
          
          exec.submit(() -> {
            try {
              executeProgram(new ArrayList<>(program), input, output);
              done.countDown();
            } catch (InterruptedException e) {
              e.printStackTrace();
              System.exit(1);
            }
          });
        }
        
        done.await();

        BlockingQueue<Integer> lastAmplifierOutput = amplifiersIO.get(amplifiersIO.size() - 1).get(1);
        return lastAmplifierOutput.take();
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.exit(1);
        return -1;
      }
    })
    .max(naturalOrder())
    .ifPresent(System.out::println);
  }
  
  private int executeProgram(List<Integer> program, BlockingQueue<Integer> in, BlockingQueue<Integer> out) throws InterruptedException {

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
        
        Integer take = in.take();
        program.set(output, take);
        
        instructionPointer += 2;
      } break;
      case 4: { // log
        int param1 = program.get(instructionPointer + 1);
        
        immediateMode = opCodeParts.charAt(opCodePointer) == '1';
        opCodePointer--;
        int param1Value = immediateMode ? param1 : program.get(param1);
        
        out.put(param1Value);
        
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
