package lsieun.bean;

import lsieun.utils.HttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpBean {
    public final String first_line;
    public final List<KeyValuePair> headers = new ArrayList<>();
    public final int content_length;
    public final byte[] content;

    public HttpBean(InputStream in) throws IOException {
        byte[] bytes = HttpUtils.getHeaderBytes(in);
        String text = new String(bytes, StandardCharsets.US_ASCII);
        String[] array = text.split("\r\n");

        this.first_line = array[0];
        String content_length_str = null;
        for (int i = 1; i < array.length; i++) {
            String item = array[i];
            if (item == null || "".equals(item)) continue;
            String[] kv = item.split(":");
            String key = kv[0];
            String value = kv[1].trim();
            headers.add(new KeyValuePair(key, value));

            if ("content-length".equalsIgnoreCase(key)) {
                content_length_str = value;
            }
        }

        this.content_length = Integer.parseInt(content_length_str);
        this.content = new byte[content_length];

        int offset = 0;
        while (offset < content_length) {
            int bytesRead = in.read(content, offset, content.length - offset);
            if (bytesRead == -1) break;
            offset += bytesRead;
        }

//        if (offset != content_length) {
//            throw new IOException("Only read " + offset
//                    + " bytes; Expected " + content_length + " bytes");
//        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(first_line + System.lineSeparator());
        for (KeyValuePair item : headers) {
            sb.append(item + System.lineSeparator());
        }
        return sb.toString();
    }
}
