package br.com.aexo.atlas.infraestructure.process;
/**
 * It is an activity that must be performed in a process
 * 
 * @author carlosr
 * 
 */
public interface Step {

	void execute(Context context, StepChain chain);

}