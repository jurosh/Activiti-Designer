package org.activiti.designer.validation.bpmn20.validation.worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.designer.eclipse.Logger;
import org.eclipse.graphiti.mm.pictograms.Diagram;

/**
 * Abstract validator with helper methods and attributes
 * 
 * @author Jurosh
 *
 */
public abstract class AbstractValidationWorker implements ProcessValidationWorker {

  /**
   * Active diagram
   */
  protected Diagram diagram;

  /**
   * Active set of nodes indexed by classes types
   */
  protected Map<String, List<Object>> nodes;

  /**
   * Active set of nodes indexed by its IDs
   */
  protected Map<String, BaseElement> nodesIndexById = new HashMap<String, BaseElement>();

  /**
   * Results
   */
  protected Collection<ProcessValidationWorkerMarker> results = new ArrayList<ProcessValidationWorkerMarker>();

  /**
   * Validate method, needs to be implemented.
   * 
   * Have access to `diagram`, `nodes` for verification and `results` for
   * creating result object
   */
  public abstract void validate();

  @Override
  public Collection<ProcessValidationWorkerMarker> validate(Diagram diagram, Map<String, List<Object>> processNodes) {

    // set object attributes
    this.diagram = diagram;
    this.nodes = processNodes;

    // TODO do just once, pass as parameter?
    for (List<Object> nodesList : processNodes.values()) {
      for (Object object : nodesList) {
        if (object instanceof BaseElement) {
          BaseElement baseElement = (BaseElement) object;
          nodesIndexById.put(baseElement.getId().toString(), baseElement);
        }
      }
    }
    
    debug();

    // call validate function
    validate();

    // return result
    return results;
  }

  private void debug() {
    StringBuilder sb = new StringBuilder("DEBUG Elements:");
    Set<String> keys = nodesIndexById.keySet();
    for (String key : keys) {
      sb.append(key);
      sb.append(" - ");
      sb.append(nodesIndexById.get(key).getId());
      sb.append("\n");
    }
    sb.append("DEBUG End;");
    Logger.logDebug(sb.toString());
  }

  /**
   * Get nodes defined by class, Pool.class, Activity.class, etc.
   * 
   * @param clas
   *          class of node
   * @return
   */
  protected <T extends BaseElement> List<T> getNodes(Class<T> clas) {
    List< ? extends Object> listing = nodes.get(clas.getCanonicalName());
    return listing == null ? new ArrayList<T>() : (List<T>) listing;
  }

  /**
   * Get node by its ID
   * 
   * @param id
   * @return
   */
  public BaseElement getNode(String id) {
    return nodesIndexById.get(id);
  }

}
