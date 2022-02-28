package lsieun.socks.utils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteUtils {
    public static String toIPV4(byte[] bytes) {
        if (bytes == null || bytes.length != 4) throw new IllegalArgumentException("bytes is not legal");
        return String.format("%s.%s.%s.%s", bytes[0] & 0xFF, bytes[1] & 0xFF, bytes[2] & 0xFF, bytes[3] & 0xFF);
    }

    public static int toPort(byte[] bytes) {
        if (bytes == null || bytes.length != 2) throw new IllegalArgumentException("bytes is not legal");
        return (bytes[0] & 0xFF) << 8 | bytes[1] & 0xFF;
    }

    public static String toHex(byte[] bytes) {
        String[] array = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            array[i] = toHex(bytes[i]);
        }
        return Arrays.toString(array);
    }

    public static String toStr(byte[] bytes, int from, int length) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(bytes, from, length);
        return toStr(bao.toByteArray());
    }

    public static String toStr(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String toHex(byte b) {
        return Integer.toHexString(b & 0xFF);
    }
}
