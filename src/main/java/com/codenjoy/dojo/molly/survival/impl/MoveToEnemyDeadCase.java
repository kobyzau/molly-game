package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;

public class MoveToEnemyDeadCase implements DeadCase {
  @Override
  public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
    if (hero.equals(newPosition)) {
      return false;
    }
    return matrix
        .getBoard()
        .get(
            Element.ENEMY_HERO,
            Element.ENEMY_POTION_HERO,
            Element.OTHER_HERO,
            Element.OTHER_POTION_HERO)
        .stream()
        .map(PointKey::new)
        .anyMatch(newPosition::equals);
  }
}
