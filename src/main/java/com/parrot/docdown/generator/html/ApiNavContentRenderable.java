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
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;
import java.util.Arrays;

import static org.rendersnake.HtmlAttributesFactory.*;


public class ApiNavContentRenderable implements Renderable {

    private final DefaultGenerator generator;
    private final PackageDoc[] packages;

    public ApiNavContentRenderable(DefaultGenerator generator) {
        this.generator = generator;
        packages = generator.getRootDoc().specifiedPackages();
        generator.getRootDoc().specifiedClasses();
        Arrays.sort(packages);
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        ApiPage page = PageRenderer.getPage(html);
        // packages list
        html.div(id("nav-top")).div(class_("nav"));
        for (PackageDoc packageDoc : packages) {
            DocReferenceable ref = generator.getRefLocator().getPackageDocRef(packageDoc);
            String relPath = page.getReferenceTo(ref);
            html.p().a(href(relPath));
            if (packageDoc.equals(page.getPackageDoc())) {
                html.span(class_("highlighted")).content(packageDoc.name());
            } else {
                html.write(packageDoc.name());
            }
            html._a()._p();
        }
        html._div()._div();

        // class list
        PackageDoc packageDoc = page.getPackageDoc();
        if (packageDoc != null) {
            html.div(id("nav-bottom")).div(class_("nav").data("ctx", packageDoc.name()));
            renderTypes(packageDoc.interfaces(), "Interfaces", page, html);
            renderTypes(packageDoc.ordinaryClasses(), "Classes", page, html);
            renderTypes(packageDoc.errors(), "Exceptions", page, html);
            renderTypes(packageDoc.enums(), "Enums", page, html);
            renderTypes(packageDoc.annotationTypes(), "Annotation Types", page, html);
            html._div()._div();
        }
    }


    private void renderTypes(ClassDoc[] classDocs, String title, ApiPage page, HtmlCanvas html) throws IOException {
        if (classDocs.length > 0) {
            Arrays.sort(classDocs);
            html.h1().content(title);
            for (ClassDoc classDoc : classDocs) {
                if (classDoc.equals(page.getClassDoc())) {
                    html.p().span(class_("highlighted")).content(classDoc.name())._p();
                } else {
                    DocReferenceable ref = generator.getRefLocator().getClassDocRef(classDoc);
                    String relPath = page.getReferenceTo(ref);
                    html.p().a(href(relPath)).content(classDoc.name())._p();
                }
            }
        }
    }

}
