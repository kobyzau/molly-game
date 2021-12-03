package com.codenjoy.dojo.utils;

import com.codenjoy.dojo.molly.vision.MatrixCellInfo;
import com.codenjoy.dojo.molly.model.PointKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BoardUtilTest {

  @Test
  public void getBestWay_case1() {
    // given
    PointKey target = new PointKey(5, 3);
    Map<PointKey, MatrixCellInfo> cellInfoMap = getEmptyMap(7);
    cellInfoMap.put(new PointKey(3, 1), getCellInfo(0));
    cellInfoMap.put(new PointKey(3, 2), getCellInfo(1));
    cellInfoMap.put(new PointKey(3, 3), getCellInfo(2));
    cellInfoMap.put(new PointKey(3, 4), getCellInfo(3));
    cellInfoMap.put(new PointKey(4, 4), getCellInfo(4));
    cellInfoMap.put(new PointKey(2, 4), getCellInfo(4));
    cellInfoMap.put(new PointKey(1, 4), getCellInfo(5));
    cellInfoMap.put(new PointKey(2, 5), getCellInfo(5));
    cellInfoMap.put(new PointKey(4, 5), getCellInfo(5));
    cellInfoMap.put(new PointKey(5, 4), getCellInfo(5));
    cellInfoMap.put(new PointKey(1, 5), getCellInfo(6));
    cellInfoMap.put(new PointKey(5, 5), getCellInfo(6));
    cellInfoMap.put(new PointKey(5, 3), getCellInfo(6));

    // when
    Set<PointKey> result = BoardUtil.getBestWay(target, cellInfoMap);

    // then
    Assert.assertEquals(
        new HashSet<>(
            Arrays.asList(
                new PointKey(5, 3),
                new PointKey(5, 4),
                new PointKey(4, 4),
                new PointKey(3, 4),
                new PointKey(3, 3),
                new PointKey(3, 2),
                new PointKey(3, 1))),
        result);
  }

  private MatrixCellInfo getCellInfo(int numSteps) {
    return MatrixCellInfo.builder().numStepsToGo(numSteps).build();
  }

  private Map<PointKey, MatrixCellInfo> getEmptyMap(int size) {
    Map<PointKey, MatrixCellInfo> map = new HashMap<>();
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        map.put(new PointKey(x, y), getCellInfo(-1));
      }
    }
    return map;
  }
}
