package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import java.util.List;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;
import org.eclipse.core.resources.IMarker;

/**
 * Start Event Rules
 * 
 * @author Jurosh
 * 
 */
public class Level2EndValidationWorker extends AbstractAdvancedValidatorWorker {

	@Override
	public void validate() {

		// get end all END EVENT elements 
		List<EndEvent> endEvents = getNodes(EndEvent.class);

		// [e1] An end event may not have outgoing sequence flow.
		// -- editor not allow to create outgoing flow from end event
		if (allowValidateForbiddenByEditor()) {
			for (EndEvent endEvent : endEvents) {
				List<SequenceFlow> outgoingFlows = endEvent.getOutgoingFlows();
				if (outgoingFlows != null && outgoingFlows.size() > 0) {
					createErr(IMarker.SEVERITY_ERROR, formatName(endEvent) + " shouldn't have outgoing flows.", endEvent);
				}
			}
		}

		// [e2] An end event may not have incoming message flow.
		// TODO this is probably forbidden by editor
		//
		if (allowValidateForbiddenByEditor()) {
			for (EndEvent endEvent : endEvents) {
				List<SequenceFlow> inSequenceFlows = endEvent.getIncomingFlows();
				for (SequenceFlow sequenceFlow : inSequenceFlows) {
					sequenceFlow.getSourceRef();
					// TODO
				}
			}
		}

		// [e3] An end event with outgoing message flow must have Message or Multiple result.
		// TODO

	}

}
