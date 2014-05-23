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
import com.parrot.docdown.data.markup.MarkupDoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class MarkupPage implements IDocPage, DocReferenceable {

    private final MarkupDoc markupDoc;
    private final Path destFilePath;

    public MarkupPage(MarkupDoc markupDoc, Path outputDir) {
        this.markupDoc = markupDoc;
        if (markupDoc.getContainer() != null) {
            this.destFilePath = outputDir.resolve(markupDoc.getContainer()).resolve(markupDoc.getName() + ".html");
        } else {
            this.destFilePath = outputDir.resolve(markupDoc.getName() + ".html");
        }
    }

    @Override
    public String getTitle() {
        return markupDoc.getName();
    }

    public MarkupDoc getMarkupDoc() {
        return markupDoc;
    }

    @Override
    public String getReferenceTo(DocReferenceable referenceable) {
        return referenceable.getReferenceFrom(destFilePath.getParent());
    }

    @Override
    public String getReferenceTo(Path to) {
        return destFilePath.getParent().relativize(to).toString();
    }

    @Override
    public String getName() {
        return markupDoc.getName();
    }

    @Override
    public String getQualifiedName() {
        return markupDoc.getQualifiedName();
    }

    public String getReferenceFrom(Path from) {
        return from.relativize(destFilePath).toString();
    }

    @Override
    public Writer createWriter() throws IOException {
        File destFile = destFilePath.toFile();
        destFile.getParentFile().mkdirs();
        return new FileWriter(destFile);
    }

}
