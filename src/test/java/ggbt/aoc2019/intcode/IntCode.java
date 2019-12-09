package ggbt.aoc2019.intcode;

import java.util.HashMap;
import java.util.List;

import ggbt.aoc2019.common.ThrowingConsumer;
import ggbt.aoc2019.common.ThrowingSupplier;

public class IntCode {

  private final HashMap<Long, Long> program;
  private ThrowingSupplier<Long, Throwable> input;
  private ThrowingConsumer<Long, Throwable> output;

  private long instructionPointer = 0;
  private long relativeBase = 0;
  
  public IntCode(List<Long> program, ThrowingSupplier<Long, Throwable> input, ThrowingConsumer<Long, Throwable> output) {
    this.program = new HashMap<>(program.size() * 4);
    for (int i = 0; i < program.size(); i++) {
      this.program.put((long) i, program.get(i));
    }
    
    this.input = input;
    this.output = output;
  }
  
  public void run() throws Throwable {
    
    while (instructionPointer < program.size()) {
      Instruction instruction = new Instruction(program, instructionPointer, relativeBase);
      
      switch (instruction.opCode) {
      case 1: { // SUM
        
        long param1 = instruction.inParam(1);
        long param2 = instruction.inParam(2);
        long param3 = instruction.outParam(3);
        
        program.put(param3, param1 + param2);
        instructionPointer += 4;
      } break;
      case 2: { // MULTIPLY
        
        long param1 = instruction.inParam(1);
        long param2 = instruction.inParam(2);
        long param3 = instruction.outParam(3);
        
        program.put(param3, param1 * param2);
        instructionPointer += 4;
      } break;
      case 3: { // INPUT
        
        long param1 = instruction.outParam(1);
        
        program.put(param1, input.get());
        instructionPointer += 2;
      } break;
      case 4: { // OUTPUT
        
        long param1 = instruction.inParam(1);
        
        output.accept(param1);
        instructionPointer += 2;
      } break;
      case 5: { // JUMP-IF-TRUE
        
        long param1 = instruction.inParam(1);
        long param2 = instruction.inParam(2);
        
        if (param1 != 0) {
          instructionPointer = param2;
        } else {
          instructionPointer += 3;
        }
      } break;
      case 6: { // JUMP-IF-FALSE
        
        long param1 = instruction.inParam(1);
        long param2 = instruction.inParam(2);
        
        if (param1 == 0) {
          instructionPointer = param2;
        } else {
          instructionPointer += 3;
        }
      } break;
      case 7: { // LESS-THAN
        
        long param1 = instruction.inParam(1);
        long param2 = instruction.inParam(2);
        long param3 = instruction.outParam(3);
        
        if (param1 < param2) {
          program.put(param3, 1L);
        } else {
          program.put(param3, 0L);
        }
        
        instructionPointer += 4;
      } break;
      case 8: { // EQUALS
        
        long param1 = instruction.inParam(1);
        long param2 = instruction.inParam(2);
        long param3 = instruction.outParam(3);
        
        if (param1 == param2) {
          program.put(param3, 1L);
        } else {
          program.put(param3, 0L);
        }
        
        instructionPointer += 4;
      } break;
      case 9: { // RELATIVE-BASE-OFFSET
        
        long param1 = instruction.inParam(1);
        relativeBase += param1;
        
        instructionPointer += 2;
      } break;
      case 99: { // EXIT
        instructionPointer = program.size();
      } break;
      default:
        throw new Error("Something went wrong");
      }
    }
  }
}
