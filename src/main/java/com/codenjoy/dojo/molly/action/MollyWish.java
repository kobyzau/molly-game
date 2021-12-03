package com.codenjoy.dojo.molly.action;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MollyWish {
  private final MollyAction mollyAction;
  private final int steps;
  private final MollyWishLevel wishLevel;

  @Override
  public String toString() {
    return "[" + mollyAction + ':' + steps + ':' + wishLevel + ':' + getWeight() + ']';
  }

  public double getWeight() {
    double levelWeight = wishLevel.getWeight();
    double stepWeight = getStepWeight(steps);
    return levelWeight * stepWeight;
  }

  private double getStepWeight(int steps) {
    if (steps < 0) {
      return 0.000000000001;
    }
    return 1 / (1 + Math.exp(steps / 3d));
  }
}
