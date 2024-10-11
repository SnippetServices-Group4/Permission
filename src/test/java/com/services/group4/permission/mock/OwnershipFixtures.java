package com.services.group4.permission.mock;

import com.services.group4.permission.model.Ownership;

import java.util.Arrays;
import java.util.List;

public class OwnershipFixtures {
  public static List<Ownership> all() {
    return Arrays.asList(
        new Ownership(1L,1L),
        new Ownership(2L,2L),
        new Ownership(3L,3L)
    );
  }
}
