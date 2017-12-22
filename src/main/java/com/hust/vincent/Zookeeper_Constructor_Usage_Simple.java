package com.hust.vincent;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
public class Zookeeper_Constructor_Usage_Simple implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
       //@Override
    public void  process(WatchedEvent event){
        System.out.println("Receive watched event : " + event);
        if (KeeperState.SyncConnected == event.getState()){
            connectedSemaphore.countDown();
        }
    }
    public static void main(String[] args) throws Exception{
        ZooKeeper zookeeper = new ZooKeeper("192.168.189.158:2181",5000,new Zookeeper_Constructor_Usage_Simple());
        System.out.println(zookeeper.getState());
        try{
            connectedSemaphore.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Zookeeper session established");
        String path1 = zookeeper.create("/zk-test-ephemeral-","sssss".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Success create znode:" + path1);
        String path2 = zookeeper.create("/zk-test-ephemeral-","ssswww".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Success create znode:" + path2);
    }
}
