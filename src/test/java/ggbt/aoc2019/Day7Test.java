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

import ggbt.aoc2019.intcode.IntCode;

public class Day7Test {

  @Test
  public void part1() throws Exception {
    Path inputPath = Paths.get("inputs", "day.7");
    
    String programInput = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8);
    
    List<Integer> program = Arrays.asList(programInput.split(",")).stream()
    .map(Integer::parseInt)
    .collect(toList());

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
          
          new IntCode(new ArrayList<>(program), input::take, output::put).run();
        }

        BlockingQueue<Integer> lastAmplifierOutput = amplifiersIO.get(amplifiersIO.size() - 1).get(1);
        return lastAmplifierOutput.take();
      } catch (Throwable e) {
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
              new IntCode(new ArrayList<>(program), input::take, output::put).run();
              
              done.countDown();
            } catch (Throwable e) {
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
}
