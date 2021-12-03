package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.model.PointKey;

public class BlockDeadCase implements DeadCase {

  @Override
  public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
    int numStepsToGo = matrix.getCellInfoMap().get(newPosition).getNumStepsToGo();
    return numStepsToGo < 0;
  }
}
