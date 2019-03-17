package chapter16;
 
public class FlightSecurity {
 
    private  int count = 0 ;
    // 登机牌
    private  String boardingPass = "null" ;
    // 身份证
    private  String idCard =  "null" ;
 
 
    // 通过安检信息
    public synchronized void pass(String boardingPass , String idCard){
        this.boardingPass = boardingPass ;
        this.idCard = idCard ;
        this.count ++;
        check();
    }
 
    // 安全检查
    private void check() {
        // 简单测试，当登机牌和身份证首字母不相同时，则表示检查不通过 仅为测试，别较真。。。。
        if(boardingPass.charAt(0) != idCard.charAt(0)){
            throw   new RuntimeException("=====Exeception============"+ toString()) ;
        }
 
    }
 
    @Override
    public String toString() {
        return "The" + count+ " passengers , boardingPass["+boardingPass+"] , idCard ["+idCard+"]。";
    }
 
}
