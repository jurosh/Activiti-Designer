package org.activiti.designer.eclipse.views.validator.table;

import java.util.Collection;

import org.activiti.designer.eclipse.extension.validation.ValidationResults.ValidationResult;
import org.eclipse.jface.viewers.TableViewer;

/**
 * Validation View Table manager interface
 * 
 * @author Jurosh
 *
 */
public interface ValidationTableManager {

	TableViewer getViewer();
	
	void clear();
	
	void setAll(Collection<ValidationResult> validationResults);
	
}
