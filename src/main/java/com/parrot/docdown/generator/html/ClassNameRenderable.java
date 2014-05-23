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

package com.parrot.docdown.generator.html;

import com.parrot.docdown.data.DocReferenceable;
import com.parrot.docdown.generator.DefaultGenerator;
import com.parrot.docdown.generator.PageRenderer;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.Type;
import com.sun.javadoc.TypeVariable;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.href;

public class ClassNameRenderable implements Renderable {
    /**
     * write link for the main class name
     */
    public static final int LINK = 0x01;
    /**
     * write class name qualified name
     */
    public static final int FQN = 0x02;
    /**
     * write generics bounds
     */
    public static final int BOUNDS = 0x04;

    private final DefaultGenerator generator;
    private final Type type;
    private final int flags;

    public ClassNameRenderable(DefaultGenerator generator, Type type, int flags) {
        this.generator = generator;
        this.type = type;
        this.flags = flags;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        if (type.isPrimitive()) {
            html.write(type.typeName() + type.dimension());
        } else {
            String name;
            ClassDoc classDoc = type.asClassDoc();
            DocReferenceable referenceable = generator.getRefLocator().getClassDocRef(classDoc);
            String ref = PageRenderer.getPage(html).getReferenceTo(referenceable);
            if ((flags & FQN) != 0) {
                name = classDoc.qualifiedName();
            } else {
                name = referenceable.getName();
            }
            if ((flags & LINK) != 0 && ref != null) {
                html.a(href(PageRenderer.getPage(html).getReferenceTo(referenceable))).content(name,
                        HtmlCanvas.NO_ESCAPE);
            } else {
                html.write(name);
            }
        }

        ParameterizedType parameterizedType = type.asParameterizedType();
        if (parameterizedType != null) {
            Type[] typeArguments = parameterizedType.typeArguments();
            if (typeArguments.length > 0) {
                html.write("<");
                for (int typeVarCount = 0; typeVarCount < typeArguments.length; typeVarCount++) {
                    Type typeArgument = typeArguments[typeVarCount];
                    if (typeVarCount > 0) {
                        html.write(", ");
                    }
                    renderReferenceable(generator.getRefLocator().getClassDocRef(typeArgument.asClassDoc()), html);
                }
                html.write(">");
            }
        } else if (!type.isPrimitive()) {
            TypeVariable[] typeVariables = type.asClassDoc().typeParameters();
            if (typeVariables.length > 0) {
                html.write("<");
                for (int typeVarCount = 0; typeVarCount < typeVariables.length; typeVarCount++) {
                    TypeVariable typeVariable = typeVariables[typeVarCount];
                    if (typeVarCount > 0) {
                        html.write(", ");
                    }
                    html.write(typeVariable.typeName());
                    if ((flags & BOUNDS) != 0) {
                        Type[] bounds = typeVariable.bounds();
                        if (bounds.length > 0) {
                            html.write(" extends ");
                            for (int boundCnt = 0; boundCnt < bounds.length; boundCnt++) {
                                ClassDoc boundClass = bounds[boundCnt].asClassDoc();
                                if (boundCnt > 0) {
                                    html.write(" & ");
                                }
                                renderReferenceable(generator.getRefLocator().getClassDocRef(boundClass), html);
                            }
                        }
                    }
                }
                html.write(">");
            }
        }
    }

    public void renderReferenceable(DocReferenceable referenceable, HtmlCanvas html) throws IOException {
        String ref = PageRenderer.getPage(html).getReferenceTo(referenceable);
        if (ref != null) {
            html.a(href(ref)).content(referenceable.getName(), HtmlCanvas.NO_ESCAPE);
        } else {
            html.write(referenceable.getName(), HtmlCanvas.NO_ESCAPE);
        }
    }
}
