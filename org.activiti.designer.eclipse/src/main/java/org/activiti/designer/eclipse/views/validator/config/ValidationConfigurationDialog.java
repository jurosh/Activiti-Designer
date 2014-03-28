package org.activiti.designer.eclipse.views.validator.config;

import org.activiti.designer.eclipse.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Configuration Dialog for process validation tool
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 *
 */
public class ValidationConfigurationDialog extends Dialog {

	public ValidationConfigurationDialog() {
		super(getCurrentShell());
	}

	static private Shell getCurrentShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		container.setLayout(layout);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		lblNewLabel.setText("BPMN 2.0 Process Verification");
		
		Label lblValidate = new Label(container, SWT.NONE);
		lblValidate.setText("Check:");
		
		Button btnCriticalRules = new Button(container, SWT.CHECK);
		btnCriticalRules.setText("Critical Rules");
		new Label(container, SWT.NONE);
		
		Button btnImportantRules = new Button(container, SWT.CHECK);
		btnImportantRules.setText("Important Rules");
		new Label(container, SWT.NONE);
		
		Button btnWarnings = new Button(container, SWT.CHECK);
		btnWarnings.setText("Low Level Rules");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Button btnBpmnRules = new Button(container, SWT.CHECK);
		btnBpmnRules.setText("BPMN 2.0 Rules");
		new Label(container, SWT.NONE);
		
		Button btnStyleRules = new Button(container, SWT.CHECK);
		btnStyleRules.setText("Style Rules and Good Practises");
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void okPressed() {
		// Copy data from SWT widgets into fields on button press.
		// Reading data from the widgets later will cause an SWT widget disposed exception.
		
		// TODO configuration
		ValidationConfiguration validationConfiguration = new ValidationConfiguration();
		
		Logger.logInfo("Configuration Filters changed");
		
		super.okPressed();
	}

}
