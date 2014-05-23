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

import com.parrot.docdown.DocGenerator;
import com.parrot.docdown.DocdownOption;
import com.parrot.docdown.data.RefLocator;
import com.parrot.docdown.data.markup.MarkupDoc;
import com.parrot.docdown.data.markup.ResourceDoc;
import com.parrot.docdown.data.markup.RootProjectDoc;
import com.parrot.docdown.data.page.*;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DefaultGenerator extends DocGenerator {

    private final AssetsDir assertDir;

    public DefaultGenerator(RootDoc rootDoc, RootProjectDoc rootProjectDoc, RefLocator refLocator,
                            DocdownOption options, DocPageStore store, String title) {
        super(rootDoc, rootProjectDoc, refLocator, options, store, title);

        this.assertDir = new AssetsDir(Paths.get("default/"), options.getOutputDir(), Paths.get("assets"),
                this.getClass().getClassLoader());
    }

    @Override
    public boolean generate(PackageDoc[] packages, ClassDoc[] classes, MarkupDoc[] markups,
                            ResourceDoc[] resources) throws IOException {
        PackageListRenderer packageListRenderer = new PackageListRenderer(this);
        PackageRenderer packageRenderer = new PackageRenderer(this);
        ClassRenderer classRenderer = new ClassRenderer(this);
        MarkupRenderer markupRenderer = new MarkupRenderer(this);

        // copy assets files
        assertDir.copy();

        // generate package list page
        packageListRenderer.render(store.getPackageListPage());

        // generate package summary pages
        for (PackageDoc packageDoc : packages) {
            PackagePage page = store.getPackagePage(packageDoc);
            packageRenderer.render(page);
        }

        // generate class pages
        for (ClassDoc classDoc : classes) {
            ClassPage page = store.getClassPage(classDoc);
            System.out.println(page.getClassDoc());
            classRenderer.render(page);
        }

        // generate markup pages
        for (MarkupDoc markupDoc : markups) {
            MarkupPage page = store.getMarkupPage(markupDoc);
            markupRenderer.render(page);
        }

        // copy all resource files
        for (ResourceDoc resourceDoc : resources) {
            ResourceFile file = store.getResourceFile(resourceDoc);
            Files.createDirectories(file.getPath().getParent());
            try (InputStream is = Files.newInputStream(resourceDoc.getSourceFilePath())) {
                Files.copy(is, file.getPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        return true;
    }

    public AssetsDir getAssertsDir() {
        return assertDir;
    }
}
