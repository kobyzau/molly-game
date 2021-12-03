package com.codenjoy.dojo.molly.behavior;

import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.molly.vision.Matrix;

import java.util.List;

public interface MollyBehavior {

    List<MollyWish> getActions(Matrix matrix);
}
