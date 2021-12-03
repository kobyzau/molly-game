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
import com.codenjoy.dojo.molly.vision.MollyVision;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.utils.BoardUtil;
import com.codenjoy.dojo.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ShotTowerMollyBehavior implements MollyBehavior {

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    boolean hasTower = MollyMemory.getInstance().isHasTower();
    if (!hasTower) {
      return Collections.emptyList();
    }
    PointKey hero = new PointKey(matrix.getBoard().getHero());
    List<Direction> enemies = findEnemiesWhichCanBeBlowUp(matrix.getBoard(), hero);

    if (!enemies.isEmpty()) {
      Direction enemy = CollectionUtil.getRandomValue(enemies);
      return Collections.singletonList(
          new MollyWish(MollyAction.shotTower(enemy), 0, MollyWishLevel.TOP));
    }

    return Collections.emptyList();
  }

  public List<Direction> findEnemiesWhichCanBeBlowUp(Board board, PointKey bomb) {
    List<Direction> directions = new ArrayList<>();
    findFirstEnemyWhichCanBeBlowUp(board, bomb, Direction.UP)
        .ifPresent(r -> directions.add(Direction.UP));
    findFirstEnemyWhichCanBeBlowUp(board, bomb, Direction.DOWN)
        .ifPresent(r -> directions.add(Direction.DOWN));
    findFirstEnemyWhichCanBeBlowUp(board, bomb, Direction.LEFT)
        .ifPresent(r -> directions.add(Direction.LEFT));
    findFirstEnemyWhichCanBeBlowUp(board, bomb, Direction.RIGHT)
        .ifPresent(r -> directions.add(Direction.RIGHT));
    return directions;
  }

  private Optional<PointKey> findFirstEnemyWhichCanBeBlowUp(
      Board board, PointKey bomb, Direction direction) {
    PointKey checkPoint = bomb.next(direction);
    for (int step = 1; step <= 3; step++) {
      Element element = board.getAt(checkPoint.getX(), checkPoint.getY());
      if (element == Element.OTHER_HERO
          || element == Element.OTHER_POTION_HERO
          || element == Element.ENEMY_HERO
          || element == Element.ENEMY_POTION_HERO) {
        return Optional.of(checkPoint);
      }
      if (!BoardUtil.isFreeForFire(board, checkPoint)) {
        return Optional.empty();
      }
      checkPoint = checkPoint.next(direction);
    }

    return Optional.empty();
  }
}
