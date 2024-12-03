# -*- coding: utf-8 -*-
"""
Created on Mon Jul 22 20:34:23 2024

@author: jalori
"""

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

public class run_sim extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {
    
      Simulation simulation_0 = 
      getActiveSimulation();
    }
  }

