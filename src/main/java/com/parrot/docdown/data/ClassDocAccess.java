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

import com.sun.javadoc.*;

import java.util.*;

public class ClassDocAccess {

    private final RootDoc rootDoc;
    private final ClassDoc classDoc;

    public static class InheritedClassFields {
        private Type classType;
        private List<FieldDoc> fields;

        public Type getClassType() {
            return classType;
        }

        public List<FieldDoc> getFields() {
            return fields;
        }
    }

    public static class InheritedClassMethods {
        private Type classType;
        private List<MethodDoc> methods;

        public Type getClassType() {
            return classType;
        }

        public List<MethodDoc> getMethods() {
            return methods;
        }
    }

    public ClassDocAccess(RootDoc rootDoc, ClassDoc classDoc) {
        this.rootDoc = rootDoc;
        this.classDoc = classDoc;
    }

    public Type getSuperclass() {
        return classDoc.superclassType();
    }

    public List<Type> getImplementedInterfaces() {
        return Arrays.asList(classDoc.interfaceTypes());
    }

    public List<Type> getHierarchy() {
        LinkedList<Type> result = new LinkedList<>();
        Type classType = classDoc;
        while (classType != null) {
            result.addFirst(classType);
            classType = classType.asClassDoc().superclassType();
        }
        return result;
    }


    public List<Type> getSubclasses() {
        List<Type> result = new ArrayList<>();
        for (ClassDoc subclass : rootDoc.classes()) {
            if (!subclass.equals(classDoc) && subclass.subclassOf(classDoc)) {
                result.add(subclass);
            }
        }
        return result;
    }

    public List<FieldDoc> getEnumValues(boolean sorted) {
        List<FieldDoc> result = Arrays.asList(classDoc.enumConstants());
        if (sorted) {
            Collections.sort(result);
        }
        return result;
    }

    public List<FieldDoc> getFields(boolean sorted) {
        List<FieldDoc> result = Arrays.asList(classDoc.fields());
        if (sorted) {
            Collections.sort(result);
        }
        return result;
    }

    public List<InheritedClassFields> getInheritedFields(boolean sorted) {
        List<InheritedClassFields> result = new ArrayList<>();

        Set<String> includedFields = new HashSet<>();
        for (FieldDoc fieldDoc : getFields(false)) {
            includedFields.add(fieldDoc.name());
        }

        ClassDoc superClass = classDoc.superclass();
        while (superClass != null) {
            InheritedClassFields inheritedFields = new InheritedClassFields();
            inheritedFields.classType = superClass;
            inheritedFields.fields = new ArrayList<>();
            for (FieldDoc fieldDoc : superClass.fields()) {
                if (!includedFields.contains(fieldDoc.name())) {
                    inheritedFields.fields.add(fieldDoc);
                    includedFields.add(fieldDoc.name());
                }
            }
            if (!inheritedFields.fields.isEmpty()) {
                if (sorted) {
                    Collections.sort(inheritedFields.fields);
                }
                result.add(inheritedFields);
            }
            superClass = superClass.superclass();
        }
        return result;
    }

    public List<ConstructorDoc> getConstructors(boolean sorted) {
        List<ConstructorDoc> result = Arrays.asList(classDoc.constructors());
        if (sorted) {
            Collections.sort(result);
        }
        return result;
    }

    public List<MethodDoc> getMethods(boolean sorted) {
        List<MethodDoc> result = new ArrayList<>();
        for (MethodDoc methodDoc : classDoc.methods()) {
            if (!methodDoc.isSynthetic()) {
                if (!methodDoc.getRawCommentText().isEmpty()) {
                    result.add(methodDoc);
                } else {
                    boolean added = false;
                    MethodDoc overriddenMethod = methodDoc.overriddenMethod();
                    while (overriddenMethod != null && !added) {
                        if (!overriddenMethod.getRawCommentText().isEmpty()) {
                            result.add(overriddenMethod);
                            added = true;
                        } else {
                            overriddenMethod = overriddenMethod.overriddenMethod();
                        }
                    }
                    if (!added) {
                        result.add(methodDoc);
                    }
                }
            }
        }
        if (sorted) {
            Collections.sort(result);
        }
        return result;
    }

    public List<InheritedClassMethods> getInheritedMethods(boolean sorted) {
        List<InheritedClassMethods> result = new ArrayList<>();

        Set<String> includedMethods = new HashSet<>();
        for (MethodDoc methodDoc : getMethods(false)) {
            includedMethods.add(methodDoc.name() + methodDoc.signature());
        }

        ClassDoc superClass = classDoc.superclass();
        while (superClass != null) {
            InheritedClassMethods inheritedMethods = new InheritedClassMethods();
            inheritedMethods.classType = superClass;
            inheritedMethods.methods = new ArrayList<>();
            for (MethodDoc methodDoc : superClass.methods()) {
                if (!methodDoc.isSynthetic() && !includedMethods.contains(methodDoc.name() + methodDoc.signature())) {
                    inheritedMethods.methods.add(methodDoc);
                    includedMethods.add(methodDoc.name() + methodDoc.signature());
                }
            }
            if (!inheritedMethods.methods.isEmpty()) {
                if (sorted) {
                    Collections.sort(inheritedMethods.methods);
                }
                result.add(inheritedMethods);
            }
            superClass = superClass.superclass();
        }
        return result;
    }

}
