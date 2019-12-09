package ggbt.aoc2019.common;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {

  void accept(T t) throws E;
}