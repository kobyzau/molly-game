package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.utils.BoardUtil;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.games.mollymage.Element.DEAD_GHOST;

public class StepOnGhostDeadCase implements DeadCase {
  @Override
  public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
    List<PointKey> ghosts =
        matrix.getBoard().get(DEAD_GHOST, Element.GHOST).stream()
            .map(PointKey::new)
            .collect(Collectors.toList());
    Set<PointKey> positionsWithGhosts =
        ghosts.stream()
            .map(p -> BoardUtil.getNearPoints(p.getX(), p.getY()))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    positionsWithGhosts.addAll(ghosts);
    return positionsWithGhosts.contains(newPosition);
  }
}
