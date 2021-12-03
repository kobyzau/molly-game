package com.codenjoy.dojo.molly.vision;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.memory.MollyMemory;
import com.codenjoy.dojo.molly.model.PointKey;
import com.codenjoy.dojo.molly.vision.builder.MatrixBuilderStage;
import com.codenjoy.dojo.molly.vision.builder.stage.FindDeadStepsMatrixBuilderStage;
import com.codenjoy.dojo.molly.vision.builder.stage.FindStepsToGoMatrixBuilderStage;
import com.codenjoy.dojo.molly.vision.builder.stage.InitMatrixBuilderStage;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.utils.BoardUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.games.mollymage.Element.DEAD_GHOST;
import static com.codenjoy.dojo.games.mollymage.Element.ENEMY_HERO;
import static com.codenjoy.dojo.games.mollymage.Element.ENEMY_POTION_HERO;
import static com.codenjoy.dojo.games.mollymage.Element.GHOST;
import static com.codenjoy.dojo.games.mollymage.Element.OTHER_HERO;
import static com.codenjoy.dojo.games.mollymage.Element.OTHER_POTION_HERO;

@Slf4j
public class MollyVision {

  private final List<MatrixBuilderStage> stages =
      Arrays.asList(
          new InitMatrixBuilderStage(),
          new FindDeadStepsMatrixBuilderStage(),
          new FindStepsToGoMatrixBuilderStage());

  public Matrix buildMatrix(Board board) {
    Matrix matrix = new Matrix(board);
    stages.forEach(
        s -> {
          log.info("Stage {} is started", s.getClass().getSimpleName());
          s.processMatrix(matrix);
          log.info("Stage {} is ended", s.getClass().getSimpleName());
        });
    return matrix;
  }

  public boolean hasNearFixedEnemies(Matrix matrix) {
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    return MollyMemory.getInstance().getFixedEnemies(matrix.getBoard()).stream()
        .anyMatch(
            e ->
                cellInfoMap.get(e).getNumStepsToGo() > 0
                    && cellInfoMap.get(e).getNumStepsToGo() < 5);
  }

  public List<PointKey> findNearEnemies(Matrix matrix) {
    Board board = matrix.getBoard();
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    return board
        .get(OTHER_HERO, OTHER_POTION_HERO, ENEMY_HERO, ENEMY_POTION_HERO, GHOST, DEAD_GHOST)
        .stream()
        .map(PointKey::new)
        .filter(p -> cellInfoMap.get(p).getNumStepsToGo() > 0)
        .filter(p -> cellInfoMap.get(p).getNumStepsToGo() < 6)
        .collect(Collectors.toList());
  }

  public List<PointKey> findNearBonuses(Matrix matrix) {
    Board board = matrix.getBoard();
    Map<PointKey, MatrixCellInfo> cellInfoMap = matrix.getCellInfoMap();
    return board
        .get(
            Element.POTION_COUNT_INCREASE,
            Element.POTION_IMMUNE,
            Element.POTION_REMOTE_CONTROL,
            Element.POTION_BLAST_RADIUS_INCREASE,
            Element.POISON_THROWER,
            Element.POTION_EXPLODER)
        .stream()
        .map(PointKey::new)
        .filter(p -> cellInfoMap.get(p).getNumStepsToGo() > 0)
        .filter(p -> cellInfoMap.get(p).getNumStepsToGo() < 8)
        .collect(Collectors.toList());
  }

  public List<PointKey> findBoxesWhichCanBeBlowUp(Board board, PointKey bomb) {
    List<PointKey> boxes = new ArrayList<>();
    findFirstBoxWhichCanBeBlowUp(board, bomb, Direction.UP).ifPresent(boxes::add);
    findFirstBoxWhichCanBeBlowUp(board, bomb, Direction.DOWN).ifPresent(boxes::add);
    findFirstBoxWhichCanBeBlowUp(board, bomb, Direction.LEFT).ifPresent(boxes::add);
    findFirstBoxWhichCanBeBlowUp(board, bomb, Direction.RIGHT).ifPresent(boxes::add);
    return boxes;
  }

  private Optional<PointKey> findFirstBoxWhichCanBeBlowUp(
      Board board, PointKey bomb, Direction direction) {
    PointKey checkPoint = bomb.next(direction);
    for (int step = 1; step <= 3; step++) {
      if (board.getAt(checkPoint.getX(), checkPoint.getY()) == Element.TREASURE_BOX) {
        return Optional.of(checkPoint);
      }
      if (!BoardUtil.isFreeForFire(board, checkPoint)) {
        return Optional.empty();
      }
      checkPoint = checkPoint.next(direction);
    }

    return Optional.empty();
  }

  public List<PointKey> findBonusesWhichCanBeBlowUp(Board board, PointKey bomb) {
    List<PointKey> bonuses = new ArrayList<>();
    findFirstBonusWhichCanBeBlowUp(board, bomb, Direction.UP).ifPresent(bonuses::add);
    findFirstBonusWhichCanBeBlowUp(board, bomb, Direction.DOWN).ifPresent(bonuses::add);
    findFirstBonusWhichCanBeBlowUp(board, bomb, Direction.LEFT).ifPresent(bonuses::add);
    findFirstBonusWhichCanBeBlowUp(board, bomb, Direction.RIGHT).ifPresent(bonuses::add);
    return bonuses;
  }

  private Optional<PointKey> findFirstBonusWhichCanBeBlowUp(
      Board board, PointKey bomb, Direction direction) {
    Set<PointKey> bonuses =
        board.getPerks().stream().map(PointKey::new).collect(Collectors.toSet());
    PointKey checkPoint = bomb.next(direction);
    for (int step = 1; step <= 3; step++) {
      if (bonuses.contains(checkPoint)) {
        return Optional.of(checkPoint);
      }
      if (!BoardUtil.isFreeForFire(board, checkPoint)) {
        return Optional.empty();
      }
      checkPoint = checkPoint.next(direction);
    }

    return Optional.empty();
  }
}
