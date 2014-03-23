package org.activiti.designer.property;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.designer.util.TextUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class PropertySequenceFlowSection extends ActivitiPropertySection implements ITabbedPropertyConstants {
	
  protected Text flowLabelWidthText;
  protected Text conditionExpressionText;
  
  @Override
  public void createFormControls(TabbedPropertySheetPage aTabbedPropertySheetPage) {
    flowLabelWidthText = createTextControl(false);
    createLabel("Label width (50-500)", flowLabelWidthText);
    conditionExpressionText = createTextControl(true);
    createLabel("Condition", conditionExpressionText);
  }

  @Override
  protected Object getModelValueForControl(Control control, Object businessObject) {
    SequenceFlow sequenceFlow = (SequenceFlow) businessObject;
    if (control == flowLabelWidthText) {
      EList<ConnectionDecorator> decoratorList = ((FreeFormConnection) getSelectedPictogramElement()).getConnectionDecorators();
      for (ConnectionDecorator decorator : decoratorList) {
        if (decorator.getGraphicsAlgorithm() instanceof org.eclipse.graphiti.mm.algorithms.MultiText) {
          org.eclipse.graphiti.mm.algorithms.MultiText text = (org.eclipse.graphiti.mm.algorithms.MultiText) decorator.getGraphicsAlgorithm();
          return "" + text.getWidth();
        }
      }
      
    } else if (control == conditionExpressionText) {
      return sequenceFlow.getConditionExpression();
    }
    return null;
  }

  @Override
  protected void storeValueInModel(Control control, Object businessObject) {
    SequenceFlow sequenceFlow = (SequenceFlow) businessObject;
    if (control == flowLabelWidthText) {
      EList<ConnectionDecorator> decoratorList = ((FreeFormConnection) getSelectedPictogramElement()).getConnectionDecorators();
      for (ConnectionDecorator decorator : decoratorList) {
        if (decorator.getGraphicsAlgorithm() instanceof org.eclipse.graphiti.mm.algorithms.MultiText) {
          final org.eclipse.graphiti.mm.algorithms.MultiText text = (org.eclipse.graphiti.mm.algorithms.MultiText) decorator.getGraphicsAlgorithm();
          final String widthText = flowLabelWidthText.getText();
          
          if (NumberUtils.isNumber(widthText)) {
            long width = Long.valueOf(widthText);
            if (width >= 50 || width <= 500) {
              TextUtil.setTextSize((int) width, text);
            }
          }
        }
      }
      
    } else if (control == conditionExpressionText) {
      sequenceFlow.setConditionExpression(conditionExpressionText.getText());
    }
  }
}
