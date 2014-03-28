package org.activiti.designer.eclipse.views.validator.table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.designer.eclipse.extension.validation.ValidationResults.ValidationResult;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Validation table viewer as Validation table manager
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 *
 */
public class ValidationTableViewer extends TableViewer implements ValidationTableManager {
	
	private List<ValidationResult> validationViolations = new ArrayList<ValidationResult>();
	
	// names of getters methods in model
	private static final String PROP_TYPE = "Type";
	private static final String PROP_ELEM1 = "Element1Id";
	private static final String PROP_ELEM2 = "Element2Id";
	private static final String PROP_REASON = "Reason";

	public ValidationTableViewer(Composite parent, int params) {
		super(parent, params);
		
		createColumn(this, "Type", PROP_TYPE);
		createColumn(this, "Element", PROP_ELEM1);
		createColumn(this, "2nd Element", PROP_ELEM2);
		createColumn(this, "Message", PROP_REASON);
		
		setContentProvider(new ArrayContentProvider());
		setInput(validationViolations);
	}

	
	/**
	 * Inspired by http://www.vogella.com/tutorials/EclipseJFaceTable/article.html
	 * @param validationTableViewer
	 * @param title
	 * @param propertyName property name beginning with capital (must be getter in model)
	 */
	private TableViewerColumn createColumn(TableViewer viewer, String title, final String propertyName) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		// column.setWidth(10);
		column.setResizable(true);
		column.setMoveable(true);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof ValidationResult) {
					try {
						Method method = ValidationResult.class.getDeclaredMethod("get" + propertyName);
						Object returns = method.invoke(element);
						return returns == null ? "-" : returns.toString();

					} catch (NoSuchMethodException e) {
						// TODO use logger?
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
				return "-";
			}
		});
		return viewerColumn;
	}
	
	/**
	 * Repaint Table
	 */
	private void updateTable() {
		setInput(validationViolations);
	}
	
	@Override
	public void clear() {
		validationViolations.clear();
		updateTable();
	}

	@Override
	public void setAll(Collection<ValidationResult> validationResults) {
		validationViolations.clear();
		validationViolations.addAll(validationResults);
		updateTable();
	}


	@Override
	public TableViewer getViewer() {
		return this;
	}
}
