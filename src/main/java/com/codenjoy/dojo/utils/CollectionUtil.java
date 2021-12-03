package com.codenjoy.dojo.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CollectionUtil {

  private static final Random RANDOM = new Random();

  public static boolean isEmpty(Collection<?> c) {
    return c == null || c.isEmpty();
  }

  public static boolean isNotEmpty(Collection<?> c) {
    return !isEmpty(c);
  }

  public static <T> T getRandomValue(Collection<T> c) {
    if (isEmpty(c)) {
      throw new RuntimeException("Empty collection in getRandomValue");
    }
    return getRandomList(c).get(0);
  }

  public static <T> List<T> getRandomList(Collection<T> c) {
    List<T> list = new ArrayList<>(c);
    Collections.shuffle(list, RANDOM);
    return list;
  }
}
