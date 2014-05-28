# Images

Syntax is:

```
![alt_text](image_link "Title")
```

* `alt_text` is required and is the "alt" attribute of the `<img>` tag.
* `Title` is optional and is the "title" attribute of the `<img>` tag.
* `image_link` is the link to the image file. 

# Local images

Link to local image file can be:

* the file name unique in all the documentation directories: `unique_name.png`.
* relative file name from the current directory: `img/name.png`.
* absolute file name (starting with /) from the root documentation directory: `/data/img/name.png`

```
![uml class diagram](uml.png "Provider Class Diagram")
```

Example:

![uml class diagram](uml.png "Provider Class Diagram")


# External images

Absolute link:

```
![apache](http://www.apache.org/images/feather-small.gif)
`
``

Example:

![apache](http://www.apache.org/images/feather-small.gif)

