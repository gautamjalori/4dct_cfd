package macro;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import star.common.*;
import star.base.neo.*;
import star.morpher.*;
import star.motion.*;

public class rs_interfaceBoundary_withoutMorphing extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    int n_periods = 360;
    int period_count = 0;
    Double period_length = 0.01;

    Simulation simulation_0 = 
      getActiveSimulation();
      
    PhysicalTimeStoppingCriterion stop_time = 
      ((PhysicalTimeStoppingCriterion) simulation_0.getSolverStoppingCriterionManager().getSolverStoppingCriterion("Maximum Physical Time"));
      
    Double time1 = getActiveSimulation().getSolution().getPhysicalTime();
    long round_time = Math.round(time1 * 1000.0)/10;
    period_count = (int) round_time; 
    simulation_0.println("Period Count: " + period_count);
    BoundaryInterface boundaryInterface_0 = ((BoundaryInterface) simulation_0.getInterfaceManager().getInterface("interface_PM"));
    boundaryInterface_0.swapBoundaries();
    

    while(period_count <= n_periods-1){
      
      //Set max physical time equal to period length and run until then
      period_count = period_count + 1;
      stop_time.setMaximumTime(period_length * period_count);
      simulation_0.getSimulationIterator().run();
      
      double extracted_time = getActiveSimulation().getSolution().getPhysicalTime();
      
      double time = Math.round(extracted_time * 100.0)/100.0;
      
      simulation_0.println("Simulation Time: " + time);
      
      
      /*
      if ((time >= 0.3) && (time < 0.32)){
          //switch to PM - closing
	  
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(1.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(0.3, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_3 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("baffle"));
          Units units_3 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_3.getQuantity().setValueAndUnits(0.0, units_3);

          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 0.32) && (time < 0.48)){
          // switch to baffle
          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(0.32, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_3 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("baffle"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_3.getQuantity().setValueAndUnits(1.0, units_1);
          
          BaffleInterface baffleInterface_0 = 
            ((BaffleInterface) simulation_0.get(ConditionTypeManager.class).get(BaffleInterface.class));
          boundaryInterface_0.setInterfaceType(baffleInterface_0);
      }
      
      if ((time >= 0.48) && (time < 0.5)){
          //switch to PM - opening
          
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(0.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(0.48, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_3 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("baffle"));
          Units units_3 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_3.getQuantity().setValueAndUnits(0.0, units_3);
          
          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 0.5) && (time < 0.6)){
          InternalInterface internalInterface_0 = 
          ((InternalInterface) simulation_0.get(ConditionTypeManager.class).get(InternalInterface.class));
          boundaryInterface_0.setInterfaceType(internalInterface_0);
      }
      */
      
      if (time == 0.6){
          boundaryInterface_0.swapBoundaries();
      }
      
      
      
      if (time == 1.2){
          boundaryInterface_0.swapBoundaries();
      }
      
      /*
      if ((time >= 1.5) && (time < 1.52)){
          //switch to PM - closing
	  
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(1.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(1.5, units_2);

          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 1.52) && (time < 1.68)){
          // switch to baffle
          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(1.52, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_3 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("baffle"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_3.getQuantity().setValueAndUnits(1.0, units_1);
          
          BaffleInterface baffleInterface_0 = 
            ((BaffleInterface) simulation_0.get(ConditionTypeManager.class).get(BaffleInterface.class));
          boundaryInterface_0.setInterfaceType(baffleInterface_0);
      }
      
      if ((time >= 1.68) && (time < 1.70)){
          //switch to PM - opening
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(0.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(1.68, units_2);
          
          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 1.70) && (time < 2.7)){
          InternalInterface internalInterface_0 = 
          ((InternalInterface) simulation_0.get(ConditionTypeManager.class).get(InternalInterface.class));
          boundaryInterface_0.setInterfaceType(internalInterface_0);
      }
      
      if ((time >= 2.7) && (time < 2.72)){
          //switch to PM - closing
	  
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(1.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(2.7, units_2);

          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 2.72) && (time < 2.88)){
          // switch to baffle
          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(2.72, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_3 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("baffle"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_3.getQuantity().setValueAndUnits(1.0, units_1);
          
          BaffleInterface baffleInterface_0 = 
            ((BaffleInterface) simulation_0.get(ConditionTypeManager.class).get(BaffleInterface.class));
          boundaryInterface_0.setInterfaceType(baffleInterface_0);
      }
      
      if ((time >= 2.88) && (time < 2.90)){
          //switch to PM - opening
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(0.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(2.88, units_2);
          
          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 2.90) && (time < 3.6)){
          InternalInterface internalInterface_0 = 
          ((InternalInterface) simulation_0.get(ConditionTypeManager.class).get(InternalInterface.class));
          boundaryInterface_0.setInterfaceType(internalInterface_0);
      }
      */
      


    }
  }
}
