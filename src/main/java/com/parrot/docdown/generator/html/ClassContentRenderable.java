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

import com.parrot.docdown.data.ClassDocAccess;
import com.parrot.docdown.data.DocReferenceable;
import com.parrot.docdown.data.page.ClassPage;
import com.parrot.docdown.generator.DefaultGenerator;
import com.parrot.docdown.generator.PageRenderer;
import com.sun.javadoc.*;
import org.rendersnake.HtmlCanvas;

import java.io.IOException;
import java.util.List;

import static org.rendersnake.HtmlAttributesFactory.*;

public class ClassContentRenderable extends PageContentRenderable {

    public ClassContentRenderable(DefaultGenerator generator) {
        super(generator);
    }

    @Override
    protected void doRenderOn(HtmlCanvas html) throws IOException {
        ClassPage page = PageRenderer.getPage(html);
        ClassDocAccess classDocAccess = new ClassDocAccess(generator.getRootDoc(), page.getClassDoc());
        ClassDoc classDoc = page.getClassDoc();

        html.div(class_("api_header")).p().content(getModifier(classDoc)).h1();
        html.render(new ClassNameRenderable(generator, classDoc, ClassNameRenderable.BOUNDS));
        html._h1();
        renderSuperclass(html, classDocAccess);
        renderImplementedInterfaces(html, classDocAccess);
        html._div();

        renderHierarchy(html, classDocAccess);
        renderSubclasses(html, classDocAccess);

        if (classDoc.inlineTags() != null && classDoc.inlineTags().length > 0) {
            html.h2().content("Overview");
            html.p().render(new InlineTagsRenderer(generator, classDoc.inlineTags(), classDoc.position()))._p();
        }

        html.h2().content("Summary");
        renderEnumValuesSummary(html, classDocAccess);
        renderFieldSummary(html, classDocAccess);
        renderConstructorsSummary(html, classDocAccess);
        renderMethodsSummary(html, classDocAccess);

        renderEnumValues(html, classDocAccess);
        renderFields(html, classDocAccess);
        renderConstructors(html, classDocAccess);
        renderMethods(html, classDocAccess);
    }

    private void renderSuperclass(HtmlCanvas html, ClassDocAccess classDocAccess) throws IOException {
        Type superclass = classDocAccess.getSuperclass();
        if (superclass != null) {
            html.p().write("extends ");
            html.render(new ClassNameRenderable(generator, superclass, ClassNameRenderable.LINK));
            html._p();
        }
    }

    private void renderImplementedInterfaces(HtmlCanvas html, ClassDocAccess classDocAccess) throws IOException {
        List<Type> interfaces = classDocAccess.getImplementedInterfaces();
        if (interfaces.size() > 0) {
            html.p().write("implements ");
            for (int cnt = 0; cnt < interfaces.size(); cnt++) {
                Type intf = interfaces.get(cnt);
                if (cnt > 0) {
                    html.write(", ");
                }
                html.render(new ClassNameRenderable(generator, intf, ClassNameRenderable.LINK));
            }
            html._p();
        }
    }

    private void renderHierarchy(HtmlCanvas html, ClassDocAccess classDocAccess) throws IOException {
        List<Type> hierarchy = classDocAccess.getHierarchy();
        if (!hierarchy.isEmpty()) {
            html.div(class_("hierarchy"));
            for (int cnt = 0; cnt < hierarchy.size(); cnt++) {
                int flags = ClassNameRenderable.FQN;
                if (cnt < hierarchy.size() - 1) {
                    flags |= ClassNameRenderable.LINK;
                }
                html.p(style("text-indent:" + cnt * 2 + "em;")).write(cnt > 0 ? "&#8627;" : "&nbsp;",
                        false).render(new ClassNameRenderable(generator, hierarchy.get(cnt), flags))._p();
            }
            html._div();
        } else {
            html.div(class_("separator")).close();
        }
    }

    private void renderSubclasses(HtmlCanvas html, ClassDocAccess classDocAccess) throws IOException {
        List<Type> subclasses = classDocAccess.getSubclasses();
        if (!subclasses.isEmpty()) {
            html.h4().content("Subclasses:").p(class_("indent"));
            for (int cnt = 0; cnt < subclasses.size(); cnt++) {
                if (cnt >= 1) {
                    html.write(", ");
                }
                html.render(new ClassNameRenderable(generator, subclasses.get(cnt), ClassNameRenderable.LINK));
            }
            html._p();
        }
    }

