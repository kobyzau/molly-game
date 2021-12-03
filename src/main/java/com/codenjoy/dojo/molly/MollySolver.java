package com.codenjoy.dojo.molly;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyCommand;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishComparator;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.behavior.MollyBehaviorResolver;
import com.codenjoy.dojo.molly.memory.MollyMemory;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.MollySurvival;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.molly.vision.MollyVision;
import com.codenjoy.dojo.utils.BoardUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MollySolver implements Solver<Board> {

  private final MollyBehavior mollyBehavior;
  private final MollyVision mollyVision = new MollyVision();
  private final MollySurvival mollySurvival = new MollySurvival();
  private final MollyBehaviorResolver mollyBehaviorResolver = new MollyBehaviorResolver();

  @Override
  public String get(Board board) {
    log.info("New iteration is started...");
    log.info("Molly Memory: " + MollyMemory.getInstance());
    MollyAction mollyAction = getMollyAction(board);
    log.info("Molly action is {}", mollyAction);
    PointKey nextPoint = BoardUtil.nextPoint(board, mollyAction);
    Element elementAt = board.getAt(nextPoint.getX(), nextPoint.getY());
    log.info("Next point is {}", elementAt.name());
    switch (elementAt) {
      case POTION_EXPLODER:
        MollyMemory.getInstance().setHasBigBoom();
        break;
      case POTION_REMOTE_CONTROL:
        MollyMemory.getInstance().setHasRemote();
        break;
      case POISON_THROWER:
        MollyMemory.getInstance().setHasTower();
        break;
    }
    if (mollyAction.getCommand() == MollyCommand.ACT) {
      MollyMemory.getInstance().decreaseRemote();
    }
    if (mollyAction.getCommand() == MollyCommand.ACT_2) {
      MollyMemory.getInstance().clearBigBoom();
    }

    MollyMemory.getInstance().decreaseTower();
    MollyMemory.getInstance().decreaseBigBoom();
    MollyMemory.getInstance().setBoard(board);
    return mollyAction.toString();
  }

  private MollyAction getMollyAction(Board board) {
    boolean isDead = !board.get(Element.DEAD_HERO).isEmpty();
    log.info("Molly location is {}", new PointKey(board.getHero()));
    if (isDead) {
      MollyMemory.getInstance().clear();
      log.warn("Molly is dead");
      return MollyAction.act();
    }
    Matrix matrix = mollyVision.buildMatrix(board);
    log.info("Matrix is built");
    List<MollyWish> wishes =
        mollyBehavior.getActions(matrix).stream()
            .sorted(new MollyWishComparator().reversed())
            .collect(Collectors.toList());
    log.info("Completing iteration with {} wishes:  {}", wishes.size(), wishes);
    List<MollyWish> safeWishes = new ArrayList<>();
    for (MollyWish wish : wishes) {
      if (!mollySurvival.willDie(matrix, wish.getMollyAction())) {
        safeWishes.add(wish);
      }
    }
    if (safeWishes.isEmpty()) {
      log.warn("All suggested actions are bring death, looking for another options..");
      for (MollyAction action : MollyAction.allActions()) {
        if (!mollySurvival.willDie(matrix, action)) {
          log.warn("Final action is {}", action);
          return action;
        }
      }
      log.warn("No good options left, do random");
      Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
      for (MollyAction action : MollyAction.allActions()) {
        if (cellInfoMap.get(BoardUtil.nextPoint(board, action)).getNumStepsToGo() > 0) {
          log.warn("Final action is {}", action);
          return action;
        }
      }
      return MollyAction.act();
    } else {
      log.info("Found {} safe wishes: {}", safeWishes.size(), safeWishes);
      return mollyBehaviorResolver.resolve(safeWishes);
    }
  }
}
