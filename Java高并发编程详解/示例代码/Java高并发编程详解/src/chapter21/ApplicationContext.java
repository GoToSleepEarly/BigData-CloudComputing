package chapter21;

import java.awt.Desktop.Action;
import java.util.concurrent.ConcurrentHashMap;

class ApplicationConfiguration{};
class RuntimeInfo{};


public final class ApplicationContext {
	//在Context中保存configuration实例
	private ApplicationConfiguration configuration;
	
	//在Context中保存runtimeinfo实例
	private RuntimeInfo runtimeInfo;
	
	//其他
	
	//采用Holder的方式实现单例
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
	
	//线程上下文
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
