package cm.study.java.assembly.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 采用NIO实现多路复用IO
 * 所有的客户端连接及工作线程都是单线程
 */
public class ReactorRedisServer {

    private static Logger ILOG = LoggerFactory.getLogger(ReactorRedisServer.class);

    // 缓存存储对象
    private CacheStore store = new CacheStore();

    private int id = 10000;

    private ServerSocketChannel socketChannel;
    private Selector selector;

    /**
     * 采用多路复用技术提升IO效率
     */
    public void start() {
        try {
            socketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 6379);
            socketChannel.socket().bind(serverAddress);
            socketChannel.configureBlocking(false);
            SelectionKey acceptKey = socketChannel.register(selector, SelectionKey.OP_ACCEPT); // 用来接受客户端连接的selection
            acceptKey.attach(id++);
            ILOG.info("start redis reactor server on 6379 success, {}, id: {}", acceptKey, acceptKey.attachment());

            work();

        } catch (Exception e) {
            ILOG.error("start redis server error", e);
        }
    }

    public void work() throws Exception {
        while (true) {
            selector.select();

            Set<SelectionKey> readSelectionKeys = selector.selectedKeys();
            for (Iterator<SelectionKey> it = readSelectionKeys.iterator(); it.hasNext(); ) {
                SelectionKey selectionKey = it.next();

                if(selectionKey.isAcceptable()) {
                    ILOG.info("selection key: {} acceptable, id: {}", selectionKey, selectionKey.attachment());
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SelectionKey clientKey = serverSocketChannel
                            .accept()
                            .configureBlocking(false)
                            .register(selector,SelectionKey.OP_READ); // 每个新增的客户端连接, 目前都是由客户端主动发起请求, 所以只用监听READ事件
                    clientKey.attach(id++);
                    ILOG.info("accept  complete: {}, id: {}", clientKey, clientKey.attachment());
                }

                if(selectionKey.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
                    try {
                        String input = getClientRequest(clientChannel);
                        if (StringUtils.isNotBlank(input)) {
                            cmdProcess(input, clientChannel);
                        }

                    } catch (RuntimeException e) { // 改成自定义异常比较好
                        clientChannel.close();
                        ILOG.info("connection close: {}, id: {}", selectionKey, selectionKey.attachment());
                        continue;
                    }
                }

                if (selectionKey.isWritable()) {
                    ILOG.info("selection key: {} writeable", selectionKey);
                }

                it.remove();
            }
        }
    }

    String getClientRequest(SocketChannel clientChannel) throws Exception {
        ByteBuffer receiveBuf = ByteBuffer.allocate(4); // 故意弄的很小
        StringBuilder sb = new StringBuilder();

        while(true) {
            int size = clientChannel.read(receiveBuf);
            if(size > 0) {
                String sub = new String(receiveBuf.array(), 0, size, "UTF-8");
                sb.append(sub);
//                ILOG.debug("selection key readable, #{}, #{}, #{}, #{}", StringUtils.trim(sub), size, receiveBuf.position(), receiveBuf.array().length);
                receiveBuf.flip();

            } else if(size == -1) {
                throw new RuntimeException("connection is closed.");

            } else {
                break;
            }
        }

        return sb.toString();
    }

    public void cmdProcess(String clientInput, SocketChannel clientChannel) {
        ILOG.info("Received client command: {}", StringUtils.trim(clientInput));
        String[] cmdInfo = StringUtils.split(clientInput);
        String cmdName = cmdInfo[0];

        try {
            Object ret = CmdInvoker.call(cmdInfo, store);
            byte[] output = String.format("Output: %s\n-----\n", ret).getBytes();
            ByteBuffer response = ByteBuffer.allocate(output.length);
            response.put(output);
            response.flip();
            clientChannel.write(response);

        } catch (Exception e) {
            ILOG.error("cmdProcess client command error, {}", cmdName, e);
        }
    }

    public void stop() {
        try {
            if(socketChannel != null) {
                socketChannel.close();
            }

            if(selector != null) {
                selector.close();
            }
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
//        System.out.println(InetAddress.getByName("0.0.0.0"));
        new ReactorRedisServer().start();
    }
}
