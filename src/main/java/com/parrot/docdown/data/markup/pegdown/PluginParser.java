/*
 * Copyright (C) 2013-2013 Nicolas Christe
 * Copyright (C) 2013-2013 Parrot S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.parrot.docdown.data.markup.pegdown;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Extensions;
import org.pegdown.Parser;
import org.pegdown.ast.SimpleNode;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.WikiLinkNode;
import org.pegdown.plugins.BlockPluginParser;

public class PluginParser extends Parser implements BlockPluginParser {

    public PluginParser() {
        super(Extensions.FENCED_CODE_BLOCKS | Extensions.WIKILINKS, 1000L, Parser.DefaultParseRunnerProvider);
    }

    @Override
    public Rule[] blockPluginRules() {
        return new Rule[]{includeCodeRule()};
    }

    public Rule includeCodeRule() {
        return Sequence(
                "@(",
                OneOrMore(TestNot(')'), ANY), // might have to restrict from ANY
                push(new ExternalCodeNode(match())),
                ")",Sp(),Newline()
        );
    }
}
