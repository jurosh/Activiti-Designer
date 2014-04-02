package org.activiti.designer.eclipse.extension.validation;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.BaseElement;

/**
 * Process verification results listing
 * 
 * @author Jurosh
 *
 */
public class ValidationResults {
	
	private List<ValidationResult> results = new ArrayList<ValidationResult>();
	
	public void add(ValidationResult result) {
		results.add(result);
	}
	
	public List<ValidationResult> getResults() {
		return results;
	}
	
	/**
	 * Validation result wrapper
	 * 
	 * @author Jurosh
	 *
	 */
	public static class ValidationResult {
	
		private int type;
		private String reason;
		
		private BaseElement element1;
		private BaseElement element2;
	
		public ValidationResult(int severity, String reason, BaseElement... element) {
			this.type = severity;
			this.reason = reason;
			this.element1 = element[0];
			if(element.length > 1) {
				this.element2 = element[1];
			}
		}
	
		@Override
		public String toString() {
			return type + " " + reason;
		}
	
		public int getType() {
			return type;
		}
	
		public String getElement1Id() {
			return element1 == null ? "" : element1.getId();
		}
	
		public String getElement2Id() {
			return element2 == null ? "" : element2.getId();
		}
	
		public String getReason() {
			return reason;
		}

		public BaseElement getElement() {
			return element1;
		}
	
	}
}
