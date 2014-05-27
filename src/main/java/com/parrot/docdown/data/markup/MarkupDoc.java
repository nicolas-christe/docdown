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


import com.sun.javadoc.SourcePosition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public abstract class MarkupDoc extends BaseDoc {

    public interface IReferenceProcessor {
        void processRef(String text, String reference);
    }

    public MarkupDoc(Path sourceFilePath) {
        super(sourceFilePath);
    }

    public abstract void processMarkup() throws IOException;

    public abstract void loadIndex(IReferenceProcessor processor);

    public abstract String getTitle();

    /**
     * Build the doc name from the source file name without extension
     *
     * @return doc name
     */
    @Override
    protected String buildName() {
        String fileName = sourceFilePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    public SourcePosition getSourcePosition() {
        return new SourcePosition() {
            @Override
            public File file() {
                return sourceFilePath.toFile();
            }

            @Override
            public int line() {
                return 0;
            }

            @Override
            public int column() {
                return 0;
            }

            @Override
            public String toString() {
                return file().toString();
            }
        };
    }

    @Override
    public String toString() {
        return qualifiedName;
    }
}
