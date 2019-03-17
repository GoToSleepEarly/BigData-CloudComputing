package chapter10;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader{
	
	//定义默认的class存放路径
	private final static Path DEFAULT_CLASS_DIR = Paths.get("F:/");
	private final Path classDir;
	
	//默认的class
	public MyClassLoader(){
		super();
		this.classDir = DEFAULT_CLASS_DIR;
	}
	
	//传入指定的class
	public MyClassLoader(String classDir){
		super();
		this.classDir = Paths.get(classDir);
	}
	
	//指定class，指定父类加载器
	public MyClassLoader(String classDir, ClassLoader parent){
		super(parent);
		this.classDir = Paths.get(classDir);
	}
	
	//重写父类的findClass方法，至关重要
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException{
		//读取二进制的class
		byte[] classBytes = this.readClassBytes(name);
		//如果为空，则报错
		if(null == classBytes || classBytes.length == 0){
			throw new ClassNotFoundException("找不到"+ name);
		}
		return this.defineClass(name, classBytes, 0, classBytes.length);
	}
	
	//将class读入内存
	private byte[] readClassBytes(String name) throws ClassNotFoundException {
		//将包名转换成文件分隔符
		String classPath = name.replace(".",",") ;
		//加上.class
        Path classFullPath = classDir.resolve(Paths.get(classPath+".class")) ;
        //找不到抛出异常
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
