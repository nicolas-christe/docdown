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

import com.parrot.docdown.data.page.PackagePage;
import com.parrot.docdown.generator.DefaultGenerator;
import com.parrot.docdown.generator.PageRenderer;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import org.rendersnake.HtmlCanvas;

import java.io.IOException;
import java.util.Arrays;

import static org.rendersnake.HtmlAttributesFactory.class_;

public class PackageContentRenderable extends PageContentRenderable {

    public PackageContentRenderable(DefaultGenerator generator) {
        super(generator);
    }

    @Override
    protected void doRenderOn(HtmlCanvas html) throws IOException {
        PackagePage page = PageRenderer.getPage(html);
        PackageDoc packageDoc = page.getPackageDoc();

        html.div(class_("api_header")).p().content("package").h1().content(packageDoc.name())._div();
        if (packageDoc.inlineTags() != null && packageDoc.inlineTags().length > 0) {
            html.h2().content("Overview");
            html.p().render(new InlineTagsRenderer(generator, packageDoc.inlineTags(), packageDoc.position()))._p();
        }
        renderTypes(packageDoc.interfaces(), "Interfaces", page, html);
        renderTypes(packageDoc.ordinaryClasses(), "Classes", page, html);
        renderTypes(packageDoc.errors(), "Exceptions", page, html);
        renderTypes(packageDoc.enums(), "Enums", page, html);
        renderTypes(packageDoc.annotationTypes(), "Annotation Types", page, html);
    }

    private void renderTypes(ClassDoc[] classDocs, String title, PackagePage page, HtmlCanvas html) throws IOException {
        if (classDocs.length > 0) {
            Arrays.sort(classDocs);
            html.h2().content(title);
            html.table().tbody();
            int lineCnt = 0;
            for (ClassDoc classDoc : classDocs) {
                if ((lineCnt++) % 2 == 0) {
                    html.tr(class_("alt-color"));
                } else {
                    html.tr();
                }
                html.td().render(new ClassNameRenderable(generator, classDoc, ClassNameRenderable.LINK))._td();
                html.td().render(new InlineTagsRenderer(generator, classDoc.firstSentenceTags(),
                        classDoc.position()))._td();
                html._tr();
            }
            html._tbody()._table();
        }
    }
}
