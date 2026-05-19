# Jiffy

An interactive fiction creator, from a syntax similar to Wiki/arkdown. Check the wiki in order to learn how to write interactive fiction with Jiffy.

## Build

You don't need to build if you don't want to. Just download the `jar` file from the latest release!

This is a Java desktop application, created with Netbeans and Maven (you'll have to change the version below with the actual version).

```bash
$ mvn package
$ java -jar target/jiffy-1.0.jar
```

## Run

**Jiffy** can run as CLI compiler or as an interactive GUI. Use the `--compiler` option, and a file path, in order to trigger the CLI compiler. Add nothing (or optionally, a file path), in order to launch the GUI. If given, the file will be automatically loaded.

### CLI compiler

    $ java -jar Jiffy-1.0.jar --compile samples/lanchoa.jif
    Jiffy v1.1 20260509 'fresh'

    Compiling samples/lanchoa.jif
    Ok

This will create `lanchoa.html`, `lanchoa.css`, and `lanchoa.js` in the same directory `lanchoa.jif` sits in.

### The GUI

Just launch the app with a double-click, or from the console specifying (or not) a path. Then use Tools >> Compile, in order to generate the CSS, HTML and JavaScript files.
