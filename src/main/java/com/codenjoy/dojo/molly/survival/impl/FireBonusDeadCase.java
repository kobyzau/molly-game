package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MollyVision;

import java.util.List;

public class FireBonusDeadCase implements DeadCase {

  private final MollyVision mollyVision = new MollyVision();

  @Override
  public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
    if (!leftBomb) {
      return false;
    }
    List<PointKey> bonuses = mollyVision.findBonusesWhichCanBeBlowUp(matrix.getBoard(), hero);
    return !bonuses.isEmpty();
  }
}
