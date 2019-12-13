package ggbt.aoc2019.common;

public class Point {
  public long x;
  public long y;
  
  public Point(long x, long y) {
    this.x = x;
    this.y = y;
  }

  public Point(Point other) {
    this.x = other.x;
    this.y = other.y;
  }
  
  public long distance(Point other) {
    return Math.abs(x - other.x) + Math.abs(y - other.y);
  }
  
  public void moveOrigin(Point newOrigin) {
    this.x = this.x - newOrigin.x;
    this.y = this.y - newOrigin.y;
    this.y *= -1;
  }
  
  @Override
  public String toString() {
    return "[" + x + ", " + y + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (x ^ (x >>> 32));
    result = prime * result + (int) (y ^ (y >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Point other = (Point) obj;
    if (x != other.x)
      return false;
    if (y != other.y)
      return false;
    return true;
  }
}
