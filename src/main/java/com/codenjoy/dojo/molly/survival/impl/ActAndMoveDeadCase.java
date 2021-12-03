package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.utils.BoardUtil;

import java.util.Map;

public class ActAndMoveDeadCase implements DeadCase {

  @Override
  public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
    if (hero.equals(newPosition)) {
      return false;
    }
    if (!leftBomb) {
      return false;
    }
    return isTunnel(hero, newPosition, matrix);
  }

  private boolean isTunnel(PointKey hero, PointKey newPosition, Matrix matrix) {
    Direction direction = BoardUtil.getDirection(hero, newPosition);

    boolean isVertical = hero.getX() == newPosition.getX();
    PointKey checkPoint = newPosition;
    for (int i = 1; i <= 4; i++) {
      if (!canMoveTo(matrix, checkPoint, i)) {
        return true;
      }
      if (isVertical
          && (canMoveTo(matrix, new PointKey(checkPoint.getX() + 1, checkPoint.getY()), i + 1)
              || canMoveTo(
                  matrix, new PointKey(checkPoint.getX() - 1, checkPoint.getY()), i + 1))) {
        return false;
      }
      if (!isVertical
          && (canMoveTo(matrix, new PointKey(checkPoint.getX(), checkPoint.getY() + 1), i + 1)
              || canMoveTo(
                  matrix, new PointKey(checkPoint.getX(), checkPoint.getY() - 1), i + 1))) {
        return false;
      }
      checkPoint = checkPoint.next(direction);
    }

    return !canMoveTo(matrix, checkPoint, 5);
  }

  private boolean canMoveTo(Matrix matrix, PointKey pointKey, int second) {
    Board board = matrix.getBoard();
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    return cellInfoMap.get(pointKey).getNumStepsToGo() == second
        && BoardUtil.isFreeCell(board, pointKey.getX(), pointKey.getY());
  }
}
