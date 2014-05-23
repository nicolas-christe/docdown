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

package com.parrot.docdown.generator.html;

import com.parrot.docdown.data.DocReferenceable;
import com.parrot.docdown.data.markup.IndexDoc;
import com.parrot.docdown.data.markup.MarkupDoc;
import com.parrot.docdown.data.page.MarkupPage;
import com.parrot.docdown.generator.DefaultGenerator;
import com.parrot.docdown.generator.PageRenderer;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.href;


public class MarkupNavContentRenderable implements Renderable {

    private final DefaultGenerator generator;
    private final IndexDoc rootIndexDoc;

    public MarkupNavContentRenderable(DefaultGenerator generator) {
        this.generator = generator;
        this.rootIndexDoc = generator.getRootProjectDoc().getRootIndexDoc();
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        MarkupPage page = PageRenderer.getPage(html);
        MarkupDoc markupDoc = page.getMarkupDoc();
        LinkedList<IndexDoc> indexes = new LinkedList<>();

        // build indexes list in reverse order
        IndexDoc indexDoc = generator.getRootProjectDoc().getIndexDoc(markupDoc);
        while (indexDoc != null && indexDoc != rootIndexDoc) {
            indexes.addFirst(indexDoc);
            indexDoc = generator.getRootProjectDoc().getIndexDoc(indexDoc.getParentContainer());
        }

        html.div(class_("nav"));
        writeIndexes(indexes, 0, markupDoc, page, html);
        html._div();
    }

    private void writeIndexes(List<IndexDoc> indexes, int pos, MarkupDoc markupDoc, MarkupPage page,
                              HtmlCanvas html) throws IOException {
        if (pos < indexes.size()) {
            IndexDoc index = indexes.get(pos);
            IndexDoc subIndex = (pos + 1) < indexes.size() ? indexes.get(pos + 1) : null;
            for (IndexDoc.Entry entry : index.getContent()) {
                html.ul();
                MarkupDoc target = entry.getTarget();
                if (target != null) {
                    if (target.equals(markupDoc)) {
                        html.li().span(class_("highlighted")).content(entry.getLabel())._li();
                    } else {
                        DocReferenceable ref = generator.getRefLocator().getMarkupDocRef(target);
                        String relPath = page.getReferenceTo(ref);
                        html.li().a(href(relPath)).content(entry.getLabel())._li();
                    }
                    if (generator.getRootProjectDoc().getIndexDoc(target).equals(subIndex)) {
                        writeIndexes(indexes, pos + 1, markupDoc, page, html);
                    }
                }
                html._ul();
            }
        }
    }

}
