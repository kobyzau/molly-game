package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishLevel;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.vision.Matrix;

import java.util.Collections;
import java.util.List;

public class RandomStepMollyBehavior implements MollyBehavior {
  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    return Collections.singletonList(
        new MollyWish(MollyAction.moveRandom(), 30, MollyWishLevel.LOW));
  }
}
