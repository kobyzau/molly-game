package com.codenjoy.dojo.games.mollymage;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.printer.CharElement;

import java.util.Arrays;
import java.util.List;

public enum Element implements CharElement {

/// the potions

    POTION_TIMER_5('5',        "After Molly set the potion, the timer starts (5 ticks)."),

    POTION_TIMER_4('4',        "This potion will blow up after 4 ticks."),

    POTION_TIMER_3('3',        "This after 3..."),

    POTION_TIMER_2('2',        "Two.."),

    POTION_TIMER_1('1',        "One."),

    BOOM('҉',                  "Boom! this is what is potion does, everything that is " +
                               "destroyable got destroyed."),

/// walls

    WALL('☼',                  "Indestructible wall - it will not fall from potion."),

    TREASURE_BOX('#',          "This is a treasure box, it opens with an explosion."),

    OPENING_TREASURE_BOX('H',  "This is like a treasure box opens looks like, it will " +
                               "disappear on next move. If it's you did it - you'll " +
                               "get score points. Perhaps a prize will appear."),

/// soulless creatures

    GHOST('&',                 "This guys runs over the board randomly and gets in the way " +
                               "all the time. If it will touch Molly - she will die. " +
                               "You'd better kill this piece of ... soul, you'll get score " +
                               "points for it."),

    DEAD_GHOST('x',            "This is ghost corpse."),

/// perks

    POTION_BLAST_RADIUS_INCREASE('+', "Temporarily increase potion radius blast. " +
                                      "Applicable only to new potions."),

    POTION_COUNT_INCREASE('c', "Temporarily increase available potions count. " +
                               "Number of extra potions can be set in settings*."),

    POTION_REMOTE_CONTROL('r', "Next several potions would be with remote control. " +
                               "Activating by command ACT. Number of RC triggers " +
                               "is limited and can be set in settings*."),

    POTION_IMMUNE('i',         "Temporarily gives you immunity from potion blasts " +
                               "(own potion and others as well)."),

    POISON_THROWER('T',        "Hero can shoot by poison cloud. Using: ACT(1)+Direction. " +
                               "Temporary."),

    POTION_EXPLODER('A',       "Hero can explode all potions on the field. " +
                               "Using: ACT(2). Temporary."),

/// a void

    NONE(' ',                  "A void. This is the only place where you can move your Molly."),

/// your Molly

    HERO('☺',                  "This is what your Molly usually looks like."),

    POTION_HERO('☻',           "This is if your Molly is sitting on own potion."),

    DEAD_HERO('Ѡ',             "Oops, your Molly is dead (don't worry, she will appear " +
                               "somewhere in next move). You're getting penalty points " +
                               "for each death."),

/// other players heroes

    OTHER_HERO('♥',            "This is what other heroes looks like."),

    OTHER_POTION_HERO('♠',     "This is if other hero is sitting on own potion."),

    OTHER_DEAD_HERO('♣',       "Other hero corpse (it will disappear shortly, right " +
                               "on the next move). If you've done it you'll get " +
                               "score points."),

/// enemy players heroes

    ENEMY_HERO('ö',            "This is what enemy heroes looks like."),

    ENEMY_POTION_HERO('Ö',     "This is if enemy hero is sitting on own potion."),

    ENEMY_DEAD_HERO('ø',       "Enemy hero corpse (it will disappear shortly, right " +
                               "on the next move). If you've done it you'll get " +
                               "score points.");

    public static final String POTIONS = "12345";

    private final char ch;
    private final String info;

    Element(char ch, String info) {
        this.ch = ch;
        this.info = info;
    }

    public static List<Element> perks() {
        return Arrays.asList(
                POTION_BLAST_RADIUS_INCREASE,
                POTION_COUNT_INCREASE,
                POTION_IMMUNE,
                POTION_REMOTE_CONTROL,
                POISON_THROWER,
                POTION_EXPLODER
        );
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String info() {
        return info;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Element valueOf(char ch) {
        for (Element el : Element.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

    public boolean isPotion() {
        return POTIONS.indexOf(ch) != -1;
    }

    public boolean isHero() {
        return this == Element.HERO ||
               this == Element.POTION_HERO ||
               this == Element.DEAD_HERO;
    }

}
