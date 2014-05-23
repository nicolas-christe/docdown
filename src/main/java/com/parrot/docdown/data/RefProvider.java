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
import com.parrot.docdown.data.page.DocPageStore;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.PackageDoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reference provider. Provide references to doc, package, class, field and methods.
 * Search for local reference, external reference.
 */
public class RefProvider implements IRefProvider {

    private final List<IRefProvider> apiRefProviderList;
    private final UnknownRefProvider unknownRefProvider;

    /**
     * Constructor
     *
     * @param store             page store instance
     * @param externalProviders collection of external reference provider
     */
    public RefProvider(DocPageStore store, Collection<IRefProvider> externalProviders) {
        apiRefProviderList = new ArrayList<>();
        apiRefProviderList.add(new LocalRefProvider(store));
        apiRefProviderList.addAll(externalProviders);
        unknownRefProvider = new UnknownRefProvider();
    }

    @Override
    public DocReferenceable getPackageDocRef(PackageDoc packageDoc) {
        for (IRefProvider provider : apiRefProviderList) {
            DocReferenceable result = provider.getPackageDocRef(packageDoc);
            if (result != null) {
                return result;
            }
        }
        return unknownRefProvider.getPackageDocRef(packageDoc);
    }

    @Override
    public DocReferenceable getClassDocRef(ClassDoc classDoc) {
        for (IRefProvider provider : apiRefProviderList) {
            DocReferenceable result = provider.getClassDocRef(classDoc);
            if (result != null) {
                return result;
            }
        }
        return unknownRefProvider.getClassDocRef(classDoc);
    }

    @Override
    public DocReferenceable getFieldDocRef(FieldDoc fieldDoc) {
        for (IRefProvider provider : apiRefProviderList) {
            DocReferenceable result = provider.getFieldDocRef(fieldDoc);
            if (result != null) {
                return result;
            }
        }
        return unknownRefProvider.getFieldDocRef(fieldDoc);
    }

    @Override
    public DocReferenceable getExecutableMemberDocRef(ExecutableMemberDoc executableMemberDoc) {
        for (IRefProvider provider : apiRefProviderList) {
            DocReferenceable result = provider.getExecutableMemberDocRef(executableMemberDoc);
            if (result != null) {
                return result;
            }
        }
        return unknownRefProvider.getExecutableMemberDocRef(executableMemberDoc);
    }

    @Override
    public DocReferenceable getMarkupDocRef(MarkupDoc markupDoc) {
        for (IRefProvider provider : apiRefProviderList) {
            DocReferenceable result = provider.getMarkupDocRef(markupDoc);
            if (result != null) {
                return result;
            }
        }
        return unknownRefProvider.getMarkupDocRef(markupDoc);
    }

    @Override
    public DocReferenceable getResourceFileRef(ResourceDoc resourceDoc) {
        for (IRefProvider provider : apiRefProviderList) {
            DocReferenceable result = provider.getResourceFileRef(resourceDoc);
            if (result != null) {
                return result;
            }
        }
        return unknownRefProvider.getResourceFileRef(resourceDoc);
    }
}
