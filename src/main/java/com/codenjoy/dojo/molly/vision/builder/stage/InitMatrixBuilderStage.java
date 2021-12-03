package com.codenjoy.dojo.molly.vision.builder.stage;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.molly.vision.Matrix;
import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.builder.MatrixBuilderStage;

public class InitMatrixBuilderStage implements MatrixBuilderStage {

  @Override
  public void processMatrix(Matrix matrix) {
    Board board = matrix.getBoard();
    for (int x = 0; x < board.size(); x++) {
      for (int y = 0; y < board.size(); y++) {
        matrix.getCellInfoMap().put(new PointKey(x, y), new MatrixCellInfo());
      }
    }
  }
}
