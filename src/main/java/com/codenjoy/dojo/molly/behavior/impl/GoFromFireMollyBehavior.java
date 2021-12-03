package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishLevel;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.utils.BoardUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GoFromFireMollyBehavior implements MollyBehavior {

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    PointKey hero = new PointKey(matrix.getBoard().getHero());
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    boolean noFire = cellInfoMap.get(hero).getFiredAfterSeconds().isEmpty();
    if (noFire) {
      return Collections.emptyList();
    }
    List<PointKey> greenPoints =
        cellInfoMap.keySet().stream()
            .filter(p -> isGreenCell(p, matrix))
            .collect(Collectors.toList());
    int minStep = Integer.MAX_VALUE;
    List<PointKey> nearestPoints = new ArrayList<>();
    for (PointKey greenPoint : greenPoints) {
      int numStepsToGo = cellInfoMap.get(greenPoint).getNumStepsToGo();
      if (numStepsToGo == minStep) {
        nearestPoints.add(greenPoint);
      } else if (numStepsToGo < minStep) {
        nearestPoints.clear();
        nearestPoints.add(greenPoint);
        minStep = numStepsToGo;
      }
    }

    return nearestPoints.stream()
        .distinct()
        .map(p -> getWishByPoint(hero, p, matrix))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  private boolean isGreenCell(PointKey pointKey, Matrix matrix) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    MatrixCellInfo matrixCellInfo = cellInfoMap.get(pointKey);
    return matrixCellInfo.getFiredAfterSeconds().isEmpty()
        && matrixCellInfo.getNumStepsToGo() < 7
        && matrixCellInfo.getNumStepsToGo() >= 0
        && BoardUtil.isFreeCell(matrix.getBoard(), pointKey.getX(), pointKey.getY());
  }

  private List<MollyWish> getWishByPoint(PointKey hero, PointKey pointKey, Matrix matrix) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    int numStepsToGo = cellInfoMap.get(pointKey).getNumStepsToGo();
    Set<PointKey> nearPoints = new HashSet<>(BoardUtil.getNearPoints(hero));
    List<PointKey> nextSteps =
        BoardUtil.getBestWay(pointKey, matrix.getCellInfoMap()).stream()
            .filter(nearPoints::contains)
            .collect(Collectors.toList());

    return nextSteps.stream()
        .map(p -> BoardUtil.getDirection(hero, p))
        .map(d -> new MollyWish(MollyAction.fromDirection(d), numStepsToGo, MollyWishLevel.TOP))
        .collect(Collectors.toList());
  }
}
