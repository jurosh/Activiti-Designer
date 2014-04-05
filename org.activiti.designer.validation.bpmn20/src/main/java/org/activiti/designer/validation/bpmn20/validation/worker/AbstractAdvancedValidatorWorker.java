package org.activiti.designer.validation.bpmn20.validation.worker;

import java.util.List;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.designer.eclipse.Logger;
import org.activiti.designer.eclipse.extension.validation.ValidationResults.ValidationResult;
import org.activiti.designer.util.editor.BpmnMemoryModel;
import org.activiti.designer.util.editor.ModelHandler;
import org.activiti.designer.validation.bpmn20.validation.worker.impl.ValidationCode;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 * 
 */
public abstract class AbstractAdvancedValidatorWorker extends AbstractValidationWorker {

	/**
	 * Validate even rules forbidden by editor itself
	 */
	private final static boolean VALIDATE_ALL = true; 
	
	/**
	 * Validate event forbidden by editor
	 * @param element
	 * @return
	 */
	public static boolean allowValidateForbiddenByEditor() {
		return VALIDATE_ALL;
	}
	
	
	/**
	 * Format name for output
	 * 
	 * @param element
	 * @return
	 */
	public static String formatName(BaseElement element) {
		return formatName(element, null);
	}
	
	public static String formatName(BaseElement element, String id) {
		return element == null ? id : element.getClass().getSimpleName() + " [" + element.getId() + "]";
	}

	/**
	 * 
	 * @param type
	 * @param elem
	 * @param msg
	 * @return
	 */
	public void createErr(int severity, String msg, BaseElement elem) {
		Logger.logDebug("[ValidationError]" + msg);

		// new (but temporary) result object
		ValidationResult validationResult = new ValidationResult(severity, msg, elem);

		// original result object
		ProcessValidationWorkerMarker processValidationWorkerMarker = new ProcessValidationWorkerMarker(validationResult, ValidationCode.VAL_100);
		
		results.add(processValidationWorkerMarker);
	}
	
	/**
	 * Try to do stuff without this method, but probably will be needed TODO:
	 * correct way of getting model, not it should probably take not always
	 * selected
	 * 
	 * @return
	 */
	public BpmnModel getModel() {
		URI uri = EcoreUtil.getURI(diagram);
		BpmnMemoryModel model = ModelHandler.getModel(uri);
		return model.getBpmnModel();
	}
	
	/**
	 * Get Containing pool/process of element defined by param 
	 * @param element
	 * @return
	 */
	public String getIdOfProcess(BaseElement element) {
		if (element != null) {
			List<Process> processes = getModel().getProcesses();
			for (Process process : processes) {
				if (process.getFlowElement(element.getId()) != null) {
					return process.getId();
				}
			}
		}
		return null;
	}
}
