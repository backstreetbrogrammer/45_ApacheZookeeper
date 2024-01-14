# Apache Zookeeper

> This is a tutorial course covering Apache Zookeeper.

Tools used:

- JDK 8
- Maven
- JUnit 5, Mockito
- IntelliJ IDE

## Table of contents

1. Distributed Systems and Cluster Coordination
2. Zookeeper Installation and Setup
3. Leader Election
4. Cluster Auto-Healer

---

## Chapter 01. Distributed Systems and Cluster Coordination

**_Distributed Systems_**

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

**_Scalability_**

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

