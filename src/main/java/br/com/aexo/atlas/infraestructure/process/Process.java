package br.com.aexo.atlas.infraestructure.process;

import java.util.Arrays;

/**
 * base class to represent a process with several steps
 * 
 * @author carlosr
 * 
 */
public abstract class Process implements Step {

	/**
	 * process steps
	 * 
	 * @return
	 */
	public abstract Step[] steps();

	/**
	 * executes the process based on context
	 * 
	 * @param context
	 */
	public void execute(Context context) {
		StepChain chain = new StepChain(Arrays.asList(steps()));
		chain.executeNextStepFor(context);
	}

	/**
	 * performs a process as a step in a larger process
	 */

	public void execute(Context context, StepChain chain) {
		execute(context);

		chain.executeNextStepFor(context);
	}

}