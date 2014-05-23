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

import java.nio.file.Path;

/**
 * A document that can be referenced
 */
public interface DocReferenceable {

    /**
     * Gets the document name
     *
     * @return the document name
     */
    String getName();

    /**
     * Gets the document qualified name
     *
     * @return the document qualified name
     */
    String getQualifiedName();

    /**
     * Get the relative path to this document from an other path
     *
     * @param from the path from which to get the relative path
     * @return relative path from the 'from' path
     */
    String getReferenceFrom(Path from);
}
