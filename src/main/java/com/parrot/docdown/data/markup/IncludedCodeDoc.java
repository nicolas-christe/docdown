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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * An doc on a code source file that can be included as a code block in a markup document
 */
public class IncludedCodeDoc extends BaseDoc {

    private static final String BEGIN_INCLUDE = "BEGIN_INCLUDE";
    private static final String END_INCLUDE = "END_INCLUDE";

    public IncludedCodeDoc(Path sourceFilePath) {
        super(sourceFilePath);
    }

    public String[] getContent(String id) {
        boolean skip = id!=null;
        String begin = BEGIN_INCLUDE + "(" + id + ")";
        String end = END_INCLUDE + "(" + id + ")";
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(sourceFilePath, Charset.defaultCharset())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (skip) {
                    skip = !(line.contains(begin));
                } else {
                    if (line.contains(end)) {
                        break;
                    }
                    lines.add(line);
                }
            }
        } catch (IOException e) {
        }

        if (!skip) {
            return lines.toArray(new String[lines.size()]);
        }
        return null;
    }
}
