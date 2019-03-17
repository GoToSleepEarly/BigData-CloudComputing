package chapter27;

import chapter19.FutureTask;

public class ActiveFuture<T> extends FutureTask<T>{
	@Override
	public void finish(T result){
		super.finish(result);
	}

}
