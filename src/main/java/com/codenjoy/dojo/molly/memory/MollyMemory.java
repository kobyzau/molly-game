package com.codenjoy.dojo.molly.memory;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.games.mollymage.Element;
import com.codenjoy.dojo.molly.model.PointKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MollyMemory {

  private static final int REMOTE_TIMES = 3;
  private static final int TOWER_TIMES = 10;
  private static final int BIG_BOOM_TIMES = 20;
  private static final MollyMemory INSTANCE = new MollyMemory();

  public static MollyMemory getInstance() {
    return INSTANCE;
  }

  private int remoteTimes;
  private int towerTimes;
  private int bigBoomTimes;
  private Board board;

  public void setBoard(Board board) {
    log.info("New Memory board hash: " + board.toString().hashCode());
    this.board = (Board) new Board().forString(board.boardAsString());
  }

  public void clear() {
    log.info("Clear memory");
    cleanBoard();
    remoteTimes = 0;
    towerTimes = 0;
    bigBoomTimes = 0;
  }

  public void decreaseRemote() {
    if (remoteTimes > 0) {
      log.info("Decrease remote to {}", remoteTimes - 1);
      remoteTimes = remoteTimes - 1;
    }
  }

  public void decreaseBigBoom() {
    if (bigBoomTimes > 0) {
      log.info("Decrease big boom to {}", bigBoomTimes - 1);
      bigBoomTimes = bigBoomTimes - 1;
    }
  }

  public void clearBigBoom() {
    log.info("Clear big boom");
    bigBoomTimes = 0;
  }

  public void decreaseTower() {
    if (towerTimes > 0) {
      log.info("Decrease tower to {}", towerTimes - 1);
      towerTimes = towerTimes - 1;
    }
  }

  public void setHasRemote() {
    log.info("Set has remote");
    this.remoteTimes = REMOTE_TIMES;
  }

  public void setHasBigBoom() {
    log.info("Set has big boom");
    this.bigBoomTimes = BIG_BOOM_TIMES;
  }

  public boolean hasBigBoom() {
    return bigBoomTimes > 0;
  }
  public boolean isHasRemote() {
    return remoteTimes > 0;
  }

  public void setHasTower() {

    log.info("Set has tower");
    this.towerTimes = TOWER_TIMES;
  }

  public boolean isHasTower() {
    return towerTimes > 0;
  }

  public Optional<Board> getBoard() {
    return Optional.ofNullable(board);
  }

  public void cleanBoard() {
    this.board = null;
  }

  public List<PointKey> getFixedEnemies(Board newBoard) {
    if (board == null) {
      return Collections.emptyList();
    }
    Set<PointKey> lastEnemyPoints =
        board.get(Element.ENEMY_HERO, Element.OTHER_HERO).stream()
            .map(PointKey::new)
            .collect(Collectors.toSet());
    log.info("Memory last enemy points: {}", lastEnemyPoints);
    return newBoard.get(Element.ENEMY_HERO, Element.OTHER_HERO).stream()
        .map(PointKey::new)
        .filter(lastEnemyPoints::contains)
        .distinct()
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return "MollyMemory[" + "remote: " + remoteTimes + ", tower:" + towerTimes + "]";
  }
}
