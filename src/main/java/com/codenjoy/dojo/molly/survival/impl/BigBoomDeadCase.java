package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;

public class BigBoomDeadCase {

  public boolean willDie(Matrix matrix, PointKey hero) {
    return !matrix.getCellInfoMap().get(hero).getFiredAfterSeconds().isEmpty();
  }
}
