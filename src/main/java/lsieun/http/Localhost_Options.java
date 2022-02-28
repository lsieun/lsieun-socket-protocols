package lsieun.http;

import lsieun.bean.HttpBean;
import lsieun.utils.HttpUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Localhost_Options {
    public static void main(String[] args) {
        try(
                Socket s = new Socket("127.0.0.1", 80);
                InputStream in = s.getInputStream();
                BufferedInputStream bin = new BufferedInputStream(in);
                OutputStream out = s.getOutputStream();
                BufferedOutputStream bout = new BufferedOutputStream(out);
                OutputStreamWriter writer = new OutputStreamWriter(bout)
                ) {
            // first request
            HttpUtils.writeToServer(writer, getHeadRequest());

            // first response
            HttpBean html_bean = new HttpBean(bin);
            System.out.println(html_bean);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getOptionsRequest() {
        List<String> list = new ArrayList<>();
        list.add("OPTIONS / HTTP/1.1");
        list.add("Host: 127.0.0.1");
        list.add("Connection: close");
        list.add("User-Agent: Mozilla/5.0");
        list.add("Accept: text/html");
        list.add("Accept-Language: en-US");
        return list;
    }

    public static List<String> getHeadRequest() {
        List<String> list = new ArrayList<>();
        list.add("HEAD / HTTP/1.1");
        list.add("Host: 127.0.0.1");
        list.add("Connection: close");
        list.add("User-Agent: Mozilla/5.0");
        list.add("Accept: text/html");
        list.add("Accept-Language: en-US");
        return list;
    }

}