    private void renderEnumValuesSummary(HtmlCanvas html, ClassDocAccess classDocAccess) throws IOException {
        TableRenderer tableRenderer = new TableRenderer(2, "Enum Constants");
        List<FieldDoc> enumValues = classDocAccess.getEnumValues(true);
        for (FieldDoc fieldDoc : enumValues) {
            tableRenderer.startRow(html);
            html.td().a(href("#" + fieldDoc.name())).content(fieldDoc.name())._td();
            html.td().render(new InlineTagsRenderer(generator, fieldDoc.firstSentenceTags(),
                    fieldDoc.position()))._td();
            tableRenderer.closeRow(html);
        }
        tableRenderer.closeTable(html);
    }

    private void renderFieldSummary(HtmlCanvas html, ClassDocAccess classDocAccess) throws IOException {
        TableRenderer tableRenderer = new TableRenderer(2, "Fields");
        List<FieldDoc> fields = classDocAccess.getFields(true);
        for (FieldDoc fieldDoc : fields) {
            tableRenderer.startRow(html);
            html.td().write(fieldDoc.modifiers()).write(' ').render(new ClassNameRenderable(generator,
                    fieldDoc.type(), ClassNameRenderable.LINK))._td();
            html.td().a(href("#" + fieldDoc.name())).content(fieldDoc.name());
            html.p(class_("indent")).render(new InlineTagsRenderer(generator, fieldDoc.firstSentenceTags(),
                    fieldDoc.position()))._p()._td();
            tableRenderer.closeRow(html);
        }

        // inherited fields
        for (ClassDocAccess.InheritedClassFields inheritedFields : classDocAccess.getInheritedFields(true)) {
            tableRenderer.startRow(html);
            html.td().write("from ").render(new ClassNameRenderable(generator, inheritedFields.getClassType(),
                    ClassNameRenderable.LINK))._td();
            html.td();
            for (int i = 0; i < inheritedFields.getFields().size(); i++) {
                if (i > 0) {
                    html.write(", ");
                }
                FieldDoc fieldDoc = inheritedFields.getFields().get(i);
                DocReferenceable referenceable = generator.getRefLocator().getFieldDocRef(fieldDoc);
                html.a(href(PageRenderer.getPage(html).getReferenceTo(referenceable))).content(fieldDoc.name(),
                        HtmlCanvas.NO_ESCAPE);
                html.write(fieldDoc.type().dimension());
            }
            html._td();
            tableRenderer.closeRow(html);
        }
        tableRenderer.closeTable(html);
    }

    private void renderConstructorsSummary(HtmlCanvas html, ClassDocAccess classDocAccess) throws IOException {
        TableRenderer tableRenderer = new TableRenderer(2, "Constructors");

        List<ConstructorDoc> constructors = classDocAccess.getConstructors(true);
        for (ConstructorDoc constructorDoc : constructors) {
            if (!constructorDoc.isSynthetic()) {
                tableRenderer.startRow(html);
                html.td().write(constructorDoc.modifiers())._td();
                html.td().a(href("#" + getAnchor(constructorDoc))).content(constructorDoc.name());
                renderExecutableMemberParams(html, constructorDoc);
                html.br();
                html.p(class_("indent")).render(new InlineTagsRenderer(generator, constructorDoc.firstSentenceTags(),
                        constructorDoc.position()))._p();
                html._td();
                tableRenderer.closeRow(html);
            }
        }
        tableRenderer.closeTable(html);
    }


