package chapter25;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class WeakReferenceTest {

	public static void main(String[] args) throws InterruptedException {
		Reference ref = new Reference();
		WeakReference<Reference> reference = new WeakReference<>(ref);
		ref = null;
		System.gc();
		
		ReferenceQueue<Reference> queue = new ReferenceQueue<>();
		Reference ref2 = new Reference();
		WeakReference<Reference> reference2 = new WeakReference<Reference>(ref2,queue);
		ref = null;
		System.out.println(reference2.get());
		System.gc();
		TimeUnit.SECONDS.sleep(1);
		java.lang.ref.Reference<? extends Reference> gcedRef = queue.remove();
		System.out.println(gcedRef);
		
		//Phantom Reference
		ReferenceQueue<Reference> q = new ReferenceQueue<>();
		PhantomReference<Reference> pf = new PhantomReference<Reference>(new Reference(),q);
		//һ����Null
		System.out.println(reference2.get());
		System.gc();
		java.lang.ref.Reference<? extends Reference> gcedref = q.remove();
		System.out.println(gcedref);
	}

}
