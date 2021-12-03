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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BigBoomMollyBehavior implements MollyBehavior {

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    boolean hasBigBoom = MollyMemory.getInstance().hasBigBoom();
    if (!hasBigBoom) {
      return Collections.emptyList();
    }

    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    boolean hasTargets =
        matrix
            .getBoard()
            .get(
                Element.OTHER_HERO,
                Element.OTHER_POTION_HERO,
                Element.ENEMY_POTION_HERO,
                Element.ENEMY_HERO)
            .stream()
            .map(PointKey::new)
            .map(cellInfoMap::get)
            .map(MatrixCellInfo::getFiredAfterSeconds)
            .flatMap(Collection::stream)
            .anyMatch(s -> s > 0);
    if (!hasTargets) {
      return Collections.emptyList();
    }
    return Collections.singletonList(new MollyWish(MollyAction.explode(), 0, MollyWishLevel.TOP));
  }
}
