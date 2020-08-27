package com.xiaolun.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKExists {
    String IP = "192.168.10.129:2181";
    ZooKeeper zooKeeper;

    @Before
    public void before() throws Exception {
        System.out.println("before");

        CountDownLatch countDownLatch = new CountDownLatch(1);
        zooKeeper = new ZooKeeper(IP, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接创建成功!");
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
    }

    @After
    public void after() throws Exception {
        System.out.println("after");
        zooKeeper.close();
    }

    @Test
    public void exists1() throws Exception {
        // arg1:节点的路径
        Stat stat = zooKeeper.exists("/exists1", false);
        System.out.println(stat.getVersion());
    }

    @Test
    public void exists2() throws Exception {
        zooKeeper.exists("/exists1", false, new
                AsyncCallback.StatCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx,
                                              Stat stat) {
                        // 0 判断成功
                        System.out.println(rc);
                        // 路径
                        System.out.println(path);
                        // 上下文参数
                        System.out.println(ctx);
                        // null 节点不存在
                        System.out.println(stat.getVersion());
                    }
                }, "I am Context");
        Thread.sleep(5000);
        System.out.println("结束");
    }
}

