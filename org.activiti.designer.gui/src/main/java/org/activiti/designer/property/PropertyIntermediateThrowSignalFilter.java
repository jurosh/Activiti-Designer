package org.activiti.designer.property;

import org.activiti.bpmn.model.ThrowEvent;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class PropertyIntermediateThrowSignalFilter extends ActivitiPropertyFilter {
	
	@Override
	protected boolean accept(PictogramElement pe) {
		Object bo = getBusinessObject(pe);
		if (bo instanceof ThrowEvent && ((ThrowEvent) bo).getEventDefinitions().size() > 0) {
		  return true;
		}
		return false;
	}

}
