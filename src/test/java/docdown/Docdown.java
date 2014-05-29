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

import com.parrot.docdown.DocdownDoclet;

public class Docdown {
    static public void main(String[] args) {
        com.sun.tools.javadoc.Main.execute(new String[]{"-doclet", DocdownDoclet.class.getName(), "-link",
                "http://docs.oracle.com/javase/7/docs/api/", "-t", "Docdown Doclet", "-d", "build/docdown",
                "-private",
                "-sourcepath", "src/main/java", "-docsourcepath", "doc",
                "-includepath",  "src/sample/java",
                "-subpackages", "com.parrot.docdown"});
    }
}
