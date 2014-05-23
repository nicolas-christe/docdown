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

package com.parrot.docdown.data.page;

import com.parrot.docdown.data.markup.MarkupDoc;
import com.parrot.docdown.data.markup.ResourceDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class DocPageStore {

    /**
     * Markup pages doc pages, by relative path name
     */
    private final Map<MarkupDoc, MarkupPage> markupPages;

    /**
     * Markup pages doc pages, by relative path name
     */
    private final Map<ResourceDoc, ResourceFile> resourceFiles;

    /**
     * Packages summary pages, by packageDoc
     */
    private final Map<PackageDoc, PackagePage> packagePages;

    /**
     * Types (class, interface, enum,...) pages, by classDoc
     */
    private final Map<ClassDoc, ClassPage> classPages;

    /**
     * Root packages list page
     */
    private PackageListPage packageListPage;

    /**
     * Instantiate the Doc Page Store
     */
    public DocPageStore() {
        markupPages = new HashMap<>();
        resourceFiles = new HashMap<>();
        packagePages = new HashMap<>();
        classPages = new HashMap<>();
    }

    public void addPages(RootDoc rootDoc, Path outputDir) {
        packageListPage = new PackageListPage(outputDir);
    }

    public void addPage(PackageDoc packageDoc, Path outputDir) {
        PackagePage page = new PackagePage(packageDoc, outputDir);
        packagePages.put(packageDoc, page);
    }

    public void addPage(ClassDoc classDoc, Path outputDir) {
        ClassPage page = new ClassPage(classDoc, outputDir);
        classPages.put(classDoc, page);
    }

    public void addPage(MarkupDoc markupDoc, Path outputDir) {
        MarkupPage page = new MarkupPage(markupDoc, outputDir);
        markupPages.put(markupDoc, page);
    }

    public void addResourceFile(ResourceDoc resourceDoc, Path outputDir) {
        ResourceFile file = new ResourceFile(resourceDoc, outputDir);
        resourceFiles.put(resourceDoc, file);
    }

    public PackagePage getPackagePage(PackageDoc packageDoc) {
        return packagePages.get(packageDoc);
    }

    public ClassPage getClassPage(ClassDoc classDoc) {
        return classPages.get(classDoc);
    }

    public MarkupPage getMarkupPage(MarkupDoc markupDoc) {
        return markupPages.get(markupDoc);
    }

    public ResourceFile getResourceFile(ResourceDoc resourceDoc) {
        return resourceFiles.get(resourceDoc);
    }

    public PackageListPage getPackageListPage() {
        return packageListPage;
    }
}
