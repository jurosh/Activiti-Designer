package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;

/**
 * Start Event Rules
 * 
 * @author Jurosh
 *
 */
public class Level2StartValidationWorker extends AbstractAdvancedValidatorWorker {

	@Override
	public void validate() {
		// TODO Auto-generated method stub

		// [s1] A start event may not have an incoming sequence flow.
		// forbidden by editor
		
		// [s2] A start event may not have an outgoing message flow.
		// its forbidden by editor to connects to message event after start

		// [s3] A start event with incoming message flow must have a Message or Multiple trigger.

		// [s4] A start event may not have an Error trigger. [Exceptions, not part of Level 2 pallete: event subprocess start event].

		// [s5] A start event in a subprocess must have a None trigger. [Exceptions, not part of Level 2 pallete: event subprocess start event].

		
	}

}
