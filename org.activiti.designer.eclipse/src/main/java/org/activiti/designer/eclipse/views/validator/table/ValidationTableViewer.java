package org.activiti.designer.eclipse.views.validator.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.bpmn.model.FlowElement;
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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
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
	private static final int PROP_TYPE = 1;
	private static final int PROP_ELEM = 2;
	private static final int PROP_ELEM_TYPE = 3;
	private static final int PROP_REASON = 4;

	public ValidationTableViewer(Composite parent, int params) {
		super(parent, params);

		createColumn(this, "Priority", PROP_TYPE);
		createColumn(this, "Element", PROP_ELEM);
		createColumn(this, "Element Type", PROP_ELEM_TYPE);
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
	private TableViewerColumn createColumn(TableViewer viewer, String title, final int property) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setResizable(true);
		column.setMoveable(true);

		/*
		int tableWidth = getTable().getGridLineWidth();
		int colWidth = 0; // in percentages
		switch(property) {
		case PROP_TYPE:
			colWidth = 5;
			break;
		case PROP_ELEM:
			colWidth = 20;
			break;
		case PROP_ELEM_TYPE:
			colWidth = 10;
			break;
		case PROP_REASON:
			colWidth = 64;
			break;
		}
		column.setWidth(tableWidth * colWidth / 100);
		*/

		// set content provider
		if (property == PROP_TYPE) {
			viewerColumn.setLabelProvider(new ColumnLabelTypeProvider());
		} else {
			viewerColumn.setLabelProvider(new ColumnLabelDefaultProvider(property));
		}

		return viewerColumn;
	}

	/**
	 * Bind listeners
	 */
	private void bindListeners() {
		// add row selection listener
		getTable().addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] selection = getTable().getSelection();
				if (selection.length == 1) {
					String element1Id = ((ValidationResult) selection[0].getData()).getElement1Id();
					if (element1Id != null) {
						// select element
						ValidationUtils.selectShapeByElementId(element1Id);
					}
				}
			}
		});

		// resize columns while screen width changed
		getTable().addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event event) {

				Table table = (Table) event.widget;
				int columnCount = table.getColumnCount();
				if (columnCount == 0) {
					return;
				}
				Rectangle area = table.getClientArea();
				int totalAreaWdith = area.width;
				int lineWidth = table.getGridLineWidth();
				int totalGridLineWidth = (columnCount - 1) * lineWidth;
				int totalColumnWidth = 0;
				for (TableColumn column : table.getColumns()) {
					totalColumnWidth = totalColumnWidth + column.getWidth();
				}
				int diff = totalAreaWdith - (totalColumnWidth + totalGridLineWidth);

				TableColumn lastCol = table.getColumns()[columnCount - 1];

				//check diff is valid or not. setting negetive width doesnt make sense.
				lastCol.setWidth(diff + lastCol.getWidth());

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
	private class ColumnLabelDefaultProvider extends ColumnLabelProvider {

		private int property;

		public ColumnLabelDefaultProvider(int property) {
			this.property = property;
		}

		@Override
		public String getText(Object element) {
			String value = "-";

			if (element != null && element instanceof ValidationResult) {
				ValidationResult result = ((ValidationResult) element);
				switch (property) {
				case PROP_ELEM:
					value = "[" + result.getElement().getId() + "]";
					if (result.getElement() instanceof FlowElement) {
						// append name if available
						value = ((FlowElement) result.getElement()).getName() + " " + value;
					}
					break;

				case PROP_ELEM_TYPE:
					value = result.getElement().getClass().getSimpleName();
					break;

				case PROP_REASON:
					value = result.getReason();
					break;
				}

			}
			return value;
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
