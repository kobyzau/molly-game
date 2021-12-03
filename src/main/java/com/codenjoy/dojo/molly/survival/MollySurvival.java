package com.codenjoy.dojo.molly.survival;

import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyCommand;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.impl.ActAndMoveDeadCase;
import com.codenjoy.dojo.molly.survival.impl.ActDeadCase;
import com.codenjoy.dojo.molly.survival.impl.ActWithRemoteDeadCase;
import com.codenjoy.dojo.molly.survival.impl.BigBoomDeadCase;
import com.codenjoy.dojo.molly.survival.impl.BlockDeadCase;
import com.codenjoy.dojo.molly.survival.impl.DangerAreaDeadCase;
import com.codenjoy.dojo.molly.survival.impl.FireBonusDeadCase;
import com.codenjoy.dojo.molly.survival.impl.MoveToBombDeadCase;
import com.codenjoy.dojo.molly.survival.impl.MoveToEnemyDeadCase;
import com.codenjoy.dojo.molly.survival.impl.OnOwnBombDeadCase;
import com.codenjoy.dojo.molly.survival.impl.StepOnGhostDeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.utils.BoardUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class MollySurvival {

  private final BigBoomDeadCase bigBoomDeadCase = new BigBoomDeadCase();

  private final List<DeadCase> deadCases =
      Arrays.asList(
          new ActAndMoveDeadCase(),
          new ActDeadCase(),
          new BlockDeadCase(),
          new DangerAreaDeadCase(),
          new StepOnGhostDeadCase(),
          new ActWithRemoteDeadCase(),
          new MoveToEnemyDeadCase(),
          new MoveToBombDeadCase(),
          new FireBonusDeadCase(),
          new OnOwnBombDeadCase());

  public boolean willDie(Matrix matrix, MollyAction mollyAction) {
    PointKey hero = new PointKey(matrix.getBoard().getHero());
    if (mollyAction.getCommand() == MollyCommand.ACT_2) {
      if (bigBoomDeadCase.willDie(matrix, hero)) {
        return true;
      }
    }
    PointKey nextPoint = BoardUtil.nextPoint(matrix.getBoard(), mollyAction);
    boolean leftBomb = mollyAction.getCommand() == MollyCommand.ACT;
    for (DeadCase deadCase : deadCases) {
      if (deadCase.willDie(matrix, hero, nextPoint, leftBomb)) {
        return true;
      }
    }
    return false;
  }
}
