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

package com.parrot.docdown.generator;


import com.parrot.docdown.data.page.IDocPage;
import com.parrot.docdown.generator.html.MainHeaderRenderable;
import com.parrot.docdown.generator.html.NavHeaderRenderable;
import com.parrot.docdown.generator.html.PageRenderable;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.PageContext;
import org.rendersnake.Renderable;

import java.io.IOException;

public abstract class PageRenderer {

    public static final String CTXKEY_PAGE = "page";

    protected final Renderable pageRenderable;

    protected final DefaultGenerator generator;

    public PageRenderer(DefaultGenerator generator) {
        this.generator = generator;
        pageRenderable = new PageRenderable(generator, new NavHeaderRenderable(generator),
                new MainHeaderRenderable(generator), createNavContentRenderable(), createMainContentRenderable());
    }

    protected abstract Renderable createNavContentRenderable();

    protected abstract Renderable createMainContentRenderable();

    public void render(IDocPage page) throws IOException {
        HtmlCanvas canvas = new HtmlCanvas(page.createWriter());
        setupRendering(canvas.getPageContext(), page);
        canvas.render(pageRenderable);
        canvas.getOutputWriter().close();
    }

    protected void setupRendering(PageContext context, IDocPage page) {
        context.withObject(CTXKEY_PAGE, page);
    }

    @SuppressWarnings("unchecked")
    public static <T extends IDocPage> T getPage(PageContext context) {
        return (T) context.getObject(CTXKEY_PAGE);
    }

    public static <T extends IDocPage> T getPage(HtmlCanvas html) {
        return getPage(html.getPageContext());
    }

}
