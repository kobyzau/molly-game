package com.codenjoy.dojo.molly.survival.impl;

import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.survival.DeadCase;
import com.codenjoy.dojo.molly.vision.Matrix;

public class DangerAreaDeadCase implements DeadCase {
    @Override
    public boolean willDie(Matrix matrix, PointKey hero, PointKey newPosition, boolean leftBomb) {
        return matrix.getCellInfoMap().get(newPosition).getFiredAfterSeconds().contains(1);
    }
}
