package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import java.util.List;

import org.activiti.bpmn.model.BoundaryEvent;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;
import org.eclipse.core.resources.IMarker;

/**
 * Boundary Events rules
 * 
 * Implemented:
 * 
 *  B1
 * 
 * Not implemented:
 * 
 *  
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 *
 */
public class Level2BoundaryValidationWorker extends AbstractAdvancedValidatorWorker {

  private static final String MESSAGE_1 = "/B1/ %s must have one outgoing sequence flow.";
  
  private static final String MESSAGE_3 = "/B3/ %s can't have incomming sequence flow.";
  
  @Override
  public void validate() {
    
    List<BoundaryEvent> boundaryEvents = getNodes(BoundaryEvent.class);
    
    /**
     * [B1] A boundary event must have exactly one outgoing sequence flow. [Exception, not part of Level 2 pallete: Compensation.]
     */
    for (BoundaryEvent boundaryEvent : boundaryEvents) {
      int countOfOutgoingFlows = boundaryEvent.getOutgoingFlows().size();
      if(countOfOutgoingFlows != 1) {
        createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_1, formatName(boundaryEvent)), boundaryEvent);
      }
    }
    
    /**
     * [B2] A boundary event trigger may include only Message, Timer, Signal, Error, Escalation, 
     * Conditional or Multiple. [Exception, not part of Level 2 pallete: Cancel Compensation, Multiple-Parallel.]
     */
    // TODO npmn20 rule    
    
    /**
     * [B3] A boundary event may not have incoming sequence flow.
     */
    for (BoundaryEvent boundaryEvent : boundaryEvents) {
      List<SequenceFlow> incommingFlows = boundaryEvent.getIncomingFlows();
      int countOfIncommingFlows = incommingFlows.size();
      if(countOfIncommingFlows > 0) {
        // TODO may be check if its not message
        createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_3, formatName(boundaryEvent)), boundaryEvent);
      }
    }
    
    /**
     * [B4] An error boundary event on subprocess requires a matching Error throw event.
     */
    // TODO npmn20 rule  
    
    /**
     * [B5] An Error boundary event may not be non-interrupting.
     */
    // TODO npmn20 rule    
    
    /**
     * [B6] An Escalation boundary event on a subprocess requires a matching Escalation throw event.
     */
    // TODO npmn20 rule
  }
}
