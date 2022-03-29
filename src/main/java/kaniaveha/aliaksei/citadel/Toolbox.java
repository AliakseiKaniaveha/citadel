package kaniaveha.aliaksei.citadel;

import java.util.Iterator;

public class Toolbox {

  public static <T> T extractSingleton(
      Iterable<T> iterable, String noElementErrorMessage, String moreThanOneElementErrorMessage) {
    Iterator<T> iterator = iterable.iterator();
    if (!iterator.hasNext()) {
      throw new IllegalStateException(noElementErrorMessage);
    }
    T value = iterator.next();
    if (iterator.hasNext()) {
      throw new IllegalStateException(moreThanOneElementErrorMessage);
    }
    return value;
  }
}
