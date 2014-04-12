package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;

/**
 * Message flow validation
 * 
 * Implemented:
 * 
 *  M1
 *  
 * Not implemented:
 * 
 *  M2
 *  M3
 *  M4
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 *
 */
public class Level2MessageFlowValidationWorker extends AbstractAdvancedValidatorWorker {

	@Override
	public void validate() {
	  
		/**
		 * [M1] A message flow may not connect nodes in the same process (pool).
		 */
	  // validated in Level1ValidationWorker - rule 3
	  
		/**
		 * [M2] The source of message flow must be either a Message or Multiple end event or throwing intermediate event; an activity; or black-box pool.
		 */
	  // TODO bpmn20 rule
	  
		/**
		 * [M3] The target of message flow must be either a Message or Multiple start event,
		 * catching intermediate event, or boundary event, or boundary event; an activity; 
		 * or black-box pool. [Exceptions, not part of the Level 2 pallete: event subprocess Message or Multiple start event.]
		 */
	// TODO bpmn20 rule
	  
		/**
		 * [M4] Both ends of message flow require a valid connection. Neither end may be unconnected.
		 */
	// TODO bpmn20 rule 

		
	}

}
