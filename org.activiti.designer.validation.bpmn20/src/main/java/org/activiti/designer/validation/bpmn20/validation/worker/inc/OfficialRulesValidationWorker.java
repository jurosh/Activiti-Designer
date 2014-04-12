package org.activiti.designer.validation.bpmn20.validation.worker.inc;

import org.activiti.designer.validation.bpmn20.validation.worker.AbstractValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.ProcessValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.rules.Level1ValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.rules.Level2EndValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.rules.Level2GatewayValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.rules.Level2MessageFlowValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.rules.Level2PoolValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.rules.Level2SequenceFlowValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.rules.Level2StartValidationWorker;

/**
 * Official BPMN rules validation
 * 
 * @author Juraj Husar (jurosh@jurosh.com)
 * 
 */
public class OfficialRulesValidationWorker extends AbstractValidationWorker {

  @Override
  public void validate() {

    // run all sub-validator workers

    // BPMN 2.0 Level 1 [O]
    ProcessValidationWorker level1ValidationRulesWorker = new Level1ValidationWorker();
    results.addAll(level1ValidationRulesWorker.validate(diagram, nodes));

    // BPMN 2.0 Level 2
    // sequence flow [S]
    ProcessValidationWorker seqFlowWorker = new Level2SequenceFlowValidationWorker();
    results.addAll(seqFlowWorker.validate(diagram, nodes));

    // message flow [M]
    ProcessValidationWorker messageFlowWorker = new Level2MessageFlowValidationWorker();
    results.addAll(messageFlowWorker.validate(diagram, nodes));

    // start element [s]
    ProcessValidationWorker startElementWorker = new Level2StartValidationWorker();
    results.addAll(startElementWorker.validate(diagram, nodes));

    // end element [e]
    ProcessValidationWorker endElementWorker = new Level2EndValidationWorker();
    results.addAll(endElementWorker.validate(diagram, nodes));

    // gateways [G]
    ProcessValidationWorker gatewaysWorker = new Level2GatewayValidationWorker();
    results.addAll(gatewaysWorker.validate(diagram, nodes));

    // pools [P]
    ProcessValidationWorker poolsWorker = new Level2PoolValidationWorker();
    results.addAll(poolsWorker.validate(diagram, nodes));
    
    
    // TODO all other..

  }

}
