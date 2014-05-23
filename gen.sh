javadoc \
-doclet com.parrot.docdown.DocdownDoclet \
-docletpath build/libs/docdown.jar \
-link http://docs.oracle.com/javase/7/docs/api/ \
-t "Docdown Doclet" \
-d build/docdown \
-sourcepath src/main/java \
-docsourcepath doc \
-subpackages com.parrot.docdown
