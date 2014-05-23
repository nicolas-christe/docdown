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
import com.parrot.docdown.data.markup.ResourceDoc;

import java.nio.file.Path;

public class ResourceFile implements DocReferenceable {

    private final ResourceDoc resourceDoc;
    private final Path destFilePath;

    public ResourceFile(ResourceDoc resourceDoc, Path outputDir) {
        this.resourceDoc = resourceDoc;
        if (resourceDoc.getContainer() != null) {
            this.destFilePath = outputDir.resolve(resourceDoc.getContainer()).resolve(resourceDoc.getName());
        } else {
            this.destFilePath = outputDir.resolve(resourceDoc.getName());
        }
    }

    @Override
    public String getName() {
        return resourceDoc.getName();
    }

    @Override
    public String getQualifiedName() {
        return resourceDoc.getQualifiedName();
    }

    @Override
    public String getReferenceFrom(Path from) {
        return from.relativize(destFilePath).toString();
    }

    public Path getPath() {
        return destFilePath;
    }
}
