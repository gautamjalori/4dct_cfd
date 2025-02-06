// Simcenter STAR-CCM+ macro: sparse_ini_file_upload.java
// Written by Simcenter STAR-CCM+ 19.02.009
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;

public class sparse_ini_file_upload extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    FileTable fileTable_331 = 
      (FileTable) simulation_0.getTableManager().createFromFile(resolvePath("D:\\Jalori\\RobinSequence\\Subj14\\cp_sparse_csv_dt001\\ini_file\\subj14_sparse_cp_function_00.csv"), null);
  }
}
