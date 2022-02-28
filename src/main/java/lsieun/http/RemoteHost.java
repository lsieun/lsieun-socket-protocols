package lsieun.http;

import lsieun.bean.HttpBean;
import lsieun.utils.HttpUtils;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class RemoteHost {
    public static void main(String[] args) {
        SocketFactory factory = SSLSocketFactory.getDefault();
        try(
                //Socket s = new Socket("127.0.0.1", 80);
                Socket s = factory.createSocket("images.techhive.com", 443);
                InputStream in = s.getInputStream();
                BufferedInputStream bin = new BufferedInputStream(in);
                OutputStream out = s.getOutputStream();
                BufferedOutputStream bout = new BufferedOutputStream(out);
                OutputStreamWriter writer = new OutputStreamWriter(bout)
                ) {
//            // first request
//            HttpUtils.writeToServer(writer,getHtmlRequest());
//
//            // first response
//            HttpBean html_bean = new HttpBean(bin);
//            System.out.println(html_bean);
//            HttpUtils.toFile(html_bean.content, "index.html");
//
//            System.out.println("==================================");

            // second request
            HttpUtils.writeToServer(writer,getImageRequest());

            // second response
            HttpBean img_bean = new HttpBean(bin);
            System.out.println(img_bean);
            HttpUtils.toFile(img_bean.content, "image_no_available.jpeg");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static List<String> getHtmlRequest() {
//        List<String> list = new ArrayList<>();
//        list.add("GET /index.html HTTP/1.1");
//        list.add("Host: 127.0.0.1");
//        list.add("Connection: keep-alive");
//        list.add("User-Agent: Mozilla/5.0");
//        list.add("Accept: text/html");
//        list.add("Accept-Language: en-US");
//        return list;
//    }

    public static List<String> getImageRequest() {
        List<String> list = new ArrayList<>();
        list.add("GET /images/idge/imported/article/jvw/2006/02/jw-0213-funandgames2-100155939-orig.gif HTTP/1.1");
        list.add("Host: images.techhive.com");
        list.add("Connection: close");
        list.add("User-Agent: Mozilla/5.0");
        return list;
    }
}
