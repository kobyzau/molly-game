package com.codenjoy.dojo.client.generator.language;

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

import com.codenjoy.dojo.client.generator.Template;

import java.util.List;

public class Js implements Template {

    @Override
    public String header(List<String> locales) {
        return "/*-\n" +
                " * ${tag}\n" +
                " * Codenjoy - it's a dojo-like platform from developers to developers.\n" +
                " * %%\n" +
                " * Copyright (C) 2021 Codenjoy\n" +
                " * %%\n" +
                " * This program is free software: you can redistribute it and/or modify\n" +
                " * it under the terms of the GNU General Public License as\n" +
                " * published by the Free Software Foundation, either version 3 of the\n" +
                " * License, or (at your option) any later version.\n" +
                " *\n" +
                " * This program is distributed in the hope that it will be useful,\n" +
                " * but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                " * GNU General Public License for more details.\n" +
                " *\n" +
                " * You should have received a copy of the GNU General Public\n" +
                " * License along with this program.  If not, see\n" +
                " * <http://www.gnu.org/licenses/gpl-3.0.html>.\n" +
                " * #L%\n" +
                " */\n" +
                "\n" +
                "var ${game-capitalize}Element = module.exports = {\n";
    }

    @Override
    public String line(boolean subrepo) {
        return "    ${element} : '${char}',\n";
    }

    @Override
    public String lastDelimiter() {
        return "";
    }

    @Override
    public String comment() {
        return "        // ";
    }

    @Override
    public String footer() {
        return "\n" +
                "}\n";
    }

    @Override
    public String file() {
        return "java-script/games/${game}/elements.js"; // TODO rename to element.js
    }
}
