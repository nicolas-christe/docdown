
[Witting documentation](doc.md)
[Markdown reference](markdown/)

# Docdown reference

Doclet custom option:

```
Doclet options:
-t <title>                  Documentation title
-d <directory>              Destination directory for output files
-docsourcepath <pathlist>   Where to find project documentation source files
-includepath <pathlist>     Where to find included code blocks
-link <url>                 Create links to javadoc at <url>
-linkoffline <url> <url2>   Link to docs at <url> using package list at <url2>
```

**note:** docdown doesn't supports a list of java file to process. Source files must be specified by the option 
```-sourcepath``` and packages to include by the option ```-subpackages``` or by providing the packages to include.
