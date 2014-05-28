Links bellow defines the main navigation tabs.
Path "javadoc/" is a special path for the generated javadoc

[Reference](reference/)
[Javadoc](javadoc/)

Starting at the first title is the content of the landing page content

# Docdown doclet

Docdown is a custom Javadoc doclet used to generate javadoc and project documentation from markdown files.
Key features of Docdown are:

* Modern look and feel, using frameless html.
* Embeds javadoc into Wiki style pages written in markdown.
* References to the javadoc from the Wiki style pages.


## Using docdown doclet

Add the following option to your javadoc command:

```
  -doclet com.parrot.docdown.DocdownDoclet 
  -docletpath docdown.jar
  -d doc-out
  -docsourcepath doc-src
```

Docdown require Java 7 minimum.

**note:** docdown doesn't supports a list of java file to process. Source files must be specified by the option 
```-sourcepath``` and packages to include by the option ```-subpackages``` or by providing the packages to include.


## License

Docdown doclet is released under the [Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). Source code is on
[github](http://www.github.com/nicolaschriste/docdown).

