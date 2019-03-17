package chapter26;

public class ProductionChannel {
 
    // 传送带上有多少个代加工的产品
    private final static int MAX_PROD = 100 ;
 
    // 用来存放代加工产品的队列
    private final Production[] productionsQueue ;
 
 
    // 队列尾
    private int tail ;
 
    // 队列头
    private int head ;
 
    // 一共有多少个待加工的产品
    private int total ;
 
    // 流水线上有多少个工人
    private final Worker[] workers ;
 
 
    // 创建ProductionChannel，指定队列，和工人
    public ProductionChannel(int workerSize){
        this.workers = new Worker[workerSize] ;
        this.productionsQueue = new Production[MAX_PROD] ;
 
        for (int i = 0 ; i< workerSize ; i++ ) {
            workers[i] = new Worker("Worker-"+i,this);
            workers[i].start();
        }
    }
    // 接收来自上游的代加工的产品
    public void offerProduction(Production production){
        synchronized (this){
            // 代加工的产品超过最大值时，阻塞线程
            while (total >= productionsQueue.length) {
 
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
 
            productionsQueue[tail] = production ;
            tail = (tail+1)%productionsQueue.length ;
            total++;
            // 激活线程
            this.notifyAll();
 
        }
    }
 
    // 工人线程 worker 从队列上获取产品， 进行加工
    public Production takeProduction(){
 
        synchronized (this) {
            // 当队列中没有产品时，进行等待
            while (total <= 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取产品
            Production prod = productionsQueue[head] ;
            head = (head+1) % productionsQueue.length;
            total -- ;
            this.notifyAll();
            return prod;
        }
    }
}
