package br.com.aexo.atlas.infraestructure.process;

import java.util.List;

/**
 * class that serves to make the chain of responsibility controlling Performing
 * steps
 * 
 * @author carlosr
 * 
 */
public class StepChain {

	private List<Step> steps;

	private Integer index = 0;

	public StepChain(List<Step> steps) {
		this.steps = steps;
	}

	/**
	 * performs the next step in the chain
	 * 
	 * @param context
	 */
	public void executeNextStepFor(Context context) {
		if (index == steps.size())
			return;

		Step passo = steps.get(index);
		index++;
		passo.execute(context, this);
	}

}