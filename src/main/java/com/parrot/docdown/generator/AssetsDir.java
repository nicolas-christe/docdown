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

package com.parrot.docdown.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Manages resources asset directory.
 * <p/>
 * Supports getting asserts from a jar or in the classes directory
 */
public class AssetsDir {

    private final Path srcDirPath;
    private final Path destDirPath;
    private final Path contentDirPath;
    private final ClassLoader classloader;

    /**
     * Constructor
     *
     * @param srcPath     path of resource directory containing the assets to copy.
     * @param destPath    path where to copy the assets.
     * @param contentPath path of the directory to copy.
     * @param classloader classloader to get the resource.
     */
    public AssetsDir(Path srcPath, Path destPath, Path contentPath, ClassLoader classloader) {
        this.srcDirPath = srcPath;
        this.destDirPath = destPath;
        this.contentDirPath = contentPath;
        this.classloader = classloader;
    }

    /**
     * Gets the path of copied assets directory.
     *
     * @return path of copied assets directory
     */
    public Path getPath() {
        return destDirPath.resolve(contentDirPath);
    }

    /**
     * Copy assets files to the output directory.
     *
     * @throws IOException if there is an error.
     */
    public void copy() throws IOException {
        try {
            // get the ressources url
            URL url = classloader.getResource(srcDirPath.toString());
            String protocol = url.getProtocol();
            if (protocol != null) {
                if (protocol.equals("jar")) {
                    copyAssetFileListFromJar(url);
                    return;
                } else if (protocol.equals("file")) {
                    copyAssetFileListFromDir(url);
                    return;
                }
            }
        } catch (URISyntaxException | NullPointerException e) {
            throw new RuntimeException("Unable to copy assets", e);
        }
        throw new RuntimeException("Unable to copy assets");
    }

    /**
     * Copy assets from a jar
     *
     * @param assertDirUrl url of the assets directory
     * @throws IOException if there is an error.
     */
    private void copyAssetFileListFromJar(URL assertDirUrl) throws IOException {
        String pathPrefix = srcDirPath.toString() + File.separatorChar + contentDirPath.toString();
        // get the jar file
        JarFile jarFile = ((JarURLConnection) assertDirUrl.openConnection()).getJarFile();
        // iterate it to find the assets content
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.getName().startsWith(pathPrefix) && !jarEntry.isDirectory()) {
                try (InputStream is = classloader.getResourceAsStream(jarEntry.getName())) {
                    String inFileName = jarEntry.getName().substring(srcDirPath.toString().length());
                    // remove fist / from inFileName
                    Path destFile = destDirPath.resolve(inFileName.substring(1));
                    Files.createDirectories(destFile.getParent());
                    Files.copy(is, destFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    /**
     * Copy assets from the classes directory
     *
     * @param assertDirUrl url of the assets directory
     * @throws IOException if there is an error.
     */
    private void copyAssetFileListFromDir(URL assertDirUrl) throws IOException, URISyntaxException {
        Path basePath = Paths.get(assertDirUrl.toURI());
        Path assertDirPath = basePath.resolve(contentDirPath);
        if (Files.isDirectory(assertDirPath)) {
            copyDirectoryFiles(basePath, assertDirPath);
        }
    }

    /**
     * Deep copy directory files.
     *
     * @param basePath base directory containing the directory to copy.
     * @param dir      directory to copy
     * @throws IOException if there is an error.
     */
    private void copyDirectoryFiles(Path basePath, Path dir) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
            for (Path path : directoryStream) {
                Path destFile = destDirPath.resolve(basePath.relativize(path));
                if (Files.isDirectory(path)) {
                    Files.createDirectories(destFile);
                    copyDirectoryFiles(basePath, path);
                } else {
                    Files.createDirectories(destFile.getParent());
                    try (InputStream is = Files.newInputStream(path)) {
                        Files.copy(is, destFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }
}

