package chapter29;


import java.util.HashMap;
import java.util.Map;
 
public class EventDispatcher implements DynamicRouter<Message> {
 
    /**
     * EventDispatcher中有一个注册表routerTable，主要用于存放不同类型Message对应的Channel
     * 如果没有与Message对应的Channel
     */
    private final Map<Class<? extends Message>,Channel>  routerTable ;
 
    public EventDispatcher(){
        //初始化RouterTable，但是在实现中，我们使用HashMap作为路由表
        this.routerTable = new HashMap<>();
    }
 
    @Override
    public void registerChannel(Class<? extends Message> messageType, Channel<? extends Message> channel) {
        this.routerTable.put(messageType,channel) ;
    }
 
    @Override
    public void dispatch(Message message) {
        if (routerTable.containsKey(message.getType())) {
            //直接获取对应的Channel处理Message
            routerTable.get(message.getType()).dispatch(message);
 
        }else {
            throw new MessageMatcherException("Cant't match the Channel for [ "+message.getType()+" ] type");
        }
    }
}
