package lsieun.socks;

import lsieun.socks.utils.SocksConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataRelay_MultiThread implements DataRelay {
    @Override
    public void transfer(InputStream src_in, OutputStream src_out,
                         InputStream dest_in, OutputStream dest_out)
            throws IOException {

        Thread t1 = new TransferThread(src_in, dest_out);
        t1.start();

        Thread t2 = new TransferThread(dest_in, src_out);
        t2.start();

        StringBuilder sb = new StringBuilder();
        sb.append("Current Thread: " + Thread.currentThread().getId() + SocksConst.EOL)
                .append("t1: " + t1.getId() + SocksConst.EOL)
                .append("t2: " + t2.getId() + SocksConst.EOL);
        System.out.println(sb);

        wait(t1);
        wait(t2);
    }

    public void wait(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
