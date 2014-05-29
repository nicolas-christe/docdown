# Sample code block

Docdown allows to include blocks of code extracted from extrnal java files.

## Sample code definition

Sample code are extracted from the files specified in directories specified by option `-includepath`.

Section in source files can be marked by using mark `BEGIN_INCLUDE(id)` and `END_INCLUDE(id)`:

Example:

```
int value=0;
int init = 14;
//BEGIN_INCLUDE(sampleid)
if (init > 10) {
    value = init;
}
//END_INCLUDE(sampleid)

return value;

```

## Include sample code in markdown file

Sample code include is done by the following markup block:

```
@(<filename>:<id>)
```

`filename` name is the name of the file containing the sample to include. It can he file name if not ambiguous, 
or the absolute file path. 
`id` is the id of the sample to include. If not specified the whole file content is included.


Example:

```
@(SampleCode1.java:sampleid)
```

This markup generate the following block:

@(SampleCode1.java:sampleid)
