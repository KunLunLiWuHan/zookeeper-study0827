package com.xiaolun.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZkConnection {
//    private final static String CONNECT_STRING = "192.168.10.129:2181";
    private final static int SESSION_TIMEOUT = 50*1000;

    public static void main(String[] args) throws Exception {


        //计数器对象（线程同步类）
        CountDownLatch countDownLatch = new CountDownLatch(1);
        /**
         * 1.服务器iP和端口；客户端和服务端之间的会话时间，以毫秒为单位；监视对象
         * 2.
         */
        ZooKeeper zooKeeper = new ZooKeeper("192.168.10.129:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if(event.getState() == Event.KeeperState.SyncConnected){
                    System.out.println("连接创建成功---------");
                    countDownLatch.countDown(); //启动主线程，不让其进行阻塞
                }
            }
        });
        //主线程阻塞等待连接对象创建成功
        countDownLatch.await();
        //会话编号
        System.out.println("会话编号："+zooKeeper.getSessionId());
        zooKeeper.close();
    }
}

