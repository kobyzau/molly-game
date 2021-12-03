package com.codenjoy.dojo.utils;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyCommand;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BoardUtil {

  public static Set<PointKey> getBestWay(PointKey to, Map<PointKey, MatrixCellInfo> cellInfoMap) {
    Set<PointKey> way = new HashSet<>();
    way.add(to);
    int stepNumber = cellInfoMap.get(to).getNumStepsToGo() - 1;
    while (stepNumber >= 0) {
      List<PointKey> newSteps = new ArrayList<>();
      for (PointKey lastStep : way) {
        List<PointKey> nearPoints = getNearPoints(lastStep.getX(), lastStep.getY());
        for (PointKey nearPoint : nearPoints) {
          if (cellInfoMap.get(nearPoint).getNumStepsToGo() == stepNumber) {
            newSteps.add(nearPoint);
          }
        }
      }
      way.addAll(newSteps);
      stepNumber--;
    }
    return way;
  }

  public static List<PointKey> getNearPoints(int x, int y) {
    List<PointKey> pointKeys = new ArrayList<>();
    pointKeys.add(new PointKey(x + 1, y));
    pointKeys.add(new PointKey(x - 1, y));
    pointKeys.add(new PointKey(x, y + 1));
    pointKeys.add(new PointKey(x, y - 1));
    return pointKeys;
  }

  public static List<PointKey> getNearPoints(PointKey pointKey) {
    return getNearPoints(pointKey.getX(), pointKey.getY());
  }

  public static boolean isFreeForFire(Board board, PointKey pointKey) {
    Element element = board.getAt(pointKey.getX(), pointKey.getY());
    switch (element) {
      case POTION_TIMER_1:
      case POTION_TIMER_2:
      case POTION_TIMER_3:
      case POTION_TIMER_4:
      case POTION_TIMER_5:
      case BOOM:
      case HERO:
      case POTION_HERO:
      case NONE:
      case OPENING_TREASURE_BOX:
      case DEAD_GHOST:
      case GHOST:
      case POTION_BLAST_RADIUS_INCREASE:
      case POTION_COUNT_INCREASE:
      case POTION_REMOTE_CONTROL:
      case POTION_IMMUNE:
      case POISON_THROWER:
      case POTION_EXPLODER:
      case OTHER_DEAD_HERO:
      case OTHER_HERO:
      case OTHER_POTION_HERO:
      case ENEMY_DEAD_HERO:
      case ENEMY_HERO:
      case ENEMY_POTION_HERO:
        return true;
      default:
        return false;
    }
  }

  public static boolean canHeroMoveTo(Board board, PointKey pointKey) {
    Element element = board.getAt(pointKey.getX(), pointKey.getY());
    switch (element) {
      case WALL:
      case TREASURE_BOX:
        return false;
      default:
        return true;
    }
  }

  public static boolean isFreeCell(Board board, int x, int y) {
    Element element = board.getAt(x, y);
    switch (element) {
      case BOOM:
      case HERO:
      case POTION_HERO:
      case NONE:
      case OPENING_TREASURE_BOX:
      case DEAD_GHOST:
      case POTION_BLAST_RADIUS_INCREASE:
      case POTION_COUNT_INCREASE:
      case POTION_REMOTE_CONTROL:
      case POTION_IMMUNE:
      case POISON_THROWER:
      case POTION_EXPLODER:
      case OTHER_DEAD_HERO:
      case ENEMY_DEAD_HERO:
        return true;
      default:
        return false;
    }
  }

  public static Direction getDirection(PointKey from, PointKey to) {
    int fromX = from.getX();
    int fromY = from.getY();

    int toX = to.getX();
    int toY = to.getY();

    if (fromX == toX) {
      return (fromY - toY) < 0 ? Direction.UP : Direction.DOWN;
    }
    if (fromY == toY) {
      return (fromX - toX) > 0 ? Direction.LEFT : Direction.RIGHT;
    }
    return Direction.STOP;
  }


  public static PointKey nextPoint(Board board, MollyAction mollyAction) {
    PointKey hero = new PointKey(board.getHero());
    if (mollyAction.getCommand() == MollyCommand.ACT_1) {
      return hero;
    }
    if (mollyAction.getCommand() == MollyCommand.ACT_2) {
      return hero;
    }
    MollyCommand command = mollyAction.getCommand();
    MollyCommand extraCommand = mollyAction.getExtraCommand();
    Optional<MollyCommand> moveCommand = Optional.empty();
    if (command.isMove()) {
      moveCommand = Optional.of(command);
    }
    if (extraCommand != null && extraCommand.isMove()) {
      moveCommand = Optional.of(extraCommand);
    }
    if (moveCommand.isPresent()) {
      switch (moveCommand.get()) {
        case UP:
          return new PointKey(hero.getX(), hero.getY() + 1);
        case DOWN:
          return new PointKey(hero.getX(), hero.getY() - 1);
        case LEFT:
          return new PointKey(hero.getX() - 1, hero.getY());
        case RIGHT:
          return new PointKey(hero.getX() + 1, hero.getY());
      }
    }
    return new PointKey(hero.getX(), hero.getY());
  }
}
