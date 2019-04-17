![](https://i.postimg.cc/2jWJg4pj/1.png)
# 第三章 分布式文件系统HDFS
## 3.1 分布式文件系统
分布式文件系统一般采用“客户机/服务器（Client/Server）”模式，客户端以特定的通信协议通过网络与服务器建立连接，提出文件访问请求，客户端和服务器可以通过设置访问权来限制请求方对底层数据块的访问。

### 3.1.1 计算机集群结构
普通的文件系统只需单个计算机节点，包括处理器、内存、高速缓存和本地磁盘。  
分布式文件系统把文件分布存储到多个节点上，与多处理器高级硬件相反，分布式多个利用廉价的普通硬件工作。  
如图，集群中的计算机节点存放在机架中（Rack），每个机架存放8-64个节点，同一个机架上不同节点通过网络互连（采用吉比特以太网），多个不同机架之间采用另一级网络或交换机互连。

![](https://i.postimg.cc/cCJvtsGX/3-1.png)

### 3.1.2 分布式文件系统的结构
在Windows、Linux等操作系统中，文件系统一般把磁盘划分为每512字节一组（“磁盘块”），这是文件读写的最小单位。文件系统的块（“Block”）通常是磁盘块的整数倍。  
分布式文件系统也采取了块的概念，文件被分成了若干块进行存储，块是读写的基本单元。比如HDFS采取64MB的默认块。与普通文件不同的是，**如果一个文件小于默认数据块大小，它并不占用整个存储空间。**
![](https://i.postimg.cc/7ZLyvfMB/3-2.png)
如图，计算机集群中多个节点构成分布式文件系统，其分为两类，一类叫“主节点”（Master Node），或“名称结点”（NameNode）；另一类叫“从节点”（Slave Node），或“数据节点”（DataNode）。
- 名称结点：负责文件和目录的创建、删除和重命名，同时管理数据节点和文件块的映射关系。客户端只有访问名称结点才能找到请求的文件快所在的位置，进而读取所需文件。
- 数据节点：负责数据的存储和读取。存储时由名称结点分配存储位置，然后客户端写入相应的数据节点；读取时从名称结点获得数据节点和文件块的映射关系，然后就访问相应数据节点。
- 同时，数据节点要根据名称结点的命令创建、删除数据块和冗余复制。

### 3.1.3 分布式文件系统的设计需求
![](https://i.postimg.cc/j5cXrdhX/3-1.png)

## 3.2 HDFS简介
![](https://i.postimg.cc/nhf075Mq/3-3.png)
对于低延时的应用程序，可以采用HBase；NameNode存储元数据，小文件过多会影响性能，且MapReduce同理；只允许一个写入者，且只允许追加操作，不允许随机读写。

## 3.3 HDFS的相关概念
### 3.3.1 块
HDFS采用“块”的概念，以块为单位读写数据，可以把磁盘寻道时间分摊到大量数据中，从而最小化寻址开销。HDFS寻址开销不止磁盘寻道开销，还包括数据块的定位开销。（获取数据块的位置列表，然后获取Linux系统实际文件）。
这样做的好处如下：
- 支持大规模文件存储。大文件会被分割，不受单个节点的影响。
- 简化系统设计：简化存储管理；方便元数据管理（不需要和文件块一起存储）
- 适合数据备份：冗余备份提高容错性和可用性

### 3.3.2 名称结点和数据节点
![](https://i.postimg.cc/zGq26658/3-3-1.png)
![](https://i.postimg.cc/kgwYmsX8/3-3-2.png)
HDFS中，名称节点（NameNode）负责管理分布式文件系统的命名空间（Namespace），其保存两个核心结构（如图）——FsImage和EditLog。
1. FsImage用于维护文件系统树以及文件树中所有的文件和文件夹的元数据（inode序列化形式）。
2. EditLog记录所有针对文件的创建、删除、重命名操作。  

名称节点记录了每个文件夹中各个块的位置信息，但并没有记录块存储在哪个数据节点，也就是不是持久化存储（保存在内存而非硬盘），而是**每次系统启动时扫描所有数据节点重构得到这些信息，此后也会定期执行此操作**

名称节点启动时，会将FsImage的内容加载到内存中，然后执行EditLog文件中的各项操作，使内存中的元数据保持最新。这个操作完成后，会创建一个新的FsImage文件和空的Editlog。HDFS对文件的各项操作不直接写入FsImage，是因为FsImage文件一般很大，如果直接读写性能得不到保障，但EditLog相反。名称节点在启动时处于“安全模式”，只能对外提供读操作，不能写。启动结束后才能。

数据节点（DataNode）负责数据的存储和读取，会根据客户端或名称节点的调度来进行数据的存储和检索，并且**定期向NameNode发送自己所存储块的列表**。每个块的数据会被保存在节点本地的Linux文件系统中。

### 3.3.3 第二名称节点
在名称节点运行期间，HDFS会不断发生更新操作，写入EditLog。当名称节点重启时，内存加载FsImage，然后逐条执行EditLog，如果EditLog过大，该过程会变得非常缓慢，而上文说过，“安全模式”不能写，影响使用。

第二名称节点（SecondaryNameNode）用来解决这个问题。其有两个作用：
- 完成EditLog和FsImage的合并，减小EditLog大小，缩短名称节点重启时间
- 作为名称节点的“检查点”，保存名称节点的元数据。

![](https://i.postimg.cc/mgXxwB90/3-4.png)

1. EditLog和FsImage的合并。每隔一段时间，第二名称节点会与名称节点通信，请求停止使用EditLog文件(时间为t1)，暂时将新的操作写入新的EditLog.new中。然后第二名称节点将FsImage和Editl拉回本地加载到内存中进行合并。结束后将新的FsImage发送给名称节点进行替换，同时用EditLog.new代替EditLog文件（时间为t2），从而减少EditLog文件的大小。
2. 作为“检查点”。由上可知，第二名称节点可以周期性的进行备份，但是t1-t2时间段的数据将会丢失，所以不能起到热备份的作用。

**TIPS：启动后FsImage会存入内存，每次操作EditLog更新成功后会在内存中修改，所以FsImage一直是最新的！**
## 3.4 HDFS体系结构
### 3.4.1 概述
![](https://i.postimg.cc/q7x3YDJV/3-5-2.png)
![](https://i.postimg.cc/BbVDp8qj/3-5-1.png)
HDFS采用主从（Master/Slave）结构模型，一个HDFS集群包括一个名称节点和多个数据节点。名称节点作为中心服务器，负责管理文件系统的命名和对文件的访问。每个数据节点会定期发送“心跳”给名称节点，否则被标记为“宕机”，不分配I/O请求。

当客户端访问一个文件时，将文件名发送给名称节点，名称节点根据文件名找到对应的数据块，然后找到实际存储文件快的数据节点的位置，并将数据节点位置发送回客户端，最后客户端访问具体的数据节点获取数据。整个过程名称节点不参与数据的传输，这样提高了在不同数据节点上的并发性。

### 3.4.2 HDFS命名空间管理
HDFS命名空间包含目录、文件和块。使用传统的分级文件体系，可以像普通文件系统一样创建、删除目录和文件，在目录见转移文件，重命名文件等。

### 3.4.3 通信协议
所有的HDFS通信协议都是构建在TCP/IP协议几乎之上。客户端通过一个可配置的端口向名称节点主动发起TCP连接，并使用客户端协议与名称节点进行交互。而名称节点和数据节点之间使用数据节点协议进行交互。客户端和数据节点则通过RPC（Remote Procedure Call）来实现，设计上名称节点不会发起RPC，而是响应客户端和数据节点的请求。

### 3.4.4 客户端
客户端提供了JAVA API和Shell命令行的形式进行访问。

### 3.5.5 HDFS体系结构的局限性
- 命名空间的限制：名称节点保存在内存中，大小有限
- 性能瓶颈：受限于单个名称节点的吞吐量
- 格力问题：单个名称节点无法对不同程序进行隔离
- 集群的可用性：名称节点故障则整个集群不能用

## 3.5 HDFS的存储原理
### 3.5.1 数据的冗余存储
![](https://i.postimg.cc/Y2h3PFPn/3-6.png)
HDFS采用多副本方式进行冗余存储，优点如下：
1. 加快数据传输速度：多个客户端访问一个文件，可以从不同的数据块副本中读取数据
2. 容易检查数据错误：HDFS通过网络传输数据，采用多个副本可以轻松判断数据传输是否出错
3. 保证数据的可靠性：即使某个数据节点出现故障，也不会造成数据丢失

### 3.5.2 数据存储策略
1. 数据存放  
为了提高数据可靠性和系统可用性，以及充分利用网络带宽，HDFS采用了以机架（Rack）为基础的数据存放策略。因为不同机架通信需要经过交换机或路由器，所以同一机架不同机器的通行速度要快。  
HDFS默认数据节点在不同机架上，虽然写入时不能利用上述优势，但是有三个显著的优点：数据可靠性（机架故障相互不影响）及提高读取速度及负载均衡。  
存放规则如下：
![](https://i.postimg.cc/NfFWpL8B/3-7.png)
2. 数据读取  
HDFS提供了API可以确定数据节点所属的机架ID，客户端可以条用API获取自己所属的机架ID。当读取时，会选取与自己相同机架数据块进行读取，否则就随机选。
3. 数据复制  
HDFS采用流水线复制的策略，当客户端写入一个文件时，这个文件会被写入本地，切分为若干块，每个块向名称节点发起写请求，名称节点会返回一个数据节点列表。客户端会首先写入列表的第一个数据节点，然后传给第二个节点，以此类推，直到全部写完。

### 3.5.3 数据错误与恢复
1. 名称节点出错  
在HDFS1.0中，一般采取两种：远程挂在到其他文件系统和运行第二名称节点。一般是结合使用
2. 数据节点出错  
数据节点会发送“心跳”信息。如果数据节点故障或网络出错，则会标记为“宕机”，数据变成“不可读”。此处如果数据块副本数量小于设定值，则会启动数据冗余复制。HDFS的特点就由调整冗余数据的位置。
3. 数据出错  
客户端读取数据时，会采用md5和shal对数据块进行验证。在文件创建时，会对每一个文件块进行信息摘录，保存在一个隐藏文件中，当读取时会报错。

## 3.6 HDFS的数据读写过程
FileSystem是一个通用文件系统的抽象基类，DistributedFileSystem是FileSystem在HDFS中的实现。FileSystem的open()方法返回一个输入流FSDataInputStream对象，在HDFS中具体的输入流就是DFSInputStream；FileSystem的create()方法返回一个FSDataOutputStream对象，对应的就是DFSOutPutStream。
### 3.6.1 读数据
客户端连续调用open()、read()、close()读取数据时，情况如下：

![](https://i.postimg.cc/jStKvRjX/3-8.png)
1. 客户端通过FileSystem.open()打开文件，在HDFS文件系统中，DistributedFileSystem具体实现了FileSystem，其会创建输入流FSDataInputStream，对于HDFS，就是DFSInputStream。
2. 在DFSInputStream的构造函数中，输入流通过ClientProtocol.getBlockLocations()远程调用名称节点，获得文件开始部分数据块的保存位置。对于该数据块，名称节点返回保存该数据的所有数据节点的地址，同时根据距离客户端的远近对数据节点排序。然后DistributedFileSystem会利用DFSInputStream来实例化FSDataInputStream，返回给客户端，同时返回数据块的数据节点地址。
3. 获得输入流FSDataInputStream后，客户端调用read()函数开始读取数据，输入流根据前面的排序，选择最近的建立连接并读取。
4. 数据从该数据节点读到客户端；当读取完毕，FSDataInputStream关闭连接
5. 输入流通过getBlockLocations()方法查找下一个数据块（如果缓存了就不用）
6. 重复上述动作
7. 调用FSDataInputStream的close()函数，关闭输入流。

三步：从名称结点获得输入流（包括数据节点的位置） + 从最近的数据节点读取数据 + 重复直到结束关闭连接

### 3.6.2 写数据
客户端连续调用create()、write()、close()写数据时，情况如下：
![](https://i.postimg.cc/L6T4cJw7/3-9.png)
1. 客户端通过FileSystem.create()打开文件，在HDFS文件系统中，DistributedFileSystem具体实现了FileSystem，其会创建输入流FSDataOutputStream，对于HDFS，就是DFSInputStream。
2. DistributedFileSystem通过RPC远程调用名称结点，在文件系统的命名空间中创建一个新的文件。名称节点会执行一些检查。检查通过后，名称节点会构造一个新文件，并添加文件信息。RPC结束后，DistributedFileSystem会利用DFSOutputStream来实例化FSDataOutputStream，返回给客户端，客户端使用这个数据流写入数据。
3. 获得输入流FSDataInputStream后，客户端调用write()函数开始写入数据。
4. 客户端向输出流FSDataOutputStream写入的数据会首先被分成一个一个分包，这些包被放入DFSOutputStream对象的内部队列。输出流FSDataOutpStream会向名称节点申请保存文件和副本数据块的若干数据节点。这些数据节点形成一个数据流管道。队列中的分包会被打包成数据包，然后发往第一个节点、再到第二个……（流水线复制策略）。
5. 因为各个数据节点位于不同机器上，数据需要通过网络发送。为了保证准确性，接收到数据的数据节点都要发送“确认包”（ACK Packet），确认包顺着数据流管道逆流而上，最终发往客户端。
6. 重复上述动作
7. 客户端调用close()方法关闭输出流，此时客户端不再写入数据。所以，当DFSOutputStream对象内部的分包都收到应答之后，就可以使用ClientProtocol.complete()方法通知名称节点关闭文件，完成一次写操作。

三步：从名称结点获得输出流（名称节点创建新文件） + 数据分包并通过数据流管道写入（包括逆流确认） + 重复上述直到结束。

# 编程实战
## Hadoop Shell 和 Java API具体见官方文档。
Shell命令基本是： **./bin/hdfs dfs -命令**

### 目录操作
创建一个用户目录：-p 用户递归创建子文件

    $./bin/hdfs dfs -mkdir -p /user/hadoop

创建一个input目录：

    $./bin/hdfs dfs -mkdir input

删除上述input目录：-r递归删除

    $./bin/hdfs dfs -rm -r input

### 文件操作
从本地上传文件至HDFS：

    $ ./bin/hdfs dfs -put /home/hadoop/myLocalFile.txt input
查看文件内容：

    $ ./bin/hdfs dfs -cat input/myLocalFile.txt
从HDFS下载文件到本地：

    $./bin/hdfs dfs -get input/myLocalFile.txt /home/hadoop/下载

HDFS复制文件：

    $ ./bin/hdfs dfs -cp input/myLocalFile.txt /input

    
# Java API操作
检测文件是否存在，具体api见文档

    package hadoop;
    
    import org.apache.hadoop.conf.Configuration;
    import org.apache.hadoop.fs.FileSystem;
    import org.apache.hadoop.fs.Path;
    
    public class HDFSFileIfExist {
    
    	public static void main(String[] args) {
    		try {
    			String fileName = "input/myLocalFile.txt";
    			Configuration conf = new Configuration();
    			conf.set("fs.defaultFS", "hdfs://localhost:9000");
    			conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
    			FileSystem fs = FileSystem.get(conf);
    			if(fs.exists(new Path(fileName))) {
    				System.out.println("file exists");
    			}else {
    				System.out.println("file not exists");
    			}
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    
    }
    