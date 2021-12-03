package com.codenjoy.dojo.molly.action;

public enum MollyCommand {
  NONE(""),
  UP("UP"),
  DOWN("DOWN"),
  LEFT("LEFT"),
  RIGHT("RIGHT"),
  ACT("ACT"),
  ACT_1("ACT(1)"),
  ACT_2("ACT(2)");

  private final String command;

  MollyCommand(String command) {
    this.command = command;
  }

  public String getCommand() {
    return command;
  }

  public boolean isMove() {
    switch (this) {
      case RIGHT:
      case LEFT:
      case DOWN:
      case UP:
        return true;
      default:
        return false;
    }
  }
}
