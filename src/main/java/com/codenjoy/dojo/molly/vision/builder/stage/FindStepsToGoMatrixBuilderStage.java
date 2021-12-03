package com.codenjoy.dojo.molly.vision.builder.stage;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.molly.vision.builder.MatrixBuilderStage;
import com.codenjoy.dojo.utils.BoardUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class FindStepsToGoMatrixBuilderStage implements MatrixBuilderStage {

  @Override
  public void processMatrix(Matrix matrix) {
    Map<PointKey, Integer> numSteps = getSteps(matrix);
    for (PointKey pointKey : numSteps.keySet()) {
      MatrixCellInfo matrixCellInfo = matrix.getCellInfoMap().get(pointKey);
      matrixCellInfo.setNumStepsToGo(numSteps.get(pointKey));
    }
  }

  private Map<PointKey, Integer> getSteps(Matrix matrix) {
    Board board = matrix.getBoard();
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    PointKey hero = new PointKey(board.getHero());

    List<PointKey> needToCheckPoints = new ArrayList<>(BoardUtil.getNearPoints(hero));
    Map<PointKey, Integer> numOfSteps = new HashMap<>();
    numOfSteps.put(hero, 0);
    for (int second = 1; second < 35; second++) {
      needToCheckPoints = needToCheckPoints.stream().distinct().collect(Collectors.toList());
      if (needToCheckPoints.isEmpty()) {
        break;
      }
      List<PointKey> freePoints = new ArrayList<>();
      for (PointKey needToCheckPoint : needToCheckPoints) {
        if (numOfSteps.containsKey(needToCheckPoint)) {
          continue;
        }
        if (BoardUtil.canHeroMoveTo(board, needToCheckPoint)) {
          freePoints.add(needToCheckPoint);
        }
      }
      needToCheckPoints.clear();
      for (PointKey freePoint : freePoints) {
        if (cellInfoMap.get(freePoint).getFiredAfterSeconds().contains(second)) {
          numOfSteps.put(freePoint, -1);
          continue;
        }
        List<PointKey> nearPoints = BoardUtil.getNearPoints(freePoint);
        OptionalInt nearSteps =
            nearPoints.stream()
                .map(numOfSteps::get)
                .filter(Objects::nonNull)
                .filter(v -> v >= 0)
                .mapToInt(Integer::intValue)
                .findFirst();
        if (nearSteps.isPresent()) {
          numOfSteps.put(freePoint, second);
          needToCheckPoints.addAll(nearPoints);
        }
      }
    }
    return numOfSteps;
  }
}
