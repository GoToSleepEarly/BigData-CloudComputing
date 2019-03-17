package chapter26;

/**
 * 抽象类InstructionBook，代表着组装产品的说明书，
 * 其中经过流水线传送带的产品将通过create()方法进行加工
 * firstProcess、secondProcess代表加工每个产品的步骤。
 */
public abstract class InstructionBook {
 
    public final void create(){
        this.firstProcess();
        this.secondProcess();
 
    }
 
 
    protected abstract void firstProcess();
 
    protected abstract  void secondProcess();
 
 
}
