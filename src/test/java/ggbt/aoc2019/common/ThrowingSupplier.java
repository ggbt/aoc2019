package ggbt.aoc2019.common;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {

  T get() throws E;
}
