package com.xiaolun.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZKCreate {
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
    public void create1() throws Exception {
        System.out.println("create1");

        /**
         * arg1:节点的路径
         * arg2:节点的数据
         * arg3:权限列表 world:anyone:cdrwa
         * arg4:节点类型 持久化节点
         */
        zooKeeper.create("/create/node1", "node1".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void create2() throws Exception {
        // Ids.READ_ACL_UNSAFE world:anyone:r
        zooKeeper.create("/create/node2", "node2".getBytes(),
                ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test //自定义权限
    public void create3() throws Exception {
        // world授权模式
        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("world", "anyone");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        acls.add(new ACL(ZooDefs.Perms.WRITE, id));
        zooKeeper.create("/create/node3", "node3".getBytes(), acls,
                CreateMode.PERSISTENT);
    }

    @Test
    public void create4() throws Exception {
        // ip授权模式
        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("ip", "192.168.10.129");
        // 权限设置（所有权限）
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("/create/node4-1", "node4-1".getBytes(), acls,
                CreateMode.PERSISTENT);
    }

    @Test
    public void create5() throws Exception {
        // auth授权模式（用户名和密码）
        // 添加授权用户
        zooKeeper.addAuthInfo("digest", "itcast:123456".getBytes());
        zooKeeper.create("/create/node5", "node5".getBytes(),
                ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
    }

    @Test
    public void create6() throws Exception {
        // auth授权模式
        // 添加授权用户
        zooKeeper.addAuthInfo("digest", "itcast:123456".getBytes());
        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("auth", "itcast");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        zooKeeper.create("/create/node6", "node6".getBytes(), acls,
                CreateMode.PERSISTENT);
    }
    @Test
    public void create7() throws Exception {
        // digest授权模式
        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("digest", "itheima:qlzQzCLKhBROghkooLvb+Mlwv4A=");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("/create/node7", "node7".getBytes(), acls,
                CreateMode.PERSISTENT);
    }

    @Test
    public void create8() throws Exception {
        // 持久化有序节点
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        String result = zooKeeper.create("/create/node8",
                "node8".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);
        ///create/node80000000007
        System.out.println(result);
    }

    @Test
    public void create11() throws Exception {
        // 异步方式创建节点
        zooKeeper.create("/create/node11", "node11".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new
                        AsyncCallback.StringCallback() {
                            @Override
                            public void processResult(int rc, String path, Object ctx, String name) {
                                // 0 代表创建成功
                                System.out.println(rc);
                                // 节点的路径 /create/node11
                                System.out.println(path);
                                // 节点的名字 /create/node11
                                System.out.println(name);
                                // 上下文参数
                                System.out.println(ctx);
                            }
                        }, "I am context"); //这里的数据会传到参数中
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
