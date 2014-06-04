# Writing documentation

## Background

Docdown generated the javadoc embedded in wiki style documentation written in markdown. Wiki pages are linked together
to provide documentation navigation

Documentations navigation is composed of:

* Tabs, displayed at the top of the layout. Those tabs are for global sections of the documentation. The javadoc is 
one of those tabs.
* Side panel, displayed on the left side of the layout. This side panel contains the section of the current tab, 
and the package and classes list for the javadoc tab.

This navigation is based on content and directories layout of markup files.

## Documentation source directory

Those directories contains the source markdown files and other resource files (like image) used to generate the 
documentation. Each directory correspond to a section of the documentation. Directories must at least contains a 
index file named "index.md" containing the side panel links and the content of the section landing page. It can also
contains other markup and resources files. The index.md file in the root source documentation directory defines the 
top level tabs and the main landing page of the documentation. index.md in sub-directories define the side panel 
content for the directory, and the landing page of this directory.

 
## Index files

Index files (index.md) are require in documentation directories, contains 2 sections.

* Content before the first level 1 header: links in this section defines the navigation content. Other elements in 
this section are ignores.
* First header and everything after: content of the landing page for this navigation level.

Example:

File `doc/index.md` :

```
[Overview](overview/)
[Reference](ref/)
[Javadoc](javadoc/)

# Root file

Hello this is a sample root.index file
```

This file generates 3 tabs:

* The first one is "Overview" and displays the content of `doc/overview/index.md`.
* The second one is "Reference" and displays the content of `doc/ref/index.md`.
* The last one is "Javadoc" and and displays the generated javadoc.

File `doc/overview/index.md` :

```
[Page 1](page1.md)
[Other Page](page2.md)
[Sub-content](content/)

# Overview

This is a sample of overview directory index file
```

This file generates 3 entries in the navigation pane:

* The first one is "Page 1" and displays the content of `doc/overview/page1.md`.
* The second one is "Other Page" and displays the content of `doc/overview/page2.md```.
* The last one is "Sub-content" and and displays the content of `doc/overview/content/index.md`.

Content of the navigation pane is hierarchic, link in the first section of `doc/overview/content/index.md`
will be displayed bellow "Sub-content" when it's selected.


