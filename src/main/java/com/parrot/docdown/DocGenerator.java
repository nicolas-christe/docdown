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

package com.parrot.docdown;

import com.parrot.docdown.data.RefLocator;
import com.parrot.docdown.data.markup.MarkupDoc;
import com.parrot.docdown.data.markup.ResourceDoc;
import com.parrot.docdown.data.markup.RootProjectDoc;
import com.parrot.docdown.data.page.DocPageStore;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

import java.io.IOException;

/**
 * Base class for a docdown documentation generator.
 * <p/>
 * Subclass must implements the method that generate the actual documentation
 */
public abstract class DocGenerator {
    protected final RefLocator refLocator;
    protected final DocPageStore store;
    protected final DocdownOption options;
    protected final RootDoc rootDoc;
    protected final RootProjectDoc rootProjectDoc;
    protected final String title;


    public DocGenerator(RootDoc rootDoc, RootProjectDoc rootProjectDoc, RefLocator refLocator, DocdownOption options, DocPageStore store, String title) {
        this.rootDoc = rootDoc;
        this.rootProjectDoc = rootProjectDoc;
        this.refLocator = refLocator;
        this.options = options;
        this.store = store;
        this.title = title;
    }

    public RefLocator getRefLocator() {
        return refLocator;
    }

    public RootDoc getRootDoc() {
        return rootDoc;
    }

    public RootProjectDoc getRootProjectDoc() {
        return rootProjectDoc;
    }

    public String getTitle() {
        return title;
    }

    public abstract boolean generate(PackageDoc[] packages, ClassDoc[] classes, MarkupDoc[] markups,
                                     ResourceDoc[] resources) throws IOException;

}
