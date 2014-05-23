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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * One level index, referencing MarkupDoc pages
 */
public class IndexDoc {

    /**
     * An entry in the index, with a label and the path from a source directory (including the src directory)
     */
    public static class Entry {
        private final String label;
        private final MarkupDoc target;
        private IndexDoc childIndex;

        private Entry(String label, MarkupDoc target) {
            this.label = label;
            this.target = target;
        }

        protected void setChildIndex(IndexDoc childIndex) {
            this.childIndex = childIndex;
        }

        public String getLabel() {
            return label;
        }

        public MarkupDoc getTarget() {
            return target;
        }

        public IndexDoc getChildIndex() {
            return childIndex;
        }
    }

    public static class Builder {

        private final List<Entry> entryList;

        public Builder() {
            entryList = new ArrayList<>();
        }

        public void add(String label, MarkupDoc target) {
            entryList.add(new Entry(label, target));
        }
    }

    private final List<Entry> entryList;
    private final String parentContainer;

    void linkEntries(Map<MarkupDoc, IndexDoc> indexes) {
        for (Entry entry : entryList) {
            entry.setChildIndex(indexes.get(entry.getTarget()));
        }
    }

    public IndexDoc(Builder builder, String parentContainer) {
        this.parentContainer = parentContainer;
        this.entryList = builder.entryList;
    }

    public Entry[] getContent() {
        return entryList.toArray(new Entry[entryList.size()]);
    }

    public String getParentContainer() {
        return parentContainer;
    }

}
