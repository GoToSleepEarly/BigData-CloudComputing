package chapter26;

public class ProductionChannel {
 
    // ���ʹ����ж��ٸ����ӹ��Ĳ�Ʒ
    private final static int MAX_PROD = 100 ;
 
    // ������Ŵ��ӹ���Ʒ�Ķ���
    private final Production[] productionsQueue ;
 
 
    // ����β
    private int tail ;
 
    // ����ͷ
    private int head ;
 
    // һ���ж��ٸ����ӹ��Ĳ�Ʒ
    private int total ;
 
    // ��ˮ�����ж��ٸ�����
    private final Worker[] workers ;
 
 
    // ����ProductionChannel��ָ�����У��͹���
    public ProductionChannel(int workerSize){
        this.workers = new Worker[workerSize] ;
        this.productionsQueue = new Production[MAX_PROD] ;
 
        for (int i = 0 ; i< workerSize ; i++ ) {
            workers[i] = new Worker("Worker-"+i,this);
            workers[i].start();
        }
    }
    // �����������εĴ��ӹ��Ĳ�Ʒ
    public void offerProduction(Production production){
        synchronized (this){
            // ���ӹ��Ĳ�Ʒ�������ֵʱ�������߳�
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
            // �����߳�
            this.notifyAll();
 
        }
    }
 
    // �����߳� worker �Ӷ����ϻ�ȡ��Ʒ�� ���мӹ�
    public Production takeProduction(){
 
        synchronized (this) {
            // ��������û�в�Ʒʱ�����еȴ�
            while (total <= 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // ��ȡ��Ʒ
            Production prod = productionsQueue[head] ;
            head = (head+1) % productionsQueue.length;
            total -- ;
            this.notifyAll();
            return prod;
        }
    }
}
