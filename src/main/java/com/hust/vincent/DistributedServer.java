package com.hust.vincent;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class DistributedServer {
    private static final String connectString = "192.168.189.158:2181,192.168.189.160:2181,192.168.189.176:2181";
    private static final int sessionTimeout = 2000;
    private static final String parentNode = "/servers/";
    private ZooKeeper zk = null;
    public void getConnect() throws Exception{
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println(event.getType() + "..." + event.getPath());
                try {
                    zk.getChildren("/", true);
                } catch (Exception e) {
                }
            }
        });
    }
    public void registerServer(String hostname) throws Exception{
        String create = zk.create(parentNode+"server",hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(hostname + "is online..." + create);
    }

    /**
     * 业务功能
     */
    public void handBusiness(String hostname) throws InterruptedException {
        System.out.println(hostname + "starting working...");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception{
        //获取ZK的链接
        DistributedServer server = new DistributedServer();
        server.getConnect();
        //链接并注册服务器的信息
        server.registerServer(args[0]);
        //启动业务功能
        server.handBusiness(args[0]);

    }
}