    private void renderMethodsSummary(HtmlCanvas html, ClassDocAccess classDocAccess) throws IOException {
        TableRenderer tableRenderer = new TableRenderer(2, "Methods");
        List<MethodDoc> methods = classDocAccess.getMethods(true);
        // methods
        for (MethodDoc methodDoc : methods) {
            if (!methodDoc.isSynthetic()) {
                tableRenderer.startRow(html);
                html.td().write(methodDoc.modifiers()).write(' ').render(new ClassNameRenderable(generator,
                        methodDoc.returnType(), ClassNameRenderable.LINK))._td();
                html.td().a(href("#" + getAnchor(methodDoc))).content(methodDoc.name());
                renderExecutableMemberParams(html, methodDoc);
                html.br();
                html.p(class_("indent")).render(new InlineTagsRenderer(generator, methodDoc.firstSentenceTags(),
                        methodDoc.position()))._p();
                html._td();
                tableRenderer.closeRow(html);
            }
        }
        // inherited methods
        for (ClassDocAccess.InheritedClassMethods inheritedMethods : classDocAccess.getInheritedMethods(true)) {
            tableRenderer.startRow(html);
            html.td().write("from ").render(new ClassNameRenderable(generator, inheritedMethods.getClassType(),
                    ClassNameRenderable.LINK))._td();
            html.td();
            for (int i = 0; i < inheritedMethods.getMethods().size(); i++) {
                if (i > 0) {
                    html.write(", ");
                }
                ExecutableMemberDoc memberDoc = inheritedMethods.getMethods().get(i);
                DocReferenceable referenceable = generator.getRefLocator().getExecutableMemberDocRef(memberDoc);
                html.a(href(PageRenderer.getPage(html).getReferenceTo(referenceable))).content(memberDoc.name(),
                        HtmlCanvas.NO_ESCAPE);
            }
            html._td();
            tableRenderer.closeRow(html);
        }
        tableRenderer.closeTable(html);
    }

    private void renderEnumValues(HtmlCanvas html, ClassDocAccess classDoc) throws IOException {
        List<FieldDoc> enumValues = classDoc.getEnumValues(false);
        if (enumValues.size() > 0) {
            html.h2().content("Enum Constants");
        }
        for (FieldDoc fieldDoc : enumValues) {
            html.div(class_("api")).a(name(fieldDoc.name()))._a().strong().content(fieldDoc.name())._div();
            html.p().render(new InlineTagsRenderer(generator, fieldDoc.inlineTags(), fieldDoc.position()))._p();
            renderSince(html, fieldDoc);
            renderSee(html, fieldDoc);
        }
    }

    private void renderFields(HtmlCanvas html, ClassDocAccess classDoc) throws IOException {
        List<FieldDoc> fields = classDoc.getFields(false);
        if (fields.size() > 0) {
            html.h2().content("Fields");
        }
        for (FieldDoc fieldDoc : fields) {
            html.div(class_("api")).a(name(fieldDoc.name()))._a().write(fieldDoc.modifiers()).write(' ').render(new
                    ClassNameRenderable(generator, fieldDoc.type(), ClassNameRenderable.LINK)).write(" ").strong()
                    .content(fieldDoc.name())._div();
            renderDeprecated(html, fieldDoc);
            html.p().render(new InlineTagsRenderer(generator, fieldDoc.inlineTags(), fieldDoc.position()))._p();
            renderSince(html, fieldDoc);
            renderSee(html, fieldDoc);
        }
    }

    private void renderConstructors(HtmlCanvas html, ClassDocAccess classDoc) throws IOException {
        List<ConstructorDoc> constructors = classDoc.getConstructors(false);
        if (constructors.size() > 0) {
            html.h2().content("Constructors");
        }
        for (ConstructorDoc constructor : constructors) {
            html.div(class_("api")).a(name(getAnchor(constructor)))._a();
            html.write(constructor.modifiers()).write(' ').strong().content(constructor.name());
            renderExecutableMemberParams(html, constructor);
            html._div();
            renderDeprecated(html, constructor);
            html.p().render(new InlineTagsRenderer(generator, constructor.inlineTags(), constructor.position()))._p();
            if (constructor.paramTags().length > 0) {
                html.h4().content("Parameters:");
                html.p(class_("indent"));
                for (ParamTag param : constructor.paramTags()) {
                    html.strong().content(param.parameterName()).write(": ").render(new InlineTagsRenderer(generator,
                            param.inlineTags(), param.position())).br();
                }
                html._p();
            }
            renderSince(html, constructor);
            renderSee(html, constructor);
        }
    }

