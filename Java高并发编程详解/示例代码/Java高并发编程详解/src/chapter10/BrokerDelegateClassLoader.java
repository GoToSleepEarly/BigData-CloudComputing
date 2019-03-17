package chapter10;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BrokerDelegateClassLoader extends ClassLoader {
	 
    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        // �������ȫ�־����ƽ��м����� ȷ��ÿһ�����ڶ��̵߳������ֻ������һ�Ρ�
        synchronized (getClassLoadingLock(name)) {
            // ���Ѽ�����Ļ����в鿴���Ƿ��Ѿ������أ�����Ѿ�������ֱ�ӷ���
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                // �״μ��� ��������ȫ·����java ��javax��ͷ��ֱ��ί�и�ϵͳ��������������
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
 
                    // ����Զ�����û����ɶ���ļ��أ�ί�и�������������ϵͳ����������м���
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
                // �޷����أ��׳��쳣
               throw new ClassNotFoundException("The class : "+ name + " not found ." )  ;
            }
 
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}

