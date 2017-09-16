package cm.study.common.io;

import jdk.management.resource.internal.inst.ServerSocketChannelImplRMHooks;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * Created by chenming on 2017/9/16.
 */
public class FileNIO {

    public static String read(String fileName) {
        StringBuilder sb = new StringBuilder();
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(fileName);
            FileChannel fileChannel = inputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            for(int read = 0; (read = fileChannel.read(buffer)) != -1; ) {
                buffer.flip();
                String text = new String(buffer.array(), 0, read);
                sb.append(text);
                buffer.compact();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close
        }
        return sb.toString();
    }

    public static void SocketServerRead() {
        Selector selector = null;
        ServerSocketChannel socketChannel = null;

        try {
            selector = Selector.open();
            socketChannel = ServerSocketChannel.open();
            socketChannel.socket().bind(new InetSocketAddress(8888));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                if (selector.select() == 0) {
                    continue;
                }

                for(Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext();) {
                    SelectionKey selectionKey = iterator.next();
                    System.out.println("--> " + selectionKey);
                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close
        }
    }
}
