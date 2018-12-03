package com.megacreep.jinotify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LibLoader {

    public static void load(String path, String name) {
        name = "/lib" + name + ".so";
        try {
            InputStream in = LibLoader.class.getResourceAsStream(name);
            File fileOut = new File(System.getProperty("java.io.tmpdir") + "/" + path + "/" + name);
            OutputStream out = openOutputStream(fileOut);
            copy(in, out);
            in.close();
            out.close();
            System.load(fileOut.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }

            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Directory '" + parent + "' could not be created");
            }
        }
        return new FileOutputStream(file);
    }

    private static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0L;
        byte[] buffer = new byte[4096];
        int n;
        for (; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
        }
        return count;
    }
}
