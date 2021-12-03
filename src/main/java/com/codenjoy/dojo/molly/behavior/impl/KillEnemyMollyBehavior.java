package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishLevel;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MollyVision;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.utils.BoardUtil;

import java.util.ArrayList;
import java.util.List;

public class KillEnemyMollyBehavior implements MollyBehavior {

  private final MollyVision mollyVision = new MollyVision();

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    PointKey hero = new PointKey(matrix.getBoard().getHero());
    List<PointKey> enemies = mollyVision.findNearEnemies(matrix);
    boolean canAct = enemies.stream().anyMatch(b -> canBeExploded(hero, b, matrix));
    List<MollyWish> wishes = new ArrayList<>();
    if (canAct) {
      wishes.add(new MollyWish(MollyAction.actAndUp(), 1, MollyWishLevel.LOW));
      wishes.add(new MollyWish(MollyAction.actAndDown(), 1, MollyWishLevel.LOW));
      wishes.add(new MollyWish(MollyAction.actAndLeft(), 1, MollyWishLevel.LOW));
      wishes.add(new MollyWish(MollyAction.actAndRight(), 1, MollyWishLevel.LOW));
      wishes.add(new MollyWish(MollyAction.act(), 1, MollyWishLevel.LOW));
    }
    return wishes;
  }

  private boolean canBeExploded(PointKey hero, PointKey enemy, Matrix matrix) {
    if (hero.getX() != enemy.getX() && hero.getY() != enemy.getY()) {
      return false;
    }
    Direction direction = BoardUtil.getDirection(hero, enemy);
    Board board = matrix.getBoard();
    PointKey checkPoint = hero.next(direction);
    for (int step = 1; step <= 3; step++) {
      if (enemy.equals(checkPoint)) {
        return true;
      }
      if (!BoardUtil.isFreeForFire(board, checkPoint)) {
        return false;
      }
      checkPoint = checkPoint.next(direction);
    }
    return false;
  }
}
