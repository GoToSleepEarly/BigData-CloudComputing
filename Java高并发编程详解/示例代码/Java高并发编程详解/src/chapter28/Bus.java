package chapter28;


public interface Bus {
    /**
     * ��ĳ������ע�ᵽBus�ϣ��Ӵ�֮�����ͳ�Ϊ Subscriber ��
     */
    void register(Object subscriber);
    /**
     * ��ĳ�������Bus��ȡ��ע�ᣬȡ��ע��󲻻����յ�����Bus���κ���Ϣ
     */
    void unregister(Object subscriber);
    /**
     * �ύEventĬ�ϵ�topic
     */
    void post(Object event);
    /**
     * �ύEvent��ָ����topic
     */
    void post(Object event,String topic);
    /**
     * �ر�bus
     */
    void close();
    /**
     * ����bus�����Ʊ�ʶ
     */
    String getBusName();
}
