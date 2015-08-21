package com.info.digger.web.functor;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * worker implemented in form of functor
 * @author nviradia
 *
 */
public class WorkerFunctorImpl implements Runnable{

	private final CountDownLatch doneSignal;
	private IWorkerFunctor workerFunctor = null;

	public WorkerFunctorImpl(CountDownLatch doneSignal,IWorkerFunctor workerFunctor) {
		this.doneSignal = doneSignal;
		this.workerFunctor = workerFunctor;
	}

	@Override
	public void run() {
		try {
			workerFunctor.execute();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doneSignal.countDown();
	}

}
