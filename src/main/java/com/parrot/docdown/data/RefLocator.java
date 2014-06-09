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

import com.parrot.docdown.DocdownDoclet;
import com.parrot.docdown.data.markup.IncludedCodeDoc;
import com.parrot.docdown.data.markup.MarkupDoc;
import com.parrot.docdown.data.markup.ResourceDoc;
import com.parrot.docdown.data.markup.RootProjectDoc;
import com.parrot.docdown.data.page.DocPageStore;
import com.sun.javadoc.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Locate references to documentation pages and resources.
 * <p/>
 * References can be:
 * <ul>
 * <li>name of a markup document: doc1</li>
 * <li>relative of absolute path of markup document</li>
 * <li>package name: com.parrot.package</li>
 * <li>unqualified class name: MyClass</li>
 * <li>qualified class name: com.parrot.MyClass</li>
 * <li>class  field: MyClass#value or com.parrot.MyClass#value</li>
 * <li>class method: MyClass#getValue or MyClass#getValue(int)</li>
 * </ul>
 */
public class RefLocator extends RefProvider {

    private final RootDoc rootDoc;
    private final RootProjectDoc projectRootDoc;

    /**
     * Constructor
     *
     * @param store             page store instance
     * @param externalProviders collection of external reference provider
     * @param rootDoc           docClass rootDoc
     * @param projectRootDoc    project rootDoc
     */
    public RefLocator(DocPageStore store, Collection<IRefProvider> externalProviders, RootDoc rootDoc,
                      RootProjectDoc projectRootDoc) {
        super(store, externalProviders);
        this.rootDoc = rootDoc;
        this.projectRootDoc = projectRootDoc;
    }

    /**
     * Look for a reference to a package, class, field, method or markup document
     *
     * @param name           reference name
     * @param sourcePosition current position in the source containing the reference
     * @return a DocReferenceable, null if not found
     */
    public DocReferenceable find(String name, SourcePosition sourcePosition) {
        return this.find(name, sourcePosition, null);
    }

    /**
     * Look for a reference to a package, class, field, method or markup document,
     * in the context of a markup document path
     *
     * @param name               reference name.
     * @param sourcePosition     current position in the source containing the reference.
     * @param markupDocContainer markup doc container to search for relative references.
     * @return a DocReferenceable, null if not found.
     */
    public DocReferenceable find(String name, SourcePosition sourcePosition, String markupDocContainer) {
        // if its an absolute ref, or a ref relative to a markup doc path, it ca be markup or a resource
        if (name.contains("/") || markupDocContainer != null) {
            MarkupDoc doc = findMarkupDoc(name, markupDocContainer);
            if (doc != null) {
                return getMarkupDocRef(doc);
            }
            ResourceDoc resourceDoc = findResourceFile(name, markupDocContainer);
            if (resourceDoc != null) {
                return getResourceFileRef(resourceDoc);
            }
        }

        // if can be anything
        List<DocReferenceable> matches = findAll(name);
        // check the result is unique
        if (matches.size() == 1) {
            return matches.get(0);
        } else if (matches.size() > 0) {
            StringBuilder error = new StringBuilder();
            error.append("Ambiguous reference ").append(name).append(":\n");
            for (DocReferenceable match : matches) {
                error.append("\t");
                error.append(match.getQualifiedName());
                error.append("\n");
            }
            DocdownDoclet.getErrorReporter().printWarning(sourcePosition, error.toString());
        } else if (matches.size() == 0) {
            // not found
            DocdownDoclet.getErrorReporter().printWarning(sourcePosition, "Can't find reference " + name);
        }
        return null;
    }

    public String[] findIncludedCode(String name, String id, SourcePosition sourcePosition) {
        if (name.startsWith("/")) {
            IncludedCodeDoc doc = projectRootDoc.getIncludedCodeDoc(name);
            if (doc != null) {
                return doc.getContent(id);
            }
            DocdownDoclet.getErrorReporter().printWarning(sourcePosition, "Can't find included code " + name);
        } else {
            List<IncludedCodeDoc> docs = new ArrayList<>();
            for (IncludedCodeDoc codeDoc : projectRootDoc.getIncludedCodeDocs()) {
                if (name.equals(codeDoc.getName())) {
                    docs.add(codeDoc);
                }
            }
            if (docs.size()==1) {
                String[] result =  docs.get(0).getContent(id);
                if (result==null) {
                    DocdownDoclet.getErrorReporter().printWarning(sourcePosition, "Can't find included code " + name
                            + (id!=null?":"+id:""));
                }
                return result;
            } else if (docs.isEmpty()) {
                DocdownDoclet.getErrorReporter().printWarning(sourcePosition, "Can't find included code " + name
                        + (id!=null?":"+id:""));
            } else {
                StringBuilder error = new StringBuilder();
                error.append("Ambiguous reference ").append(name).append(":\n");
                for (IncludedCodeDoc match : docs) {
                    error.append("\t");
                    error.append(match.getQualifiedName());
                    error.append("\n");
                }
                DocdownDoclet.getErrorReporter().printWarning(sourcePosition, error.toString());
            }
        }
        return null;
    }

    /**
     * Find a markup doc in the given path, relative or absolute.
     *
     * @param markupDocPath      path of the doc to find.
     * @param markupDocContainer container path to start for relative paths.
     * @return a DocReferenceable, null if not found.
     */
    private MarkupDoc findMarkupDoc(String markupDocPath, String markupDocContainer) {
        // relative path
        if (!markupDocPath.startsWith("/") && markupDocContainer != null) {
            return projectRootDoc.getMarkupDoc(markupDocContainer + "/" + markupDocPath);
        }
        // absolute path
        return projectRootDoc.getMarkupDoc(markupDocPath.substring(1));
    }

