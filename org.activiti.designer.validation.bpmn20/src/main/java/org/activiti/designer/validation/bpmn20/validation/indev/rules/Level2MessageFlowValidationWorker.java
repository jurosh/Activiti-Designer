package org.activiti.designer.validation.bpmn20.validation.indev.rules;

import org.activiti.designer.validation.bpmn20.validation.indev.AbstractAdvancedValidatorWorker;

/**
 * 
 * @author Jurosh
 *
 */
public class Level2MessageFlowValidationWorker extends AbstractAdvancedValidatorWorker {

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
		//[M1] A message flow may not connect nodes in the same process (pool).
		//[M2] The source of message flow must be either a Message or Multiple end event or throwing intermediate event; an activity; or black-box pool.
		//[M3] The target of message flow must be either a Message or Multiple start event, catching intermediate event, or boundary event, or boundary event; an activity; or black-box pool. [Exceptions, not part of the Level 2 pallete: event subprocess Message or Multiple start event.]
		//[M4] Both ends of message flow require a valid connection. Neither end may be unconnected.

		
	}

}
