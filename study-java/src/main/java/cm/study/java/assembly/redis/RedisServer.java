package cm.study.java.assembly.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个SocketServer实例
 * 接收客户端命令, 解析成CacheStore里的对应方法及参数
 */
public class RedisServer {

    private static Logger ILOG = LoggerFactory.getLogger(RedisServer.class);

    // 缓存存储对象
    private CacheStore store = new CacheStore();

    private ExecutorService connections = Executors.newFixedThreadPool(16); // 最大连接数
    private ExecutorService work = Executors.newFixedThreadPool(1); // 工作线程池

    private ServerSocket serverSocket;

    public void start() {
        try {
            serverSocket = new ServerSocket(6379); // 实际中是读取redis.conf配置文件
            ILOG.info("start redis server on 6379 success");

            while (true) { // 监听所有客户端的连接请求
                try {
                    Socket client = serverSocket.accept(); // 进来一个客户端
                    handleClient(client);

                } catch (Exception e) {
                    ILOG.error("client transfer fail", e);
                }
            }

        } catch (Exception e) {
            ILOG.error("start redis server error", e);
        }
    }

    public void stop() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                ILOG.error("stop redis server success");
            } catch (Exception e) {
                ILOG.error("stop redis server error", e);
            }
        }
    }

    public void handleClient(Socket client) {
        // 每一个客户端连接起一个线程
        connections.submit(() -> {
            ILOG.warn("connection one client: {}", client);

            try {
                InputStream inputStream = client.getInputStream();
                OutputStream outputStream = client.getOutputStream();

                byte[] bytes = new byte[1024];
                int len = 0;

                while ((len = inputStream.read(bytes)) != -1) {
                    String input = new String(bytes, 0, len, "UTF-8");

                    // 执行命令, 并返回结果
                    String[] cmdInfo = StringUtils.split(input);
                    if (cmdInfo.length < 1) {
                        return;
                    }

                    // 处理每个请求的命令
                    cmdProcess(cmdInfo, outputStream);
                }

                // 关闭客户端
                inputStream.close();
                outputStream.close();
                client.close();

                ILOG.warn("disconnection with client: {}", client);

            } catch (Exception e) {
                ILOG.error("cmdProcess client command error", e);
            }

        });
    }

    public void cmdProcess(String[] cmdInfo, OutputStream outputStream) {
        // 所有的命令处理使用工作线程池
        work.submit(() -> {
            String cmdName = cmdInfo[0];
            ILOG.info("Received client command: {}", StringUtils.join(cmdInfo, " "));

            try {
                Object ret = CmdInvoker.call(cmdInfo, store);
                String output = String.format("Output: %s\n-----\n", ret);
                outputStream.write(output.getBytes("UTF-8"));
                outputStream.flush();

            } catch (Exception e) {
                ILOG.error("cmdProcess client command error, {}", cmdName, e);
            }
        });
    }

    public static void main(String[] args) {
        new RedisServer().start();
    }
}
