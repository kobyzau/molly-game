package com.codenjoy.dojo.molly.vision;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.molly.model.PointKey;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Matrix {

  private final Board board;
  private final Map<PointKey, MatrixCellInfo> cellInfoMap;

  public Matrix(Board board) {
    this.cellInfoMap = new HashMap<>();
    this.board = board;
  }
}
