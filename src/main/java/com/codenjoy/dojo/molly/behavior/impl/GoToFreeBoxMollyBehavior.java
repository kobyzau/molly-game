package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishLevel;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.molly.vision.MollyVision;
import com.codenjoy.dojo.utils.BoardUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GoToFreeBoxMollyBehavior implements MollyBehavior {

  private final MollyVision mollyVision = new MollyVision();

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    Board board = matrix.getBoard();
    PointKey hero = new PointKey(board.getHero());
    Set<PointKey> deadBoxes =
        board.getPotions().stream()
            .map(PointKey::new)
            .map(p -> mollyVision.findBoxesWhichCanBeBlowUp(board, p))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    List<PointKey> freeBoxes =
        board.getTreasureBoxes().stream()
            .map(PointKey::new)
            .filter(p -> !deadBoxes.contains(p))
            .collect(Collectors.toList());
    return freeBoxes.stream()
        .map(p -> getNearBoxPoint(p, matrix))
        .flatMap(Collection::stream)
        .distinct()
        .filter(p -> matrix.getCellInfoMap().get(p).getNumStepsToGo() > 0)
        .map(p -> getWishByBoxPoint(hero, p, matrix))
        .flatMap(Collection::stream)
        .sorted(Comparator.comparing(MollyWish::getSteps).reversed())
        .collect(Collectors.toList());
  }

  private List<PointKey> getNearBoxPoint(PointKey boxPoint, Matrix matrix) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    List<PointKey> nearPoints = BoardUtil.getNearPoints(boxPoint);
    int minStep = Integer.MAX_VALUE;
    List<PointKey> bestPoints = new ArrayList<>();
    for (PointKey point : nearPoints) {
      int numStepsToGo = cellInfoMap.get(point).getNumStepsToGo();
      if (numStepsToGo < 0) {
        continue;
      }
      if (numStepsToGo == minStep) {
        bestPoints.add(point);
      } else if (numStepsToGo < minStep) {
        bestPoints.clear();
        bestPoints.add(point);
        minStep = numStepsToGo;
      }
    }
    return bestPoints;
  }

  private List<MollyWish> getWishByBoxPoint(PointKey hero, PointKey pointKey, Matrix matrix) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    int numStepsToGo = cellInfoMap.get(pointKey).getNumStepsToGo();
    Set<PointKey> nearPoints = new HashSet<>(BoardUtil.getNearPoints(hero));
    List<PointKey> nextSteps =
        BoardUtil.getBestWay(pointKey, matrix.getCellInfoMap()).stream()
            .filter(nearPoints::contains)
            .collect(Collectors.toList());

    return nextSteps.stream()
        .map(p -> BoardUtil.getDirection(hero, p))
        .map(d -> new MollyWish(MollyAction.fromDirection(d), numStepsToGo, MollyWishLevel.LOW))
        .collect(Collectors.toList());
  }
}
