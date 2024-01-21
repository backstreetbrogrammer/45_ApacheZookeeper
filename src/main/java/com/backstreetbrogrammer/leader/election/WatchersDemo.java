package com.backstreetbrogrammer.leader.election;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class WatchersDemo implements Watcher {
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String TARGET_ZNODE = "/target_znode";
    private ZooKeeper zooKeeper;

    public static void main(final String[] args) throws InterruptedException, IOException, KeeperException {
        final WatchersDemo watchersDemo = new WatchersDemo();
        watchersDemo.connectToZookeeper();
        watchersDemo.watchTargetZnode();
        watchersDemo.run();
        watchersDemo.close();
    }

    public void connectToZookeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
    }

    public void run() throws InterruptedException {
        synchronized (zooKeeper) {
            zooKeeper.wait();
        }
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }

    public void watchTargetZnode() throws KeeperException, InterruptedException {
        final Stat stat = zooKeeper.exists(TARGET_ZNODE, this);
        if (stat == null) {
            return;
        }

        final byte[] data = zooKeeper.getData(TARGET_ZNODE, this, stat);
        final List<String> children = zooKeeper.getChildren(TARGET_ZNODE, this);

        System.out.printf("Data : %s, children : %s%n", new String(data), children);
    }

    @Override
    public void process(final WatchedEvent event) {
        switch (event.getType()) {
            case None:
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("Successfully connected to Zookeeper");
                } else {
                    synchronized (zooKeeper) {
                        System.out.println("Disconnected from Zookeeper event");
                        zooKeeper.notifyAll();
                    }
                }
                break;
            case NodeDeleted:
                System.out.printf("%s was deleted%n", TARGET_ZNODE);
                break;
            case NodeCreated:
                System.out.printf("%s was created%n", TARGET_ZNODE);
                break;
            case NodeDataChanged:
                System.out.printf("%s data changed%n", TARGET_ZNODE);
                break;
            case NodeChildrenChanged:
                System.out.printf("%s children changed%n", TARGET_ZNODE);
                break;
            default:
                // do nothing
        }

        try {
            watchTargetZnode();
        } catch (final KeeperException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
