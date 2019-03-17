package chapter21;

import java.awt.Desktop.Action;
import java.util.concurrent.ConcurrentHashMap;

class ApplicationConfiguration{};
class RuntimeInfo{};


public final class ApplicationContext {
	//��Context�б���configurationʵ��
	private ApplicationConfiguration configuration;
	
	//��Context�б���runtimeinfoʵ��
	private RuntimeInfo runtimeInfo;
	
	//����
	
	//����Holder�ķ�ʽʵ�ֵ���
	private static class Holder{
		private static ApplicationContext instance = new ApplicationContext();
	}
	
	public static ApplicationContext getContext(){
		return Holder.instance;
	}
	
	public void setConfiguration(ApplicationConfiguration configuration){
		this.configuration = configuration;
	}
	
	public RuntimeInfo getRuntimeInfo() {
		return this.runtimeInfo;
	}

	public void setRuntimeInfo(RuntimeInfo runtimeInfo) {
		this.runtimeInfo = runtimeInfo;
	}

	public ApplicationConfiguration getConfiguration() {
		return this.configuration;
	}
	
	//�߳�������
	private ConcurrentHashMap<Thread, ActionContext> contexts = new ConcurrentHashMap<>();
	
	public ActionContext getActionContext(){
		ActionContext actionContext = contexts.get(Thread.currentThread());
		if(actionContext == null){
			actionContext = new ActionContext();
			contexts.put(Thread.currentThread(), actionContext);
		}
		return actionContext;
	}
}
