package org.activiti.designer.validation.bpmn20.validation.worker;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.graphiti.mm.pictograms.Diagram;

/**
 * 
 * Interface for validation workers.
 * 
 * @author Tiese Barrell
 * @since 5.6
 * @version 1
 */
public interface ProcessValidationWorker {

  // void set(Diagram diagram, BpmnModel Model, Map<String, List<Object>> processNodes, Map<String, BaseElement> nodesIndexById);
  // Collection<ProcessValidationWorkerMarker> validate();
  
  Collection<ProcessValidationWorkerMarker> validate(Diagram diagram, Map<String, List<Object>> processNodes);
  
}
