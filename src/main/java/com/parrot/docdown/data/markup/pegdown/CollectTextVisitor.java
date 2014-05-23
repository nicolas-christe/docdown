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

package com.parrot.docdown.data.markup.pegdown;


import org.pegdown.Printer;
import org.pegdown.ast.*;

public class CollectTextVisitor extends NullVisitor {

    protected Printer printer;
    protected boolean exit;

    @Override
    public void visit(AbbreviationNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(AutoLinkNode node) {
        visit((TextNode) node);
    }

    @Override
    public void visit(BlockQuoteNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(BulletListNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(CodeNode node) {
        visit((TextNode) node);
    }

    @Override
    public void visit(DefinitionListNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(DefinitionNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(DefinitionTermNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(ExpImageNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(ExpLinkNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(HeaderNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(HtmlBlockNode node) {
        visit((TextNode) node);
    }

    @Override
    public void visit(InlineHtmlNode node) {
        visit((TextNode) node);
    }

    @Override
    public void visit(ListItemNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(MailLinkNode node) {
        visit((TextNode) node);
    }

    @Override
    public void visit(OrderedListNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(ParaNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(QuotedNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(ReferenceNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(RefImageNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(RefLinkNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(RootNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(SimpleNode node) {
        if (printer != null) {
            switch (node.getType()) {
                case Apostrophe:
                    printer.print("&rsquo;");
                    break;
                case Ellipsis:
                    printer.print("&hellip;");
                    break;
                case Emdash:
                    printer.print("&mdash;");
                    break;
                case Endash:
                    printer.print("&ndash;");
                    break;
                case HRule:
                    printer.println().print("<hr/>");
                    break;
                case Linebreak:
                    printer.print("<br/>");
                    break;
                case Nbsp:
                    printer.print("&nbsp;");
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    @Override
    public void visit(SpecialTextNode node) {
        visit((TextNode) node);
    }

    @Override
    public void visit(StrikeNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(StrongEmphSuperNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(TableBodyNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(TableCaptionNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(TableCellNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(TableColumnNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(TableHeaderNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(TableNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(TableRowNode node) {
        visit((SuperNode) node);
    }

    @Override
    public void visit(VerbatimNode node) {
        visit((TextNode) node);
    }

    @Override
    public void visit(WikiLinkNode node) {
        visit((TextNode) node);
    }

    @Override
    public void visit(TextNode node) {
        if (printer != null) {
            printer.print(node.getText());
        }
    }

    @Override
    public void visit(SuperNode node) {
        if (!exit) {
            for (Node child : node.getChildren()) {
                child.accept(this);
                if (exit) {
                    return;
                }
            }
        }
    }

    @Override
    public void visit(Node node) {
    }
}
