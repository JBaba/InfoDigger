package com.info.digger.web.functor;

import java.io.IOException;

/**
 * functor interface for worker
 * @author nviradia
 *
 */
public interface IWorkerFunctor {
	/**
	 * execute functor 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void execute() throws InterruptedException, IOException;
}
