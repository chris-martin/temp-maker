temp-maker
==========

A utility class for creating temporary files and folders.

The primary purpose of this project is to provide the
```createTempDir``` method that is missing from Java 6.
If you're using Java 7, you probaby have no need for this.

Example usage
-------------

```java
import org.codeswarm.tempmaker.TempMaker;
import java.io.File;

public class TempMakerDemo {
    public static void main(String[] args) {
        {
            // Create a new file. This is equivalent to
            // java.io.File.createTempFile("aaa", "").
            File file = new TempMaker().createFile();

            // Example output: /tmp/aaa5479369300239724246
            System.out.println(file);
        }
        {
            // Create a new directory.
            File dir = new TempMaker().createDir();

            // Example output: /tmp/1355553939811-0
            System.out.println(dir);
        }
        {
            // Create a directory, specifying prefix and suffix.
            File dir = new TempMaker()
                    .withPrefix("xyzzy-")
                    .withSuffix("-temporary")
                    .createDir();

            // Example output: /tmp/xyzzy-1355554221889-0-temporary
            System.out.println(dir);

            // Create a file inside the dir we just created.
            File file = new TempMaker()
                    .withPrefix("foo-")
                    .withSuffix(".txt")
                    .withParent(dir)
                    .createFile();

            // Example output: /tmp/xyzzy-1355554221889-0-temporary/foo-2985090123826173955.txt
            System.out.println(file);
        }
    }
}
```

Download
--------

Temp-maker is available from Maven Central.
The latest version is 1.0.

```xml
<dependency>
  <groupId>org.codeswarm</groupId>
  <artifactId>temp-maker</artifactId>
  <version>1.0</version>
</dependency>
```
