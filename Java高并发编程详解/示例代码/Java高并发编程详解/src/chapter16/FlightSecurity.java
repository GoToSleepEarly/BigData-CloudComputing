package chapter16;
 
public class FlightSecurity {
 
    private  int count = 0 ;
    // �ǻ���
    private  String boardingPass = "null" ;
    // ���֤
    private  String idCard =  "null" ;
 
 
    // ͨ��������Ϣ
    public synchronized void pass(String boardingPass , String idCard){
        this.boardingPass = boardingPass ;
        this.idCard = idCard ;
        this.count ++;
        check();
    }
 
    // ��ȫ���
    private void check() {
        // �򵥲��ԣ����ǻ��ƺ����֤����ĸ����ͬʱ�����ʾ��鲻ͨ�� ��Ϊ���ԣ�����档������
        if(boardingPass.charAt(0) != idCard.charAt(0)){
            throw   new RuntimeException("=====Exeception============"+ toString()) ;
        }
 
    }
 
    @Override
    public String toString() {
        return "The" + count+ " passengers , boardingPass["+boardingPass+"] , idCard ["+idCard+"]��";
    }
 
}
