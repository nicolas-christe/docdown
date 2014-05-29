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

import com.parrot.docdown.data.markup.MarkupDoc;
import com.parrot.docdown.data.page.MarkupPage;
import com.parrot.docdown.generator.DefaultGenerator;
import com.parrot.docdown.generator.PageRenderer;
import org.rendersnake.HtmlCanvas;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;


public abstract class MarkupContentRenderable extends PageContentRenderable {

    public MarkupContentRenderable(DefaultGenerator generator) {
        super(generator);
    }

    @Override
    protected void doRenderOn(HtmlCanvas html) throws IOException {
        MarkupPage page = PageRenderer.getPage(html);
        MarkupDoc markupDoc = page.getMarkupDoc();
        renderContent(html, markupDoc);
    }

    public abstract void renderContent(HtmlCanvas html, MarkupDoc markupDoc) throws IOException;

    protected void renderCodeBlock(HtmlCanvas html, String language, String[] codeLines)  {
        try {
            if (codeLines != null) {
                int intentLevel = getIntentLevel(codeLines);
                html.pre().code(class_(language));
                for (String line : codeLines) {
                    html.write(unindentLine(line, intentLevel) + '\n');
                }
                html._code()._pre();
            }
        } catch(IOException e) {
            // ignore...
        }
    }

    public int getIntentLevel(String[] codeLines) {
        int level=Integer.MAX_VALUE;
        for (String line: codeLines) {
            int pos = 0;
            while (pos<line.length() && line.charAt(pos)==' ') {
                pos++;
            }
            if (pos<line.length()) {
                level = Math.min(level, pos);
            }
        }
        return level;
    }

    protected String unindentLine(String line, int intentLevel) {
        return line.substring(Math.min(intentLevel, line.length()));
    }
}
