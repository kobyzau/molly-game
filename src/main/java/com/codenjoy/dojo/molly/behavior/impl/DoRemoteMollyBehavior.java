package com.codenjoy.dojo.molly.behavior.impl;

import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.action.MollyWishLevel;
import com.codenjoy.dojo.molly.behavior.MollyBehavior;
import com.codenjoy.dojo.molly.memory.MollyMemory;
import com.codenjoy.dojo.molly.vision.Matrix;

import java.util.ArrayList;
import java.util.List;

public class DoRemoteMollyBehavior implements MollyBehavior {
  @Override
  public List<MollyWish> getActions(Matrix matrix) {
    boolean hasRemote = MollyMemory.getInstance().isHasRemote();
    boolean hasTimer = !matrix.getBoard().get(Element.POTION_TIMER_5).isEmpty();
    List<MollyWish> wishes = new ArrayList<>();
    if (hasTimer) {
      wishes.add(new MollyWish(MollyAction.actAndUp(), 1, MollyWishLevel.LOW));
      wishes.add(new MollyWish(MollyAction.actAndDown(), 1, MollyWishLevel.LOW));
      wishes.add(new MollyWish(MollyAction.actAndLeft(), 1, MollyWishLevel.LOW));
      wishes.add(new MollyWish(MollyAction.actAndRight(), 1, MollyWishLevel.LOW));
      wishes.add(new MollyWish(MollyAction.act(), 1, MollyWishLevel.LOW));
    }
    return wishes;
  }
}
