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

/**
 * Interface defining a provider of DocReferenceable
 */
public interface IRefProvider {

    /**
     * Gets a reference to a package.
     *
     * @param packageDoc javadoc package doc
     * @return a DocReferenceable, null if not found
     */
    DocReferenceable getPackageDocRef(PackageDoc packageDoc);

    /**
     * Gets a reference to a class.
     *
     * @param classDoc javadoc class doc
     * @return a DocReferenceable, null if not found
     */
    DocReferenceable getClassDocRef(ClassDoc classDoc);

    /**
     * Gets a reference to a field.
     *
     * @param fieldDoc javadoc field doc
     * @return a DocReferenceable, null if not found
     */
    DocReferenceable getFieldDocRef(FieldDoc fieldDoc);

    /**
     * Gets a reference to a executable member.
     *
     * @param executableMemberDoc javadoc executable member doc
     * @return a DocReferenceable, null if not found
     */
    DocReferenceable getExecutableMemberDocRef(ExecutableMemberDoc executableMemberDoc);

    /**
     * Gets a reference to a markup document.
     *
     * @param markupDoc markup doc
     * @return a DocReferenceable, null if not found
     */
    DocReferenceable getMarkupDocRef(MarkupDoc markupDoc);

    /**
     * Gets a reference to a markup document.
     *
     * @param resourceDoc resource doc
     * @return a DocReferenceable, null if not found
     */
    DocReferenceable getResourceFileRef(ResourceDoc resourceDoc);

}
