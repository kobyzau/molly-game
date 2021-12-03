package com.codenjoy.dojo.molly.action;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MollyWishComparatorTest {

  @Test
  public void compare_sameLevel() {
    // given
    List<MollyWish> wishList =
        Arrays.asList(
            getWish(2, MollyWishLevel.MEDIUM),
            getWish(0, MollyWishLevel.MEDIUM),
            getWish(4, MollyWishLevel.MEDIUM),
            getWish(11, MollyWishLevel.MEDIUM));

    // when
    List<MollyWish> sorted =
        wishList.stream().sorted(new MollyWishComparator().reversed()).collect(Collectors.toList());

    // then
    Assert.assertEquals(
        Arrays.asList(
            getWish(0, MollyWishLevel.MEDIUM),
            getWish(2, MollyWishLevel.MEDIUM),
            getWish(4, MollyWishLevel.MEDIUM),
            getWish(11, MollyWishLevel.MEDIUM)),
        sorted);
  }

  @Test
  public void compare_sameStep() {
    // given
    List<MollyWish> wishList =
        Arrays.asList(
            getWish(4, MollyWishLevel.MEDIUM),
            getWish(4, MollyWishLevel.LOW),
            getWish(4, MollyWishLevel.TOP),
            getWish(4, MollyWishLevel.HIGH));

    // when
    List<MollyWish> sorted =
        wishList.stream().sorted(new MollyWishComparator().reversed()).collect(Collectors.toList());

    // then
    Assert.assertEquals(
        Arrays.asList(
            getWish(4, MollyWishLevel.TOP),
            getWish(4, MollyWishLevel.HIGH),
            getWish(4, MollyWishLevel.MEDIUM),
            getWish(4, MollyWishLevel.LOW)),
        sorted);
  }

  @Test
  public void compare_mixed() {
    // given
    List<MollyWish> wishList =
        Arrays.asList(
            getWish(0, MollyWishLevel.TOP),
            getWish(0, MollyWishLevel.HIGH),
            getWish(0, MollyWishLevel.MEDIUM),
            getWish(0, MollyWishLevel.LOW),
            getWish(1, MollyWishLevel.TOP),
            getWish(1, MollyWishLevel.HIGH),
            getWish(1, MollyWishLevel.MEDIUM),
            getWish(1, MollyWishLevel.LOW),
            getWish(2, MollyWishLevel.TOP),
            getWish(2, MollyWishLevel.HIGH),
            getWish(2, MollyWishLevel.MEDIUM),
            getWish(2, MollyWishLevel.LOW),
            getWish(3, MollyWishLevel.TOP),
            getWish(3, MollyWishLevel.HIGH),
            getWish(3, MollyWishLevel.MEDIUM),
            getWish(3, MollyWishLevel.LOW),
            getWish(4, MollyWishLevel.TOP),
            getWish(4, MollyWishLevel.HIGH),
            getWish(4, MollyWishLevel.MEDIUM),
            getWish(4, MollyWishLevel.LOW),
            getWish(5, MollyWishLevel.TOP),
            getWish(5, MollyWishLevel.HIGH),
            getWish(5, MollyWishLevel.MEDIUM),
            getWish(5, MollyWishLevel.LOW),
            getWish(6, MollyWishLevel.TOP),
            getWish(6, MollyWishLevel.HIGH),
            getWish(6, MollyWishLevel.MEDIUM),
            getWish(6, MollyWishLevel.LOW),
            getWish(7, MollyWishLevel.TOP),
            getWish(7, MollyWishLevel.HIGH),
            getWish(7, MollyWishLevel.MEDIUM),
            getWish(7, MollyWishLevel.LOW));

    // when
    List<MollyWish> sorted =
        wishList.stream().sorted(new MollyWishComparator().reversed()).collect(Collectors.toList());

    // then
    Assert.assertEquals(
        Arrays.asList(
            getWish(0, MollyWishLevel.TOP),
            getWish(1, MollyWishLevel.TOP),
            getWish(2, MollyWishLevel.TOP),
            getWish(3, MollyWishLevel.TOP),
            getWish(4, MollyWishLevel.TOP),
            getWish(5, MollyWishLevel.TOP),
            getWish(6, MollyWishLevel.TOP),
            getWish(7, MollyWishLevel.TOP),
            getWish(0, MollyWishLevel.HIGH),
            getWish(1, MollyWishLevel.HIGH),
            getWish(2, MollyWishLevel.HIGH),
            getWish(3, MollyWishLevel.HIGH),
            getWish(0, MollyWishLevel.MEDIUM),
            getWish(4, MollyWishLevel.HIGH),
            getWish(1, MollyWishLevel.MEDIUM),
            getWish(5, MollyWishLevel.HIGH),
            getWish(2, MollyWishLevel.MEDIUM),
            getWish(6, MollyWishLevel.HIGH),
            getWish(3, MollyWishLevel.MEDIUM),
            getWish(0, MollyWishLevel.LOW),
            getWish(7, MollyWishLevel.HIGH),
            getWish(4, MollyWishLevel.MEDIUM),
            getWish(1, MollyWishLevel.LOW),
            getWish(5, MollyWishLevel.MEDIUM),
            getWish(2, MollyWishLevel.LOW),
            getWish(6, MollyWishLevel.MEDIUM),
            getWish(3, MollyWishLevel.LOW),
            getWish(7, MollyWishLevel.MEDIUM),
            getWish(4, MollyWishLevel.LOW),
            getWish(5, MollyWishLevel.LOW),
            getWish(6, MollyWishLevel.LOW),
            getWish(7, MollyWishLevel.LOW)),
        sorted);
  }

  private MollyWish getWish(int steps, MollyWishLevel level) {
    return new MollyWish(MollyAction.act(), steps, level);
  }
}
