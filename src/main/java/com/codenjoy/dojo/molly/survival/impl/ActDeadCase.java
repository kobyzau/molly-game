package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.services.Direction;

import java.util.Map;

public class ActDeadCase implements DeadCase {

  @Override
  public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
    if (!hero.equals(newPosition)) {
      return false;
    }
    if (!leftBomb) {
      return false;
    }
    if (hasWay(hero, Direction.UP, matrix)) {
      return false;
    }
    if (hasWay(hero, Direction.DOWN, matrix)) {
      return false;
    }
    if (hasWay(hero, Direction.LEFT, matrix)) {
      return false;
    }
    if (hasWay(hero, Direction.RIGHT, matrix)) {
      return false;
    }
    return true;
  }

  private boolean hasWay(PointKey hero, Direction direction, Matrix matrix) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    PointKey checkPoint = hero.next(direction);
    boolean isVertical = hero.getX() == checkPoint.getX();
    for (int i = 1; i <= 3; i++) {
      if (cellInfoMap.get(checkPoint).getNumStepsToGo() != i) {
        return false;
      }
      if (isVertical
          && (cellInfoMap
                      .get(new PointKey(checkPoint.getX() + 1, checkPoint.getY()))
                      .getNumStepsToGo()
                  == (i + 1)
              || cellInfoMap
                      .get(new PointKey(checkPoint.getX() - 1, checkPoint.getY()))
                      .getNumStepsToGo()
                  == (i + 1))) {
        return true;
      }
      if (!isVertical
          && (cellInfoMap
                      .get(new PointKey(checkPoint.getX(), checkPoint.getY() + 1))
                      .getNumStepsToGo()
                  == (i + 1)
              || cellInfoMap
                      .get(new PointKey(checkPoint.getX(), checkPoint.getY() - 1))
                      .getNumStepsToGo()
                  == (i + 1))) {
        return true;
      }
      checkPoint = checkPoint.next(direction);
    }

    return false;
  }
}
