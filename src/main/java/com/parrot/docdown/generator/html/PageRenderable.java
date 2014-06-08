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


import com.parrot.docdown.data.page.IDocPage;
import com.parrot.docdown.generator.AssetsDir;
import com.parrot.docdown.generator.DefaultGenerator;
import com.parrot.docdown.generator.PageRenderer;
import org.rendersnake.DocType;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.charset;
import static org.rendersnake.HtmlAttributesFactory.id;

public class PageRenderable implements Renderable {

    private final AssetsDir assetDir;
    private final Renderable navHeadRenderable;
    private final Renderable mainHeadRenderable;
    private final Renderable navContentRenderable;
    private final Renderable mainContentRenderable;

    public PageRenderable(DefaultGenerator generator, Renderable navHeadRenderable, Renderable mainHeadRenderable,
                          Renderable navContentRenderable, Renderable mainContentRenderable) {
        this.assetDir = generator.getAssertsDir();
        this.navHeadRenderable = navHeadRenderable;
        this.mainHeadRenderable = mainHeadRenderable;
        this.navContentRenderable = navContentRenderable;
        this.mainContentRenderable = mainContentRenderable;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        IDocPage page = PageRenderer.getPage(html);

        String assertRelDir = page.getReferenceTo(assetDir.getPath()) + "/";

        // write the doc type
        html.render(DocType.HTML5).html();
        // write header
        html.head();
        html.title().content(page.getTitle());
        html.meta(charset("utf-8"));
        html.macros().javascript(assertRelDir + "jquery-1.10.2.js");
        html.macros().javascript(assertRelDir + "jquery-ui-1.10.4.js");
        html.macros().javascript(assertRelDir + "highlight.js");
        html.macros().stylesheet(assertRelDir + "highlight-default.css");
        html.macros().javascript(assertRelDir + "quickdoc.js");
        html.macros().stylesheet(assertRelDir + "quickdoc.css");
        html.script().content("hljs.initHighlightingOnLoad();");
        html._head();
        // open body layout
        html.body();
        // write side box
        html.div(id("side")).div(id("header")).render(navHeadRenderable)._div().div(id("content")).render
                (navContentRenderable)._div().
                _div();

        // write main box
        html.div(id("main")).div(id("header")).render(mainHeadRenderable)._div().div(id("content")).render
                (mainContentRenderable)._div().
                _div();

        // addPage call to init script
        html.script().content("init();", false);
        // close body and generator
        html._body()._html();
        // done, close writer
        html.getOutputWriter().close();
    }

}
