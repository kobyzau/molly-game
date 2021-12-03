package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishLevel;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.molly.vision.MollyVision;
import com.codenjoy.dojo.utils.BoardUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class FindBonusMollyBehavior implements MollyBehavior {

  private final MollyVision mollyVision = new MollyVision();

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    PointKey hero = new PointKey(matrix.getBoard().getHero());
    List<PointKey> nearBonuses = mollyVision.findNearBonuses(matrix);
    return nearBonuses.stream()
        .map(p -> getWishByBoxPoint(hero, p, matrix))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
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
        .map(d -> new MollyWish(MollyAction.fromDirection(d), numStepsToGo, MollyWishLevel.HIGH))
        .collect(Collectors.toList());
  }
}
