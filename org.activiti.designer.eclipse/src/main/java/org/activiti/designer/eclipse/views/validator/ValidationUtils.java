package org.activiti.designer.eclipse.views.validator;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.designer.eclipse.Logger;
import org.activiti.designer.eclipse.common.ActivitiPlugin;
import org.activiti.designer.eclipse.editor.ActivitiDiagramEditor;
import org.activiti.designer.eclipse.extension.validation.ProcessValidator;
import org.activiti.designer.eclipse.extension.validation.ValidationResults;
import org.activiti.designer.eclipse.extension.validation.ValidationResults.ValidationResult;
import org.activiti.designer.eclipse.util.ExtensionPointUtil;
import org.activiti.designer.util.ActivitiConstants;
import org.activiti.designer.util.editor.BpmnMemoryModel;
import org.activiti.designer.util.editor.ModelHandler;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

/**
 * Validation utils
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 *
 */
public class ValidationUtils {

	/**
	 * Validate active diagram
	 */
	public static List<ValidationResult> validate() {
		Logger.logInfo("Running validation!");

		// inspired by AbstractExportMarshaller.invokeValidators
		// final int totalWork = WORK_INVOKE_VALIDATORS_VALIDATOR * validatorIds.size();
		// final IProgressMonitor activeMonitor = monitor == null ? new NullProgressMonitor() : monitor;
		// activeMonitor.beginTask("Invoking validators", totalWork);
		// boolean overallResult = true;

		// get validator, otherwise skip
		final ProcessValidator processValidator = ExtensionPointUtil.getProcessValidator(ActivitiConstants.BPMN_VALIDATOR_ID);
		if (processValidator != null) {

			Logger.logDebug("Validator resolved");

			// TODO find better way of getting active diagram
			ValidationResults validateDiagram = processValidator.validateDiagram(ActivitiDiagramEditor.EDITOR.getDiagramTypeProvider().getDiagram());

			Logger.logInfo("Validation Finished!");

			return validateDiagram.getResults();
		}
		Logger.logError(new Exception("Validator not found!"));
		return new ArrayList<ValidationResult>(0);
	}

	/**
	 * Find and select shape on actual diagram
	 * @param id
	 * @return
	 */
	public static Shape selectShapeByElementId(String id) {
		ActivitiDiagramEditor editor = ActivitiDiagramEditor.EDITOR;
		Diagram diagram = editor.getDiagramTypeProvider().getDiagram();
		
		Shape findShapeById = findShapeById(id, diagram);
		
		if(findShapeById != null) {
			editor.selectPictogramElements(new PictogramElement[] {findShapeById});
		} else {
			// show error dialog
			MessageBox dialog =  new MessageBox(ActivitiPlugin.getShell(), SWT.ICON_ERROR | SWT.OK );
			dialog.setText("Element not found in Diagram");
			dialog.setMessage("Element with id `"+id+"` was not found in diagram. Didn't you remove it already?");
			dialog.open(); 
		}
		
		return null;
	}
	
	/**
	 * Find shape in diagram by ID
	 * @param id
	 * @param diagram
	 * @return return shape object or <i>null</i> if not found
	 */
	public static Shape findShapeById(String id, Diagram diagram) {
		for (Shape shape : diagram.getChildren()) {
			Object executionListenerBO = getExecutionListenerBO(shape, diagram);
			if(executionListenerBO instanceof BaseElement) {
				if( ((BaseElement) executionListenerBO).getId().equals(id)) {
					// have correct element, select it
					return shape;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get object from diagram
	 * @param pe
	 * @param diagram
	 * @return
	 */
	private static Object getExecutionListenerBO(PictogramElement pe, Diagram diagram) {
		BpmnMemoryModel model = ModelHandler.getModel(EcoreUtil.getURI(diagram));
		Object bo = null;
		if (pe instanceof Diagram) {
			bo = model.getBpmnModel().getMainProcess();
		} else {
			bo = model.getFeatureProvider().getBusinessObjectForPictogramElement(pe);
		}
		return bo;
	}

}
