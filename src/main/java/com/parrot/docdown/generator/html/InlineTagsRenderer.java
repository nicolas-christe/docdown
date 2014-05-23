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
import com.parrot.docdown.generator.DefaultGenerator;
import com.parrot.docdown.generator.PageRenderer;
import com.sun.javadoc.*;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.href;

public class InlineTagsRenderer implements Renderable {

    private final DefaultGenerator generator;
    private final Tag[] tags;
    private final SourcePosition sourcePosition;

    public InlineTagsRenderer(DefaultGenerator generator, Tag[] tags, SourcePosition sourcePosition) {
        this.generator = generator;
        this.tags = tags;
        this.sourcePosition = sourcePosition;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        boolean needSeparator = false;
        for (Tag tag : tags) {
            switch (tag.kind()) {
                case "@see":
                    renderSeeTag((SeeTag) tag, needSeparator, html);
                    needSeparator = true;
                    break;
                case "@doclink":
                    renderLinkDocTag(tag, needSeparator, html);
                    needSeparator = true;
                    break;
                case "Text":
                default:
                    renderTextTag(tag, html);
                    needSeparator = false;
                    break;
            }
        }
    }


    private void renderTextTag(Tag tag, HtmlCanvas html) throws IOException {
        html.write(tag.text(), HtmlCanvas.NO_ESCAPE);
    }

    private void renderSeeTag(SeeTag tag, boolean separator, HtmlCanvas html) throws IOException {
        DocReferenceable referenceable = null;
        // serach the ref type
        if (tag.referencedPackage() != null) {
            referenceable = generator.getRefLocator().getPackageDocRef(tag.referencedPackage());
        } else if (tag.referencedMember() != null) {
            MemberDoc referencedMember = tag.referencedMember();
            if (referencedMember instanceof FieldDoc) {
                referenceable = generator.getRefLocator().getFieldDocRef((FieldDoc) referencedMember);
            } else if (referencedMember instanceof ExecutableMemberDoc) {
                referenceable = generator.getRefLocator().getExecutableMemberDocRef((ExecutableMemberDoc)
                        referencedMember);
            }
        } else if (tag.referencedClass() != null) {
            referenceable = generator.getRefLocator().getClassDocRef(tag.referencedClass());
        }
        renderRef(separator, html, referenceable, tag.label());
    }

    private void renderLinkDocTag(Tag tag, boolean separator, HtmlCanvas html) throws IOException {
        String refString;
        String label;
        int separatorPos = tag.text().indexOf(" ");
        if (separatorPos > 0) {
            refString = tag.text().substring(0, separatorPos);
            label = tag.text().substring(separatorPos + 1);
        } else {
            refString = tag.text();
            label = null;
        }
        DocReferenceable referenceable = generator.getRefLocator().find(refString, tag.position());
        renderRef(separator, html, referenceable, label);
    }

    private void renderRef(boolean separator, HtmlCanvas html, DocReferenceable referenceable,
                           String label) throws IOException {
        if (referenceable != null) {
            String text;
            if (label != null && !label.isEmpty()) {
                text = label;
            } else {
                text = referenceable.getName();
            }
            if (separator) {
                html.write(", ");
            }
            String ref = PageRenderer.getPage(html).getReferenceTo(referenceable);
            if (ref != null) {
                html.a(href(ref)).content(text, HtmlCanvas.NO_ESCAPE);
            } else {
                html.code().content(text, HtmlCanvas.NO_ESCAPE);
            }
        }
    }


}
