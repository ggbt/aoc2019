package ggbt.aoc2019.common;

public class Point3 {

  public long x;
  public long y;
  public long z;
  
  public Point3(long x, long y, long z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Point3(Point3 other) {
    this.x = other.x;
    this.y = other.y;
    this.z = other.z;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (x ^ (x >>> 32));
    result = prime * result + (int) (y ^ (y >>> 32));
    result = prime * result + (int) (z ^ (z >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Point3 other = (Point3) obj;
    if (x != other.x)
      return false;
    if (y != other.y)
      return false;
    if (z != other.z)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + "," + z + ")";
  }
}
