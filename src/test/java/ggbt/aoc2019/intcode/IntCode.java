package ggbt.aoc2019.intcode;

import java.util.List;

import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.api.function.ThrowingSupplier;

public class IntCode {

  private List<Integer> program;
  private ThrowingSupplier<Integer> input;
  private ThrowingConsumer<Integer> output;

  private int instructionPointer = 0;
  
  public IntCode(List<Integer> program, ThrowingSupplier<Integer> input, ThrowingConsumer<Integer> output) {
    this.program = program;
    this.input = input;
    this.output = output;
  }
  
  public void run() throws Throwable {
    
    while (instructionPointer < program.size()) {
      Instruction instruction = new Instruction(program, instructionPointer);
      
      switch (instruction.opCode) {
      case 1: { // SUM
        
        int param1 = instruction.computeParam(1);
        int param2 = instruction.computeParam(2);
        int param3 = instruction.getParam(3);
        
        program.set(param3, param1 + param2);
        instructionPointer += 4;
      } break;
      case 2: { // MULTIPLY
        
        int param1 = instruction.computeParam(1);
        int param2 = instruction.computeParam(2);
        int param3 = instruction.getParam(3);
        
        program.set(param3, param1 * param2);
        instructionPointer += 4;
      } break;
      case 3: { // INPUT
        
        int param1 = instruction.getParam(1);
        
        program.set(param1, input.get());
        instructionPointer += 2;
      } break;
      case 4: { // OUTPUT
        
        int param1 = instruction.computeParam(1);
        
        output.accept(param1);
        instructionPointer += 2;
      } break;
      case 5: { // JUMP-IF-TRUE
        
        int param1 = instruction.computeParam(1);
        int param2 = instruction.computeParam(2);
        
        if (param1 != 0) {
          instructionPointer = param2;
        } else {
          instructionPointer += 3;
        }
      } break;
      case 6: { // JUMP-IF-FALSE
        
        int param1 = instruction.computeParam(1);
        int param2 = instruction.computeParam(2);
        
        if (param1 == 0) {
          instructionPointer = param2;
        } else {
          instructionPointer += 3;
        }
      } break;
      case 7: { // LESS-THAN
        
        int param1 = instruction.computeParam(1);
        int param2 = instruction.computeParam(2);
        int param3 = instruction.getParam(3);
        
        if (param1 < param2) {
          program.set(param3, 1);
        } else {
          program.set(param3, 0);
        }
        
        instructionPointer += 4;
      } break;
      case 8: { // EQUALS
        
        int param1 = instruction.computeParam(1);
        int param2 = instruction.computeParam(2);
        int param3 = instruction.getParam(3);
        
        if (param1 == param2) {
          program.set(param3, 1);
        } else {
          program.set(param3, 0);
        }
        
        instructionPointer += 4;
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
