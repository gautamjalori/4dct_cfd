// Simcenter STAR-CCM+ macro: delete_table_file.java
// Written by Simcenter STAR-CCM+ 19.02.009
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;

public class delete_table_file extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    FileTable fileTable_75 = 
      ((FileTable) simulation_0.getTableManager().getTable("StarControlPoinstFull_2mmSpacing_Periodic_5Cycles_Inc_interpolated_reference_subj05_single_file_cycle0"));

    simulation_0.deleteObjects(new ArrayList<>(Arrays.<ClientServerObject>asList(fileTable_75)));
  }
}
