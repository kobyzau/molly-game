package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishLevel;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MollyVision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BlowUpBoxMollyBehavior implements MollyBehavior {

  private final MollyVision mollyVision = new MollyVision();

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    if (mollyVision.hasNearFixedEnemies(matrix)) {
      return Collections.emptyList();
    }
    Board board = matrix.getBoard();
    Set<PointKey> deadBoxes =
        board.getPotions().stream()
            .map(PointKey::new)
            .map(p -> mollyVision.findBoxesWhichCanBeBlowUp(board, p))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    List<PointKey> freeBoxes =
        board.getTreasureBoxes().stream()
            .map(PointKey::new)
            .filter(p -> !deadBoxes.contains(p))
            .collect(Collectors.toList());
    Set<PointKey> canBlowUpBoxes =
        new HashSet<>(mollyVision.findBoxesWhichCanBeBlowUp(board, new PointKey(board.getHero())));
    boolean canAct = freeBoxes.stream().anyMatch(canBlowUpBoxes::contains);
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
}
