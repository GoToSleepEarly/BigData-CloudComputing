package chapter16;

public class FlightSecurityTest {
 
    static class Passengers extends Thread{
        private final FlightSecurity flightSecurity ;
 
        // ���֤
        private final String idCard ;
 
        // �ǻ���
        private final String boardingPass ;
 
        // �����ÿͶ���
        public Passengers(FlightSecurity flightSecurity , String idCard ,String boardingPass){
            this.flightSecurity = flightSecurity ;
            this.idCard = idCard ;
            this.boardingPass  = boardingPass ;
        }
 
        @Override
        public void run(){
            while (true){
                // ����ͨ������
                flightSecurity.pass(boardingPass,idCard);
            }
        }
 
    }
 
    public static void main(String[] args) {
        final FlightSecurity flightSecurity = new FlightSecurity() ;
 
        new Passengers(flightSecurity,"A11","A21").start();
 
        new Passengers(flightSecurity,"B12","B22").start();
 
        new Passengers(flightSecurity,"B13","B23").start();
 
 
 
    }
 
 
 
 
}
