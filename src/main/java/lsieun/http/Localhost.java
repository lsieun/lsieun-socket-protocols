package lsieun.http;

import lsieun.bean.HttpBean;
import lsieun.utils.HttpUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Localhost {
    public static void main(String[] args) {
        try(
                Socket s = new Socket("127.0.0.1", 8888);
                InputStream in = s.getInputStream();
                BufferedInputStream bin = new BufferedInputStream(in);
                OutputStream out = s.getOutputStream();
                BufferedOutputStream bout = new BufferedOutputStream(out);
                OutputStreamWriter writer = new OutputStreamWriter(bout)
                ) {
            // first request
            HttpUtils.writeToServer(writer,getHtmlRequest());

            // first response
            HttpBean html_bean = new HttpBean(bin);
            System.out.println(html_bean);
            HttpUtils.toFile(html_bean.content, "index.html");

            System.out.println("==================================");

            // second request
            HttpUtils.writeToServer(writer,getImageRequest());

            // second response
            HttpBean img_bean = new HttpBean(bin);
            System.out.println(img_bean);
            HttpUtils.toFile(img_bean.content, "logo.png");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getHtmlRequest() {
        List<String> list = new ArrayList<>();
        list.add("GET /index.html HTTP/1.1");
        list.add("Host: 127.0.0.1");
        list.add("Connection: keep-alive");
        list.add("User-Agent: Mozilla/5.0");
        list.add("Accept: text/html");
        list.add("Accept-Language: en-US");
        return list;
    }

    public static List<String> getImageRequest() {
        List<String> list = new ArrayList<>();
        list.add("GET /icons/openlogo-75.png HTTP/1.1");
        list.add("Host: 127.0.0.1");
        list.add("Connection: close");
        list.add("User-Agent: Mozilla/5.0");
        return list;
    }
}
