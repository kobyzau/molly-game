package com.codenjoy.dojo.molly.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class PointKey {

  private final int x;
  private final int y;

  public PointKey(Point point) {
    this.x = point.getX();
    this.y = point.getY();
  }

  public PointKey(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public PointKey next(Direction direction) {
    switch (direction) {
      case UP:
        return new PointKey(x, y + 1);
      case DOWN:
        return new PointKey(x, y - 1);
      case LEFT:
        return new PointKey(x - 1, y);
      case RIGHT:
        return new PointKey(x + 1, y);
    }
    throw new IllegalArgumentException("Unknown direction: " + direction);
  }
}
