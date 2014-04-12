package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import java.util.List;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.designer.validation.bpmn20.validation.util.BpmnMessageUtils;
import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;
import org.eclipse.core.resources.IMarker;

/**
 * Start Event Rules
 * 
 * Implemented: 
 *  
 *  e1 / forbidden
 *  e2 / forbidden
 *  
 * Not implemented:
 *  
 *  e3 / forbidden
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 * 
 */
public class Level2EndValidationWorker extends AbstractAdvancedValidatorWorker {

  private static final String MESSAGE_1 = "/e1/ %s shouldn't have outgoing flows.";
  private static final String MESSAGE_2 = "/e2/ %s shouldn't have incomming message flow.";

  @Override
  public void validate() {

    // get end all END EVENT elements
    List<EndEvent> endEvents = getNodes(EndEvent.class);

    // [e1] An end event may not have outgoing sequence flow.
    // -- designer not allow to create outgoing flow from end event
    if (allowValidateForbiddenByEditor()) {
      for (EndEvent endEvent : endEvents) {
        List<SequenceFlow> outgoingFlows = endEvent.getOutgoingFlows();
        if (outgoingFlows != null && outgoingFlows.size() > 0) {
          createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_1, formatName(endEvent)), endEvent);
        }
      }
    }

    // [e2] An end event may not have incoming message flow.
    // -- designer not allow to create incoming flow to start event
    //
    if (allowValidateForbiddenByEditor()) {
      for (EndEvent endEvent : endEvents) {
        List<SequenceFlow> inSequenceFlows = endEvent.getIncomingFlows();
        if (inSequenceFlows != null && inSequenceFlows.size() > 0) {
          // Check if its message
          for (SequenceFlow sequenceFlow : inSequenceFlows) {
            // if its start message, it means that there is incomming message flow - produce error 
            if (BpmnMessageUtils.isStartMessage(getNode(sequenceFlow.getSourceRef()))) {
              createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_2, formatName(endEvent)), endEvent);
              break;
            }
          }
        }
      }
    }

    // [e3] An end event with outgoing message flow must have Message or
    // Multiple result.
    // -- forbidden by designer

  }

}
