package com.codenjoy.dojo.molly.action;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.utils.CollectionUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@EqualsAndHashCode
public class MollyAction {
  private final MollyCommand command;
  private final MollyCommand extraCommand;

  private MollyAction(MollyCommand command, MollyCommand extraCommand) {
    this.command = command;
    this.extraCommand = extraCommand;
  }

  @Override
  public String toString() {
    if (extraCommand != null) {
      return command.getCommand() + "," + extraCommand.getCommand();
    }
    return command.getCommand();
  }

  public static MollyAction shotTower(Direction direction) {
    switch (direction) {
      case UP:
        return poisonUp();
      case DOWN:
        return poisonDown();
      case LEFT:
        return poisonLeft();
      case RIGHT:
        return poisonRight();
    }
    return act();
  }

  public static MollyAction fromDirection(Direction direction) {
    return fromDirection(direction, false);
  }

  public static MollyAction fromDirection(Direction direction, boolean withAct) {
    switch (direction) {
      case UP:
        return withAct ? actAndUp() : up();
      case DOWN:
        return withAct ? actAndDown() : down();
      case LEFT:
        return withAct ? actAndLeft() : left();
      case RIGHT:
        return withAct ? actAndRight() : right();
      default:
        return withAct ? act() : new MollyAction(MollyCommand.NONE, null);
    }
  }

  public static MollyAction moveRandom() {
    return new MollyAction(
        CollectionUtil.getRandomValue(
            Arrays.asList(
                MollyCommand.UP, MollyCommand.DOWN, MollyCommand.LEFT, MollyCommand.RIGHT)),
        null);
  }

  public static MollyAction up() {
    return new MollyAction(MollyCommand.UP, null);
  }

  public static MollyAction down() {
    return new MollyAction(MollyCommand.DOWN, null);
  }

  public static MollyAction left() {
    return new MollyAction(MollyCommand.LEFT, null);
  }

  public static MollyAction right() {
    return new MollyAction(MollyCommand.RIGHT, null);
  }

  public static MollyAction act() {
    return new MollyAction(MollyCommand.ACT, null);
  }

  public static MollyAction actAndDown() {
    return new MollyAction(MollyCommand.ACT, MollyCommand.DOWN);
  }

  public static MollyAction actAndUp() {
    return new MollyAction(MollyCommand.ACT, MollyCommand.UP);
  }

  public static MollyAction actAndLeft() {
    return new MollyAction(MollyCommand.ACT, MollyCommand.LEFT);
  }

  public static MollyAction actAndRight() {
    return new MollyAction(MollyCommand.ACT, MollyCommand.RIGHT);
  }

  public static MollyAction upAndAct() {
    return new MollyAction(MollyCommand.UP, MollyCommand.ACT);
  }

  public static MollyAction downAndAct() {
    return new MollyAction(MollyCommand.DOWN, MollyCommand.ACT);
  }

  public static MollyAction leftAndAct() {
    return new MollyAction(MollyCommand.LEFT, MollyCommand.ACT);
  }

  public static MollyAction rightAndAct() {
    return new MollyAction(MollyCommand.RIGHT, MollyCommand.ACT);
  }

  public static MollyAction poisonUp() {
    return new MollyAction(MollyCommand.ACT_1, MollyCommand.UP);
  }

  public static MollyAction poisonDown() {
    return new MollyAction(MollyCommand.ACT_1, MollyCommand.DOWN);
  }

  public static MollyAction poisonLeft() {
    return new MollyAction(MollyCommand.ACT_1, MollyCommand.LEFT);
  }

  public static MollyAction poisonRight() {
    return new MollyAction(MollyCommand.ACT_1, MollyCommand.RIGHT);
  }

  public static MollyAction explode() {
    return new MollyAction(MollyCommand.ACT_2, null);
  }

  public static MollyAction upAndExplode() {
    return new MollyAction(MollyCommand.UP, MollyCommand.ACT_2);
  }

  public static MollyAction downAndExplode() {
    return new MollyAction(MollyCommand.DOWN, MollyCommand.ACT_2);
  }

  public static MollyAction leftAndExplode() {
    return new MollyAction(MollyCommand.LEFT, MollyCommand.ACT_2);
  }

  public static MollyAction rightAndExplode() {
    return new MollyAction(MollyCommand.RIGHT, MollyCommand.ACT_2);
  }

  public static List<MollyAction> allActions() {
    return Arrays.asList(up(), down(), left(), right());
  }
}
