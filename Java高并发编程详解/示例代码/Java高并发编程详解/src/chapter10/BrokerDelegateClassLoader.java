package chapter10;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BrokerDelegateClassLoader extends ClassLoader {
	 
    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        // 根据类的全局经名称进行加锁， 确保每一个类在多线程的情况下只被加载一次。
        synchronized (getClassLoadingLock(name)) {
            // 到已加载类的缓存中查看类是否已经被加载，如果已经加载则直接返回
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                // 首次加载 ，如果类的全路径以java 和javax开头，直接委托给系统类加载器对其加载
                if(name.startsWith("java.") || name.startsWith("javax")){
                    try{
                     c = getSystemClassLoader().loadClass(name) ;
                    }catch (Exception e) {
                        // ignore
                    }
                }else{
 
                    try {
                        c = this.findClass(name) ;
                    }catch (Exception e) {
                        // ignore
                    }
 
                    // 如果自定义类没有完成对类的记载，委托给父加载器或者系统类加载器进行加载
                    if(c == null ){
                        try {
                            if (getParent() != null) {
                                c = getParent().loadClass(name);
                            } else {
                                c = getSystemClassLoader().loadClass(name);
                            }
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                }
            }
 
            if(null == c){
                // 无法加载，抛出异常
               throw new ClassNotFoundException("The class : "+ name + " not found ." )  ;
            }
 
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}

