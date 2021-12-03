package com.codenjoy.dojo.molly.vision.builder.stage;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.molly.vision.builder.MatrixBuilderStage;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.utils.BoardUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FindDeadStepsMatrixBuilderStage implements MatrixBuilderStage {

  @Override
  public void processMatrix(Matrix matrix) {
    Map<PointKey, Set<Integer>> explodedPoints = new HashMap<>();
    for (int second = 1; second <= 5; second++) {
      List<PointKey> listOfBombs = getListOfBombs(matrix, second);
      Set<PointKey> fireWays = new HashSet<>();
      for (PointKey bomb : listOfBombs) {
        fireWays.addAll(getFireWay(bomb, Direction.UP, matrix));
        fireWays.addAll(getFireWay(bomb, Direction.DOWN, matrix));
        fireWays.addAll(getFireWay(bomb, Direction.LEFT, matrix));
        fireWays.addAll(getFireWay(bomb, Direction.RIGHT, matrix));
      }
      for (PointKey fireWay : fireWays) {
        Set<Integer> timeList = explodedPoints.getOrDefault(fireWay, new HashSet<>());
        timeList.add(second);
        explodedPoints.put(fireWay, timeList);
      }
    }
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    for (PointKey pointKey : cellInfoMap.keySet()) {
      if (explodedPoints.containsKey(pointKey)) {
        cellInfoMap.get(pointKey).setFiredAfterSeconds(explodedPoints.get(pointKey));
      }
    }
  }

  private List<PointKey> getFireWay(PointKey pointKey, Direction direction, Matrix matrix) {
    Board board = matrix.getBoard();
    List<PointKey> emptyCells = new ArrayList<>();
    pointKey = pointKey.next(direction);
    if (!BoardUtil.isFreeForFire(board, pointKey)) {
      return emptyCells;
    }
    emptyCells.add(pointKey);
    pointKey = pointKey.next(direction);
    if (!BoardUtil.isFreeForFire(board, pointKey)) {
      return emptyCells;
    }
    emptyCells.add(pointKey);
    pointKey = pointKey.next(direction);
    if (!BoardUtil.isFreeForFire(board, pointKey)) {
      return emptyCells;
    }
    emptyCells.add(pointKey);

    return emptyCells;
  }

  private List<PointKey> getListOfBombs(Matrix matrix, int seconds) {
    Board board = matrix.getBoard();
    Element bombType;
    switch (seconds) {
      case 1:
        bombType = Element.POTION_TIMER_1;
        break;
      case 2:
        bombType = Element.POTION_TIMER_2;
        break;
      case 3:
        bombType = Element.POTION_TIMER_3;
        break;
      case 4:
        bombType = Element.POTION_TIMER_4;
        break;
      case 5:
        bombType = Element.POTION_TIMER_5;
        break;
      default:
        return Collections.emptyList();
    }
    return board.get(bombType).stream()
        .map(p -> new PointKey(p.getX(), p.getY()))
        .collect(Collectors.toList());
  }
}
