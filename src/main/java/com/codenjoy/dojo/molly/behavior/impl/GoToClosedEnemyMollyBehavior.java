package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishLevel;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.memory.MollyMemory;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.utils.BoardUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GoToClosedEnemyMollyBehavior implements MollyBehavior {

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    List<PointKey> fixedEnemies = MollyMemory.getInstance().getFixedEnemies(matrix.getBoard());
    List<PointKey> currentEnemies = new ArrayList<>();
    matrix.getBoard().getEnemyHeroes().stream().map(PointKey::new).forEach(currentEnemies::add);
    matrix.getBoard().getOtherHeroes().stream().map(PointKey::new).forEach(currentEnemies::add);
    if (!new HashSet<>(fixedEnemies).equals(new HashSet<>(currentEnemies))) {
      return Collections.emptyList();
    }
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    boolean hasOpenEnemies =
        fixedEnemies.stream().anyMatch(e -> cellInfoMap.get(e).getNumStepsToGo() >= 0);
    if (hasOpenEnemies) {
      return Collections.emptyList();
    }
    PointKey hero = new PointKey(matrix.getBoard().getHero());
    return getEntryToClosedEnemies(matrix, fixedEnemies)
        .map(e -> getWishByBoxPoint(hero, e, matrix))
        .orElseGet(Collections::emptyList);
  }

  private Optional<PointKey> getEntryToClosedEnemies(Matrix matrix, List<PointKey> enemies) {

    List<PointKey> entryPoints = new ArrayList<>();
    for (PointKey enemy : enemies) {
      getEntry(matrix, enemy).ifPresent(entryPoints::add);
    }
    int minSteps = Integer.MAX_VALUE;
    Optional<PointKey> minEntry = Optional.empty();
    for (PointKey entryPoint : entryPoints) {
      int steps = matrix.getCellInfoMap().get(entryPoint).getNumStepsToGo();
      if (steps < minSteps) {
        minSteps = steps;
        minEntry = Optional.of(entryPoint);
      }
    }
    return minEntry;
  }

  private Optional<PointKey> getEntry(Matrix matrix, PointKey enemy) {
    Set<PointKey> checkedPoints = new HashSet<>();
    Board board = matrix.getBoard();
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    List<PointKey> needToCheck = new ArrayList<>();
    needToCheck.add(enemy);
    for (int i = 0; i < 10; i++) {
      if (needToCheck.isEmpty()) {
        break;
      }
      List<PointKey> nextPoints = new ArrayList<>();
      for (PointKey pointKey : needToCheck) {
        if (cellInfoMap.get(pointKey).getNumStepsToGo() > 0) {
          return Optional.of(pointKey);
        }
        if (checkedPoints.contains(pointKey)) {
          continue;
        }
        if (board.getAt(pointKey.getX(), pointKey.getY()) == Element.WALL) {
          continue;
        }
        checkedPoints.add(pointKey);
        nextPoints.addAll(BoardUtil.getNearPoints(pointKey));
      }
      needToCheck.clear();
      needToCheck.addAll(nextPoints);
    }

    return Optional.empty();
  }

  private List<MollyWish> getWishByBoxPoint(PointKey hero, PointKey pointKey, Matrix matrix) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    int numStepsToGo = Math.min(cellInfoMap.get(pointKey).getNumStepsToGo(), 10);
    Set<PointKey> nearPoints = new HashSet<>(BoardUtil.getNearPoints(hero));
    List<PointKey> nextSteps =
        BoardUtil.getBestWay(pointKey, matrix.getCellInfoMap()).stream()
            .filter(nearPoints::contains)
            .collect(Collectors.toList());

    return nextSteps.stream()
        .map(p -> BoardUtil.getDirection(hero, p))
        .map(d -> new MollyWish(MollyAction.fromDirection(d), numStepsToGo, MollyWishLevel.HIGH))
        .collect(Collectors.toList());
  }
}
