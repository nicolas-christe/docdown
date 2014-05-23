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
import com.parrot.docdown.data.page.ApiPage;
import com.parrot.docdown.generator.DefaultGenerator;
import com.parrot.docdown.generator.PageRenderer;
import com.sun.javadoc.PackageDoc;
import org.rendersnake.HtmlCanvas;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.href;

public class PackageListRenderable extends PageContentRenderable {

    public PackageListRenderable(DefaultGenerator generator) {
        super(generator);
    }

    @Override
    protected void doRenderOn(HtmlCanvas html) throws IOException {
        ApiPage page = PageRenderer.getPage(html);

        TableRenderer tableRenderer = new TableRenderer(2, "Package Index");
        for (PackageDoc packageDoc : generator.getRootDoc().specifiedPackages()) {
            tableRenderer.startRow(html);
            DocReferenceable ref = generator.getRefLocator().getPackageDocRef(packageDoc);
            String relPath = page.getReferenceTo(ref);
            html.td().a(href(relPath)).content(packageDoc.name())._td();
            html.td()._td();
            tableRenderer.closeRow(html);
        }
        tableRenderer.closeTable(html);
    }
}
