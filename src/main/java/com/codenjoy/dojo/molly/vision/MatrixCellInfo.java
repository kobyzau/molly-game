package com.codenjoy.dojo.molly.vision;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatrixCellInfo {
  private int numStepsToGo = -1;
  private Set<Integer> firedAfterSeconds = new HashSet<>();
}
