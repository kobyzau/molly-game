package com.codenjoy.dojo.molly;

import com.codenjoy.dojo.games.mollymage.Board;
import com.codenjoy.dojo.molly.behavior.impl.AdaptiveMollyBehavior;
import com.codenjoy.dojo.molly.memory.MollyMemory;
import com.google.common.io.Resources;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MollySolverTest {

  // @Rule public Timeout timeout = new Timeout(900, TimeUnit.MILLISECONDS);

  private final MollySolver mollySolver = new MollySolver(new AdaptiveMollyBehavior());

  @Before
  public void init() {
    MollyMemory.getInstance().clear();
  }

  @Test
  public void memory_noFixed() {

    // given
    String board = loadFile("board/memory/memory-no-fixed.txt");
    MollyMemory.getInstance()
        .setBoard(getBoard(loadFile("board/memory/memory-no-fixed-memory.txt")));

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(action, Arrays.asList("ACT,DOWN", "ACT,LEFT").contains(action));
  }

  @Test
  public void memory_usecase1() {

    // given
    Board boardOld = getBoard(loadFile("board/memory/usecase-1-memory.txt"));
    Board boardNew = getBoard(loadFile("board/memory/usecase-1.txt"));

    // when
    String action1 = mollySolver.get(boardOld);
    String action2 = mollySolver.get(boardNew);

    // then
    assertTrue(action2, Arrays.asList("ACT,DOWN", "ACT,UP", "ACT,RIGHT").contains(action2));
  }

  @Test
  public void tower_canDo() {

    // given
    String board = loadFile("board/tower/tower-can-do.txt");
    MollyMemory.getInstance().setHasTower();

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("ACT(1),RIGHT", action);
  }

  @Test
  public void tower_cannotDo() {

    // given
    String board = loadFile("board/tower/tower-cannot-do.txt");
    MollyMemory.getInstance().setHasTower();

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(action, Arrays.asList("ACT,UP", "ACT,DOWN").contains(action));
  }

  @Test
  public void tower_noNeedDo() {

    // given
    String board = loadFile("board/tower/tower-no-need-do.txt");
    MollyMemory.getInstance().setHasTower();

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("RIGHT", action);
  }

  @Test
  public void remote_canDo() {

    // given
    String board = loadFile("board/remote/remote-can-do.txt");
    MollyMemory.getInstance().setHasRemote();

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("ACT,LEFT", action);
  }

  @Test
  public void remote_cannotDo() {

    // given
    String board = loadFile("board/remote/remote-cannot-do.txt");
    MollyMemory.getInstance().setHasRemote();

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("LEFT", action);
  }

  @Test
  public void remote_noNeedDo() {

    // given
    String board = loadFile("board/remote/remote-no-need-do.txt");
    MollyMemory.getInstance().setHasRemote();

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("LEFT", action);
  }

  @Test
  public void noBlowUpBonus() {

    // given
    String board = loadFile("board/not-blow-up-bonus.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("LEFT", action);
  }

  @Test
  public void findBonus_up() {

    // given

    String board = loadFile("board/find-bonus/find-bonus-up.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("UP", action);
  }

  @Test
  public void findBonus_down() {

    // given

    String board = loadFile("board/find-bonus/find-bonus-down.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("DOWN", action);
  }

  @Test
  public void findBonus_left() {

    // given

    String board = loadFile("board/find-bonus/find-bonus-left.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("LEFT", action);
  }

  @Test
  public void findBonus_right() {

    // given

    String board = loadFile("board/find-bonus/find-bonus-right.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("RIGHT", action);
  }

  @Test
  public void findBonus_nearest() {

    // given

    String board = loadFile("board/find-bonus/find-bonus-nearest.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("RIGHT", action);
  }

  @Test
  public void findBonus_noBonus() {

    // given

    String board = loadFile("board/find-bonus/find-bonus-no-bonus.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(Arrays.asList("UP", "LEFT", "RIGHT", "DOWN").contains(action));
  }

  @Test
  public void findBonus_unreachable() {

    // given

    String board = loadFile("board/find-bonus/find-bonus-unreachable.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(Arrays.asList("UP", "LEFT", "RIGHT", "DOWN").contains(action));
  }

  @Test
  public void findBox_up() {

    // given

    String board = loadFile("board/go-to-box-up.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("UP", action);
  }

  @Test
  public void findBox_down() {

    // given

    String board = loadFile("board/go-to-box-down.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("DOWN", action);
  }

  @Test
  public void actFarBox() {

    // given

    String board = loadFile("board/act-far-box.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("ACT,UP", action);
  }

  @Test
  public void findBonus_chooseSaveWay_0() {

    // given
    String board = loadFile("board/find-bonus/find-bonus-save-way-0.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("LEFT", action);
  }

  @Test
  public void findBonus_chooseSaveWay_1() {

    // given
    String board = loadFile("board/find-bonus/find-bonus-save-way-1.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("RIGHT", action);
  }

  @Test
  public void findBonus_chooseSaveWay_2() {

    // given
    String board = loadFile("board/find-bonus/find-bonus-save-way-2.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("RIGHT", action);
  }

  @Test
  public void moveFromBomb_down_or_right() {

    // given
    String board = loadFile("board/move-from-bomb.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(Arrays.asList("DOWN", "RIGHT").contains(action));
  }

  @Test
  public void sendbox_1() {

    // given
    String board = loadFile("board/usecase/sandbox-1.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(action, Arrays.asList("DOWN", "LEFT", "RIGHT").contains(action));
  }

  @Test
  public void sendbox_2_down_left() {

    // given
    String board = loadFile("board/usecase/sandbox-2.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(Arrays.asList("DOWN", "LEFT").contains(action));
  }

  @Test
  public void sendbox_3() {

    // given
    String board = loadFile("board/usecase/sandbox-3.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("LEFT", action);
  }

  @Test
  public void sendbox_4() {

    // given
    String board = loadFile("board/usecase/sandbox-4.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(action, Arrays.asList("RIGHT", "LEFT").contains(action));
  }

  @Test
  public void sendbox_5() {

    // given
    String board = loadFile("board/usecase/sandbox-5.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("DOWN", action);
  }

  @Test
  public void sendbox_6() {

    // given
    String board = loadFile("board/usecase/sandbox-6.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(Arrays.asList("LEFT", "DOWN").contains(action));
  }

  @Test
  public void sendbox_7() {

    // given
    String board = loadFile("board/usecase/sandbox-7.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("ACT,DOWN", action);
  }

  @Test
  public void sendbox_8() {

    // given
    String board = loadFile("board/usecase/sandbox-8.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(action, Arrays.asList("ACT,DOWN", "ACT,UP").contains(action));
  }

  @Test
  public void sendbox_11() {

    // given
    String board = loadFile("board/usecase/sandbox-11.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("ACT,UP", action);
  }

  @Test
  public void sendbox_12() {

    // given
    String board = loadFile("board/usecase/sandbox-12.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(Arrays.asList("RIGHT", "DOWN").contains(action));
  }

  @Test
  public void sendbox_13() {

    // given
    String board = loadFile("board/usecase/sandbox-13.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("RIGHT", action);
  }

  @Test
  public void sendbox_14() {

    // given
    String board = loadFile("board/usecase/sandbox-14.txt");
    MollyMemory.getInstance().setBoard(getBoard(loadFile("board/usecase/sandbox-14-memory.txt")));

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(action, Arrays.asList("ACT,RIGHT", "LEFT").contains(action));
  }

  @Test
  public void sendbox_15() {

    // given
    String board = loadFile("board/usecase/sandbox-15.txt");
    MollyMemory.getInstance().setBoard(getBoard(loadFile("board/usecase/sandbox-14-memory.txt")));

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("DOWN", action);
  }

  @Test
  public void sendbox_16() {

    // given
    String board = loadFile("board/usecase/sandbox-16.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertEquals("ACT,DOWN", action);
  }

  @Test
  public void sendbox_17() {

    // given
    String board = loadFile("board/usecase/sandbox-17.txt");
    MollyMemory.getInstance().setBoard(getBoard(loadFile("board/usecase/sandbox-17-memory.txt")));

    // when
    String action = mollySolver.get(getBoard(board));

    // then\
    assertTrue(action, Arrays.asList("DOWN", "ACT,UP").contains(action));
  }

  @Test
  public void sendbox_18() {

    // given
    String board = loadFile("board/usecase/sandbox-18.txt");
    MollyMemory.getInstance().setBoard(getBoard(loadFile("board/usecase/sandbox-18.txt")));

    // when
    String action = mollySolver.get(getBoard(board));

    // then\
    assertTrue(action, Arrays.asList("ACT,DOWN", "ACT,LEFT", "ACT,UP").contains(action));
  }

  @Test
  public void sendbox_19() {

    // given
    String board = loadFile("board/usecase/usecase-19.txt");
    MollyMemory.getInstance().setBoard(getBoard(loadFile("board/usecase/usecase-19.txt")));

    // when
    String action = mollySolver.get(getBoard(board));

    // then\
    assertTrue(action, Arrays.asList("ACT,DOWN", "ACT,LEFT", "ACT,RIGHT").contains(action));
  }

  @Test
  public void sendbox_20() {

    // given
    String board = loadFile("board/usecase/usecase-20.txt");

    // when
    String action = mollySolver.get(getBoard(board));

    // then
    assertTrue(action, Arrays.asList("LEFT").contains(action));
    assertTrue(MollyMemory.getInstance().isHasRemote());
  }

  @Test
  public void bigboom_can() {
    // given
    Board board = getBoard(loadFile("board/bigboom/bigboom-can-do.txt"));
    MollyMemory.getInstance().setHasBigBoom();

    // when
    String action = mollySolver.get(board);

    // then
    assertEquals("ACT(2)", action);
  }

  @Test
  public void bigboom_cannotDo() {
    // given
    Board board = getBoard(loadFile("board/bigboom/bigboom-cannot-do.txt"));
    MollyMemory.getInstance().setHasBigBoom();

    // when
    String action = mollySolver.get(board);

    // then
    assertTrue(action, Arrays.asList("ACT,UP", "ACT,DOWN").contains(action));
  }

  @Test
  public void bigboom_notNeedDo() {
    // given
    Board board = getBoard(loadFile("board/bigboom/bigboom-no-need-do.txt"));
    MollyMemory.getInstance().setHasBigBoom();

    // when
    String action = mollySolver.get(board);

    // then
    assertTrue(action, Arrays.asList("UP", "RIGHT").contains(action));
  }

  private Board getBoard(String s) {
    return (Board) new Board().forString(s);
  }

  @SneakyThrows
  private String loadFile(String file) {
    return Resources.toString(Resources.getResource(file), StandardCharsets.UTF_8);
  }
}
