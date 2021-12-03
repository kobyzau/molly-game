package com.codenjoy.dojo.client;

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
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.SortedJSONObject;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Utils {

    public static final int COUNT_NUMBERS = 3;

    public static String clean(String json) {
        return json.replace('\"', '\'').replaceAll("\\r\\n", "\n");
    }

    public static String prettyPrintObject(Object object) {
        return clean(new SortedJSONObject(object).toString(2));
    }

    public static String prettyPrint(String json) {
        return clean(new SortedJSONObject(json).toString(2));
    }

    public static String unescapeJava(String data) {
        return StringEscapeUtils.unescapeJava(data.replaceAll("\\\\u", "\\u"));
    }

    // TODO дублирование с TestUtils.injectN
    public static String injectN(String expected) {
        int size = (int) Math.sqrt(expected.length());
        return inject(expected, size, "\n");
    }

    // TODO дублирование с TestUtils.injectNN
    public static String injectNN(String expected) {
        int size = (int) Math.sqrt(expected.length() / COUNT_NUMBERS) * COUNT_NUMBERS;
        return inject(expected, size, "\n");
    }

    // TODO дублирование с TestUtils.inject
    public static String inject(String string, int position, String substring) {
        StringBuilder result = new StringBuilder();
        for (int index = 1; index < string.length() / position + 1; index++) {
            result.append(string, (index - 1)*position, index*position).append(substring);
        }
        result.append(string.substring((string.length() / position) * position));
        return result.toString();
    }

    public static <E extends CharElement> String elements(AbstractBoard<E> board, E[] elements) {
        return split(elementsMap(board, elements),
                "]], \n",
                "[], \n");
    }

    // TODO подумать как и унести в AbstractLayeredBoard
    public static <E extends CharElement> Map<E, String> elementsMap(AbstractBoard<E> board, E[] elements) {
        return Arrays.stream(elements)
                .map(element -> Map.entry(element, board.get(element).toString()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (value1, value2) -> value2,
                        LinkedHashMap::new));
    }

    public static String split(Object object, String... splits) {
        String result = object.toString();
        for (String split : splits) {
            result = result.replace(
                    split.replaceAll("[\n\0\t]", ""),
                    split.replace("\0", " ").replace("\t", "    "));
        }
        return result;
    }

    public static List<String> getStrings(JSONArray array) {
        return new LinkedList<>(){{
            array.toList().forEach(it -> add((String)it));
        }};
    }

}
