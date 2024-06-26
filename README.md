# Apache Zookeeper

> This is a tutorial course covering Apache Zookeeper.

Tools used:

- JDK 8
- Maven
- JUnit 5, Mockito
- IntelliJ IDE

## Table of contents

1. [Distributed Systems and Cluster Coordination](https://github.com/backstreetbrogrammer/45_ApacheZookeeper?tab=readme-ov-file#chapter-01-distributed-systems-and-cluster-coordination)

- [Distributed Systems](https://github.com/backstreetbrogrammer/45_ApacheZookeeper?tab=readme-ov-file#distributed-systems)
- [Scalability](https://github.com/backstreetbrogrammer/45_ApacheZookeeper?tab=readme-ov-file#scalability)
- [Cluster Coordination](https://github.com/backstreetbrogrammer/45_ApacheZookeeper?tab=readme-ov-file#cluster-coordination)

2. [Zookeeper Installation and Setup](https://github.com/backstreetbrogrammer/45_ApacheZookeeper?tab=readme-ov-file#chapter-02-zookeeper-installation-and-setup)
3. [Zookeeper Java API and Leader Election](https://github.com/backstreetbrogrammer/45_ApacheZookeeper?tab=readme-ov-file#chapter-03-zookeeper-java-api-and-leader-election)
4. [Zookeeper Watchers and Leader Reelection](https://github.com/backstreetbrogrammer/45_ApacheZookeeper?tab=readme-ov-file#chapter-03-zookeeper-watchers-and-leader-reelection)

---

## Chapter 01. Distributed Systems and Cluster Coordination

### Distributed Systems

A distributed system is a collection of computers that work together to form a **single computer** for the end-user.

All these distributed machines have **one shared state** and operate **concurrently**.

Features of a distributed system:

- **Fault tolerance**: Distributed systems components or nodes are able to **fail independently** without damaging the
  whole system
- Have a **shared network** to connect its components, which could be connected using an IP address or even physical
  cables
- **Scaling**: Distributed systems allow us to scale **horizontally,** so we can account for more traffic
- **Parallelism**: Distributed systems can be designed for parallelism, where multiple processors divide up a complex
  problem into pieces
- **Low latency**: Users can have a node in multiple locations, so traffic will hit the closet node
- **Efficiency**: Distributed systems break complex data into smaller pieces
- **Cost-effective**: The initial cost is higher than a traditional system, but because of their scalability, they
  quickly become more cost-effective

### Scalability

**Scalability** is the biggest benefit of distributed systems.

**Vertical scaling**

It is defined as the process of increasing the capacity of a single machine by adding more resources such as memory,
storage, etc. to increase the throughput of the system. No new resource is added, rather the capability of the existing
resources is made more efficient.

Example: `MySQL`

![Vertical Scaling](VerticalScaling.PNG)

However, there is still a single point of failure. Also, there is a limit to the capacity of a single machine.

**Horizontal scaling**

It is defined as the process of adding more instances of the same type to the existing pool of resources and not
increasing the capacity of existing resources like in vertical scaling. This kind of scaling also helps in decreasing
the load on the server.

![Horizontal Scaling](HorizontalScaling.PNG)

In this process, the number of servers is increased and not the individual capacity of the server. This is done with the
help of a **Load Balancer** which basically routes the user requests to different servers according to the availability
of the server. Thereby, increasing the overall performance of the system. In this way, the entire process is distributed
among all servers rather than just depending on a single server.

Example: NoSQLs like `Cassandra` and `MongoDB`

![Load Balancer](LoadBalancer.PNG)

All the nodes are "**stateless**" meaning that each node doesn't maintain the state of client requests. In other words,
any node can serve client request without maintaining the state => however, the database can store all the client's
request and service states.

So we should choose vertical scaling or horizontal scaling based on the system architecture (big or small), user base,
web traffic expected, etc.

![Vertical Scaling Vs Horizontal Scaling](VerticalScalingVsHorizontalScaling.PNG)

### Cluster Coordination

**_Terminology_**

- **Node**: Process running on a dedicated machine
- **Cluster**: Collection of computers / nodes connected to each other working on the same task and typically are
  running the same code
- **Coordination service**: A centralized service that helps in coordination between nodes and maintaining configuration
  information, distributed synchronization and providing group services
- **Zookeeper**: Using a coordination service like Apache Zookeeper, individual nodes can exchange information, and
  run higher level algorithms to work together as a logical cluster

When the same task is given to the cluster, it's challenging to decide and distribute the task across all the nodes.

We should have an algorithm to _automatically_ assign a node as `master node` or `leader` which will distribute the task
across the other nodes.

If the leader node goes down for whatever reason, the algorithm should automatically assign a new leader node until the
previous leader node gets up and gets reassigned as leader.

By default, each node knows only about itself => **Service registry** and **discovery** is required.

Also, **failure detection** mechanism is necessary to trigger automatic leader re-election in a cluster.

**_Apache Zookeeper_**

Zookeeper is a high-performance coordination service designed specifically for distributed systems.

**Key features:**

- Zookeeper is a distributed system itself that provides us with high availability and reliability
- Typically runs in a cluster of an odd number of nodes, higher than 3
- Uses redundancy to allow failures and stay functional
- Nodes communicate with each other using Zookeeper cluster

**Zookeeper Data Model**

ZooKeeper has a hierarchical **name space**, much like a distributed file system.

The only difference is that each **node** in the **namespace** can have data associated with it as well as **children**.

It is like having a file system that allows a **file** to also be a **directory**.

**Paths** to nodes are always expressed as canonical, absolute, slash-separated paths; there are no relative references.

**Znodes**

Every node in a ZooKeeper tree is referred to as a `znode`.

Znodes maintain a **stat structure** that includes **version numbers** for data changes and also has **timestamps**.

The **version number**, together with the **timestamp**, allows ZooKeeper to validate the cache and to coordinate
updates.

Each time a znode's data changes, the version number increases.

For instance, whenever a client **retrieves** data, it also receives the **version** of the data.

And when a client performs an **update** or **delete**, it must supply the **version** of the data of the znode it is
changing.

If the version it supplies doesn't match the actual version of the data, the update will **fail**.

**Key features of Znodes:**

- **Watches**: Clients can set watches on znodes. Changes to that znode trigger the watch and then clear the watch. When
  a watch triggers, ZooKeeper sends the client a notification.
- **Data Access**: The data stored at each znode in a namespace is read and written atomically. Reads get all the data
  bytes associated with a znode and a write replaces all the data. Each node has an **Access Control List (ACL)** that
  restricts who can do what.

**Two types of Znodes:**

- **Persistent** - persists between sessions
- **Ephemeral** - deleted when the session ends

**Leader Election Algorithm**

- Every node that connects to Zookeeper volunteers to become the leader by adding itself as a znode `children` of the
  `/election` parent znode
- Zookeeper maintains an ascending order or sequence number for each of the znode `children` based on connection time
- The znode children with the lowest sequence number will automatically become a leader as all the other znodes
  connected later will know that there are znodes already connected before. Only the first connected znode will not see
  any other znodes and thus will assign itself as leader.
- Similarly, whichever znode is currently assigned the lowest sequence number will automatically become the leader
  in case a previous leader goes down.

---

## Chapter 02. Zookeeper Installation and Setup

**_Installation_**

- Navigate to Zookeeper release page: https://zookeeper.apache.org/releases.html
- Download the latest stable version: **Apache ZooKeeper 3.8.3 (latest stable release)**

https://dlcdn.apache.org/zookeeper/zookeeper-3.8.3/apache-zookeeper-3.8.3-bin.tar.gz

- Extract using `7-Zip` to local folder and set the system variable path

```
APACHE_ZOOKEEPER=C:\Users\{username}\Downloads\softwares\apache-zookeeper-3.8.3-bin
PATH=%APACHE_ZOOKEEPER%\bin
```

- Create a new `logs` folder in Apache Zookeeper home dir:

```
cd "%APACHE_ZOOKEEPER%"
mkdir logs
```

- Navigate to Apache Zookeeper conf directory and rename `zoo_sample.cfg` to `zoo.cfg`

```
cd "%APACHE_ZOOKEEPER%\conf"
mv zoo_sample.cfg zoo.cfg
```

- Make following changes in `zoo.cfg`:

```
dataDir=dataDir=C:\\Users\\{username}\\Downloads\\softwares\\apache-zookeeper-3.8.3-bin\\logs
```

**_Verify installation_**

- Open Git Bash
- Zookeeper **start**, **status** and **stop** commands:

```
$ zkServer.sh start
ZooKeeper JMX enabled by default
Using config: C:\Users\rishi\Downloads\softwares\apache-zookeeper-3.8.3-bin\conf\zoo.cfg
Starting zookeeper ... STARTED

$ zkServer.sh status
ZooKeeper JMX enabled by default
Using config: C:\Users\rishi\Downloads\softwares\apache-zookeeper-3.8.3-bin\conf\zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Mode: standalone

$ zkServer.sh stop
ZooKeeper JMX enabled by default
Using config: C:\Users\rishi\Downloads\softwares\apache-zookeeper-3.8.3-bin\conf\zoo.cfg
Stopping zookeeper ... STOPPED
```

- `logs` folder will have entries after the above commands

**_Zookeeper Client_**

- Zookeeper client can be launched using the following command: `zkCli.sh`

```
# Start server
zkServer.sh start

# Start client
zkCli.sh

# following prompt should appear to run client commands
[zk: localhost:2181(CONNECTED) 0]
```

- Some useful client commands to know:

```
help
ls / => total znodes available
create /parent "some parent data" => create parent znode
create /parent/child "some child data" => create child znode
set /parent/child "got new data" => data changed
ls /parent
get /parent => info about parent znode
delete /parent
deleteall /parent
create /election ""
quit
```

As seen above, we need to create `/election` znode to implement our leader election algorithm.

**_IntelliJ Project Setup_**

- Create a new `Java 8` project as a `Maven` project
- Add following dependency in `pom.xml`

```
<!-- https://mvnrepository.com/artifact/org.apache.zookeeper/zookeeper -->
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.8.3</version>
</dependency>
```

- Add `maven-assembly-plugin` in pom.xml:

```
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.6.0</version>
                <configuration>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id> <!-- this is used for inheritance merges -->
                        <phase>package</phase> <!-- bind to the packaging phase -->
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

- Create a file `log4j.properties` in `resources` folder with content as:

```
log4j.rootLogger=WARN, zookeeper
log4j.appender.zookeeper=org.apache.log4j.ConsoleAppender
log4j.appender.zookeeper.Target=System.out
log4j.appender.zookeeper.layout=org.apache.log4j.PatternLayout
log4j.appender.zookeeper.layout.ConversionPattern=%d{HH:mm:ss} %-5p %c{1}:%L - %m%n
```

- Create a folder `scripts` and copy the start and stop scripts provided
- Create a folder `log` for all the logs

---

## Chapter 03. Zookeeper Java API and Leader Election

**_Threading model_**

- Application start code in the `main` method is executed on the **main** thread
- When **Zookeeper** object is created, two additional threads are created:
    - IO thread
    - Event thread

**IO Thread**

- handles all the network communication with Zookeeper servers
- handles Zookeeper requests and responses
- responds to pings
- session management
- session timeouts, etc.

**Event Thread**

- manages Zookeeper events
    - connection (`KeeperState.SyncConnected`)
    - disconnection (`KeeperState.Disconnected`)
- custom znode **Watchers** and **Triggers** to subscribe to
- Events are executed on Event Thread **in order**

**_Zookeeper API Introduction_**

Basic `ZookeeperAPIDemo` class to just introduce Zookeeper API:

```java
package com.backstreetbrogrammer.leader.election;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZookeeperAPIDemo implements Watcher {
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private ZooKeeper zooKeeper;

    public static void main(final String[] arg) throws IOException, InterruptedException {
        final ZookeeperAPIDemo zookeeperAPIDemo = new ZookeeperAPIDemo();

        zookeeperAPIDemo.connectToZookeeper();
        zookeeperAPIDemo.run();
        zookeeperAPIDemo.close();
        System.out.println("Disconnected from Zookeeper, exiting application");
    }

    public void connectToZookeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
    }

    private void run() throws InterruptedException {
        synchronized (zooKeeper) {
            zooKeeper.wait();
        }
    }

    private void close() throws InterruptedException {
        this.zooKeeper.close();
    }

    @Override
    public void process(final WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("Successfully connected to Zookeeper");
                } else {
                    synchronized (zooKeeper) {
                        System.out.println("Disconnected from Zookeeper event");
                        zooKeeper.notifyAll();
                    }
                }
                break;
            default:
                // do nothing
        }
    }
}

```

**Program details:**

- when the Zookeeper **client** connects to Zookeeper **server** - as its all asynchronous and event driven, server will
  respond to the events in the separate **event threads**
- thus, **client** needs to implement `Watcher` and use **event handlers** to handle `WatchedEvent` in overridden
  `process()` method for successful connection
- server sends event of type `None` and state as `Event.KeeperState.SyncConnected`
- server also keeps on sending `ping` to check if the client is alive and connected
- client maintains a background **IO thread** that has to send and respond to pings to and from the server
- if the server is down after successful connection, the event state will change and then the client can close the
  connection

**_Start and Stop the program_**

- Open **Git Bash** to the root of the project and run: `mvn clean install`
- Start Zookeeper server: `zkServer.sh start`
- Start `ZookeeperAPIDemo` by running `runZookeeperAPIDemo.bat`
- Output logs are present in `log\ZookeeperAPIDemo.log`
- Stop `ZookeeperAPIDemo` by running `stopZookeeperAPIDemo.bat`
- Stop Zookeeper server: `zkServer.sh stop`

**_Leader Election Algorithm Code Demo_**

`LeaderElection` class:

```java
package com.backstreetbrogrammer.leader.election;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LeaderElection implements Watcher {
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String ELECTION_NAMESPACE = "/election";
    private ZooKeeper zooKeeper;
    private String currentZnodeName;

    public static void main(final String[] arg) throws IOException, InterruptedException, KeeperException {
        final LeaderElection leaderElection = new LeaderElection();

        leaderElection.connectToZookeeper();
        leaderElection.volunteerForLeadership();
        leaderElection.electLeader();
        leaderElection.run();
        leaderElection.close();

        System.out.println("Disconnected from Zookeeper, exiting application");
    }

    public void volunteerForLeadership() throws KeeperException, InterruptedException {
        final String znodePrefix = String.format("%s/c_", ELECTION_NAMESPACE);
        final String znodeFullPath = zooKeeper.create(znodePrefix, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.printf("znode name %s%n", znodeFullPath);
        this.currentZnodeName = znodeFullPath.replace("/election/", "");
    }

    public void electLeader() throws KeeperException, InterruptedException {
        final List<String> children = zooKeeper.getChildren(ELECTION_NAMESPACE, false);

        Collections.sort(children);
        final String smallestChild = children.get(0);

        if (smallestChild.equals(currentZnodeName)) {
            System.out.println("I am the leader");
            return;
        }

        System.out.printf("I am not the leader, %s is the leader%n", smallestChild);
    }

    public void connectToZookeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
    }

    private void run() throws InterruptedException {
        synchronized (zooKeeper) {
            zooKeeper.wait();
        }
    }

    private void close() throws InterruptedException {
        this.zooKeeper.close();
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
            default:
                // do nothing
        }
    }
}
```

**_Start and Stop the program_**

- Open **Git Bash** to the root of the project and run: `mvn clean install`
- Start Zookeeper server: `zkServer.sh start`
- Start `LeaderElection` 4 instances in parallel by running `runLeaderElection4instances.bat`
- We will observe that node instance with the lowest sequence number is the leader and all the other node instances are
  follower
- Stop `LeaderElection` by running `stopLeaderElection.bat`
- Stop Zookeeper server: `zkServer.sh stop`

---

## Chapter 04. Zookeeper Watchers and Leader Reelection

**_Zookeeper Watchers_**

> A watch event is a one-time trigger, sent to the client that sets the watch, which occurs when the data for which the
> watch was set **changes**.

All the read operations in ZooKeeper - `exists()`, `getData()`, and `getChildren()` - have the option of setting a watch
as a side effect. The following list details the events that a watch can trigger and the calls that enable them:

- **Created event**: Enabled with a call to `exists()`.
- **Deleted event**: Enabled with a call to `exists()`, `getData()`, and `getChildren()`.
- **Changed event**: Enabled with a call to `exists()` and `getData()`.
- **Child event**: Enabled with a call to `getChildren()`.

There are three key points to consider in this definition of a watch:

- **One-time trigger**: One watch event will be sent to the client when the data has changed. For example, if a
  client does a `getData("/znode1", true)` and later the data for `/znode1` is changed or deleted, the client will get a
  watch event for `/znode1`. If `/znode1` changes again, no watch event will be sent unless the client has done another
  read that sets a new watch.
- **Sent to the client**: This implies that an event is on the way to the client, but may not reach the client
  before the successful return code to the change operation reaches the client that initiated the change. Watches are
  sent **asynchronously** to watchers. ZooKeeper provides an **ordering guarantee**: a client will never see a change
  for which it has set a watch until it first sees the watch event. Network delays or other factors may cause different
  clients to see watches and return codes from updates at different times. The key point is that everything seen by the
  different clients will have a **consistent order**.
- **The data for which the watch was set**: This refers to the different ways a node can change. It helps to think of
  ZooKeeper as maintaining **two** lists of watches: **data watches** and **child watches**.

`getData()` and `exists()` set **data watches**.

`getChildren()` sets **child watches**.

Alternatively, it may help to think of watches being set according to the kind of data returned.

`getData()` and `exists()` return information about the data of the node, whereas `getChildren()` returns a list of
children. Thus, `setData()` will trigger data watches for the **znode** being set (assuming the set is successful).

A successful `create()` will trigger a data watch for the **znode** being created and a child watch for the parent
**znode**.

A successful `delete()` will trigger both a data watch and a child watch (since there can be no more children) for a
**znode** being deleted as well as a child watch for the **parent znode**.

Watches are maintained locally at the ZooKeeper server to which the client is connected. This allows watches to be
lightweight to set, maintain, and dispatch.

When a client connects to a new server, the watch will be triggered for any session events.

Watches will not be received while disconnected from a server. When a client reconnects, any previously registered
watches will be re-registered and triggered if needed. In general, this all occurs transparently.

There is one case where a watch may be missed: a watch for the existence of a **znode** not yet created will be missed
if the **znode** is created and deleted while disconnected.

**_Code Demo_**

`WatchersDemo` class:

```java
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
```

**_Start and Stop the program_**

- Open **Git Bash** to the root of the project and run: `mvn clean install`
- Start Zookeeper server: `zkServer.sh start`
- Start `WatchersDemo` by running `runWatchersDemo.bat`
- Start a Zookeeper client: `zkCli.sh`
- Run the following commands on client prompt:

```
ls /
create /target_znode "some test data"
set /target_znode "new data"
create /target_znode/child_znode "child data"
deleteall /target_znode
quit
```

- `WatchersDemo` output will show all the events in the logs
- Stop `WatchersDemo` by running `stopWatchersDemo.bat`
- Stop Zookeeper server: `zkServer.sh stop`

**_Leader Reelection Algorithm_**

In order for our system to be fault-tolerant, the Leader Election algorithm needs to be able to recover from failures
and re-elect a new leader automatically.

Each **znode** needs to watch its predecessor **znode**. Thus if the leader node dies and hence its corresponding
**znode**, then only the successor **znode** will get notified making its corresponding node as the new leader.

Similarly, if the deleted node and **znode** were not the leader, then the successor node / **znode** will fill the gap
making its new predecessor, without changing any leadership.

This forms a linked list type of data structure where each node and its **znode** are watching its predecessor
**znode**.

**Code Demo**

`LeaderReelection` class:

```java
package com.backstreetbrogrammer.leader.election;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LeaderReelection implements Watcher {
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String ELECTION_NAMESPACE = "/election";
    private ZooKeeper zooKeeper;
    private String currentZnodeName;

    public static void main(final String[] args) throws IOException, InterruptedException, KeeperException {
        final LeaderReelection leaderReelection = new LeaderReelection();

        leaderReelection.connectToZookeeper();
        leaderReelection.volunteerForLeadership();
        leaderReelection.reelectLeader();
        leaderReelection.run();
        leaderReelection.close();
        System.out.println("Disconnected from Zookeeper, exiting application");
    }

    public void volunteerForLeadership() throws KeeperException, InterruptedException {
        final String znodePrefix = String.format("%s/c_", ELECTION_NAMESPACE);
        final String znodeFullPath = zooKeeper.create(znodePrefix, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.printf("znode name %s%n", znodeFullPath);
        this.currentZnodeName = znodeFullPath.replace(ELECTION_NAMESPACE + "/", "");
    }

    public void reelectLeader() throws KeeperException, InterruptedException {
        Stat predecessorStat = null;
        String predecessorZnodeName = "";
        while (predecessorStat == null) {
            final List<String> children = zooKeeper.getChildren(ELECTION_NAMESPACE, false);

            Collections.sort(children);
            final String smallestChild = children.get(0);

            if (smallestChild.equals(currentZnodeName)) {
                System.out.println("I am the leader");
                return;
            } else {
                System.out.println("I am not the leader");
                final int predecessorIndex = Collections.binarySearch(children, currentZnodeName) - 1;
                predecessorZnodeName = children.get(predecessorIndex);
                predecessorStat = zooKeeper.exists(ELECTION_NAMESPACE + "/" + predecessorZnodeName, this);
            }
        }

        System.out.printf("Watching znode %s%n", predecessorZnodeName);
        System.out.println();
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
                try {
                    reelectLeader();
                } catch (final InterruptedException | KeeperException e) {
                    System.err.println(e.getMessage());
                }
                break;
            default:
                // do nothing
        }
    }
}
```

**_Start and Stop the program_**

- Open **Git Bash** to the root of the project and run: `mvn clean install`
- Start Zookeeper server: `zkServer.sh start`
- Start `LeaderReelection` 4 instances in parallel by running `runLeaderReelection4instances.bat`
- We will observe that node instance with the lowest sequence number is the leader and all the other node instances are
  follower
- As soon as we terminate the leader node, the next successor node becomes the new leader automatically
- Stop `LeaderReelection` by running `stopLeaderReelection.bat`
- Stop Zookeeper server: `zkServer.sh stop`

