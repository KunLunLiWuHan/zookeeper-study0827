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

public class ZKSet {
    String IP = "192.168.10.129:2181";
    ZooKeeper zookeeper;
    
    @Before
    public void before() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        zookeeper = new ZooKeeper(IP, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected)
                {
                    System.out.println("连接创建成功!");
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
    }

    @After
    public void after() throws Exception {
        zookeeper.close();
    }

    @Test
    public void set1() throws Exception {
        // arg1:节点的路径
        // arg2:节点修改的数据
        // arg3:版本号 -1代表版本号不作为修改条件，版本号不匹配时会报错
        Stat stat=zookeeper.setData("/set/node1","node11".getBytes(),2);
        // 节点的版本号
        System.out.println(stat.getVersion());
        // 节点的创建时间
        System.out.println(stat.getCtime());
    }

    @Test
    public void set2() throws Exception {
        // 异步方式修改节点
        zookeeper.setData("/set/node2", "node21".getBytes(), -1, new
                AsyncCallback.StatCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx, Stat stat) {
                        // 0 代表修改成功
                        System.out.println(rc);
                        // 修改节点的路径
                        System.out.println(path);
                        // 上下文的参数对象
                        System.out.println(ctx);
                        // 属性信息
                        System.out.println(stat.getVersion());
                    }
                },"I am Context");
        Thread.sleep(50000);
        System.out.println("结束");
    }
}
