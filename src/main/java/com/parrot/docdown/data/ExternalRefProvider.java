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

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * A ref provide for external references (-links or -offlinelinks)
 */
public class ExternalRefProvider implements IRefProvider {

    private static final String PCKG_LIST_FILE_NAME = "package-list";

    private final Set<String> packageList;
    private final String url;

    public ExternalRefProvider(String url) throws IOException {
        this.packageList = new HashSet<>();
        this.url = url;
        try (InputStream is = new URL(url + PCKG_LIST_FILE_NAME).openStream()) {
            loadPackageList(is);
        }
    }

    public ExternalRefProvider(String url, String pckgListPathName) throws IOException {
        this.packageList = new HashSet<>();
        this.url = url;
        Path pckgListPath = Paths.get(pckgListPathName, PCKG_LIST_FILE_NAME);
        File file = pckgListPath.toFile();
        try (InputStream is = new FileInputStream(file)) {
            loadPackageList(is);
        }
    }

    private void loadPackageList(InputStream stream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    packageList.add(line);
                }
            }
        }
    }

    @Override
    public DocReferenceable getPackageDocRef(PackageDoc packageDoc) {
        if (packageList.contains(packageDoc.name())) {
            return new ExternalRef(packageDoc);
        }
        return null;
    }

    @Override
    public DocReferenceable getClassDocRef(ClassDoc classDoc) {
        if (packageList.contains(classDoc.containingPackage().name())) {
            return new ExternalRef(classDoc);
        }
        return null;
    }

    @Override
    public DocReferenceable getFieldDocRef(FieldDoc fieldDoc) {
        if (packageList.contains(fieldDoc.containingPackage().name())) {
            return new ExternalRef(fieldDoc);
        }
        return null;
    }

    @Override
    public DocReferenceable getExecutableMemberDocRef(ExecutableMemberDoc executableMemberDoc) {
        if (packageList.contains(executableMemberDoc.containingPackage().name())) {
            return new ExternalRef(executableMemberDoc);
        }
        return null;
    }

    @Override
    public DocReferenceable getMarkupDocRef(MarkupDoc markupDoc) {
        return null;
    }

    @Override
    public DocReferenceable getResourceFileRef(ResourceDoc resourceDoc) {
        return null;
    }

    /**
     * An doc referenceable for a external doc
     */
    private class ExternalRef implements DocReferenceable {

        private final String name;
        private final String qualifiedName;
        private final String url;

        public ExternalRef(PackageDoc packageDoc) {
            name = packageDoc.name();
            qualifiedName = packageDoc.name();
            url = ExternalRefProvider.this.url + packageDoc.name().replace('.', '/') + "/package-summary.html";
        }

        public ExternalRef(ClassDoc classDoc) {
            name = classDoc.name();
            qualifiedName = classDoc.qualifiedName();
            url = ExternalRefProvider.this.url + classDoc.containingPackage().name().replace('.',
                    '/') + "/" + classDoc.name() + ".html";
        }

        public ExternalRef(FieldDoc fieldDoc) {
            name = fieldDoc.name();
            qualifiedName = fieldDoc.containingClass().qualifiedName() + "#" + fieldDoc.name();
            url = ExternalRefProvider.this.url + fieldDoc.containingPackage().name().replace('.',
                    '/') + "/" + fieldDoc.containingClass().name() + ".html#" + fieldDoc.name();
        }

        public ExternalRef(ExecutableMemberDoc executableMemberDoc) {
            name = executableMemberDoc.name() + "()";
            qualifiedName = executableMemberDoc.containingClass().qualifiedName() + "#" + executableMemberDoc.name()
                    + executableMemberDoc.signature();
            url = ExternalRefProvider.this.url + executableMemberDoc.containingPackage().name().replace('.',
                    '/') + "/" + executableMemberDoc.containingClass().name() + ".html#" + executableMemberDoc.name()
                    + executableMemberDoc.signature();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getQualifiedName() {
            return qualifiedName;
        }

        @Override
        public String getReferenceFrom(Path from) {
            return url;
        }
    }
}
