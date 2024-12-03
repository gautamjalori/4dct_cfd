// Simcenter STAR-CCM+ macro: target_pos_recording.java
// Written by Simcenter STAR-CCM+ 19.02.009
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;
import star.morpher.*;
import star.motion.*;

public class target_pos_recording extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    FileTable fileTable_0 = 
      ((FileTable) simulation_0.getTableManager().getTable("StarControlPoinstFull_2mmSpacing_Periodic_5Cycles_target_pos_subj05_01s_cycle0"));

    PointSet pointSet_1 = 
      simulation_0.get(PointSetManager.class).createTablePointSet("Table Point Set", fileTable_0, "X", "Y", "Z");

    PointSetMotionSpecification pointSetMotionSpecification_0 = 
      pointSet_1.getValues().get(PointSetMotionSpecification.class);

    MorphingMotion morphingMotion_0 = 
      ((MorphingMotion) simulation_0.get(MotionManager.class).getObject("Morphing"));

    pointSetMotionSpecification_0.setMotion(morphingMotion_0);

    pointSet_1.getConditions().get(DisplacementSpecification.class).setSelected(DisplacementSpecification.Type.TARGET_POSITION);

    TargetPositionProfile targetPositionProfile_0 = 
      pointSet_1.getValues().get(TargetPositionProfile.class);

    targetPositionProfile_0.setMethod(TimeXyzTabularVectorProfileMethod.class);

    targetPositionProfile_0.getMethod(TimeXyzTabularVectorProfileMethod.class).setTable(fileTable_0);

    targetPositionProfile_0.getMethod(TimeXyzTabularVectorProfileMethod.class).setXData("X");

    targetPositionProfile_0.getMethod(TimeXyzTabularVectorProfileMethod.class).setYData("Y");

    targetPositionProfile_0.getMethod(TimeXyzTabularVectorProfileMethod.class).setZData("Z");
  }
}
