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

package com.parrot.docdown.data.markup;

import java.nio.file.Path;


public abstract class BaseDoc {
    protected final Path sourceFilePath;
    protected final String container;
    protected final String name;
    protected final String qualifiedName;

    public BaseDoc(Path sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
        this.name = buildName();

        Path container = sourceFilePath.subpath(1, sourceFilePath.getNameCount()).getParent();
        if (container != null) {
            this.container = container.toString();
            this.qualifiedName = this.container + "/" + this.name;
        } else {
            this.container = null;
            this.qualifiedName = this.name;
        }
    }

    public String getName() {
        return name;
    }

    public String getContainer() {
        return container;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public Path getSourceFilePath() {
        return sourceFilePath;
    }

    protected abstract String buildName();

}
