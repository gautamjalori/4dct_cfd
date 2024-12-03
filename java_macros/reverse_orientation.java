// Simcenter STAR-CCM+ macro: reverse_orientation.java
// Written by Simcenter STAR-CCM+ 19.02.009
package macro;

import java.util.*;

import star.common.*;

public class reverse_orientation extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    BoundaryInterface boundaryInterface_0 = 
      ((BoundaryInterface) simulation_0.getInterfaceManager().getInterface("interface_PM"));

    boundaryInterface_0.swapBoundaries();
  }
}
