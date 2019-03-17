package chapter28;

import java.lang.reflect.Method;
 
public class Subscriber {
    private final Object subscribeObject;
    private final Method subscrebeMethod;
    private boolean disable = false;
 
    public Subscriber(Object subscribeObject, Method subscrebeMethod) {
        this.subscribeObject = subscribeObject;
        this.subscrebeMethod = subscrebeMethod;
    }
 
    public Object getSubscribeObject() {
        return subscribeObject;
    }
 
    public Method getSubscrebeMethod() {
        return subscrebeMethod;
    }
 
    public boolean isDisable() {
        return disable;
    }
 
    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
