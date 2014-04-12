package org.activiti.designer.validation.bpmn20.validation.worker.rules;

import java.util.Collection;
import java.util.List;

import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Pool;
import org.activiti.designer.validation.bpmn20.validation.util.BpmnPoolUtils;
import org.activiti.designer.validation.bpmn20.validation.worker.AbstractAdvancedValidatorWorker;
import org.eclipse.core.resources.IMarker;


/**
 * Pool rules verification
 * 
 * Implemented:
 * 
 * P1
 * 
 * Not implemented:
 * 
 * P2
 * P3 - forbidden
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 *
 */
public class Level2PoolValidationWorker extends AbstractAdvancedValidatorWorker {

  private static final String MESSAGE_1 = "/P1/ %s is Pool and every Pool should have at least one Activity.";
  private static final String MESSAGE_2 = "/P2/ %s as Pool can contains just single process. Now there are %i processes.";
  
  @Override
  public void validate() {
    List<Pool> allPools = getModel().getPools();
    
    /**
     * [P1] A Process must contain at least one activity.
     */
    for (Pool pool : allPools) {
      
      Collection<FlowElement> elementsInPool = BpmnPoolUtils.getElementsInPool(getModel(), pool);
      
      boolean haveActivity = false;
      
      for (FlowElement flowElement : elementsInPool) {
        // if there is activity in pool, then pool is OK
        if(flowElement instanceof Activity) {
          haveActivity = true;
          break;
        }
      }
      
      if(!haveActivity) {
        createErr(IMarker.SEVERITY_WARNING, String.format(MESSAGE_1, formatName(pool)), pool);
      }
    }
    
    /**
     * [P2] Elements of at most one process may be contained in single pool.
     * 
     *      -- We will search for 2 start elements in pool 
     */
    /*
    for (Pool pool : allPools) {
      
      Collection<FlowElement> elementsInPool = BpmnPoolUtils.getElementsInPool(getModel(), pool);
      
      int startElementsCount = 0;
      
      for (FlowElement flowElement : elementsInPool) {
        if(flowElement instanceof StartElement) {
          startElementsCount++;
        }
      }
      if(startElementsCount > 1) {
        createErr(IMarker.SEVERITY_WARNING, String.format(MESSAGE_2, formatName(pool), startElementsCount), pool);
      }
    }
    */
    
    /**
     * [P3] A pool may not contain pool. If a child-level subprocess expansion is enclosed in pool. 
     *      That pool must reference the same participant and its associated process as the parent level.
     */
    // TODO bpmn20 rule
  }
  
}
