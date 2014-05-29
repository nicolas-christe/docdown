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

import org.parboiled.common.ImmutableList;
import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;

import java.util.List;

public class ExternalCodeNode extends AbstractNode {

    private final String file;
    private final String id;

    public ExternalCodeNode(String ref) {
        String[] source = ref.split(":");
        this.file = source[0].trim();
        if (source.length>1) {
            this.id = source[1].trim();
        } else {
            this.id = null;
        }
    }

    public String getFile() {
        return file;
    }

    public String getId() {
        return id;
    }

    public List<Node> getChildren() {
        return ImmutableList.of();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
