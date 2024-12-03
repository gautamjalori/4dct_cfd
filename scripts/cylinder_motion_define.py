#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Jan 10 11:15:26 2024

@author: mbarb1
"""


import pyvista as pv
import pandas as pd
import numpy as np
import glob
import matplotlib.pyplot as plt


from scipy.interpolate import CubicSpline
import plotly.graph_objects as go
import plotly.express as px
import plotly.io as pio
from plotly.subplots import make_subplots

# from source.star_control_points import *


pio.renderers.default = "browser"
colors = px.colors.sequential.Jet


#%% Read in control point motion for the cylinder test motion file


df = pd.read_csv("D:/Jalori/RobinSequence/close_valve/new_cylinder_motion/moving_mesh_interface_pressure_boundary/constricted_cylinder_control_points_start_030.csv")

df = df*0.0025

p0 = pv.PolyData(df.iloc[:,0:3].values)
p1 = pv.PolyData(df.iloc[:,3:6].values)
p2 = pv.PolyData(df.iloc[:,6:9].values)
p3 = pv.PolyData(df.iloc[:,9:12].values)
p4 = pv.PolyData(df.iloc[:,12:15].values)
p5 = pv.PolyData(df.iloc[:,15:18].values)
p6 = pv.PolyData(df.iloc[:,18:21].values)
p7 = pv.PolyData(df.iloc[:,21:24].values)
p8 = pv.PolyData(df.iloc[:,24:27].values)
p9 = pv.PolyData(df.iloc[:,27:30].values)
p10 = pv.PolyData(df.iloc[:,30:33].values)
p11 = pv.PolyData(df.iloc[:,33:36].values)
p12 = pv.PolyData(df.iloc[:,36:39].values)

cmap =  plt.cm.get_cmap("jet", 15)

cp_polys = [p0,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12]

p = pv.Plotter()
df_save = []



for count,poly in enumerate(cp_polys):
    
    p.add_mesh(poly, color=cmap(count)[0:3], label=str(count), render_points_as_spheres=True)
    df_star = pd.DataFrame(data = poly.points, columns=["X","Y","Z"])
    df_save.append(df_star)
    
p.background='white' 
p.add_legend()
p.show()


#%% structure the data and interpolate
df_save.append(df_save[0])

new_time = np.arange(0, 1.2+0.00001, 0.001)   
x_new, y_new, z_new = interpolate_controlPoints_time(df_save, 0.1, new_time, show=True) # first and last points are the same



#%% convert to incrimental displacement 

df_periodic_star = periodic_star_table_IncDisp_fromArraysV2(x_new, y_new, z_new, dt=0.001, n_cycles=1, start_time = 0.0)  

df_periodic_star.to_csv("D:\Jalori\RobinSequence\close_valve/cylinder_cosine_constriction\csvs/cylinder_constriction_control_points_incDisp_1stCycle_dt001_00.csv", index=False)


#%% convert to incrimental displacement 

df_periodic_star = periodic_star_table_IncDisp_fromArraysV2(x_new, y_new, z_new, dt=0.001, n_cycles=1, start_time = 0.4)  

df_periodic_star.to_csv("D:\Jalori\RobinSequence\close_valve/cylinder_cosine_constriction\csvs/cylinder_constriction_control_points_incDisp_1stCycle_dt001_01.csv", index=False)

#%% convert to incrimental displacement 

df_periodic_star = periodic_star_table_IncDisp_fromArraysV2(x_new, y_new, z_new, dt=0.001, n_cycles=1, start_time = 0.8)  

df_periodic_star.to_csv("D:\Jalori\RobinSequence\close_valve/cylinder_cosine_constriction\csvs/cylinder_constriction_control_points_incDisp_1stCycle_dt001_02.csv", index=False)


#%% convert to incrimental displacement - multiple file at interval of given dt for multiple cycles
work_dir = 'D:/Jalori/RobinSequence/close_valve/new_cylinder_motion/moving_mesh_interface_pressure_boundary/csvs_dt001/'
end_time = 3.6
start_time_split = 0.0
dt_file = 0.01
iter = np.arange(start_time_split, end_time, dt_file)

for i in iter:
    df_periodic_star = reference_periodic_star_table_IncDisp_fromArrays_split_01(x_new, y_new, z_new, dt=0.001, n_cycles=1, start_time=i, div=120, cycle_len=1.2)
    # df_periodic_star = df_periodic_star*1e-3
    df_periodic_star.to_csv(work_dir +f"cylinder_constriction_control_points_incDisp_0001_dt_cycle{int(i*100+0.001)}.csv", index=False)
    

#%% convert to incrimental displacement - single file for multiple cycles

df_periodic_star = periodic_star_table_IncDisp_fromArraysV2(x_new, y_new, z_new, dt=0.01, n_cycles=3, start_time = 0.0)  

df_periodic_star.to_csv("D:\Jalori\RobinSequence\close_valve/cylinder_constriction_control_points_incDisp_allCycle_dt01.csv", index=False)



