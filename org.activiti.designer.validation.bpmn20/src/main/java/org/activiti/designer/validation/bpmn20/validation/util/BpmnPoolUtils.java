package org.activiti.designer.validation.bpmn20.validation.util;

import java.util.Collection;
import java.util.HashSet;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Pool;
import org.activiti.bpmn.model.Process;

/**
 * Pool (process) utils
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 *
 */
final public class BpmnPoolUtils {

  /**
   * Get flow elements in pool
   * 
   * @param model
   * @param pool
   * @return
   */
  public static Collection<FlowElement> getElementsInPool(BpmnModel model, Pool pool) {

    // search for process, and return element
    String processRef = pool.getProcessRef();

    // not works?! Process process = model.getProcess(pool.getProcessRef());
    
    for (Process process : model.getProcesses()) {
      if (process.getId().equals(processRef))
        if (process != null) {
          return process.getFlowElements();
        }
    }
    // if no process found, return empty set
    return new HashSet<FlowElement>();
  }

}
