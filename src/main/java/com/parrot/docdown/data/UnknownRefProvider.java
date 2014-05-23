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

package com.parrot.docdown.data;

import com.parrot.docdown.data.markup.MarkupDoc;
import com.parrot.docdown.data.markup.ResourceDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.PackageDoc;

import java.nio.file.Path;

/**
 * A ref provider for missing references. This provider returns empty references.
 */
public class UnknownRefProvider implements IRefProvider {

    @Override
    public DocReferenceable getPackageDocRef(PackageDoc packageDoc) {
        return new UnknownApiRef(packageDoc.name());
    }

    @Override
    public DocReferenceable getClassDocRef(ClassDoc classDoc) {
        return new UnknownApiRef(classDoc.qualifiedName());
    }

    @Override
    public DocReferenceable getFieldDocRef(FieldDoc fieldDoc) {
        return new UnknownApiRef(fieldDoc.name());
    }

    @Override
    public DocReferenceable getExecutableMemberDocRef(ExecutableMemberDoc executableMemberDoc) {
        return new UnknownApiRef(executableMemberDoc.name());
    }

    @Override
    public DocReferenceable getMarkupDocRef(MarkupDoc markupDoc) {
        return new UnknownMarkupRef();
    }

    @Override
    public DocReferenceable getResourceFileRef(ResourceDoc resourceDoc) {
        return new UnknownMarkupRef();
    }

    /**
     * Doc referenceable for an unknown api
     */
    private static class UnknownApiRef implements DocReferenceable {
        private final String displayName;

        public UnknownApiRef(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String getName() {
            return displayName;
        }

        @Override
        public String getQualifiedName() {
            return displayName;
        }

        @Override
        public String getReferenceFrom(Path from) {
            return null;
        }
    }

    /**
     * Doc referenceable for an unknown markup doc
     */
    private static class UnknownMarkupRef implements DocReferenceable {

        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getQualifiedName() {
            return "";
        }

        @Override
        public String getReferenceFrom(Path from) {
            return null;
        }
    }
}
