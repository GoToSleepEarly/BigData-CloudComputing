package chapter28;


public interface Bus {
    /**
     * 将某个对象注册到Bus上，从此之后该类就成为 Subscriber 了
     */
    void register(Object subscriber);
    /**
     * 将某个对象从Bus上取消注册，取消注册后不会再收到来自Bus的任何消息
     */
    void unregister(Object subscriber);
    /**
     * 提交Event默认的topic
     */
    void post(Object event);
    /**
     * 提交Event到指定的topic
     */
    void post(Object event,String topic);
    /**
     * 关闭bus
     */
    void close();
    /**
     * 返回bus的名称标识
     */
    String getBusName();
}
