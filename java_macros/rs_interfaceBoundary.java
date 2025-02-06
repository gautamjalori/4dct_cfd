package macro;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import star.common.*;
import star.base.neo.*;
import star.morpher.*;
import star.motion.*;

public class rs_interfaceBoundary extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    int n_periods = 300;
    int period_count = 0;
    Double period_length = 0.01; 
    // String control_point_file_prefix = "StarControlPoinstFull_2mmSpacing_Periodic_5Cycles_Inc_interpolated_ptj05_01s_cycle";

    File f = null;
    String[] paths;

    f = new File("D:/Jalori/RobinSequence/subj14/cp_csv_dt001/");
    paths = f.list();


    Simulation simulation_0 = 
      getActiveSimulation();

    PhysicalTimeStoppingCriterion stop_time = 
      ((PhysicalTimeStoppingCriterion) simulation_0.getSolverStoppingCriterionManager().getSolverStoppingCriterion("Maximum Physical Time"));
      
    // Custom comparator to compare the numeric part of the strings
    Comparator<String> numericComparator = new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            int num1 = extractNumber(s1);
            int num2 = extractNumber(s2);
            return Integer.compare(num1, num2);
        }

        // Method to extract the last numeric part from the string
        private int extractNumber(String s) {
            String num = s.substring(s.lastIndexOf('e') + 1, s.length() - 4); // Remove ".csv"
            return Integer.parseInt(num);
        }
    };

    // Sort the array using the custom comparator
    Arrays.sort(paths, numericComparator);
      
    for(String path:paths){

      simulation_0.println(path);
      
    }

    BoundaryInterface boundaryInterface_0 = ((BoundaryInterface) simulation_0.getInterfaceManager().getInterface("interface_PM"));
    // boundaryInterface_0.swapBoundaries();
      
    Double time1 = getActiveSimulation().getSolution().getPhysicalTime();
    long round_time = Math.round(time1 * 100.0)/10;
    period_count = (int) round_time; 
    simulation_0.println("Period Count: " + period_count);
    

    while(period_count <= n_periods-1){


      // Get the correct file table 
      String table_path = paths[period_count];
      String[] parts = table_path.split("\\.(?=[^\\.]+$)");
      String table_name = parts[0];
      
      FileTable fileTable_0 = 
        ((FileTable) simulation_0.getTableManager().getTable(table_name));

      // Update PointSets displacement field with new table
      PointSet pointSet_0 = 
      ((PointSet) simulation_0.get(PointSetManager.class).getObject("Table Point Set Run"));
      
      TablePointGenerator tablePointGenerator_0 = 
        ((TablePointGenerator) pointSet_0.getPointGenerator());

      tablePointGenerator_0.setTable(fileTable_0);

      tablePointGenerator_0.setX0Data("X");

      tablePointGenerator_0.setY0Data("Y");

      tablePointGenerator_0.setZ0Data("Z");

      tablePointGenerator_0.regeneratePointSet();
     


      IncrementalDisplacementProfile incrementalDisplacementProfile_0 = 
      pointSet_0.getValues().get(IncrementalDisplacementProfile.class);

      incrementalDisplacementProfile_0.getMethod(TimeXyzTabularVectorProfileMethod.class).setTable(fileTable_0);
      
      
      //Set max physical time equal to period length and run until then
      simulation_0.println("Loading CP Table: " + table_name);
      period_count = period_count + 1;
      stop_time.setMaximumTime(period_length * period_count);
      simulation_0.getSimulationIterator().run();
      
      double extracted_time = getActiveSimulation().getSolution().getPhysicalTime();
      
      double time = Math.round(extracted_time * 100.0)/100.0;
      
      simulation_0.println("Simulation Time: " + time);
      
      
      if ((time >= 0.25) && (time < 0.30)){
          //switch to PM - closing
	  
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(1.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(0.25, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_3 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("baffle"));
          Units units_3 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_3.getQuantity().setValueAndUnits(0.0, units_3);
          
          ScalarGlobalParameter scalarGlobalParameter_4 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampDuration"));
          Units units_4 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_4.getQuantity().setValueAndUnits(0.05, units_4);

          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 0.30) && (time < 0.48)){
          // switch to baffle
          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(0.30, units_2);
          
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
          
          ScalarGlobalParameter scalarGlobalParameter_4 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampDuration"));
          Units units_4 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_4.getQuantity().setValueAndUnits(0.02, units_4);
          
          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 0.5) && (time < 1.0)){
          InternalInterface internalInterface_0 = 
          ((InternalInterface) simulation_0.get(ConditionTypeManager.class).get(InternalInterface.class));
          boundaryInterface_0.setInterfaceType(internalInterface_0);
      }
      
      
      if (time == 0.5){
          boundaryInterface_0.swapBoundaries();
      }
      
      if (time == 1.0){
          boundaryInterface_0.swapBoundaries();
      }
      
      
      if ((time >= 1.25) && (time < 1.30)){
          //switch to PM - closing
	  
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(1.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(1.25, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_4 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampDuration"));
          Units units_4 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_4.getQuantity().setValueAndUnits(0.05, units_4);

          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 1.30) && (time < 1.48)){
          // switch to baffle
          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(1.30, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_3 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("baffle"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_3.getQuantity().setValueAndUnits(1.0, units_1);
          
          BaffleInterface baffleInterface_0 = 
            ((BaffleInterface) simulation_0.get(ConditionTypeManager.class).get(BaffleInterface.class));
          boundaryInterface_0.setInterfaceType(baffleInterface_0);
      }
      
      if ((time >= 1.48) && (time < 1.50)){
          //switch to PM - opening
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(0.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(1.48, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_4 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampDuration"));
          Units units_4 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_4.getQuantity().setValueAndUnits(0.02, units_4);
          
          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 1.50) && (time < 2.0)){
          InternalInterface internalInterface_0 = 
          ((InternalInterface) simulation_0.get(ConditionTypeManager.class).get(InternalInterface.class));
          boundaryInterface_0.setInterfaceType(internalInterface_0);
      }
      
      if (time == 1.5){
          boundaryInterface_0.swapBoundaries();
      }
      
      if (time == 2.0){
          boundaryInterface_0.swapBoundaries();
      }
      
      if ((time >= 2.25) && (time < 2.3)){
          //switch to PM - closing
	  
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(1.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(2.25, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_4 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampDuration"));
          Units units_4 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_4.getQuantity().setValueAndUnits(0.05, units_4);

          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 2.30) && (time < 2.48)){
          // switch to baffle
          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(2.30, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_3 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("baffle"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_3.getQuantity().setValueAndUnits(1.0, units_1);
          
          BaffleInterface baffleInterface_0 = 
            ((BaffleInterface) simulation_0.get(ConditionTypeManager.class).get(BaffleInterface.class));
          boundaryInterface_0.setInterfaceType(baffleInterface_0);
      }
      
      if ((time >= 2.48) && (time < 2.50)){
          //switch to PM - opening
          ScalarGlobalParameter scalarGlobalParameter_1 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("closing_FLAG"));
          Units units_1 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_1.getQuantity().setValueAndUnits(0.0, units_1);

          ScalarGlobalParameter scalarGlobalParameter_2 = ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampStart"));
          Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("s"));
          scalarGlobalParameter_2.getQuantity().setValueAndUnits(2.48, units_2);
          
          ScalarGlobalParameter scalarGlobalParameter_4 = 
            ((ScalarGlobalParameter) simulation_0.get(GlobalParameterManager.class).getObject("rampDuration"));
          Units units_4 = ((Units) simulation_0.getUnitsManager().getObject(""));
          scalarGlobalParameter_4.getQuantity().setValueAndUnits(0.02, units_4);
          
          PorousBaffleInterface porousBaffleInterface_0 = 
          ((PorousBaffleInterface) simulation_0.get(ConditionTypeManager.class).get(PorousBaffleInterface.class));
          boundaryInterface_0.setInterfaceType(porousBaffleInterface_0);
      }
      
      if ((time >= 2.50) && (time < 3.0)){
          InternalInterface internalInterface_0 = 
          ((InternalInterface) simulation_0.get(ConditionTypeManager.class).get(InternalInterface.class));
          boundaryInterface_0.setInterfaceType(internalInterface_0);
      }
      
      if (time == 2.5){
          boundaryInterface_0.swapBoundaries();
      }

    }
  }
}
