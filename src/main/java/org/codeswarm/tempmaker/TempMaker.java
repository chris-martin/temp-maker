package org.codeswarm.tempmaker;

import java.io.File;
import java.io.IOException;

/**
 * Immutable builder for temporary files and directories.
 */
public final class TempMaker {

    private final File parent;
    private final String prefix;
    private final String suffix;
    private final int dirAttempts;

    public TempMaker() {
        this(new File(System.getProperty("java.io.tmpdir")), "", "", 10000);
    }

    private TempMaker(File parent, String prefix, String suffix, int dirAttempts) {
        this.parent = parent;
        this.prefix = prefix;
        this.suffix = suffix;
        this.dirAttempts = dirAttempts;
    }

    /**
     * Equivalent to {@code withParent(new File(parentPath))}.
     * @see #withParent(File)
     * @throws NullPointerException if {@code parentPath == null}
     * @return A new immutable builder
     */
    public TempMaker withParent(String parentPath) {
        if (parentPath == null) throw new NullPointerException();
        return withParent(new File(parentPath));
    }

    /**
     * @param parent Parent directory in which to place new temporary files/folders.
     *               Defaults to the standard Java temporary directory specified by
     *               the "java.io.tmpdir" {@code System} property. May not be null.
     * @throws NullPointerException if {@code parent == null}
     * @return A new immutable builder
     */
    public TempMaker withParent(File parent) {
        if (parent == null) throw new NullPointerException();
        return new TempMaker(parent, prefix, suffix, dirAttempts);
    }

    /**
     * @param prefix Value prepended to the beginning of generated file/folder names.
     *               Defaults to the empty string. May not be null.
     * @throws NullPointerException if {@code prefix == null}
     * @return A new immutable builder
     */
    public TempMaker withPrefix(String prefix) {
        if (prefix == null) throw new NullPointerException();
        return new TempMaker(parent, prefix, suffix, dirAttempts);
    }

    /**
     * @param suffix Value appended to the end of generated file/folder names.
     *               Defaults to the empty string. May not be null.
     * @throws NullPointerException if {@code suffix == null}
     * @return A new immutable builder
     */
    public TempMaker withSuffix(String suffix) {
        if (suffix == null) throw new NullPointerException();
        return new TempMaker(parent, prefix, suffix, dirAttempts);
    }

    /**
     * @param dirAttempts Maximum loop count when creating temp directories.
     *                    Defaults to 10,000. Must be at least 1.
     * @throws IllegalArgumentException if {@code dirAttempts} < 1
     * @return A new immutable builder
     */
    public TempMaker withDirAttempts(int dirAttempts) {
        if (dirAttempts < 1) throw new IllegalArgumentException();
        return new TempMaker(parent, prefix, suffix, dirAttempts);
    }

    /**
     * Creates a directory.
     * @throws IllegalStateException if directory creation fails
     */
    public File createDir() {

        // This iteration strategy is the same as that of Guava's
        // com.google.common.io.Files.createTempDir():File method.

        String prefix = this.prefix;
        prefix += System.currentTimeMillis() + "-";

        for (int counter = 0; counter < dirAttempts; counter++) {
            File tempDir = new File(parent, prefix + counter + suffix);
            if (tempDir.mkdirs()) return tempDir;
        }
        throw new IllegalStateException("Failed to create directory within "
                + dirAttempts + " attempts (tried " + prefix + "0" + suffix
                + " to " + prefix + (dirAttempts - 1) + suffix + ")");
    }

    /**
     * Creates a file.
     * @throws IllegalStateException if file creation fails
     */
    public File createFile() {
        String prefix = this.prefix;
        while (prefix.length() < 3) prefix += "a";
        try {
            return File.createTempFile(prefix, suffix, parent);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
