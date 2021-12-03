package com.codenjoy.dojo.molly.survival;

import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.model.PointKey;

public interface DeadCase {
  boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb);
}
