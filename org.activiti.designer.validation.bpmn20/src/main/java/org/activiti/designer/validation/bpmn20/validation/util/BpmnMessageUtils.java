package org.activiti.designer.validation.bpmn20.validation.util;

import java.util.List;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.EventDefinition;
import org.activiti.bpmn.model.MessageEventDefinition;
import org.activiti.bpmn.model.StartEvent;

/**
 * Utils for message components
 * 
 * @author Jurosh
 *
 */
public class BpmnMessageUtils {

  /*
  /**
   * Check if its message flow
   * 
   * @param element
   * @return
   *
  public static boolean isMessageFlow(SequenceFlow element) {
    throw new RuntimeException("unimplemented");
  }*/
  
  /**
   * Check if element is message producing element
   * @param element
   * @return
   */
  public static boolean isStartMessage(BaseElement element) {
    if (element instanceof StartEvent) {
      List<EventDefinition> eventDefinitions = ((StartEvent) element).getEventDefinitions();
      boolean isMessage = false;
      for (EventDefinition eventDefinition : eventDefinitions) {
        if (eventDefinition instanceof MessageEventDefinition) {
          System.out.println("[debug] Message event, OK");
          isMessage = true;
          break;
        }
      }
      if (isMessage) {
        return true;
      }
    }
    return false;
  }
  
  /*
  public static boolean isEndMessage(BaseElement element) {
    throw new RuntimeException("unimplemented");
  }*/
  
}
