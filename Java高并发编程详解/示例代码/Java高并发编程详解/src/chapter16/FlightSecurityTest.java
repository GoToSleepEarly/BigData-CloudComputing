package chapter16;

public class FlightSecurityTest {
 
    static class Passengers extends Thread{
        private final FlightSecurity flightSecurity ;
 
        // 身份证
        private final String idCard ;
 
        // 登机牌
        private final String boardingPass ;
 
        // 构造旅客对象
        public Passengers(FlightSecurity flightSecurity , String idCard ,String boardingPass){
            this.flightSecurity = flightSecurity ;
            this.idCard = idCard ;
            this.boardingPass  = boardingPass ;
        }
 
        @Override
        public void run(){
            while (true){
                // 不断通过安检
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
