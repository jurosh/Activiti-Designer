package org.activiti.designer.eclipse.views.validator.table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.designer.eclipse.Logger;
import org.activiti.designer.eclipse.common.ActivitiPlugin;
import org.activiti.designer.eclipse.common.PluginImage;
import org.activiti.designer.eclipse.extension.validation.ValidationResults.ValidationResult;
import org.activiti.designer.eclipse.views.validator.ValidationUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

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

		createColumn(this, "Priority", PROP_TYPE);
		createColumn(this, "Element", PROP_ELEM1);
		createColumn(this, "2nd Element", PROP_ELEM2);
		createColumn(this, "Message", PROP_REASON);

		setContentProvider(new ArrayContentProvider());
		setInput(validationViolations);
		bindListeners();
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

		// set content provider
		if (propertyName == PROP_TYPE) {
			viewerColumn.setLabelProvider(new ColumnLabelTypeProvider());
		} else {
			viewerColumn.setLabelProvider(new ColumnLabelDefaultTypeProvider(propertyName));
		}
		return viewerColumn;
	}
	
	private void bindListeners() {
		// add row selection listener
		getTable().addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TableItem[] selection = getTable().getSelection();
				if(selection.length == 1) {
					String element1Id = ((ValidationResult)selection[0].getData()).getElement1Id();
					if(element1Id != null) {
						// select element
						ValidationUtils.selectShapeByElementId(element1Id);
					}
				}
			}
		});
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

	/**
	 * Default Column Label provider for validation purposes
	 * 
	 * @author Juraj Husar (jurosh@jurosh.com)
	 *
	 */
	private class ColumnLabelDefaultTypeProvider extends ColumnLabelProvider {
		
		private String propertyName;
		
		public ColumnLabelDefaultTypeProvider(String propertyName) {
			this.propertyName = propertyName;
		}
		
		@Override
		public String getText(Object element) {
			if (element != null && element instanceof ValidationResult) {
				try {
					Method method = ValidationResult.class.getDeclaredMethod("get" + propertyName);
					Object returns = method.invoke(element);
					return returns == null ? "-" : returns.toString();

				} catch (NoSuchMethodException e) {
					Logger.logError(e);

				} catch (IllegalAccessException e) {
					Logger.logError(e);

				} catch (InvocationTargetException e) {
					Logger.logError(e);
				}

			}
			return "-";
		}
	}
	
	/**
	 * Content provider for `type` - severity of validation problem
	 * 
	 * @author Juraj Husar (jurosh@jurosh.com)
	 *
	 */
	private class ColumnLabelTypeProvider extends ColumnLabelProvider {

		@Override
		public Image getImage(Object element) {
			PluginImage image;
			switch (((ValidationResult) element).getType()) {
			case IMarker.SEVERITY_ERROR:
				image = PluginImage.ACTIVITI_VALIDATION_ERROR_32;
				break;
			case IMarker.SEVERITY_WARNING:
				image = PluginImage.ACTIVITI_VALIDATION_WARN_32;
				break;
			case IMarker.SEVERITY_INFO:
				image = PluginImage.ACTIVITI_VALIDATION_INFO_32;
				break;
			default:
				image = PluginImage.ACTIVITI_VALIDATION_IMPR_32;
			}

			return ActivitiPlugin.getImage(image);
		}

		@Override
		public String getText(Object element) {
			if (element == null || !(element instanceof ValidationResult)) {
				return "";
			}
			String value;
			if (element != null && element instanceof ValidationResult) {
				switch (((ValidationResult) element).getType()) {
				case IMarker.SEVERITY_ERROR:
					value = "High";
					break;
				case IMarker.SEVERITY_WARNING:
					value = "Medium";
					break;
				case IMarker.SEVERITY_INFO:
					value = "Low";
					break;
				default:
					value = "Unknown";
				}

			} else {
				value = "-";
			}
			return value;
		}
	}
}
