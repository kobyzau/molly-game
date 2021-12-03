package com.codenjoy.dojo.molly.action;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum MollyWishLevel {
  LOW(1),
  MEDIUM(5),
  HIGH(10),
  TOP(20);

  private final int weight;

  public double getWeight() {
    double total = Arrays.stream(values()).mapToInt(v -> v.weight).sum();
    return weight / total;
  }
}
