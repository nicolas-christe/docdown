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

package com.parrot.docdown.generator.html.pegdown;

import com.parrot.docdown.data.DocReferenceable;
import com.parrot.docdown.data.markup.MarkupDoc;
import com.parrot.docdown.data.markup.pegdown.ExternalCodeNode;
import com.parrot.docdown.data.markup.pegdown.PegdownDoc;
import com.parrot.docdown.data.page.MarkupPage;
import com.parrot.docdown.generator.DefaultGenerator;
import com.parrot.docdown.generator.PageRenderer;
import com.parrot.docdown.generator.html.MarkupContentRenderable;
import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.*;
import org.rendersnake.HtmlCanvas;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PegdownContentRenderable extends MarkupContentRenderable {

    public PegdownContentRenderable(DefaultGenerator generator) {
        super(generator);
    }

    @Override
    public void renderContent(HtmlCanvas html, MarkupDoc markupDoc) throws IOException {
        final PegdownDoc pegdownDoc = (PegdownDoc) markupDoc;
        final MarkupPage page = PageRenderer.getPage(html);

        LinkRenderer linkRenderer = new LinkRenderer() {
            @Override
            public Rendering render(ExpLinkNode node, String text) {
                try {
                    if (new URI(node.url).isAbsolute()) {
                        return super.render(node, text);
                    }
                } catch (URISyntaxException e) {
                }
                return getRendering(node.url, text);
            }

            @Override
            public Rendering render(RefLinkNode node, String url, String title, String text) {
                try {
                    if (new URI(url).isAbsolute()) {
                        return super.render(node, url, title, text);
                    }
                } catch (URISyntaxException e) {
                }
                return getRendering(url, text);
            }

            @Override
            public Rendering render(WikiLinkNode node) {
                try {
                    if (new URI(node.getText()).isAbsolute()) {
                        return super.render(node);
                    }
                } catch (URISyntaxException e) {
                }
                return getRendering(node.getText(), null);
            }

            private Rendering getRendering(String url, String text) {
                DocReferenceable ref = generator.getRefLocator().find(url, pegdownDoc.getSourcePosition(),
                        pegdownDoc.getContainer());
                if (ref != null) {
                    return new Rendering(page.getReferenceTo(ref), text!=null?text:ref.getName());
                }
                return new Rendering("#", "MISSING REF: " + url).withAttribute("class", "error");
            }
        };

        String out = new ToHtmlSerializer(linkRenderer) {
            public void visit(RootNode node) {
                for (ReferenceNode refNode : node.getReferences()) {
                    visitChildren(refNode);
                    references.put(normalize(printer.getString()), refNode);
                    printer.clear();
                }
                for (AbbreviationNode abbrNode : node.getAbbreviations()) {
                    visitChildren(abbrNode);
                    String abbr = printer.getString();
                    printer.clear();
                    abbrNode.getExpansion().accept(this);
                    String expansion = printer.getString();
                    abbreviations.put(abbr, expansion);
                    printer.clear();
                }
                boolean skip = node.equals(pegdownDoc.getRootNode());
                for (Node child : node.getChildren()) {
                    // skip everything up to the first header
                    if (skip && child instanceof HeaderNode) {
                        skip = false;
                    }
                    if (!skip) {
                        child.accept(this);
                    }
                }
            }

            @Override
            public void visit(Node node) {
                if (node instanceof ExternalCodeNode) {
                    printExternalCode((ExternalCodeNode) node);
                } else {
                    super.visit(node);
                }
            }

            @Override
            protected void printImageTag(LinkRenderer.Rendering rendering) {
                if (!rendering.href.equals("#")) {
                    super.printImageTag(rendering);
                } else {
                    printer.print("<span class=\"error\">");
                    printer.print(rendering.text);
                    printer.print("</span>");
                }
            }

            private void printExternalCode(ExternalCodeNode node) {
                String[] code = generator.getRefLocator().findIncludedCode(node.getFile(), node.getId(),
                        pegdownDoc.getSourcePosition());
                if (code != null) {
                    HtmlCanvas tmpCanvas = new HtmlCanvas();
                    renderCodeBlock(tmpCanvas, "java", code);
                    printer.print(tmpCanvas.toHtml());
                } else {
                    printer.print("<span class=\"error\">");
                    printer.print("MISSING REF: " + node.getFile());
                    if (node.getId()!=null) {
                        printer.print(":" + node.getId());
                    }
                    printer.print("</span><br>");

                }
            }

        }.toHtml(pegdownDoc.getRootNode());

        html.write(out, HtmlCanvas.NO_ESCAPE);
    }
}
