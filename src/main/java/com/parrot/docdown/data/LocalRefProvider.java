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
import com.parrot.docdown.data.page.ApiPage;
import com.parrot.docdown.data.page.ApiPageRef;
import com.parrot.docdown.data.page.DocPageStore;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.PackageDoc;

/**
 * A ref provider for the local documents
 */
public class LocalRefProvider implements IRefProvider {
    private final DocPageStore docPageStore;

    public LocalRefProvider(DocPageStore docPageStore) {
        this.docPageStore = docPageStore;
    }

    @Override
    public DocReferenceable getPackageDocRef(PackageDoc packageDoc) {
        ApiPage page = docPageStore.getPackagePage(packageDoc);
        if (page != null) {
            return new ApiPageRef(packageDoc, page);
        }
        return null;
    }

    @Override
    public DocReferenceable getClassDocRef(ClassDoc classDoc) {
        ApiPage page = docPageStore.getClassPage(classDoc);
        if (page != null) {
            return new ApiPageRef(classDoc, page);
        }
        return null;
    }

    @Override
    public DocReferenceable getFieldDocRef(FieldDoc fieldDoc) {
        ApiPage page = docPageStore.getClassPage(fieldDoc.containingClass());
        if (page != null) {
            return new ApiPageRef(fieldDoc, page);
        }
        return null;
    }

    @Override
    public DocReferenceable getExecutableMemberDocRef(ExecutableMemberDoc executableMemberDoc) {
        ApiPage page = docPageStore.getClassPage(executableMemberDoc.containingClass());
        if (page != null) {
            return new ApiPageRef(executableMemberDoc, page);
        }
        return null;
    }

    @Override
    public DocReferenceable getMarkupDocRef(MarkupDoc markupDoc) {
        // if the document is null, it's a ref to the main package list (the pseudo javadoc/ link)
        if (markupDoc == null) {
            return docPageStore.getPackageListPage();
        }
        return docPageStore.getMarkupPage(markupDoc);
    }

    @Override
    public DocReferenceable getResourceFileRef(ResourceDoc resourceDoc) {
        return docPageStore.getResourceFile(resourceDoc);
    }
}