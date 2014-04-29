package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.designer.validation.bpmn20.validation.util.BpmnMessageUtils;
import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;
import org.eclipse.core.resources.IMarker;

/**
 * Start Event Rules
 * 
 * Implemnted:
 * 
 *  s1 - F
 *  s2
 *  s5
 *    
 * Not implmented:
 *  
 *  s3
 *  s4
 *  
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 *
 */
public class Level2StartValidationWorker extends AbstractAdvancedValidatorWorker {

  private static final String MESSAGE_1 = "/s1/ %s shouldn't have incomming flow.";
  private static final String MESSAGE_2 = "/s2/ %s shouldn't have outgoing message flow.";
                                            // start event in subprocess
  private static final String MESSAGE_5 = "/s5/ %s in %s must have none trigger.";
  
	@Override
	public void validate() {
	  
	  List<StartEvent> startElements = getNodes(StartEvent.class);
	  
		/**
		 *  [s1] A start event may not have an incoming sequence flow.
		 *  -- forbidden by editor
		 */
	  for (StartEvent startEvent : startElements) {
	    Map<String, List<ExtensionElement>> extensionElements = startEvent.getExtensionElements();
	    if(extensionElements == null || extensionElements.size() == 0) {
	      // its not just message start
	      if(startEvent.getIncomingFlows().size() > 0) {
	        createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_1, formatName(startEvent)), startEvent);
	      }
	    }
    }
		
		/**
		 *  [s2] A start event may not have an outgoing message flow.
		 */
	  for (StartEvent startEvent : startElements) {
	    for (SequenceFlow sequenceFlow : startEvent.getOutgoingFlows()) {
	      BaseElement node = getNode(sequenceFlow.getTargetRef());
	      if(BpmnMessageUtils.isEndMessage(node)) {
	        createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_2, formatName(startEvent)), startEvent);
	      }
      }
    }
	  
		/**
		 *  [s3] A start event with incoming message flow must have a Message or Multiple trigger.
		 */
	  // TODO bpmn20 rule

		/**
		 *  [s4] A start event may not have an Error trigger. [Exceptions, not part of Level 2 pallete: event subprocess start event].
		 */
    // TODO bpmn20 rule
	  
		/**
		 *  [s5] A start event in a subprocess must have a None trigger. [Exceptions, not part of Level 2 pallete: event subprocess start event].
		 */
    List<SubProcess> subprocesses = getNodes(SubProcess.class);
    for (SubProcess subProcess : subprocesses) {
      for (FlowElement flowElement : subProcess.getFlowElements()) {
        if (flowElement instanceof StartEvent && ((StartEvent) flowElement).getEventDefinitions().size() > 0) {
          createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_5, formatName(flowElement), formatName(subProcess)), flowElement);
        }
      }
    }
		
	}

}
