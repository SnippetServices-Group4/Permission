package com.services.group4.permission.mock;

import com.services.group4.permission.model.Reader;
import java.util.Arrays;
import java.util.List;

public class ReaderFixtures {
  public static List<Reader> all() {
    return Arrays.asList(new Reader(1L, 1L), new Reader(2L, 2L), new Reader(3L, 3L));
  }
}
