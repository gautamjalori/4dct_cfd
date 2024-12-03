// Simcenter STAR-CCM+ macro: run_control_point_read.java
// Written by Michael Barbour
// Macro manages a simulation with control point defined motion. At a specific time interval, a new contorl points file is read in and the displacement field is updated

package macro;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import star.common.*;
import star.base.neo.*;
import star.morpher.*;
import star.motion.*;

public class run_control_point_SplitFiles extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    int n_periods = 120;
    int period_count = 0;
    Double period_length = 0.01;
    // String control_point_file_prefix = "StarControlPoinstFull_2mmSpacing_Periodic_5Cycles_Inc_interpolated_ptj05_01s_cycle";

    File f = null;
    String[] paths;

    f = new File("/D:/Jalori/RobinSequence/close_valve/cylinder_cosine_constriction/csvs_01/");
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

    
      
    Double time = getActiveSimulation().getSolution().getPhysicalTime();
    long round_time = Math.round(time/period_length);
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
      
      time = getActiveSimulation().getSolution().getPhysicalTime();
      
      simulation_0.println("Simulation Time: " + time);
      



    }
  }
}
