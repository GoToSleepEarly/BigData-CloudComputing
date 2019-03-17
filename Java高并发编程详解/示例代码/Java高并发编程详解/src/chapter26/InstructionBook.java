package chapter26;

/**
 * ������InstructionBook����������װ��Ʒ��˵���飬
 * ���о�����ˮ�ߴ��ʹ��Ĳ�Ʒ��ͨ��create()�������мӹ�
 * firstProcess��secondProcess����ӹ�ÿ����Ʒ�Ĳ��衣
 */
public abstract class InstructionBook {
 
    public final void create(){
        this.firstProcess();
        this.secondProcess();
 
    }
 
 
    protected abstract void firstProcess();
 
    protected abstract  void secondProcess();
 
 
}