    /**
     * Find a resource doc in the given path, relative or absolute.
     *
     * @param resourceFilePath   path of the doc to find.
     * @param markupDocContainer container path to start for relative paths.
     * @return a DocReferenceable, null if not found.
     */
    private ResourceDoc findResourceFile(String resourceFilePath, String markupDocContainer) {
        // relative path
        if (!resourceFilePath.startsWith("/") && markupDocContainer != null) {
            return projectRootDoc.getResourceDoc(markupDocContainer + "/" + resourceFilePath);
        }
        // absolute path
        return projectRootDoc.getResourceDoc(resourceFilePath.substring(1));
    }

    /**
     * Find a markup doc, package, class, or field or method.
     *
     * @param name name to search
     * @return list of matching DocReferenceable
     */
    private List<DocReferenceable> findAll(String name) {
        List<DocReferenceable> matches = new ArrayList<>();
        if (name.contains("#")) {
            int pos = name.indexOf('#');
            String className = name.substring(0, pos);
            String memberName = name.substring(pos + 1);
            addMatchingFieldRef(className, memberName, matches);
            addMatchingExecutableRef(className, memberName, matches);
        } else {
            addMatchingMarkupDocRefs(name, matches);
            addMatchingResourceDocRefs(name, matches);
            addMatchingPackageDocRefs(name, matches);
            addMatchingClassDocRefs(name, matches);
        }
        return matches;
    }

    /**
     * Add DocReferenceable to markup documents matching the given name to the matches list.
     *
     * @param name    name to search
     * @param matches the list of match to add result to
     */
    private void addMatchingMarkupDocRefs(String name, List<DocReferenceable> matches) {
        // iterate Markdown documents
        for (MarkupDoc markupDoc : projectRootDoc.getMarkupDocs()) {
            if (name.equals(markupDoc.getName())) {
                matches.add(getMarkupDocRef(markupDoc));
            }
        }
    }

    /**
     * Add DocReferenceable to resource files matching the given name to the matches list.
     *
     * @param name    name to search
     * @param matches the list of match to add result to
     */
    private void addMatchingResourceDocRefs(String name, List<DocReferenceable> matches) {
        // iterate Resource documents
        for (ResourceDoc resourceDoc : projectRootDoc.getResourceDocs()) {
            if (name.equals(resourceDoc.getName())) {
                matches.add(getResourceFileRef(resourceDoc));
            }
        }
    }

    /**
     * Add DocReferenceable to packages matching the given name to the matches list.
     *
     * @param name    name to search
     * @param matches the list of match to add result to
     */
    private void addMatchingPackageDocRefs(String name, List<DocReferenceable> matches) {
        // check for a package name
        for (PackageDoc packageDoc : rootDoc.specifiedPackages()) {
            if (name.equals(packageDoc.name())) {
                matches.add(getPackageDocRef(packageDoc));
            }
        }
    }

    /**
     * Add DocReferenceable to classes matching the given name to the matches list.
     *
     * @param name    name to search
     * @param matches the list of match to add result to
     */
    private void addMatchingClassDocRefs(String name, List<DocReferenceable> matches) {
        // check for a class name
        for (ClassDoc classDoc : rootDoc.classes()) {
            if (name.equals(classDoc.name()) || name.equals(classDoc.qualifiedName())) {
                matches.add(getClassDocRef(classDoc));
            }
        }
    }

    /**
     * Add DocReferenceable to fields matching the given name to the matches list.
     *
     * @param className name of the class to search the field
     * @param member    name of the field to search
     * @param matches   the list of match to add result to
     */
    private void addMatchingFieldRef(String className, String member, List<DocReferenceable> matches) {
        for (ClassDoc classDoc : rootDoc.classes()) {
            if (className.equals(classDoc.name()) || className.equals(classDoc.qualifiedName())) {
                // check fields
                for (FieldDoc fieldDoc : classDoc.fields()) {
                    if (member.equals(fieldDoc.name())) {
                        matches.add(getFieldDocRef(fieldDoc));
                    }
                }
            }
        }
    }

    /**
     * Add DocReferenceable to methods matching the given name to the matches list.
     *
     * @param className name of the class to search the field
     * @param member    name of the method to search
     * @param matches   the list of match to add result to
     */
    private void addMatchingExecutableRef(String className, String member, List<DocReferenceable> matches) {
        for (ClassDoc classDoc : rootDoc.classes()) {
            if (className.equals(classDoc.name()) || className.equals(classDoc.qualifiedName())) {
                // check constructors
                for (ConstructorDoc constructorDoc : classDoc.constructors()) {
                    if (member.equals(constructorDoc.name())) {
                        matches.add(getExecutableMemberDocRef(constructorDoc));
                    }
                }
                // check methods
                for (MethodDoc methodDoc : classDoc.methods()) {
                    // check simple class name
                    boolean match = member.equals(methodDoc.name());
                    // match the qualified signature
                    match = match | member.equals(methodDoc.name() + methodDoc.signature());
                    // match the qualified signature without spaces
                    match = match | member.equals(methodDoc.name() + methodDoc.signature().replaceAll("\\s",""));
                    // match the flat signature
                    match = match | member.equals(methodDoc.name() + methodDoc.flatSignature());
                    // match the flat signature without spaces
                    match = match | member.equals(methodDoc.name() + methodDoc.flatSignature().replaceAll("\\s",""));
                   if (match) {
                        matches.add(getExecutableMemberDocRef(methodDoc));
                    }
                }
            }
        }
    }

}
