package org.activiti.designer.validation.bpmn20.validation.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.designer.eclipse.extension.validation.ValidationResults;
import org.activiti.designer.util.editor.BpmnMemoryModel;
import org.activiti.designer.util.editor.ModelHandler;
import org.activiti.designer.validation.bpmn20.validation.worker.ProcessValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.ProcessValidationWorkerMarker;
import org.activiti.designer.validation.bpmn20.validation.worker.inc.OfficialRulesValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.inc.StyleRulesValidationWorker;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.mm.pictograms.Diagram;

/**
 * Business process verificator
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 * 
 */
public class BPVerificator {

	/**
	 * Run verification process
	 * 
	 * this method is used just to quick testing
	 * 
	 * @return
	 */
	public ValidationResults validate(Diagram diagram) {

		// get model
		URI uri = EcoreUtil.getURI(diagram);
		BpmnMemoryModel model = ModelHandler.getModel(uri);
		BpmnModel bmodel = model.getBpmnModel();

		// create map
		Map<String, List<Object>> processNodes = extractProcessConstructs(bmodel);
		
		// create result collection
		Collection<ProcessValidationWorkerMarker> markers = new ArrayList<ProcessValidationWorkerMarker>();

		// create and run workers
		ProcessValidationWorker officialRulesValidationWorker = new OfficialRulesValidationWorker();
		markers.addAll(officialRulesValidationWorker.validate(diagram, processNodes));

		ProcessValidationWorker styleRulesValidationWorker = new StyleRulesValidationWorker();
		markers.addAll(styleRulesValidationWorker.validate(diagram, processNodes));

		// results transformation, temporary
		ValidationResults results = new ValidationResults();
		for (ProcessValidationWorkerMarker processValidationWorkerMarker : markers) {
			results.add(processValidationWorkerMarker.getResult());
		}
		
		return results;
	}

	/**
   * Extract process constructs, elements, flows
   * @param monitor
   * @return
   */
  private Map<String, List<Object>> extractProcessConstructs(final BpmnModel model) {
    final Map<String, List<Object>> resultNodes = new HashMap<String, List<Object>>();
    // iterate processes
    List<Process> processes = model.getProcesses();
    for (Process process : processes) {
      
      // get iterated process constructs
      Collection<FlowElement> flowElements = process.getFlowElements();
      for (final FlowElement object : flowElements) {
        
        String nodeType = object.getClass().getCanonicalName();
        if (nodeType != null) {
          
          if (!resultNodes.containsKey(nodeType)) {
            resultNodes.put(nodeType, new ArrayList<Object>());
          }
          resultNodes.get(nodeType).add(object);
        }
      }
    }
    return resultNodes;
  }
  
}
