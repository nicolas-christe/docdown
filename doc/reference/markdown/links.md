# Links

Links can have 3 forms:

### Normal link:
 
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



### Class methods

Class methods are referenced using the class name reference, followed by # and the method name
if not ambiguous:

```no-highlight
method [[DocdownOption#optionLength]]
```

Example:

method [[DocdownOption#optionLength]]


If ambiguous, the method parameters type can be specified:

```no-highlight
method [[DocdownOption#optionLength(String)]]
```

Example:

method [[DocdownOption#optionLength(String)]]

