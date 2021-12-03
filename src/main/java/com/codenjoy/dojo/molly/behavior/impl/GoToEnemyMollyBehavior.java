package com.codenjoy.dojo.molly.behavior.impl;

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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GoToEnemyMollyBehavior implements MollyBehavior {

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    PointKey hero = new PointKey(matrix.getBoard().getHero());
    List<PointKey> fixedEnemies = MollyMemory.getInstance().getFixedEnemies(matrix.getBoard());
    if (!fixedEnemies.isEmpty()) {
      return Collections.emptyList();
    }
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    List<PointKey> enemies =
        matrix
            .getBoard()
            .get(
                Element.ENEMY_POTION_HERO,
                Element.ENEMY_HERO,
                Element.OTHER_POTION_HERO,
                Element.OTHER_HERO)
            .stream()
            .map(PointKey::new)
            .filter(e -> cellInfoMap.get(e).getNumStepsToGo() > 0)
            .collect(Collectors.toList());

    List<MollyWish> wishes = new ArrayList<>();
    enemies.stream()
        .map(e -> getWishByBoxPoint(hero, e, matrix, MollyWishLevel.MEDIUM))
        .flatMap(Collection::stream)
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
}
