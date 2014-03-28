package org.activiti.designer.eclipse.views.validator;

import org.activiti.designer.eclipse.Logger;
import org.activiti.designer.eclipse.editor.ActivitiDiagramEditor;
import org.activiti.designer.eclipse.extension.validation.ProcessValidator;
import org.activiti.designer.eclipse.extension.validation.ValidationResults;
import org.activiti.designer.eclipse.util.ExtensionPointUtil;
import org.activiti.designer.eclipse.views.validator.config.ValidationConfigurationDialog;
import org.activiti.designer.eclipse.views.validator.table.ValidationTableManager;
import org.activiti.designer.eclipse.views.validator.table.ValidationTableViewer;
import org.activiti.designer.util.ActivitiConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

/**
 * Activiti view providing results and actions for validation and verification
 * of BPMN 2.0 diagrams.
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 * 
 */
public class ValidationView extends ViewPart {
	public ValidationView() {
	}

	private ValidationTableManager tableManager;
	
	private Button validateButton;
	private Button clearButton;
	private Button configButton;
	private Table table;

	@Override
	public void createPartControl(Composite parent) {
		// Config layout, tutorial: http://zetcode.com/gui/javaswt/layout/
		parent.setLayout(new GridLayout(3, true));
		initUI(parent);
		bindActions();
	}

	/**
	 * Create UI
	 * @param parent
	 */
	private void initUI(Composite parent) {
		// create UI

		validateButton = new Button(parent, SWT.PUSH);
		validateButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		validateButton.setText("Validate");

		clearButton = new Button(parent, SWT.PUSH);
		clearButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		clearButton.setText("Clear");

		configButton = new Button(parent, SWT.PUSH);
		configButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		configButton.setText("Configuration");

		// define the TableViewer
		tableManager = new ValidationTableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);

		// make lines and header visible
		table = tableManager.getViewer().getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		resizeTable();
	}

	/**
	 * Bindings
	 */
	private void bindActions() {
		validateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				validate();
			}
		});
		clearButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				tableManager.clear();
			}
		});
		configButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				new ValidationConfigurationDialog().open();
			}
		});
	}

	/**
	 * Validate active diagram
	 */
	protected void validate() {
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
				
			// update table
			tableManager.setAll(validateDiagram.getResults());
			
			Logger.logInfo("Validation Finished!");
		}
	}

	/**
	 * Resize table columns
	 */
	private void resizeTable() {
		for (TableColumn tableColumn : table.getColumns()) {
			tableColumn.pack();
		}
	}
	
	@Override
	public void setFocus() {
		// empty
	}

}
