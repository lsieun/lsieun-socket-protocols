package lsieun.socks;

import lsieun.socks.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TransferThread extends Thread {
    private final InputStream in;
    private final OutputStream out;

    public TransferThread(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            IOUtils.copy(in, out, true);
        }
        catch (IOException ex) {
            // do nothing
            // 如果真出了什么错误，除了关闭InputStream和OutputStream，其他的也不知道做什么
        }
        finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
}
