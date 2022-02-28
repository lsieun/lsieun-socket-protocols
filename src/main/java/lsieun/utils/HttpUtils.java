package lsieun.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpUtils {
    public static void writeToServer(Writer writer, List<String> list) throws IOException {
        list.stream().forEach(System.out::println);
        System.out.println(System.lineSeparator());

        for (String line : list) {
            writer.write(line);
            writer.write("\r\n");
        }
        writer.write("\r\n");
        writer.flush();
    }

    public static byte[] getHeaderBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] marks = new byte[4];
        for (int value = in.read(); value != -1; value = in.read()) {
            out.write(value);

            byte b = (byte) value;
            marks[0] = marks[1];
            marks[1] = marks[2];
            marks[2] = marks[3];
            marks[3] = b;
            if (marks[0] == '\r' && marks[1] == '\n' && marks[2] == '\r' && marks[3] == '\n') {
                break;
            }
        }
        return out.toByteArray();
    }

    public static String toStr(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void toFile(byte[] bytes, String filename) throws IOException {
        String filepath = System.getProperty("user.dir") + "/target/" + filename;
        try (
                FileOutputStream fout = new FileOutputStream(filepath);
                BufferedOutputStream out = new BufferedOutputStream(fout);
        ) {
            out.write(bytes);
            System.out.println("file://" + filepath);
        }
    }
}
