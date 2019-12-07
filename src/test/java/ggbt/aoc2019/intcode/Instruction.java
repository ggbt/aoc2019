package ggbt.aoc2019.intcode;

import java.util.List;

public class Instruction {

  private final List<Integer> program;
  private final int instructionPointer;
  
  private final String opCodeParts;
  private final int opCodePointer;
  
  public final int opCode;
  
  public Instruction(List<Integer> program, int instructionPointer) {
    this.program = program;
    this.instructionPointer = instructionPointer;
    
    this.opCodeParts = String.format("%05d" , program.get(instructionPointer));
    this.opCodePointer = opCodeParts.length() - 2;
    
    this.opCode = Integer.parseInt(opCodeParts.substring(opCodeParts.length() - 2));
  }
  
  public int getParam(int index) {
    return program.get(instructionPointer + index);
  }
  
  public int computeParam(int index) {
    int param = program.get(instructionPointer + index);
    boolean immediateMode = opCodeParts.charAt(opCodePointer - index) == '1';
    
    return immediateMode ? param : program.get(param);
  }
}
