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
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.PackageDoc;

import java.nio.file.Path;

public class ApiPageRef implements DocReferenceable {

    private final ApiPage apiPage;
    private final String anchor;
    private final String name;
    private final String qualifiedName;


    public ApiPageRef(PackageDoc packageDoc, ApiPage apiPage) {
        this.name = packageDoc.name();
        this.qualifiedName = packageDoc.name();
        this.apiPage = apiPage;
        this.anchor = null;
    }

    public ApiPageRef(ClassDoc classDoc, ApiPage apiPage) {
        this.name = classDoc.name();
        this.qualifiedName = classDoc.qualifiedName();
        this.apiPage = apiPage;
        this.anchor = null;
    }

    public ApiPageRef(FieldDoc fieldDoc, ApiPage apiPage) {
        this.name = fieldDoc.containingClass().name() + "." + fieldDoc.name();
        this.qualifiedName = fieldDoc.containingClass().qualifiedName() + "." + fieldDoc.name();
        this.apiPage = apiPage;
        this.anchor = fieldDoc.name();
    }

    public ApiPageRef(ExecutableMemberDoc executableMemberDoc, ApiPage apiPage) {
        this.name = executableMemberDoc.containingClass().name() + "#" + executableMemberDoc.name() + "()";
        this.qualifiedName = executableMemberDoc.containingClass().qualifiedName() + "#" + executableMemberDoc.name() +
                executableMemberDoc.signature();
        this.apiPage = apiPage;
        this.anchor = executableMemberDoc.name() + executableMemberDoc.signature();
    }

    @Override
    public String getReferenceFrom(Path from) {
        if (anchor != null) {
            return apiPage.getReferenceFrom(from) + "#" + anchor;
        }
        return apiPage.getReferenceFrom(from);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getQualifiedName() {
        return qualifiedName;
    }

}
