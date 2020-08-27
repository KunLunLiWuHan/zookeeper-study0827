package com.xiaolun.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZKGetChid {
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
    public void get1() throws Exception {
        // arg1:节点的路径
        List<String> list = zooKeeper.getChildren("/get", false);
        for (String str : list) {
            System.out.println(str);
        }
    }

    @Test    // 异步用法
    public void get2() throws Exception {
        zooKeeper.getChildren("/get", false, new
                AsyncCallback.ChildrenCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx, List<String> children) {
                        // 0代表读取成功
                        System.out.println(rc);
                        // 节点的路径
                        System.out.println(path);
                        // 上下文参数对象
                        System.out.println(ctx);
                        // 子节点信息
                        for (String str : children) {
                            System.out.println(str);
                        }
                    }
                },"I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
