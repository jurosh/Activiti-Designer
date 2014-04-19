package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import java.util.List;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.EventDefinition;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.MessageEventDefinition;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.designer.validation.bpmn20.validation.util.BpmnMessageUtils;
import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;
import org.eclipse.core.resources.IMarker;

/**
 * BPMN 2.0 Level 1 Palete Verification Rules
 * 
 * Implemented:
 *  
 *  O1
 *  O2
 *  O3
 *  O4
 *  
 * Not implemented:
 * 
 *  O5
 * 
 * @author Jurosh
 * 
 */
public class Level1ValidationWorker extends AbstractAdvancedValidatorWorker {

  private static final String MESSAGE_1A = "/O1/ %s should be connected to %s only with message flow.";
  private static final String MESSAGE_1B = "/O1/ %s should be connected to %s only with message flow. Use message flow to connection between pools.";

  private static final String MESSAGE_2 = "/O2/ %s should have all sequence flows within its (subprocess) boundary. Sequence flow: %s.";
  
  private static final String MESSAGE_3 = "/O3/ Message flow should cross 2 pools. Starts at element %s.";

  private static final String MESSAGE_4IO = "/O4/ %s have no incomming and outgoing sequence flow. ";
  private static final String MESSAGE_4I = "/O4/ %s have no incomming sequence flow.";
  private static final String MESSAGE_4O = "/O4/ %s don't have next element connection.";

  @Override
  public void validate() {

    /**
     * [O1] A sequence flow may not cross a pool (process) boundary.
     */
    List<SequenceFlow> allSequenceFlows = getNodes(SequenceFlow.class);
    for (SequenceFlow sequenceFlow : allSequenceFlows) {
      String targetRef = sequenceFlow.getTargetRef();
      String sourceRef = sequenceFlow.getSourceRef();

      BaseElement target = getNode(targetRef);
      BaseElement source = getNode(sourceRef);

      // if its not message start flow
      String processOfTarget = getIdOfProcess(target);
      String processOfSource = getIdOfProcess(source);

      if (processOfTarget != processOfSource) {

        // need to check if its start event with message
        if (source instanceof StartEvent) {
          boolean isStartMessage = BpmnMessageUtils.isStartMessage(source);
          if (!isStartMessage) {
            createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_1A, formatName(source, sourceRef), formatName(target, targetRef)), source);
          }

        } else {
          createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_1B, formatName(source, sourceRef), formatName(target, targetRef)), source);
        }
      }
    }

    /**
     * [O2] A sequence flow may not cross a subprocess boundary.
     */
    List<SubProcess> subprocesses = getNodes(SubProcess.class);
    for (SubProcess subProcess : subprocesses) {
      // there can't exist flow reaching outside world
      for(FlowElement element : subProcess.getFlowElements()) {
        if(element instanceof SequenceFlow) {
          SequenceFlow flow = (SequenceFlow) element;
          String targetRef = flow.getTargetRef();
          String sourceRef = flow.getSourceRef();
          BaseElement targetNode = getNode(targetRef);
          BaseElement sourceNode = getNode(sourceRef);
          
          String idOfProcess1 = getIdOfProcess(targetNode);
          String idOfProcess2 = getIdOfProcess(sourceNode);
          
          String idOfSubProcess = getIdOfProcess(element);
          
          if(idOfProcess1 != null && !idOfProcess1.equals(idOfSubProcess) 
                  || idOfProcess2 != null && !idOfProcess2.equals(idOfSubProcess)) {
            createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_2, formatName(subProcess), flow), subProcess);
          }
        }
      }
    }

    /**
     * [O3] A Message flow may not connect nodes in the same pool
     * 
     * priority: MEDIUM [ not allowed by editor]
     */
    for (SequenceFlow sequenceFlow : allSequenceFlows) {

      String sourceRef = sequenceFlow.getSourceRef();
      BaseElement source = getNode(sourceRef);

      // if it is Message flow, so start is message start event
      if (source instanceof StartEvent) {
        List<EventDefinition> eventDefinitions = ((StartEvent) source).getEventDefinitions();
        boolean isMessage = false;
        for (EventDefinition eventDefinition : eventDefinitions) {
          if (eventDefinition instanceof MessageEventDefinition) {
            System.out.println("[debug] Message event, OK");
            isMessage = true;
          }
        }
        if (isMessage) {
          // now check if pools are different

          String targetRef = sequenceFlow.getTargetRef();
          BaseElement target = getNode(targetRef);

          String processOfTarget = getIdOfProcess(target);
          String processOfSource = getIdOfProcess(source);

          if (processOfTarget == processOfSource) {
            createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_3, formatName(source, sourceRef)), source);
          }
        }

      }
    }

    /**
     * [O4] A sequence flow may only be connect to an activity, gateway, or
     * event, and both ends must be properly connected
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
            createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_4IO, formatName(elem)), elem);

          } else if (flowInError) {
            createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_4I, formatName(elem)), elem);

          } else if (flowOutError) {
            createErr(IMarker.SEVERITY_ERROR, String.format(MESSAGE_4O, formatName(elem)), elem);
          }

        }

      }
    }

    /**
     * [O5] A message flow may only connect to an activity, Message (or
     * Multiple) event, or black-box pool, and both ends must be properly
     * connected.
     */
    // TODO bpmn20 rule implement O5

  }

}
