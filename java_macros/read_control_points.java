// Simcenter STAR-CCM+ macro: read_control_points.java
// Written by Michael Barbour, Simcenter STAR-CCM+ 18.06.006
// Reads in all control point files located in a specified directory
//

package macro;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import star.common.*;
import star.base.neo.*;
import star.morpher.*;
import star.motion.*;


public class read_control_points extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();
    /*  
    FileTable fileTable_ini = 
      (FileTable) simulation_0.getTableManager().createFromFile(resolvePath("D:\\Jalori\\RobinSequence\\Subj14\\ini_file\\subj14_sparse_cp_function_00.csv"), null);
    */

    File f = null;
    String[] paths;
    int numFiles = 0;


    f = new File("D:/Jalori/RobinSequence/4DCT/Subj05/single_dt_csvs/");
    paths = f.list();


    for(String path:paths){

      simulation_0.println(path);

      FileTable fileTable_100 = (FileTable) simulation_0.getTableManager().createFromFile(resolvePath(f + "/" + path), null);
      numFiles++;
    }

    simulation_0.println("Loaded " + numFiles + " Control Point Files");
    
    String table_path = paths[0];
    String[] parts = table_path.split("\\.(?=[^\\.]+$)");
    String table_name = parts[0];

    FileTable fileTable_100 = 
      ((FileTable) simulation_0.getTableManager().getTable(table_name));
    
    PointSet pointSet_0 = 
      simulation_0.get(PointSetManager.class).createTablePointSet("Table Point Set Run", fileTable_100, "X", "Y", "Z");

    TablePointGenerator tablePointGenerator_0 = 
      ((TablePointGenerator) pointSet_0.getPointGenerator());

    tablePointGenerator_0.setTable(fileTable_100);

    tablePointGenerator_0.setX0Data("X");

    tablePointGenerator_0.setY0Data("Y");

    tablePointGenerator_0.setZ0Data("Z");

    tablePointGenerator_0.regeneratePointSet();

    PointSetMotionSpecification pointSetMotionSpecification_0 = 
      pointSet_0.getValues().get(PointSetMotionSpecification.class);

    MorphingMotion morphingMotion_0 = 
      ((MorphingMotion) simulation_0.get(MotionManager.class).getObject("Morphing"));

    pointSetMotionSpecification_0.setMotion(morphingMotion_0);

    IncrementalDisplacementProfile incrementalDisplacementProfile_0 = 
      pointSet_0.getValues().get(IncrementalDisplacementProfile.class);

    incrementalDisplacementProfile_0.setMethod(TimeXyzTabularVectorProfileMethod.class);

    incrementalDisplacementProfile_0.getMethod(TimeXyzTabularVectorProfileMethod.class).setTable(fileTable_100);

    incrementalDisplacementProfile_0.getMethod(TimeXyzTabularVectorProfileMethod.class).setXData("X");

    incrementalDisplacementProfile_0.getMethod(TimeXyzTabularVectorProfileMethod.class).setYData("Y");

    incrementalDisplacementProfile_0.getMethod(TimeXyzTabularVectorProfileMethod.class).setZData("Z");
    
  }
}
