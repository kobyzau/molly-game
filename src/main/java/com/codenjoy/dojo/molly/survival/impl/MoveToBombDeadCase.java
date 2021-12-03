package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;

public class MoveToBombDeadCase implements DeadCase {
  @Override
  public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
    if (hero.equals(newPosition)) {
      return false;
    }
    return matrix
        .getBoard()
        .get(
            Element.POTION_TIMER_1,
            Element.POTION_TIMER_2,
            Element.POTION_TIMER_3,
            Element.POTION_TIMER_4,
            Element.POTION_TIMER_5)
        .stream()
        .map(PointKey::new)
        .anyMatch(newPosition::equals);
  }
}
