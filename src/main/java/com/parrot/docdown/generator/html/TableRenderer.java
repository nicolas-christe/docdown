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


import org.rendersnake.HtmlCanvas;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.colspan;

public class TableRenderer {

    private final int columnNr;
    private final String title;
    private int lineCnt;


    public TableRenderer(int columnNr, String title) {
        this.columnNr = columnNr;
        this.title = title;
        this.lineCnt = 0;
    }

    public void startRow(HtmlCanvas html) throws IOException {
        if (lineCnt == 0) {
            html.table().thead();
            html.tr().th(colspan(Integer.toString(columnNr))).write(title)._th()._tr();
            html._thead().tbody();
        }

        if ((lineCnt++) % 2 == 0) {
            html.tr(class_("alt-color"));
        } else {
            html.tr();
        }
    }

    public void closeRow(HtmlCanvas html) throws IOException {
        html._tr();
    }

    public void closeTable(HtmlCanvas html) throws IOException {
        if (lineCnt > 0) {
            html._tbody()._table();
        }
    }
}
