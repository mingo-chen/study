package cm.study.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

public class FirstDemo {

    public static void main(String[] args) throws Exception {
        RetryPolicy policy = new RetryNTimes(3, 1000);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", policy);
        client.start();

        try {
            String path = "/zk-test/a";
            Stat stat = client.checkExists().forPath(path);
            System.out.println("--> " + stat);
            if(stat != null) {
                client.delete().forPath(path);
            }

            String ret = client.create().forPath(path, "cm".getBytes());
            System.out.println("--> " + ret);

            byte[] data = client.getData().forPath(path);
            System.out.println("--> " + new String(data));

            NodeCache watchNode = new NodeCache(client, path);
            watchNode.start();  // 启动监听

            watchNode.getListenable().addListener(() -> {
                byte[] newestData = client.getData().forPath(path);
                System.out.println("---节点发生变化: " + new String(newestData));
            });

            watchNode.close(); // 停止监听
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("------");
        TimeUnit.MINUTES.sleep(1);
    }

}
