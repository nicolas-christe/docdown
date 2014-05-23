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

import com.parrot.docdown.generator.DefaultGenerator;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.href;

public abstract class PageContentRenderable implements Renderable {

    protected final DefaultGenerator generator;

    public PageContentRenderable(DefaultGenerator generator) {
        this.generator = generator;
    }

    @Override
    public final void renderOn(HtmlCanvas html) throws IOException {
        html.div(class_("content"));
        doRenderOn(html);
        html.div(class_("footer")).write("Generated by ").a(href("https://github.com/nicolaschriste/docdown"))
                .content("Docdown")._div();
        html._div();
    }

    protected abstract void doRenderOn(HtmlCanvas html) throws IOException;
}
