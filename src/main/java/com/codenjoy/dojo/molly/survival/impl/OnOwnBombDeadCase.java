package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;

import static com.codenjoy.dojo.games.mollymage.Element.POTION_HERO;

public class OnOwnBombDeadCase implements DeadCase {
  @Override
  public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
    boolean isOnBomb = !matrix.getBoard().get(POTION_HERO).isEmpty();
    if (!isOnBomb) {
      return false;
    }
    if (leftBomb) {
      return true;
    }
    if (hero.equals(newPosition)) {
      return true;
    }
    return false;
  }
}
