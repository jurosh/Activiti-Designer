package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import java.util.List;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BoundaryEvent;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.EventDefinition;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.MessageEventDefinition;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;
import org.eclipse.core.resources.IMarker;

/**
 * BPMN 2.0 Level 1 Palete Verification Rules
 * 
 * @author Jurosh
 * 
 */
public class Level1ValidationWorker extends AbstractAdvancedValidatorWorker {

	@Override
	public void validate() {

		/**
		 * [O1-1] A sequence flow may not cross a pool (process) boundary.
		 */		
		List<SequenceFlow> allSequenceFlows = getNodes(SequenceFlow.class);
		for (SequenceFlow sequenceFlow : allSequenceFlows) {
			String targetRef = sequenceFlow.getTargetRef();
			String sourceRef = sequenceFlow.getSourceRef();
			
			BaseElement target = getNode(targetRef);
			BaseElement source = getNode(sourceRef);
			
			// if its not message start flow
			if(source instanceof StartEvent) {
				List<EventDefinition> eventDefinitions = ((StartEvent) source).getEventDefinitions();
				boolean isOk = false;
				for (EventDefinition eventDefinition : eventDefinitions) {
					if(eventDefinition instanceof MessageEventDefinition) {
						System.out.println("[debug] Message event, OK");
						isOk = true;
					}
				}
				if(!isOk) {
					createErr(IMarker.SEVERITY_ERROR, formatName(source, sourceRef) + " should be connected to " + formatName(target, targetRef) + " only with message flow.", source);
				}
				
			} else {
			
				String processOfTarget = getIdOfProcess(target);
				String processOfSource = getIdOfProcess(source);
				
				if(processOfTarget != processOfSource) {
					createErr(IMarker.SEVERITY_ERROR, formatName(source, sourceRef) + " should be connected to " + formatName(target, targetRef) + " only with message flow.", source);
					System.out.println("PRUSER - " + sourceRef + " | " + targetRef);
				}
			}
		}

		/**
		 * [O1-2] A sequence flow may not cross a subprocess boundary.
		 */
		// TODO

		/**
		 * [O1-3] A Message flow may not connect nodes in the same pool
		 * 
		 * priority: MEDIUM [ not allowed by editor]
		 */
		for (Process process : getModel().getProcesses()) {
			for (FlowElement elem : process.getFlowElements()) {
				if (elem instanceof BoundaryEvent) {
					// System.out.println(elem.getId());
					// TODO detect message boundary and check ref element, if is on same pool or not
				}
			}
		}

		/**
		 * [O1-4] A sequence flow may only be connect to an activity, gateway,
		 * or event, and both ends must be properly connected
		 * 
		 * priority: HIGH
		 */
		for (Process process : getModel().getProcesses()) {
			for (FlowElement elem : process.getFlowElements()) {
				if (elem instanceof FlowNode) {
					List<SequenceFlow> outgoingFlows = ((FlowNode) elem).getOutgoingFlows();
					List<SequenceFlow> incomingFlows = ((FlowNode) elem).getIncomingFlows();

					// not allow cycle connections
					if (outgoingFlows.contains(elem) || incomingFlows.contains(elem)) {
						createErr(IMarker.SEVERITY_ERROR, formatName(elem) + " is connected to itself.", elem);
						break;
					}

					// just end element should not have outgoing flow
					boolean flowOutError = outgoingFlows.size() == 0 && !(elem instanceof EndEvent);

					// just start element should not have incoming flow
					boolean flowInError = incomingFlows.size() == 0 && !(elem instanceof StartEvent);

					if (flowInError && flowOutError) {
						createErr(IMarker.SEVERITY_ERROR, formatName(elem) + " have no incomming and outgoing sequence flow.", elem);

					} else if (flowInError) {
						createErr(IMarker.SEVERITY_ERROR, formatName(elem) + " have no incomming sequence flow.", elem);

					} else if (flowOutError) {
						createErr(IMarker.SEVERITY_ERROR, formatName(elem) + " don't have next element connection.", elem);
					}

				}

			}
		}

		/**
		 * [O1-5] A message flow may only connect to an activity, Message (or
		 * Multiple) event, or black-box pool, and both ends must be properly
		 * connected.
		 */
		// TODO

	}

}
