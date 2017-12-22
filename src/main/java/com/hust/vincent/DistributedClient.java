package com.hust.vincent;


import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;


public class DistributedClient {
    private static final java.lang.String connectString = "192.168.189.158:2181,192.168.189.160:2181,192.168.189.176:2181";
    private static final int sessionTimeout = 2000;
    private static final java.lang.String parentNode = "/servers/";
    private volatile List<String> serverList;
    private ZooKeeper zk = null;
    public void getConnect() throws Exception{
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    getServerList();
                } catch (Exception e) {
                }
            }
        });
    }




    public void getServerList() throws Exception {
        //获取服务器子节点的信息，并且对父节点进行监听
        List<java.lang.String> children = zk.getChildren(parentNode,true);
        List<String> servers =  new ArrayList<String>();
        for (java.lang.String child:children){
            byte[] data = zk.getData(parentNode + child,false,null);
            servers.add(new String(data));
        }
        serverList=servers;
        System.out.println(serverList);
    }
    public void handBusiness() throws InterruptedException {
        System.out.println("starting working...");
        Thread.sleep(Long.MAX_VALUE);}


    public static void main(String[] args) throws Exception {
        //获取zk链接
        DistributedClient client = new DistributedClient();
        client.getConnect();

        //获取servers的子节点信息，从中获取列表
        client.getServerList();
        client.handBusiness();
    }
}
