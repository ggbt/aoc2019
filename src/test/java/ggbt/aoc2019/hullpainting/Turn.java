package ggbt.aoc2019.hullpainting;

public enum Turn {
  LEFT,
  RIGHT;
  
  public static Turn from(long instruction) {
    if (instruction == 0) {
      return LEFT;
    } else {
      return RIGHT;
    }
  }
}
