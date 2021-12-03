package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishLevel;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.memory.MollyMemory;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.utils.BoardUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class GoToFixedEnemiesMollyBehavior implements MollyBehavior {

  private static final int FAR_DISTANCE = 6;

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    PointKey hero = new PointKey(matrix.getBoard().getHero());
    List<PointKey> fixedEnemies = MollyMemory.getInstance().getFixedEnemies(matrix.getBoard());
    log.info("Found {} fixed enemies: {}", fixedEnemies.size(), fixedEnemies);
    List<MollyWish> wishes = new ArrayList<>();
    getFarEnemies(fixedEnemies, matrix).stream()
        .map(e -> getWishByBoxPoint(hero, e, matrix, MollyWishLevel.TOP))
        .flatMap(Collection::stream)
        .forEach(wishes::add);
    getNearEnemies(fixedEnemies, matrix).stream()
        .map(e -> getWishByBoxPoint(hero, e, matrix, MollyWishLevel.HIGH))
        .flatMap(Collection::stream)
        .sorted(Comparator.comparing(MollyWish::getSteps).reversed())
        .forEach(wishes::add);
    return wishes;
  }

  private List<MollyWish> getWishByBoxPoint(
      PointKey hero, PointKey pointKey, Matrix matrix, MollyWishLevel level) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    int numStepsToGo = Math.min(cellInfoMap.get(pointKey).getNumStepsToGo(), 10);
    Set<PointKey> nearPoints = new HashSet<>(BoardUtil.getNearPoints(hero));
    List<PointKey> nextSteps =
        BoardUtil.getBestWay(pointKey, matrix.getCellInfoMap()).stream()
            .filter(nearPoints::contains)
            .collect(Collectors.toList());

    return nextSteps.stream()
        .map(p -> BoardUtil.getDirection(hero, p))
        .map(d -> new MollyWish(MollyAction.fromDirection(d), numStepsToGo, level))
        .collect(Collectors.toList());
  }

  private List<PointKey> getFarEnemies(List<PointKey> enemies, Matrix matrix) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();

    return enemies.stream()
        .filter(e -> cellInfoMap.get(e).getNumStepsToGo() >= FAR_DISTANCE)
        .filter(e -> cellInfoMap.get(e).getFiredAfterSeconds().isEmpty())
        .collect(Collectors.toList());
  }

  private List<PointKey> getNearEnemies(List<PointKey> enemies, Matrix matrix) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();

    return enemies.stream()
        .filter(e -> cellInfoMap.get(e).getNumStepsToGo() > 0)
        .filter(e -> cellInfoMap.get(e).getNumStepsToGo() < FAR_DISTANCE)
        .filter(e -> cellInfoMap.get(e).getFiredAfterSeconds().isEmpty())
        .collect(Collectors.toList());
  }
}
