// STAR-CCM+ macro: interfaceChange.java
// Written by STAR-CCM+ 15.02.009
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;

public class interfaceChange extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = getActiveSimulation();

    PhysicalTimeStoppingCriterion physicalTimeStoppingCriterion_0 = 
      ((PhysicalTimeStoppingCriterion) simulation_0.getSolverStoppingCriterionManager().getSolverStoppingCriterion("Maximum Physical Time"));

    //stopping crit and run until stop
    physicalTimeStoppingCriterion_0.getMaximumTime().setValue(0.8);
    simulation_0.getSimulationIterator().run();

    //switch to PM - closing
    BoundaryInterface boundaryInterface_0 = ((BoundaryInterface) simulation_0.getInterfaceManager().getInterface("interface_PM"));
    PorousBaffleInterface porousBaffleInterface_0 = 
    ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
    boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);

    //stopping crit and run until stop
    physicalTimeStoppingCriterion_0.getMaximumTime().setValue(.5);
    simulation_0.getSimulationIterator().run();

    //switch to baffle
    BaffleInterface baffleInterface_0 = 
      ((BaffleInterface) simulation_0.get(ConditionTypeManager.class).get(BaffleInterface.class));
    boundaryInterface_0.setInterfaceType(baffleInterface_0);

    //stopping crit and run until stop (end)
    physicalTimeStoppingCriterion_0.getMaximumTime().setValue(.55);
    simulation_0.getSimulationIterator().run();

    //switch to PM - opening
    ScalarGlobalParameter scalarGlobalParameter_1 = 
      ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
    Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
    scalarGlobalParameter_1.getQuantity().setValueAndUnits(0.0, units_1);

    ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
    Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
    scalarGlobalParameter_2.getQuantity().setValueAndUnits(0.55, units_2);
    
    boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);

    //stopping crit and run until stop
    physicalTimeStoppingCriterion_0.getMaximumTime().setValue(.65);
    simulation_0.getSimulationIterator().run();

    InternalInterface internalInterface_0 = 
    ((InternalInterface) simulation_0.get(ConditionTypeManager.class).get(InternalInterface.class));
    boundaryInterface_0.setInterfaceType(internalInterface_0);

    physicalTimeStoppingCriterion_0.getMaximumTime().setValue(1.0);
    simulation_0.getSimulationIterator().run();

  }
}
