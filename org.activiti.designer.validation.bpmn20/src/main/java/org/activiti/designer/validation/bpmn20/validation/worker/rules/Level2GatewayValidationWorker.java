package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.designer.validation.bpmn20.validation.util.BpmnGatewayUtils;
import org.activiti.designer.validation.bpmn20.validation.util.BpmnMessageUtils;
import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;
import org.eclipse.core.resources.IMarker;

/**
 * 
 * Validation Rules for gateways, pallete level 2
 * 
 * Implemented:
 * 
 *  G1
 *  G2 / F
 *  
 * Not implemented:
 * 
 *  G3
 *  G4
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 * 
 */
public class Level2GatewayValidationWorker extends AbstractAdvancedValidatorWorker {

  private static final String MESSAGE_1 = "/G1/ %s shouldn't have incomming message flow. Gateways should have just sequence flow as input flow.";
  private static final String MESSAGE_2 = "/G2/ %s shouldn't have outgoing message flow. Gateways should have just sequence flow as outgoing flow.";
  
  @Override
  public void validate() {
    List<Gateway> gateways = new ArrayList<Gateway>();
    
    // get all gateway, from all gateway subtypes
    Set<Class> allGatewaysClasses = BpmnGatewayUtils.getAllGatewaysClasses();
    for (Class gtwClass : allGatewaysClasses) {
      gateways.addAll(getNodes(gtwClass));
    }
    
    /**
     * [G1] A gateway may not have incoming message flow.
     */
    for (Gateway gateway : gateways) {
      List<SequenceFlow> incomingFlows = gateway.getIncomingFlows();
      for (SequenceFlow flow : incomingFlows) {
        if(BpmnMessageUtils.isStartMessage(getNode(flow.getSourceRef()))) {
          createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_1, formatName(gateway)), gateway);
        }
      }
    }
    
    
    /**
     * [G2] A gateway may not have outgoing message flow.
     */
    for (Gateway gateway : gateways) {
      List<SequenceFlow> outgoingFlows = gateway.getOutgoingFlows();
      for (SequenceFlow flow : outgoingFlows) {
        if(BpmnMessageUtils.isEndMessage(getNode(flow.getTargetRef()))) {
          createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_2, formatName(gateway)), gateway);
        }
      }
    }
    
    /**
     * [G3] A splitting gateway must have more than one gate.
     */
    // TODO
    
    /**
     * [G4] Gates of an event gateway may include only a catching intermediate event or Receive task.
     */
    // TODO bpmn20 rule
  }

}
