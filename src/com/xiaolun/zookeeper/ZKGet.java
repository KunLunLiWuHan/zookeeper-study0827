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

public class ZKGet {
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
        // arg3:读取节点属性的对象
        Stat stat=new Stat();
        byte [] bys=zooKeeper.getData("/get/node1",false,stat);
        // 打印数据
        System.out.println(new String(bys));
        // 版本信息
        System.out.println(stat.getVersion());
    }

    @Test
    public void get2() throws Exception {
        //异步方式
        zooKeeper.getData("/get/node1", false, new
                AsyncCallback.DataCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx,
                                              byte[] data, Stat stat) {
                        // 0代表读取成功
                        System.out.println(rc);
                        // 节点的路径
                        System.out.println(path);
                        // 上下文参数对象
                        System.out.println(ctx);
                        // 数据
                        System.out.println(new String(data));
                        // 属性对象
                        System.out.println(stat.getVersion());
                    }
                },"I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}

