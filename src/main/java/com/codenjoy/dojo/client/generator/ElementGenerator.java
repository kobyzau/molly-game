package com.codenjoy.dojo.client.generator;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2021 Codenjoy
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

import com.codenjoy.dojo.client.generator.language.Go;
import com.codenjoy.dojo.games.sample.Element;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.utils.SmokeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.capitalize;

public class ElementGenerator {

    public static final int COMMENT_MAX_LENGTH = 60;

    public static final List<String> SUBREPO_GAMES = Arrays.asList(
            "chess", "clifford", "excitebike",
            "japanese", "mollymage", "selfdefense",
            "vacuum", "verland", "xonix");

    public static final List<String> ENGLISH_PRESENT = Arrays.asList(
            "a2048", "clifford", "kata", "snake",
            "snakebattle", "vacuum");

    public static final List<String> DIFFERENT_NAME_GAMES = Arrays.asList(
            "sample-text");

    private final String game;
    private final String canonicalGame;

    private final boolean subrepo;
    private final String language;
    private final Template template;

    public ElementGenerator(String game, String language) {
        this.canonicalGame = game;
        this.game = getGame(game);

        this.language = language;
        this.template = template();
        subrepo = SUBREPO_GAMES.contains(game);
    }

    public static String getCanonicalGame(String game) {
        for (String canonicalName : DIFFERENT_NAME_GAMES) {
            if (game.equals(getGame(canonicalName))) {
                return canonicalName;
            }
        }
        return game;
    }

    public static String getGame(String canonicalGame) {
        return canonicalGame.replaceAll("[-_.]", "");
    }

    public String generate() {
        return build(elements());
    }

    public void generateToFile(String base) {
        String data = build(elements());
        String preffix = "/";
        if ("CodingDojo".equals(new File(base).getAbsoluteFile().getName())) {
            preffix = "/clients/";
        }
        File dest = new File(base + preffix + replace(template.file()));
        System.out.printf("Store '%s-%s' in file: '%s'\n",
                game, language, dest.getAbsolutePath());
        SmokeUtils.saveToFile(dest, data);
    }

    private CharElement[] elements() {
        try {
            String className = Element.class.getCanonicalName().replace("sample", game);

            return (CharElement[]) getClass().getClassLoader().loadClass(className)
                    .getEnumConstants();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Template template() {
        try {
            String className = Go.class.getPackageName() + "."
                    + capitalize(language);

            Class<?> clazz = getClass().getClassLoader().loadClass(className);
            return (Template) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String build(CharElement[] elements) {
        Template template = template();

        String header = replace(template.header(locales()));

        List<String> lines = Arrays.stream(elements)
                .map(el -> replace(template.line(subrepo), el))
                .collect(toList());

        List<List<String>> infos = Arrays.stream(elements)
                .map(el -> splitLength(el.info(), COMMENT_MAX_LENGTH))
                .collect(toList());

        StringBuilder body = new StringBuilder();
        for (int index = 0; index < lines.size(); index++) {
            if (template.printComment()) {
                List<String> comments = infos.get(index);
                if (!comments.isEmpty()) {
                    comments.forEach(comment ->
                            body.append('\n')
                                    .append(template.comment())
                                    .append(comment));
                    if (template.printNewLine()) {
                        body.append('\n');
                    }
                }
            }

            if (template.printLines()) {
                if (template.printNewLine()) {
                    body.append('\n');
                }
                String line = lines.get(index);
                if (template.lastDelimiter() != null && index == lines.size() - 1) {
                    int count = (line.charAt(line.length() - 1) == '\n') ? 2 : 1;
                    line = line.substring(0, line.length() - count)
                            + template.lastDelimiter();
                }
                body.append(line);
            }
        }

        String footer = replace(template.footer());

        return header
                + body
                + footer;
    }

    private List<String> locales() {
        if (ENGLISH_PRESENT.contains(canonicalGame)) {
            return Arrays.asList("ru", "en");
        } else {
            return Arrays.asList("ru");
        }
    }

    private String replace(String template, CharElement element) {
        return replace(template)
                .replace("${element-lower}", element.name().toLowerCase())
                .replace("${element}", element.name())
                .replace("${char}", String.valueOf(element.ch()))
                .replace("${info}", element.info());
    }

    private String replace(String template) {
        return template
                .replace("${tag}", "#" + "%L") // because of warning in the mvn compile in phase lecense header generation
                .replace("${language}", language)
                .replace("${game}", game)
                .replace("${game-canonical}", canonicalGame)
                .replace("${game-capitalize}", capitalize(game));
    }

    private List<String> splitLength(String text, int length) {
        return new LinkedList<>(){{
            if (StringUtils.isNotEmpty(text)) {
                List<String> words = new LinkedList<>(Arrays.asList(text.split(" ")));

                String line = "";
                while (!words.isEmpty()) {
                    if ((line + " " + words.get(0)).length() <= length) {
                        if (StringUtils.isNotEmpty(line)) {
                            line += " ";
                        }
                        line += words.remove(0);
                    } else {
                        add(line);
                        line = "";
                    }
                }
                add(line);
            }
        }};
    }
}