    private void renderMethods(HtmlCanvas html, ClassDocAccess classDoc) throws IOException {
        List<MethodDoc> methods = classDoc.getMethods(false);
        if (methods.size() > 0) {
            html.h2().content("Methods");
        }
        for (MethodDoc methodDoc : methods) {
            html.div(class_("api")).a(name(getAnchor(methodDoc)))._a();
            html.write(methodDoc.modifiers()).write(' ').render(new ClassNameRenderable(generator,
                    methodDoc.returnType(), ClassNameRenderable.LINK)).write(' ').strong().content(methodDoc.name());
            renderExecutableMemberParams(html, methodDoc);
            html._div();
            renderDeprecated(html, methodDoc);
            html.p().render(new InlineTagsRenderer(generator, methodDoc.inlineTags(), methodDoc.position()))._p();
            if (methodDoc.paramTags().length > 0) {
                html.h4().content("Parameters:");
                html.p(class_("indent"));
                for (ParamTag param : methodDoc.paramTags()) {
                    html.strong().content(param.parameterName()).write(": ").render(new InlineTagsRenderer(generator,
                            param.inlineTags(), param.position())).br();
                }
                html._p();
            }
            Tag[] returnTags = methodDoc.tags("return");
            if (returnTags.length > 0) {
                html.h4().content("Returns:");
                html.p(class_("indent")).render(new InlineTagsRenderer(generator, returnTags[0].inlineTags(),
                        returnTags[0].position()))._p();
            }

            renderSince(html, methodDoc);
            renderSee(html, methodDoc);
        }
    }

    private void renderDeprecated(HtmlCanvas html, Doc doc) throws IOException {
        Tag[] deprecated = getTagComments(doc, "deprecated");
        if (deprecated != null) {
            html.p(class_("deprecated")).i().strong().content("Deprecated: ").render(new InlineTagsRenderer
                    (generator, deprecated, doc.position()))._i()._p();
        }
    }

    private void renderSince(HtmlCanvas html, Doc doc) throws IOException {
        Tag[] deprecated = getTagComments(doc, "since");
        if (deprecated != null) {
            html.p().strong().content("Since: ").render(new InlineTagsRenderer(generator, deprecated,
                    doc.position()))._p();
        }
    }

    private Tag[] getTagComments(Doc doc, String tagName) {
        Tag[] tag = doc.tags(tagName);
        if (tag.length > 0) {
            return tag[0].inlineTags();
        }
        return null;
    }

    private String getAnchor(ExecutableMemberDoc executableMemberDoc) {
        return executableMemberDoc.name() + executableMemberDoc.signature();
    }

    private void renderSee(HtmlCanvas html, Doc doc) throws IOException {
        Tag[] tags = doc.tags("see");
        if (tags.length > 0) {
            html.p().strong().content("See also: ").render(new InlineTagsRenderer(generator, tags,
                    doc.position()))._p();
        }
    }

    private void renderExecutableMemberParams(HtmlCanvas html, ExecutableMemberDoc executableMemberDoc) throws
            IOException {
        html.write('(');
        Parameter[] params = executableMemberDoc.parameters();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                html.write(", ");
            }
            Parameter param = params[i];
            html.render(new ClassNameRenderable(generator, param.type(), ClassNameRenderable.LINK));
            html.write(' ').write(param.name());
            html.write(param.type().dimension());
        }
        html.write(')');
    }

    private static String getModifier(ClassDoc classDoc) {
        StringBuilder sb = new StringBuilder();
        if (classDoc.isPublic()) {
            sb.append("public ");
        } else if (classDoc.isProtected()) {
            sb.append("protected ");
        } else if (classDoc.isPrivate()) {
            sb.append("private ");
        }
        if (classDoc.isAbstract() && !classDoc.isInterface() && !classDoc.isAnnotationType()) {
            sb.append("abstract ");
        }
        if (classDoc.isFinal() && !classDoc.isEnum()) {
            sb.append("final ");
        }

        if (classDoc.isAnnotationType()) {
            sb.append("@interface ");
        } else if (classDoc.isInterface()) {
            sb.append("interface ");
        } else if (classDoc.isEnum()) {
            sb.append("enum ");
        } else {
            sb.append("class ");
        }
        return sb.toString();
    }

}

