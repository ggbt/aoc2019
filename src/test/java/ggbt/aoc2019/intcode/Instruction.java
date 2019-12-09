package ggbt.aoc2019.intcode;

import java.util.HashMap;

public class Instruction {

  private final HashMap<Long, Long> program;
  private final long instructionPointer;
  private final long relativeBase;
  
  private final String opCodeParts;
  private final int opCodePointer;
  
  public final int opCode;
  
  
  public Instruction(HashMap<Long, Long> program, long instructionPointer, long relativeBase) {
    this.program = program;
    
    this.instructionPointer = instructionPointer;
    this.relativeBase = relativeBase;
    
    this.opCodeParts = String.format("%05d" , program.computeIfAbsent(instructionPointer, k -> 0L));
    this.opCodePointer = opCodeParts.length() - 2;
    
    this.opCode = Integer.parseInt(opCodeParts.substring(opCodeParts.length() - 2));
  }
  
  public long outParam(int index) {
    long param = program.computeIfAbsent(instructionPointer + index, k -> 0L);
    
    boolean relativeMode = opCodeParts.charAt(opCodePointer - index) == '2';
    
    if (relativeMode) {
      param += relativeBase;
    }
    
    return param;
  }
  
  public long inParam(int index) {
    long param = program.computeIfAbsent(instructionPointer + index, k -> 0L);
    
    boolean immediateMode = opCodeParts.charAt(opCodePointer - index) == '1';
    boolean relativeMode = opCodeParts.charAt(opCodePointer - index) == '2';
    
    if (immediateMode) {
      return param;
    } else if (relativeMode) {
      return program.computeIfAbsent(param + relativeBase, k -> 0L);
    } else {
      return program.computeIfAbsent(param, k -> 0L);
    }
  }
}
