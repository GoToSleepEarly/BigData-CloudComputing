package chapter22;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Document {
	//����ĵ������ı䣬changed�ᱻ����Ϊtrue
	private boolean changed = false;
	
	//һ����Ҫ��������ݣ����Խ������Ϊ���ݻ���
	private List<String> content = new ArrayList<>();
	
	private final FileWriter writer;

	//�Զ������ĵ����߳�
	private static AutoSaveThread autoSaveThread;
	
	//���캯����Ҫ�����ĵ������·�����ĵ�����
	private Document(String documentPath, String documentName) throws IOException{
		this.writer = new FileWriter(new File(documentPath, documentName), true);
	}
	// ��̬���������ڴ����ĵ���˳�������Զ������ĵ����߳�
    public static Document create(String documentPath , String documentName) throws IOException{
        Document document = new Document(documentPath,documentName );
        autoSaveThread = new AutoSaveThread(document);
        autoSaveThread.start();
        return document ;
    }
    
 // �ĵ��༭����ʵ������content�������ύ�ַ���
    public void edit(String content){
        synchronized (this) {
            this.content.add(content) ;
            this.changed = true ;
        }
    }
 
    // �رգ� �����Զ������߳�
    public void close() throws IOException {
        autoSaveThread.interrupt();
        writer.close();
    }
 
    // �������,��ʽ��save
    public void save() throws IOException {
        synchronized (this) {
        	//����ѱ��棬��ֱ�ӷ��أ�balking
            if(!changed){
                return;
            }
 
            System.out.println(Thread.currentThread()+" execute the save action .... ");
 
            // ����д��
            for (String cacheLine : content) {
                this.writer.write(cacheLine);
                this.writer.write("\r\n");
            }
 
            this.writer.flush();
 
            // �޸�״̬
            this.changed = false;
            this.content.clear();
 
        }
    }

}