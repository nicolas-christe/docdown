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


import com.parrot.docdown.generator.html.PackageListRenderable;
import org.rendersnake.Renderable;

public class PackageListRenderer extends ApiPageRenderer {

    public PackageListRenderer(DefaultGenerator generator) {
        super(generator);
    }

    @Override
    protected Renderable createMainContentRenderable() {
        return new PackageListRenderable(generator);
    }
}
