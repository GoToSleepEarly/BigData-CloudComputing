package chapter29;


import java.util.HashMap;
import java.util.Map;
 
public class EventDispatcher implements DynamicRouter<Message> {
 
    /**
     * EventDispatcher����һ��ע���routerTable����Ҫ���ڴ�Ų�ͬ����Message��Ӧ��Channel
     * ���û����Message��Ӧ��Channel
     */
    private final Map<Class<? extends Message>,Channel>  routerTable ;
 
    public EventDispatcher(){
        //��ʼ��RouterTable��������ʵ���У�����ʹ��HashMap��Ϊ·�ɱ�
        this.routerTable = new HashMap<>();
    }
 
    @Override
    public void registerChannel(Class<? extends Message> messageType, Channel<? extends Message> channel) {
        this.routerTable.put(messageType,channel) ;
    }
 
    @Override
    public void dispatch(Message message) {
        if (routerTable.containsKey(message.getType())) {
            //ֱ�ӻ�ȡ��Ӧ��Channel����Message
            routerTable.get(message.getType()).dispatch(message);
 
        }else {
            throw new MessageMatcherException("Cant't match the Channel for [ "+message.getType()+" ] type");
        }
    }
}
