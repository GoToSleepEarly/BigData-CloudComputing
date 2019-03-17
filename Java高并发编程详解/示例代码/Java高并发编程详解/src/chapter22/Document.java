package chapter22;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Document {
	//如果文档发生改变，changed会被设置为true
	private boolean changed = false;
	
	//一次需要保存的内容，可以将其理解为内容缓存
	private List<String> content = new ArrayList<>();
	
	private final FileWriter writer;

	//自动保存文档的线程
	private static AutoSaveThread autoSaveThread;
	
	//构造函数需要传入文档保存的路径和文档名称
	private Document(String documentPath, String documentName) throws IOException{
		this.writer = new FileWriter(new File(documentPath, documentName), true);
	}
	// 静态方法，用于创建文档，顺便启动自动保存文档的线程
    public static Document create(String documentPath , String documentName) throws IOException{
        Document document = new Document(documentPath,documentName );
        autoSaveThread = new AutoSaveThread(document);
        autoSaveThread.start();
        return document ;
    }
    
 // 文档编辑，其实就是往content队列中提交字符串
    public void edit(String content){
        synchronized (this) {
            this.content.add(content) ;
            this.changed = true ;
        }
    }
 
    // 关闭， 结束自动保存线程
    public void close() throws IOException {
        autoSaveThread.interrupt();
        writer.close();
    }
 
    // 保存操作,显式的save
    public void save() throws IOException {
        synchronized (this) {
        	//如果已保存，则直接返回，balking
            if(!changed){
                return;
            }
 
            System.out.println(Thread.currentThread()+" execute the save action .... ");
 
            // 内容写入
            for (String cacheLine : content) {
                this.writer.write(cacheLine);
                this.writer.write("\r\n");
            }
 
            this.writer.flush();
 
            // 修改状态
            this.changed = false;
            this.content.clear();
 
        }
    }

}