package com.codenjoy.dojo.molly.behavior;

import com.codenjoy.dojo.molly.action.MollyAction;
import com.codenjoy.dojo.molly.action.MollyCommand;
import com.codenjoy.dojo.molly.action.MollyWish;
import com.codenjoy.dojo.services.Direction;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MollyBehaviorResolver {

  public MollyAction resolve(List<MollyWish> mollyWishes) {
    if (mollyWishes.isEmpty()) {
      return MollyAction.act();
    }
    Optional<MollyAction> act1MollyAction =
        mollyWishes.stream()
            .filter(w -> w.getMollyAction().getCommand() == MollyCommand.ACT_1)
            .findFirst()
            .map(MollyWish::getMollyAction);
    Optional<MollyAction> act2MollyAction =
            mollyWishes.stream()
                    .filter(w -> w.getMollyAction().getCommand() == MollyCommand.ACT_2)
                    .findFirst()
                    .map(MollyWish::getMollyAction);
    if (act2MollyAction.isPresent()) {
      return act2MollyAction.get();
    }
    Set<Direction> actDirections =
        mollyWishes.stream()
            .map(MollyWish::getMollyAction)
            .filter(a -> a.getCommand() == MollyCommand.ACT)
            .map(this::getDirection)
            .collect(Collectors.toSet());

    return act1MollyAction.orElseGet(() -> getMollyActionFromSimpleWishes(mollyWishes));
  }

  private MollyAction getMollyActionFromSimpleWishes(List<MollyWish> mollyWishes) {
    Map<Direction, List<Double>> directionsWeights = new HashMap<>();
    for (MollyWish mollyWish : mollyWishes) {
      double weight = mollyWish.getWeight();
      Direction direction = getDirection(mollyWish.getMollyAction());
      List<Double> directionWeight = directionsWeights.getOrDefault(direction, new ArrayList<>());
      directionWeight.add(weight);
      directionsWeights.put(direction, directionWeight);
    }
    Map<Direction, Double> directions = formatDirectionWeights(directionsWeights);
    Map.Entry<Direction, Double> maxEntry = null;

    for (Map.Entry<Direction, Double> entry : directions.entrySet()) {
      if (entry.getKey() == Direction.ACT) {
        continue;
      }
      if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
        maxEntry = entry;
      }
    }

    log.info("Directions weights: {}", directions);
    Direction direction = maxEntry == null ? Direction.ACT : maxEntry.getKey();
    if (maxEntry == null) {

      log.info("Chosen primary direction {}", direction);
    } else {

      log.info("Chosen primary direction {} with weight {}", direction, maxEntry.getValue());
    }
    boolean hasAct =
        mollyWishes.stream()
            .map(MollyWish::getMollyAction)
            .filter(a -> getDirection(a) == direction)
            .anyMatch(this::hasAct);
    return MollyAction.fromDirection(direction, hasAct);
  }

  private Map<Direction, Double> formatDirectionWeights(
      Map<Direction, List<Double>> directionsWeights) {
    Map<Direction, Double> result = new HashMap<>();
    for (Direction direction : directionsWeights.keySet()) {
      List<Double> sortedWeights =
          directionsWeights.get(direction).stream()
              .sorted(Comparator.reverseOrder())
              .collect(Collectors.toList());
      result.put(direction, sortedWeights.get(0));
      int size = sortedWeights.size();
      switch (size) {
        case 1:
          result.put(direction, sortedWeights.get(0));
          break;
        case 2:
          result.put(direction, 0.1 * sortedWeights.get(1) + sortedWeights.get(0));
          break;
        default:
          result.put(
              direction,
              sortedWeights.get(0) + 0.1 * sortedWeights.get(1) + 0.01 * sortedWeights.get(2));
          break;
      }
    }
    return result;
  }

  private boolean hasAct(MollyAction mollyAction) {
    return mollyAction.toString().contains("ACT");
  }

  private Direction getDirection(MollyAction mollyAction) {
    MollyCommand command = mollyAction.getCommand();
    MollyCommand extraCommand = mollyAction.getExtraCommand();
    Optional<MollyCommand> moveCommand = Optional.empty();
    if (command.isMove()) {
      moveCommand = Optional.of(command);
    }
    if (extraCommand != null && extraCommand.isMove()) {
      moveCommand = Optional.of(extraCommand);
    }
    if (moveCommand.isPresent()) {
      switch (moveCommand.get()) {
        case UP:
          return Direction.UP;
        case DOWN:
          return Direction.DOWN;
        case LEFT:
          return Direction.LEFT;
        case RIGHT:
          return Direction.RIGHT;
      }
    }
    return Direction.STOP;
  }
}
