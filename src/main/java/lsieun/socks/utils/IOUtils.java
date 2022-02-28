package lsieun.socks.utils;

import java.io.*;

public class IOUtils {
    public static byte[] readBytes(InputStream in, int length) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (int count = 0; count < length; count++) {
            int value = in.read();
            if (value == -1) break;
            out.write(value);
        }

        return out.toByteArray();
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, false);
    }

    public static void copy(InputStream in, OutputStream out, boolean verbose) throws IOException {
        byte[] buff = new byte[SocksConst.BUFFER_SIZE];
        for (int len = in.read(buff); len != -1; len = in.read(buff)) {
            if (verbose) {
                println(System.out, buff, 0, len);
            }
            out.write(buff, 0, len);
            out.flush();
        }
    }

    public static void println(PrintStream out, byte[] bytes) {
        println(out, bytes, 0, bytes.length);
    }

    public static void println(PrintStream out, byte[] bytes, int offset, int length) {
        String str = ByteUtils.toStr(bytes, offset, length);
        out.println("tid: " + Thread.currentThread().getId() + System.lineSeparator() + str);
    }

    public static void closeQuietly(AutoCloseable ac) {
        try {
            ac.close();
        } catch (Exception e) {
            // do nothing
        }
    }
}
