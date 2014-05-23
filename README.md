# Docdown doclet

Docdown is a custom javadoc doclet used to generate project documentation. It generate frameless html javadoc, 
embedded in custom documentation paged written in markdown.

![screen copt](doc/screen.png?raw=true)

## Using docdown doclet

add the following option to your javadoc command:

```
  -doclet com.parrot.docdown.DocdownDoclet 
  -docletpath docdown.jar
  -d doc-out
  -docsourcepath doc-src
```

## License

Doccdown is provided under the Apache License Version 2.0.

## Release

The first release is v0.1-alpha1 and is a work in progress.

**note:** docdown doesn't supports a list of java file to process. Source files must be specified by the option 
```-sourcepath``` and packages to include by the option ```-subpackages``` or by providing the packages to include.
