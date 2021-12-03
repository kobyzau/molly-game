package com.codenjoy.dojo.molly.action;

import java.util.Comparator;

public class MollyWishComparator implements Comparator<MollyWish> {

  @Override
  public int compare(MollyWish w1, MollyWish w2) {
    return Double.compare(w1.getWeight(), w2.getWeight());
  }
}
