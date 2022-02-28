package lsieun.socks;

import lsieun.socks.utils.ByteUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static lsieun.socks.utils.SocksConst.*;

/**
 * NOTE: 这里的实现是不对的
 */
public class DataRelay_SingleThread implements DataRelay {
    @Override
    public void transfer(InputStream src_in, OutputStream src_out, InputStream dest_in, OutputStream dest_out) throws IOException {
        System.out.println("from source to destination");
        transfer(src_in, dest_out);
        // 无法执行到下面
        System.out.println("from destination to source");
        transfer(dest_in, src_out);
    }

    public void transfer(InputStream in, OutputStream out) {
        try {
            byte[] buff = new byte[BUFFER_SIZE];
            // 错误的根源：in.read(buff)会造成阻塞
            for (int len = in.read(buff); len != -1; len = in.read(buff)) {
                printData(buff, 0, len);

                out.write(buff, 0, len);
                out.flush();
            }
        }catch (IOException ex) {
            // do nothing
            // ex.printStackTrace();
        }
    }

    public static void printData(byte[] bytes, int from, int length) {
        System.out.println("transfer bytes: " + length);
        String transfer_data = ByteUtils.toStr(bytes, from, length);
        System.out.println(transfer_data);
    }

    public void transfer2(InputStream local_in, OutputStream local_out,
                      InputStream remote_in, OutputStream remote_out
    ) throws IOException {
        byte[] buff = new byte[1024 * 128];
        boolean local_data_over = false;
        boolean remote_data_over = false;
        while (true) {
            if (!local_data_over) {
                int byteRead = local_in.read(buff);
                if (byteRead == -1) {
                    local_data_over = true;
                } else {
                    remote_out.write(buff, 0, byteRead);
                }
            }

            if (!remote_data_over) {
                int byteRead = remote_in.read(buff);
                if (byteRead == -1) {
                    remote_data_over = true;
                } else {
                    local_out.write(buff, 0, byteRead);
                }
            }

            if (local_data_over && remote_data_over) {
                break;
            }
        }
    }
}
