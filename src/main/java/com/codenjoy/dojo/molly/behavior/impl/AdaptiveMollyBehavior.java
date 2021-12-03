package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.vision.Matrix;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class AdaptiveMollyBehavior implements MollyBehavior {
  private final List<MollyBehavior> behaviors =
      Arrays.asList(
          new KillEnemyMollyBehavior(),
          new BlowUpBoxMollyBehavior(),
          new GoToEnemyMollyBehavior(),
          new GoToFreeBoxMollyBehavior(),
          new DoRemoteMollyBehavior(),
          new GoToClosedEnemyMollyBehavior(),
          new BigBoomMollyBehavior(),
          new ShotTowerMollyBehavior(),
          new GoFromFireMollyBehavior(),
          new GoToFixedEnemiesMollyBehavior(),
          new FindBonusMollyBehavior());

  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    List<MollyWish> wishes = new ArrayList<>();
    for (MollyBehavior behavior : behaviors) {
      List<MollyWish> behaviorWishes = behavior.getActions(matrix);
      if (!behaviorWishes.isEmpty()) {
        log.info(
            "Behavior {} gave wishes: {}", behavior.getClass().getSimpleName(), behaviorWishes);
      }
      wishes.addAll(behaviorWishes);
    }
    return wishes;
  }
}
