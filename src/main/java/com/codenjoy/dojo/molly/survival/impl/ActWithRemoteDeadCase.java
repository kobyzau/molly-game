package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.molly.memory.MollyMemory;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;

public class ActWithRemoteDeadCase implements DeadCase {

  @Override
  public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
    boolean hasRemote = MollyMemory.getInstance().isHasRemote();
    if (!hasRemote || !leftBomb) {
      return false;
    }
    return !matrix.getCellInfoMap().get(newPosition).getFiredAfterSeconds().isEmpty();
  }
}
