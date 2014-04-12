package org.activiti.designer.validation.bpmn20.validation.util;

import java.util.HashSet;
import java.util.Set;

import org.activiti.bpmn.model.EventGateway;
import org.activiti.bpmn.model.InclusiveGateway;
import org.activiti.bpmn.model.ParallelGateway;

/**
 * Utils for gateways
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 *
 */
final public class BpmnGatewayUtils {
  
  public static Set<Class> getAllGatewaysClasses() {
    Set<Class> classes = new HashSet<Class>();
    classes.add(InclusiveGateway.class);
    classes.add(EventGateway.class);
    classes.add(ParallelGateway.class);
    return classes;
  }

}
