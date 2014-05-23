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

import com.parrot.docdown.data.DocReferenceable;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Base class for a page on Api (versus markdown poage).
 */
public abstract class ApiPage implements IDocPage {

    private final Path destFilePath;

    public ApiPage(Path outDirPath, String packageName, String fileName) {
        this.destFilePath = outDirPath.resolve(packageName.replace(".", File.separator)).resolve(fileName);
    }

    public ApiPage(Path outDirPath, String fileName) {
        this.destFilePath = outDirPath.resolve(fileName);
    }

    public abstract PackageDoc getPackageDoc();

    public abstract ClassDoc getClassDoc();

    @Override
    public String getReferenceTo(DocReferenceable referenceable) {
        return referenceable.getReferenceFrom(destFilePath.getParent());
    }

    @Override
    public String getReferenceTo(Path to) {
        return destFilePath.getParent().relativize(to).toString();
    }

    public String getReferenceFrom(Path from) {
        return from.relativize(destFilePath).toString();
    }

    @Override
    public Writer createWriter() throws IOException {
        Files.createDirectories(destFilePath.getParent());
        return new FileWriter(destFilePath.toFile());
    }
}
