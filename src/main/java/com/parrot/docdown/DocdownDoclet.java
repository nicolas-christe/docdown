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
import com.parrot.docdown.generator.DefaultGenerator;
import com.sun.javadoc.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Doclet entry point
 */
public class DocdownDoclet extends Doclet {

    private static DocdownOption docdownOption = new DocdownOption();
    private static RootProjectDoc projectDoc;
    private static RootDoc rootDoc;

    /**
     * Gets the error reporter
     *
     * @return error reporter
     */
    public static DocErrorReporter getErrorReporter() {
        return rootDoc;
    }

    /**
     * Generate documentation here.
     * This method is required for all doclets.
     *
     * @param root the javadoc root document
     * @return true on success.
     */
    public static boolean start(RootDoc root) {
        try {
            rootDoc = root;

            // build markup doc content
            projectDoc = new RootProjectDoc(new RootProjectDoc.Builder(), docdownOption.getSrcDirs());

            // create the doc store for local classes and add it
            final DocPageStore store = new DocPageStore();
            // and the reference locator
            RefLocator refLocator = new RefLocator(store, docdownOption.getExternalRefProviders(), rootDoc, projectDoc);

            Path javadocPath = docdownOption.getOutputDir().resolve("javadoc");
            Files.createDirectories(javadocPath);

            // add global javadoc page
            store.addPages(rootDoc, javadocPath);

            // addPage all packages pages
            PackageDoc[] packages = root.specifiedPackages();
            for (PackageDoc packageDoc : packages) {
                store.addPage(packageDoc, javadocPath);
            }

            // addPage all classes pages
            ClassDoc[] classes = root.classes();
            for (ClassDoc classDoc : classes) {
                store.addPage(classDoc, javadocPath);
            }

            // load all markup pages
            MarkupDoc[] markups = projectDoc.getMarkupDocs();
            for (MarkupDoc markupDoc : markups) {
                store.addPage(markupDoc, docdownOption.getOutputDir());
            }

            // load resource files
            ResourceDoc[] resources = projectDoc.getResourceDocs();
            for (ResourceDoc resourceDoc : resources) {
                store.addResourceFile(resourceDoc, docdownOption.getOutputDir());
            }

            // create the doc generator
            DocGenerator generator = createDocGenerator(rootDoc, projectDoc, refLocator, docdownOption, store);
            // generate documentation
            return generator.generate(packages, classes, markups, resources);
        } catch (IOException e) {
            e.printStackTrace();
        }
        docdownOption = null;
        projectDoc = null;
        rootDoc = null;

        return false;
    }

    /**
     * Create the document generator.
     *
     * @param rootDoc        javadoc root doc
     * @param rootProjectDoc markupdoc root dof
     * @param refLocator     reference locator
     * @param options        options
     * @param store          document store
     * @return the doc generator
     */
    private static DocGenerator createDocGenerator(RootDoc rootDoc, RootProjectDoc rootProjectDoc,
                                                   RefLocator refLocator, DocdownOption options, DocPageStore store) {
        // for now there is only one generator
        return new DefaultGenerator(rootDoc, rootProjectDoc, refLocator, options, store, options.getHeadTitle());
    }

    /**
     * Return the version of the Java Programming Language supported
     * by this doclet.
     *
     * @return the language version supported by this doclet.
     */
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

    /**
     * Check for doclet-added options.  Returns the number of
     * arguments you must specify on the command line for the
     * given option.
     *
     * @param option the option to check
     * @return number of arguments on the command line for an option
     * including the option name itself.  Zero return means
     * option not known.  Negative value means error occurred.
     */
    public static int optionLength(String option) {
        return docdownOption.optionLength(option);
    }

    /**
     * Check that options have the correct arguments.
     *
     * @param options  the command line options
     * @param reporter error reporter
     * @return true if the options are valid.
     */
    public static boolean validOptions(String options[][], DocErrorReporter reporter) {
        return docdownOption.parseOptions(options, reporter);
    }
}
