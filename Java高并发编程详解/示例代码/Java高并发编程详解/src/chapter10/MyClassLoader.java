package chapter10;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader{
	
	//����Ĭ�ϵ�class���·��
	private final static Path DEFAULT_CLASS_DIR = Paths.get("F:/");
	private final Path classDir;
	
	//Ĭ�ϵ�class
	public MyClassLoader(){
		super();
		this.classDir = DEFAULT_CLASS_DIR;
	}
	
	//����ָ����class
	public MyClassLoader(String classDir){
		super();
		this.classDir = Paths.get(classDir);
	}
	
	//ָ��class��ָ�����������
	public MyClassLoader(String classDir, ClassLoader parent){
		super(parent);
		this.classDir = Paths.get(classDir);
	}
	
	//��д�����findClass������������Ҫ
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException{
		//��ȡ�����Ƶ�class
		byte[] classBytes = this.readClassBytes(name);
		//���Ϊ�գ��򱨴�
		if(null == classBytes || classBytes.length == 0){
			throw new ClassNotFoundException("�Ҳ���"+ name);
		}
		return this.defineClass(name, classBytes, 0, classBytes.length);
	}
	
	//��class�����ڴ�
	private byte[] readClassBytes(String name) throws ClassNotFoundException {
		//������ת�����ļ��ָ���
		String classPath = name.replace(".",",") ;
		//����.class
        Path classFullPath = classDir.resolve(Paths.get(classPath+".class")) ;
        //�Ҳ����׳��쳣
        if(!classFullPath.toFile().exists()){
            throw new ClassNotFoundException(" The class  " + name + "not found." ) ;
        }
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            Files.copy(classFullPath,baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ClassNotFoundException("  load the class  " + name +"  error ." ,e) ;
        }
	}	
	
	@Override
	public String toString(){
		return "MyClassLoader";
	}
}
