package ggbt.aoc2019.hullpainting;

public enum Color {
  BLACK(0L),
  WHITE(1L);
  
  public final long value;

  private Color(long color) {
    this.value = color;
  }
  
  public static Color from(long instruction) {
    if (instruction == 0) {
      return BLACK;
    } else {
      return WHITE;
    }
  }
}
