package org.activiti.designer.eclipse.views.validator.config;

import java.io.Serializable;
import java.util.Set;

/**
 * Configuration
 * 
 * @author Jurosh
 *
 */
public class ValidationConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean enabledCheckBpmn20;
	private boolean enabledCheckStyleRules;
	
	private Set<Integer> enabledCheckLevels;

}
