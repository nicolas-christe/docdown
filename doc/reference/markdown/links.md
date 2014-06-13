# Links

Links can have 3 forms:

### Inline link:
 
```
[github](http://www.github.com)
``` 

Example:

[github](http://www.github.com)

### Wiki Style link:

```
[[http://www.github.com]]
``` 

Example:

[[http://www.github.com]]


### Reference link:

```
[github home page][github]
```
And somewhere else in the page:

``` 
[github]: http://www.github.com
```

Example:

[github home page][github]

[github]: http://www.github.com


## Links to Markdown pages or other resources

Markdown pages can be referenced with the page file if not ambiguous.

```no-highlight
See [[doc.md]]
```

Example:

See [[doc.md]]


Reference can be relative to the page directory:

```
link to [file in img directory](img/uml.png)
```

Example:

link to [file in img directory](img/uml.png)


Use absolute path for other files:

```no-highlight
See [[/reference/doc.md]]
```

Example:

See [[/reference/doc.md]]


## Links to javadoc entries

Generated javadoc pages can be referenced from markup pages.

### Packages

Reference to a package is done using the qualified package name:

```
[[com.parrot.docdown.generator]]
```

Example:

[[com.parrot.docdown.generator]]


### Classes

Reference to a class is done using the simple class name if not ambiguous:

```
package [[DocdownOption]]
```

Example:

package [[DocdownOption]]

Reference to a class can also be done using the full qualified class name:


```
class [[com.parrot.docdown.DocdownOption]]
```

Example:

class [[com.parrot.docdown.DocdownOption]]

### Class fields

Class fields are referenced using the class name reference, followed by # and the field name:

```no-highlight
field [[DocdownOption#title]]
```

Example:

field [[DocdownOption#title]]

Enum values are referenced like fields.

### Class methods

Class methods are referenced using the class name reference, followed by # and the method name
if not ambiguous:

```no-highlight
method [[DocdownOption#validOptions]]
```

Example:

method [[DocdownDoclet#validOptions]]


If ambiguous, the method parameters type can be specified, either unqualified
```no-highlight
method [[DocdownOption#validOptions(String[][], DocErrorReporter)]]]
```

Example:

method [[DocdownDoclet#validOptions(String[][], DocErrorReporter)]]]

or fully qualified

```no-highlight
method [[DocdownOption#validOptions(java.lang.String[][], com.sun.javadoc.DocErrorReporter)]]]
```

Example:

method [[DocdownDoclet#validOptions(java.lang.String[][], com.sun.javadoc.DocErrorReporter)]]]

Inner classes must always be qualified with the outer class name.

**Note**:

Due to limitation in the markdown parser, inline links to methods are not recognizes as links if they contains spaces or
parenthesis. To create a inline link to a method while specifying its parameters, parenthesis must be escaped 
with `\` and parameters **must not** be separated by spaces:

```no-highlight
method [validOptions](DocdownDoclet#validOptions\(String[][],DocErrorReporter\))
```

Example:

method [validOptions](DocdownDoclet#validOptions\(String[][],DocErrorReporter\))

Parenthesis escape are not require for reference links, but as in inline links parameters **must not** be separated by 
spaces:


```no-highlight
method [validOptions][](DocdownDoclet#validOptions\(String[][],DocErrorReporter\))

[validOptions]: DocdownDoclet#validOptions(String[][],DocErrorReporter)
```

Example:

method [validOptions][]

[validOptions]: DocdownDoclet#validOptions(String[][],DocErrorReporter)
