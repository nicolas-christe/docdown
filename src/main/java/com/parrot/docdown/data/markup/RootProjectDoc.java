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

import com.parrot.docdown.DocdownDoclet;
import com.parrot.docdown.data.markup.pegdown.PegdownDoc;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RootProjectDoc {

    public static class Builder {
        /**
         * index.md files, by qualified name
         */
        private final Map<String, IndexDoc> indexDocs;
        /**
         * markup doc files, by qualified name
         */
        private final Map<String, MarkupDoc> markupDocs;
        /**
         * code source file that can be included as a code block, by qualified name
         */
        private final Map<String, IncludedCodeDoc> includedCodeDocs;
        /**
         * other resources files (images), by qualified name
         */
        private final Map<String, ResourceDoc> resourceDocs;

        public Builder() {
            this.indexDocs = new HashMap<>();
            this.markupDocs = new HashMap<>();
            this.includedCodeDocs = new HashMap<>();
            this.resourceDocs = new HashMap<>();
        }

        public Map<String, IndexDoc> getIndexDocs() {
            return indexDocs;
        }

        public Map<String, MarkupDoc> getMarkupDocs() {
            return markupDocs;
        }

        public Map<String, IncludedCodeDoc> getIncludedCodeDocs() {
            return includedCodeDocs;
        }

        public Map<String, ResourceDoc> getResourceDocs() {
            return resourceDocs;
        }

        private void build(Collection<Path> docSourceDir, Collection<Path> includedCodeSourceDir) throws IOException {
            loadSourceFileLists(docSourceDir);
            if (includedCodeSourceDir != null) {
                loadIncludedCodeFileLists(includedCodeSourceDir);
            }
            if (markupDocs.get(null) != null) {
                processMarkupFiles();
                loadIndexes();
            }
        }

        private void loadSourceFileLists(Collection<Path> srcDirs) throws IOException {
            // build a list of all .md files in source paths
            for (Path sourcePath : srcDirs) {
                if (Files.exists(sourcePath)) {
                    Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws
                                IOException {
                            DocdownDoclet.getErrorReporter().printNotice("Loading markup files of " + dir + "...");
                            return super.preVisitDirectory(dir, attrs);
                        }

                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                            // skip all hiddenList<Path> files
                            if (!Files.isHidden(path)) {
                                if (path.toString().endsWith(".md")) {
                                    // add all .md files as pegdown doc
                                    PegdownDoc markupDoc = new PegdownDoc(path);
                                    if (markupDoc.getQualifiedName().equals("index.md")) {
                                        markupDocs.put(null, markupDoc);
                                    } else {
                                        markupDocs.put(markupDoc.getQualifiedName(), markupDoc);
                                    }
                                } else {
                                    // add all other files as resource doc
                                    ResourceDoc resourceDoc = new ResourceDoc(path);
                                    resourceDocs.put(resourceDoc.getQualifiedName(), resourceDoc);
                                }
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
            }
        }

        private void loadIncludedCodeFileLists(Collection<Path> srcDirs) throws IOException {
            // build a list of allfiles  in included source code paths
            for (Path sourcePath : srcDirs) {
                if (Files.exists(sourcePath)) {
                    Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws
                                IOException {
                            DocdownDoclet.getErrorReporter().printNotice("Loading samples files of " + dir + "...");
                            return super.preVisitDirectory(dir, attrs);
                        }

                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                            // skip all hiddenList<Path> files
                            if (!Files.isHidden(path)) {
                                IncludedCodeDoc doc = new IncludedCodeDoc(path);
                                includedCodeDocs.put(doc.getQualifiedName(), doc);

                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
            }
        }

        private void processMarkupFiles() throws IOException {
            DocdownDoclet.getErrorReporter().printNotice("Processing markup files...");
            for (MarkupDoc markupDoc : markupDocs.values()) {
                markupDoc.processMarkup();
            }
        }

        private void loadIndexes() {
            loadIndexContent(markupDocs.get(null), null);
        }

        private void loadIndexContent(MarkupDoc doc, String parentContainer) {
            IndexDoc index = new IndexDoc(buildIndex(doc), parentContainer);
            this.indexDocs.put(doc.getContainer(), index);
            // iterate all entries, add corresponding indexes
            for (IndexDoc.Entry entry : index.getContent()) {
                MarkupDoc target = entry.getTarget();
                if (target != null && target.getName().equals("index.md")) {
                    loadIndexContent(entry.getTarget(), doc.getContainer());
                }
            }
        }

        private IndexDoc.Builder buildIndex(final MarkupDoc doc) {
            final IndexDoc.Builder indexBuilder = new IndexDoc.Builder();
            final String path = doc.getContainer();
            doc.loadIndex(new MarkupDoc.IReferenceProcessor() {
                @Override
                public void processRef(String text, String reference) {
                    if (reference.equals("javadoc/")) {
                        // special case for javadoc
                        indexBuilder.add(text, null);
                    } else {
                        String target;
                        String ref = reference;
                        if (ref.endsWith("/")) {
                            ref += "index.md";
                        }
                        if (path != null) {
                            target = path + "/" + ref;
                        } else {
                            target = ref;
                        }

                        MarkupDoc targetDoc = markupDocs.get(target);
                        if (targetDoc != null) {
                            indexBuilder.add(text, targetDoc);
                        } else {
                            DocdownDoclet.getErrorReporter().printWarning(doc.getSourcePosition(),
                                    "Ignoring missing index entry [" + text + "](" + target + ")");
                        }
                    }
                }
            });
            return indexBuilder;
        }
    }

    private final Map<String, IndexDoc> indexDocs;
    private final Map<String, MarkupDoc> markupDocs;
    private final Map<String, IncludedCodeDoc> includedCodeDocs;
    private final Map<String, ResourceDoc> resourceDocs;


    public RootProjectDoc(Builder builder, Collection<Path> docSourceDirs, Collection<Path> includedCodeSourceDir) throws
            IOException {
        builder.build(docSourceDirs, includedCodeSourceDir);
        indexDocs = builder.getIndexDocs();
        markupDocs = builder.getMarkupDocs();
        includedCodeDocs = builder.getIncludedCodeDocs();
        resourceDocs = builder.getResourceDocs();
    }

    public MarkupDoc[] getMarkupDocs() {
        return markupDocs.values().toArray(new MarkupDoc[markupDocs.size()]);
    }

    public MarkupDoc getRootMarkupDoc() {
        return markupDocs.get(null);
    }

    public MarkupDoc getMarkupDoc(String path) {
        return markupDocs.get(path);
    }

    public IndexDoc getRootIndexDoc() {
        return indexDocs.get(null);
    }

    public IndexDoc getIndexDoc(MarkupDoc markupDoc) {
        return indexDocs.get(markupDoc.getContainer());
    }

    public IndexDoc getIndexDoc(String container) {
        return indexDocs.get(container);
    }

    public IncludedCodeDoc getIncludedCodeDoc(String path) {
        return includedCodeDocs.get(path);
    }
    public IncludedCodeDoc[] getIncludedCodeDocs() {
        return includedCodeDocs.values().toArray(new IncludedCodeDoc[includedCodeDocs.size()]);
    }

    public ResourceDoc[] getResourceDocs() {
        return resourceDocs.values().toArray(new ResourceDoc[resourceDocs.size()]);
    }

    public ResourceDoc getResourceDoc(String path) {
        return resourceDocs.get(path);
    }

    public void dump() {
        for (Map.Entry<String, IndexDoc> i : indexDocs.entrySet()) {
            System.out.println(i.getKey());
            for (IndexDoc.Entry e : i.getValue().getContent()) {
                System.out.println("\t" + e.getLabel() + "\t" + e.getTarget().getQualifiedName());
            }
        }
    }
}
