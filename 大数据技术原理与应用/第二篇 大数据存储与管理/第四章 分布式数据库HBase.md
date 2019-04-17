# 第四章 分布式数据库HBase
HBase是针对谷歌BigTable的开源实现，是一个高可靠、高性能、面向列、可伸缩的分布式数据库，主要用来存储非结构化和半结构化的松散数据。HBase可以支持超大规模数据存储，可以通过水平拓展的方式组成数据表。

## 4.1 概述
### 4.1.1 BigTable
![](https://i.postimg.cc/FzCggzR1/BigTable.png)
BigTable是一个分布式存储系统，利用谷歌提出的MapReduce分布式并行计算模型来处理海量数据，使用GFS作为底层数据存储，采用Chubby提供协同服务管理，是一个灵活高性能的解决方案。  
具体以下特性：支持大规模海量数据、分布式并发数据处理效率极高、易于拓展且支持动态伸缩、适用于廉价设备、适合于读操作不适合写操作。

### 4.1.2 HBase简介
HBase是一个高可靠、高性能、面向列、可伸缩的分布式数据库，主要用来存储非结构化和半结构化的松散数据。
![](https://i.postimg.cc/zX3kDCth/4-1.png)
HBase利用Hadoop MapReduce来处理HBase中的海量数据，实现高性能计算；利用ZooKeeper作为协同服务，实现稳定服务失败恢复；利用HDFS作为高可靠的底层存储（也可以用本地文件系统）；Sqoop为HBase提供了高效便捷的RDBMS导入功能；Pig和Hive提供高层语言支持。
![](https://i.postimg.cc/vHb9NW99/4-1.png)

### 4.1.3 HBase和传统数据库的对比分析
主要体现在以下几个方面：
1. 数据类型：关系数据库采用关系模型，数据类型丰富；HBase采用未经解释的字符串
2. 数据操作：关系数据库包含丰富的操作，如插入、删除、多表连接；HBase只有简单的插入删除等，无法实现表表连接
3. 存储模式：关系数据库采用基于行模式存储（如果只有少量值有用，则顺序扫描会浪费资源）；HBase基于列，每个列族都由几个文件保存，不同列族文件分离，可以降低I/O开销，支持大量并发
4. 数据索引：关系数据库通常有多个索引提高性能；HBase只有一个索引——行键，同时可以利用Hadoop MapReduce快速高效生成索引表
5. 数据维护：关系型数据库旧值会被覆盖；HBase会保留
6. 可伸缩性：关系数据库横向纵向扩展都较难；HBase分布式能够简单添加。

当然，HBase不支持事务，因此也无法实现跨行的原子性。

## ❤既然已经有了关系数据库、HDFS和MapReduce，为什么还需要HBase？❤
1. Hdoop无法满足大规模数据实时处理应用的请求 
2. HDFS面向批量访问模式，不是随机访问模式
3. 传统关系数据库分库分表也无法提升规模
4. 传统关系数据库结构变化时一般需要停机维护；空列浪费存储空间

## 4.2 HBase访问接口
![](https://i.postimg.cc/FzDjZRTw/4-2.png)

## 4.3 HBase数据模型
HBase列族数据模型包括列族、列限定符、单元格、时间戳等概念。此外还有HBase数据库概念视图和物理视图的差别。

### 4.3.1 数据模型概述
- HBase是一个稀疏的、多维度、排序的映射表，索引是行键、列族、列限定符和时间戳
- 每个值是未经解释的字符串，没有数据类型
- 表中每一行都有一个可排序的行键和任意多的列
- 水平方向由一或多个列族组成，一个列族包含任意多列，同一列族里面的数据存储在一起
- 列族支持动态拓展，无需定义列的数量以及类型
- HBase更新时不会覆盖旧版本，而是生成新的（这与HDFS只允许追加不允许修改的特性相关）

### 4.3.2 数据模型的相关概念
![](https://i.postimg.cc/Bnd2NRCZ/4-2.png)
采用行键（Row Key）、列族（Column Famil）、列限定符（Column Qualifier）和时间戳（TimeStamp）进行索引，每个值都是未经解释的字节数组byte[]。
1. **表**  
HBase采用表来组织，由行、列组成，列分为多个列族
2. **行**  
每个行由行键（Row Key）标识。访问表中行只有3中方式：单个行键访问、通过行键区间访问、全表扫描。在HBase内部，数据按照行键的字典序排序存储。设计时需要考虑这点。
3. **列族**  
一个HBase表被分组成许多“列族”（Column Family）的集合，它是基本的访问控制单元。存储在同一列族的所有数据一般属于同一数据类型，这意味着更高的压缩率。列名都以列族为前缀，如courses::history 和 courses::math都属于courses这个列族。HBase中，访问控制、磁盘和内存的使用统计都是在列族层面进行的，包括支持不同类型的访问模式
4. **列限定符**  
列族里的数据通过列限定符来定位
5. **单元格**  
HBase表通过行、列族和列限定符确定一个“单元格”（Cell）。没有类型限定，介意有多个版本（时间戳对应）
6. **时间戳**  
每次对一个单元格执行操作（新建、修改、删除）时，HBase会隐式自动生成并存储一个时间戳（可以自定义也可以系统生成）。一个单元格不同版本是根据时间戳降序排序，所以最新的最先被读取。

### 4.3.3 数据坐标
传统关系数据库通过行和列就可以确定表中的具体值。HBase采用行键、列族、列限定符、时间戳来确定一个单元格。
如图
![](https://i.postimg.cc/sDxnLd55/4-3.png)

### 4.3.4 概念视图
![](https://i.postimg.cc/CKgsSWnP/4-4.png)
HBase的概念视图中，一个表可以视为稀疏的、多维的映射表。**按照行键字典序排序存储数据**，可以存在很多空的单元格

### 4.3.5 物理视图
![](https://i.postimg.cc/v8VLjrBH/4-5.png)
物理存储层面，它是采用了基于列的存储方式。他会按照列族分开存放，每个列族存放在一起的还有行键和时间戳。而空的列不会被存储（不是null，而是直接不存）。

### 4.3.6 面向列的存储
![](https://i.postimg.cc/TPsgN1vD/4-3.png)
行式数据库采用NSM（N-ary Storage Model）存储数据，一个元祖（行）会被连续的存储在磁盘中。从磁盘中读取数据时，需要从磁盘扫描**每个元祖的完整内容**，再筛选所需属性。如果没行只有少量信息有用，则会造成大量浪费。  

列数据库采用DSM（Decomposition Storage Model）存储模型。DSM会对关系进行垂直分解，为每个属性分配一个子关系。每个子关系只有当其相应的属性被请求时才会被访问。

如图：
![](https://i.postimg.cc/SxJKTrSg/4-4.png)

列式数据库主要用于批量数据处理和即席查询（Ad-Hoc Query）。

优点：降低I/O开销；支持大量并发用户查询；较高的数据压缩比。一般用于数据挖掘，决策支持等查询密集型系统。

缺点：执行连接操作需要高昂的开销（因为列族分散存储）。

## 4.4 HBase实现原理
### 4.4.1 HBase的功能组件
HBase包含3个主要的功能组件：
1. 库函数：链接到每个客户端；
2. 一个Master主服务器：负责管理和维护HBase表的分区信息，如一个表被分成了哪些Region，每个Region存储在哪些服务器等。还会实时监控Region服务器，分配Region和维持负载均衡。
3. 许多个Region服务器：负责存储和维护分配给自己的Region，处理来自客户端的请求。

客户端并不依赖Master，而是通过Zookeeper来获得Region位置信息，大多数客户端甚至从来不和Master通信，这样Master的负载很小。

### 4.4.2 表和Region
HBase一张表包含的行数量可能非常庞大，无法存储在一台机器上，因此会根据行键的值进行分区，每个行区间构成的分区称为“Region”，包含了某个值域区间内所有数值，它是负载均衡和数据分发的基本单位，这些Region会被分发到不同的Region服务器上。

初始时只有一个Region，随着插入数据，Region到底阈值时会自动拆分成两个新的Region。

Master会把不同Region分配到不同的Region服务器上，但是同一个Region不会被拆分到多个Region服务器上。
![](https://i.postimg.cc/htf9LTHB/4-6.png)
![](https://i.postimg.cc/ncL7xqVD/4-7.png)

### 4.4.3 Region的定位
每个Region都有一个RegionID来标识它的唯一性，这样一个Region标识符都可以表现为“表名+开始主键+RegionID”。

![](https://i.postimg.cc/tCf1HfmK/4-8.png)

- 元数据表，又称“.META.表”，存储了Region和Region服务器之间的映射关系。当HBase表很大时，元数据表也会被分裂成多个Region
- 根数据表，又称“-ROOT-表”，记录所有元数据的具体位置，根数据表只有唯一一个Region，名字是写死的。
- Zookeeper文件记录了-ROOT-表的位置

![](https://i.postimg.cc/SxFydpPx/4-6.png)
为了加快访问，.META.表的全部Region全被保存在内存中。并且客户端在访问Zookeeper，获取-ROOT-表，.META.表，具体Region后，会将查询过的信息缓存。

通过上述的“三级寻址”，没有必要再访问服务器Master，因此主服务器的负载小了很多。

## 4.5 HBase运行机制
包括HBase系统架构以及Region服务器、Store和HLog三者的工作原理

### 4.5.1 HBase系统架构
如图，HBase系统架构包括客户端、Zookeeper服务器、Master主服务器、Region服务器。一般HBase采用HDFS作为底层数据存储。
![](https://i.postimg.cc/3r0szZnW/4-9.png)

1. 客户端  
客户端包含HBase接口，同时在缓存中维护着已经访问过的Region位置信息，用来加快后续数据访问过程。HBase客户端采用HBase的RPC机制与Master（管理类操作）和Region服务器（数据读取类操作）进行通信。 

2. Zookeeper  
![](https://i.postimg.cc/cLh2fk4k/Zookeeper.png)
Zookeeper可以选举出一个Master作为集群的总管，并保证在任何时刻总有唯一一个Master在运行，这就避免了Master的“单点失效”问题。  
每个Region服务器都需要到Zookeeper中进行注册，Zookeeper会实时监控并通知给Master，并且HBase可以启动多个Master，Zookeeper选举其中一个作为“主管”。  
Zookeeper保存了-ROOT-表和Master的地址，客户端通过“三级寻址”找到数据。Zookeeper还存储了HBase的模式，包括哪些表，哪些列族等。  

3. Master  
- 管理用户对表的增加、删除、修改、查询等操作
- 实现不同Region服务器之间的负载均衡
- 在Region分裂或合并后，负责重新调整Region的分布
- 对发生故障失效的Region服务器上的Region进行迁移

Master仅仅维护表和Region的元数据信息，因此负载很低。任何时刻，一个Region只能分配给一个Region服务器，Master负责分配。

4. Region服务器  
Region服务器是HBase中最核心的模块，负责维护分配给自己的Region，并响应用户的读写请求。  
HBase一般采用HDFS作为底层文件系统，因此需要向HDFS文件系统读写数据，这也提供了（HBase本身没有）数据复制和数据副本的功能。

### 4.5.2 Region服务器的工作原理
![](https://i.postimg.cc/nrnqK2LT/4-10.png)

Region服务器内部，管理和一些列Region对象和一个HLog文件，其中HLog是磁盘上面的记录文件，它记录着所有的更新操作。每个Region对象又是由多个Store组成的，每个Store对应了表中的一个**列族**的存储。 每个Store又包含一个MemStore和若干个StoreFile，其中，MenStore是内存中的缓存，保存最近更新的数据；StoreFile是磁盘中的文件，这些文件都是B树结构，方便快速读取。StoreFile在底层的实现方式是HDFS文件系统的HFile，Hfile的数据块通常采用压缩方式存储，压缩之后大大减少网络I/O和磁盘I/O。

1. 用户读写数据的过程  
用户写入数据时，被分配到相应的Region服务器去执行操作。用户数据首先写入MemStore和HLog中，当操作写入HLog之后，commit()调用才会将其返回给客户端。  
当用户读取数据时，Region服务器会首先访问MemStore缓存，然后再去StoreFile寻找。

2. 缓存的刷新  
MemStore缓存的容量有限，系统会周期性地把MemStore内容刷写到磁盘StoreFile文件，并在Hlog写入一个标记。每次刷写都会有一个新的StoreFile，所以每个Store包含多个StoreFile文件。  
每个Region服务器都会有一个自己的HLog文件，每次启动都检查该文件，确认最新一次执行缓存刷新操作之后是否发生新的写入操作。如果发现更新，就先将更新写入MemStore，然后再刷新缓存，写入StoreFile并删除旧的HLog。

3. StoreFile的合并  
StoreFile过多会影响查找速度，于是在StoreFIle到达一个阈值时，系统调用Store.ompact()合并StoreFile（因为合并非常耗费资源，于是需要设置阈值）。
![](https://i.postimg.cc/P5HNCJPz/4-11.png)
### 4.5.3 Store的工作原理
Store是Region服务器的核心，每个Store对应一个列族的存储，每个Store包含一个MemStore和若干个StoreFile文件。  

当多个StoreFile合并成一个大文件，超过阈值后，就会触发分裂操作。同时，一个父Region会被分裂成2个子Region，父Region会下线，子Region会通过Master分配到相应的Region服务器上。

### 4.5.4 HLog的工作原理
- 分布式环境必须考虑系统出错，HBase采用HLog保证故障恢复
- HBase系统为每个Region服务器配置一个HLog文件，它是一种预写式日志（Write Ahead Log），也就是更新先写入Hlog再写入MemStore，再写入磁盘
- Zookeeper实时监测Region服务器的状态，当故障时，Zookeeper通知Master。首先处理故障Region服务器的HLog， 由于一个Region服务器包含多个Region对象的日志记录，所以系统会将HLog根据Region进行拆分，再将失效的Region重新分配给可用的Region服务器，同时把该Region对象相关的HLog日志也发给Region服务器
- 服务器领到分配给自己的Region和Hlog日志记录之后，会重新做一遍日志中的操作，写入MemStore，写入StoreFile完成恢复。

优点：只需不断把日志记录追加到单个日志文件，减少磁盘寻址次数，提升写操作性能。

缺点：故障时需要拆分日志。

## 4.6 HBase应用方案
### 4.6.1 HBase实际应用中的性能优化方案
- 行键：行键是按照字典序存储，要充分利用排序特性，如最新写入时最可能访问的，则将时间戳作为行键的一部分。
- InMemory：创建表的时候，可以HColumnDescriptor.setInMemory(true)将表放到Region副武器的缓存中，保证cache命中
- MaxVersion：设置表中数据最大版本HColumnDescriptor.setMaxVersions(int maxVersions)
- Time To Live：HColumnDescriptor.setTimeToLive(int timeToLive)设置，过期数据自动删除

### 4.6.2 HBase性能监视
- Master-status(自带)
- Ganglia
- OpenTSDB
- Ambari

### 4.6.3 在HBase上构建SQL引擎
NoSql实际上不使用SQL作为查询语言，但是为了方便使用和减少编码，所以提出方案。
- Hive整合HBase
- Phoenix

### 4.6.4 构建HBase二级索引
HBase只有一个针对行键的索引，所以只能：单个行键访问、行键区间访问、全表扫描。

其他产品提供了索引功能：
- Hindex 二级索引，华为公司开发，提供多个表索引，多个列索引，基于部分列值的索引。
- HBase+Redis ![](https://i.postimg.cc/tJPWHcMX/HBase-Redis.png)
- HBase+solr

原理：采用HBase后面引入的Coprocessor特性。其实现了两个功能：endpoint和observer，相当于存储过程和触发器。observer允许我们在记录put前后做一些操作，因此，我们可以在插入数据时同步写入索引表。

优点：非侵入性，不对HBase进行改动，也不需要上层应用改变。

缺点：双倍耗时。


# HBases实践
具体看官方文档，以下只是基础操作。

启动HBase:

    $ cd /usr/local/hadoop
    $ ./sbin/start-dfs.sh
	$ cd /usr/local/hbase
	$ bin/start-hbase.sh

进入shell：

    $ hbase shell    

在HBase创建表：  
和关系数据库不一样，不需要先创建数据库，直接建表即可。student表，包含5个列族。
    
	> create 'student','Sname','Ssex','Sage','Sdept','course'
    
查看HBase已创建的表
	
	> list

查看具体的表：
	
	> describe 'student'

添加数据：
Shell命令一次只能插入一个单元格，所以效率很低，一般采用编.程操作。
一般把put操作表后的一个位置当成**行键**

	> put 'student','95001','Sname','LiYing'
	> put 'student','95001','Sage','22'
	> put 'student','95001','Sdept','CS'
	> put 'student','95001,'course:math','80'

查看数据：
1. get命令：查看一个单元格
2. scan命令：查看某表全部数据


    > get 'student','95001'
    > scan 'student'

删除数据：
1. delete：删除一个单元格
2. delete：删除一行


	>delete 'student','95001','Ssex'
	>deleteall 'student','95001'

删除表：
需要先让表不可用，再删除表

	>disable 'student'
	>drop 'student'
	
退出HBase：
	
	>exit
	$bin/stop-hbase.sh


JAVA-API：
具体看文档

    package hbase;
    
    import java.io.IOException;
    
    import org.apache.hadoop.conf.Configuration;
    import org.apache.hadoop.hbase.*;
    import org.apache.hadoop.hbase.client.*;
    
    public class HBaseOperation {
    
    	public static Configuration configuration;
    	public static Connection connection;
    	public static Admin admin;
    	
    	public static void main(String[] args) throws IOException {
    		init();
    		createTable("student", new String[] {"score"});
    		insertData("student","zhangsan","score","English","69");
    		insertData("student","zhangsan","score","Math","86");
    		getData("student","zhangsan","score","English");
    		close();
    	}
    
    	private static void getData(String tableName, String rowKey, String colFamily, String col) throws IOException {
    		Table table = connection.getTable(TableName.valueOf(tableName));
    		Get get = new Get(rowKey.getBytes());
    		get.addColumn(colFamily.getBytes(),col.getBytes());
    		Result result = table.get(get);
    		System.out.println(result.getValue(colFamily.getBytes(), col.getBytes()));
    		table.close();
    	}
    
    	private static void close() {
    		try {
    			if(admin != null) {
    				admin.close();
    			}
    			if(null != connection) {
    				connection.close();
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    
    	private static void insertData(String tableName, String rowKey, String colFamily, String col, String val) throws IOException {
    		Table table = connection.getTable(TableName.valueOf(tableName));
    		Put put = new Put(rowKey.getBytes());
    		put.addColumn(colFamily.getBytes(), col.getBytes(), val.getBytes());
    		table.put(put);
    		table.close();
    	}
    
    	private static void createTable(String myTableName, String[] colFamily) throws IOException {
    		TableName tableName = TableName.valueOf(myTableName);
    		if(admin.tableExists(tableName)) {
    			System.out.println("table exists");
    		}else {
    			HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
    			for(String str: colFamily) {
    				HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(str);
    				hTableDescriptor.addFamily(hColumnDescriptor);
    			}
    			admin.createTable(hTableDescriptor);
    		}
    		
    	}
    
    	private static void init() {
    		configuration = HBaseConfiguration.create();
    		configuration.set("hbase.rootdir", "hdfs://localhost:9000/hbase");
    		try {
    			connection = ConnectionFactory.createConnection(configuration);
    			admin = connection.getAdmin();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    	}
    
    }
    
    
    
    