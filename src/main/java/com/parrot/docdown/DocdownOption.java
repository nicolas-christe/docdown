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

import com.parrot.docdown.data.ExternalRefProvider;
import com.parrot.docdown.data.IRefProvider;
import com.sun.javadoc.DocErrorReporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Doclet command line option parser
 */
public class DocdownOption {

    private String title;
    private List<Path> projectDocSrcDirs;
    private Path outputDir;
    private List<IRefProvider> externalRefProviders = new ArrayList<>();

    /**
     * Gets the numer of argument of an option.
     *
     * @param option the option to get the number of arguments
     * @return the number of argument of the option
     */
    public int optionLength(String option) {
        switch (option.toLowerCase()) {
            case "-t":
                return 2;
            case "-d":
                return 2;
            case "-docsourcepath":
                return 2;
            case "-link":
                return 2;
            case "-linkoffline":
                return 3;
            case "-help":
                printUsage();
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Parse command line options.
     *
     * @param options command line options to parse
     * @param reporter error reporter
     * @return true if options are valid.
     */
    public boolean parseOptions(String[][] options, DocErrorReporter reporter) {
        boolean valid = true;
        for (String[] option : options) {
            switch (option[0].toLowerCase()) {
                case "-t" :
                    valid = setTitle(option[1], reporter);
                    break;
                case "-d":
                    valid = setOutputDir(option[1], reporter);
                    break;
                case "-docsourcepath":
                    valid = setDocSourcePaths(option[1], reporter);
                    break;
                case "-link":
                    valid = addLinkProvider(option[1], reporter);
                    break;
                case "-linkoffline":
                    valid = addLinkProvider(option[1], option[2], reporter);
                    break;
            }
            if (!valid) {
                return false;
            }
        }
        if (outputDir == null) {
            reporter.printError("Output directory not defined.");
            return false;
        }
        if (projectDocSrcDirs == null) {
            reporter.printError("Project documentation source directories not defined.");
            return false;
        }
        return true;
    }

    private boolean setTitle(String title, DocErrorReporter reporter) {
        this.title = title;
        return true;
    }

    private boolean setOutputDir(String outputDirName, DocErrorReporter reporter) {
        outputDir = Paths.get(outputDirName);
        if (Files.exists(outputDir)) {
            return Files.isDirectory(outputDir);
        } else {
            try {
                Files.createDirectories(outputDir);
            } catch (IOException e) {
                reporter.printError("Error creating output directory " + outputDirName + ". " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    private boolean setDocSourcePaths(String sourcePaths, DocErrorReporter reporter) {
        projectDocSrcDirs = new ArrayList<>();
        for (String sourcePath : sourcePaths.split(File.pathSeparator)) {
            projectDocSrcDirs.add(Paths.get(sourcePath));
        }
        return projectDocSrcDirs.size() > 0;
    }

    private boolean addLinkProvider(String url, DocErrorReporter reporter) {
        try {
            externalRefProviders.add(new ExternalRefProvider(url));
            return true;
        } catch (IOException e) {
            reporter.printWarning("Unable to load link for " + url + ". " + e.getMessage());
            return false;
        }
    }

    private boolean addLinkProvider(String url, String packageListUrl, DocErrorReporter reporter) {
        try {
            externalRefProviders.add(new ExternalRefProvider(url, packageListUrl));
            return true;
        } catch (IOException e) {
            reporter.printWarning("Unable to load linksoffline for " + packageListUrl + ". " + e.getMessage());
            return false;
        }
    }


    private void printUsage() {
        System.out.println("Doclet options:");
        System.out.println("-t <title>                  Documentation title");
        System.out.println("-d <directory>              Destination directory for output files");
        System.out.println("-docsourcepath <pathlist>   Specify where to find project documentation source files");
        System.out.println("-link <url>                 Create links to javadoc at <url>");
        System.out.println("-linkoffline <url> <url2>   Link to docs at <url> using package list at <url2>");
    }

    public Path getOutputDir() {
        return outputDir.toAbsolutePath();
    }

    public Collection<Path> getSrcDirs() {
        return projectDocSrcDirs;
    }

    public Collection<IRefProvider> getExternalRefProviders() {
        return externalRefProviders;
    }

    public String getHeadTitle() {
        return title!=null?title:"Docdown";
    }

}


