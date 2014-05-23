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

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;

import java.nio.file.Path;


public class PackagePage extends ApiPage {

    private final PackageDoc packageDoc;

    public PackagePage(PackageDoc packageDoc, Path outputDir) {
        super(outputDir, packageDoc.name(), "package-summary.html");
        this.packageDoc = packageDoc;
    }

    @Override
    public String getTitle() {
        return "Package " + packageDoc.name();
    }

    @Override
    public PackageDoc getPackageDoc() {
        return packageDoc;
    }

    @Override
    public ClassDoc getClassDoc() {
        return null;
    }

}
