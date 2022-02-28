package lsieun.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class HTTPS {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://www.zhihu.com");
        URLConnection conn = url.openConnection();
        try (InputStream in = conn.getInputStream()) {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            for (int len = in.read(buffer); len != -1;len = in.read(buffer)) {
                bao.write(buffer, 0, len);
            }
            byte[] bytes = bao.toByteArray();
            String html = new String(bytes, StandardCharsets.UTF_8);
            System.out.println(html);
        }
    }
}
