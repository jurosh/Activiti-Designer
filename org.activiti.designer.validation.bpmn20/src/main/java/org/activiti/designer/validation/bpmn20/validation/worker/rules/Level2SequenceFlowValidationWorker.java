package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;

/**
 * 
 * @author Jurosh
 *
 */
public class Level2SequenceFlowValidationWorker extends AbstractAdvancedValidatorWorker {

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
		// [S1] A Sequence flow must connect to flow node (activity, gateway, or event) at both ends. Neither end may be unconnected.
		// [S2] All flow nodes other than start events, boundary events, and catching Link events must have an incoming sequence flow, if process level includes any or end events. [Excetion, not part of Level 2 pallete: compensating activity, event subprocess].
		// [S3] All flow nodes other than end events and throwing Link events must have an outgoing sequence flow, if process level includes any start or end evens. [Exceptions, not part of the Level 2 pallete: compensating activity, event subprocess].
		// [S4] A sequence flow may not cross a pool (process) boundary.
		// [S5] A sequence flow may not cross a process level (subprocess) boundary.
		// [S6] A conditional sequence flow may not be used if it is the only outgoing flow.
		// [S7] Sequence flow out of parallel gateway or event gateway may not be conditional. [Note: On sequence flow out of gateways, conditional is an invisible attribute; the conditional tail marker is suppressed on sequence flows out of gateways.]
		// [S8] An activity or gateway may have at most one default flow.

		
	}

}
