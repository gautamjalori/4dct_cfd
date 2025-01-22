# -*- coding: utf-8 -*-
"""
Created on Mon Apr  8 16:13:07 2024

@author: jalori
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

#%% Load in the original and remeshed surfaces

work_dir = '/Jalori/RobinSequence/subj14/airway_18/'

surface_file_dir = work_dir

surf = pv.read(surface_file_dir + "Airway_18_simulation_clip_extn_cap.stl")
surfCoarse = pv.read(work_dir+ "subj14_airway_18_1000_cp.ply")

#uncomment below if loading points from fcsv file
# def read_fcsv(file_path):
#     # Read the .fcsv file
#     with open(file_path, 'r') as file:
#         lines = file.readlines()

#     # Skip the first lines until the data
#     start = 0
#     for i, line in enumerate(lines):
#         if line.startswith('#'):
#             start = i + 1
#         else:
#             break

#     # Read the data using pandas
#     data = pd.read_csv(file_path, skiprows=start)
    
#     # Assuming the first three columns are x, y, z coordinates
#     points = data.iloc[:, 1:4].values

#     return points

# # Load points from the .fcsv file
# points = read_fcsv(work_dir + 'control_points_mesh_3000_extn.fcsv')

# # Create a PyVista PolyData object
# surfCoarse = pv.PolyData(points)
#uncomment above if loading points from fcsv file

# surf.scale(1e3, inplace=True)
# surfCoarse.scale(1e3, inplace=True)

p = pv.Plotter()
p.add_mesh(surf)
p.add_mesh(surfCoarse.points, render_points_as_spheres=True, color='red')
p.show()

#%% Manually select points to exclude

# coarse_pc = pv.PolyData(surfCoarse.points)
coarse_pc = new_cp
remove_points, remove_ids = pointPickerRemove(coarse_pc, surf)


cp_df = pd.DataFrame(data = coarse_pc.points, columns=["l", "p", "s"])
cp_df.drop(remove_ids, inplace=True)


new_cp = pv.PolyData(np.array([cp_df.l.values, cp_df.p.values, cp_df.s.values]).T)

p = pv.Plotter()
p.add_mesh(surf)
p.add_mesh(new_cp, render_points_as_spheres=True, point_size=15, color='blue')
p.show()

#%% Add control points

new_points, pt_ids = pointPickerAdd(new_cp, surf)

cp_df = pd.DataFrame(data = new_cp.points, columns=["l", "p", "s"])

df_add = pd.DataFrame(data = new_points, columns=["l", "p", "s"])

new_cp_df = cp_df.append(df_add, ignore_index=True)

new_cp = pv.PolyData(np.array([new_cp_df.l.values, new_cp_df.p.values, new_cp_df.s.values]).T)



p = pv.Plotter()
p.add_mesh(surf)
p.add_mesh(new_cp, render_points_as_spheres=True, point_size=15, color='blue')
p.show()



#%% save trimmed control points as cp file to load slicer

df = pd.DataFrame(data = new_cp.points, columns=["l", "p", "s"])

# df = pd.DataFrame(data = remove_points, columns=["l", "p", "s"])
# df = df * 1e+3
df['selected'] = np.ones(len(df))
df['visible'] = np.ones(len(df))
df['locked'] = np.ones(len(df))

df.index.name='label'
# df.index = range(4205, 4205 + len(df))
save_name = work_dir + 'subj05_interface_cp.fcsv'
print(save_name)
df.to_csv(save_name)

#%% Loop and save the cp locations !!! This gets dropped into the slicer python interpretor window

save_dir = 'D:/Jalori/RobinSequence/subj14/cp_fcsv/'
prefix = "Airway_18_cp_subj14_"
transformSequenceID = 'vtkMRMLSequenceNode7' # name of sequence node with registration
# transformSequenceID = 'vtkMRMLTransformNode1'

controlPointNode = getNode("subj05_interface_cp_1")

shNode = slicer.vtkMRMLSubjectHierarchyNode.GetSubjectHierarchyNode(slicer.mrmlScene)
itemIDToClone = shNode.GetItemByDataNode(controlPointNode)


transformSeq = slicer.mrmlScene.GetNodeByID(transformSequenceID)


for node_idx in range(10):
    # get the transform
    
    transformNode = transformSeq.GetNthDataNode(node_idx)
    transform = transformNode.GetTransformToParent()
    
    #copy the original node
    clonedItemID = slicer.modules.subjecthierarchy.logic().CloneSubjectHierarchyItem(shNode, itemIDToClone)
    clonedControlPointNode = shNode.GetItemDataNode(clonedItemID)
    
    #apply transform
    clonedControlPointNode.ApplyTransform(transform)
    save_id = node_idx + 18
    # Write to file
    outputFileName = save_dir + f"{prefix}_{save_id:03}.fcsv"
    print(outputFileName)
    slicer.util.saveNode(clonedControlPointNode, outputFileName)
    
#%% Load the cp locations as calulcated from slicer. Prior to this the control points are defined using remeshing, manually tweaked, and loaded into slicer with the dynamic registration computed

work_dir = 'D:/Jalori/RobinSequence/subj14/cp_fcsv/porous_frame/'
cmap =  plt.cm.get_cmap("jet", 10)
cp_files = sorted(glob.glob(work_dir + "Airway_18_cp_subj14_*.fcsv"))

dfs_save = []
p = pv.Plotter()
polys = []
for count,file in enumerate(cp_files):
    
    print(file)
    df_mod = pd.read_csv(file, skiprows=3, 
                     names=['id','X','Y','Z','ow','ox','oy','oz','vis','sel','lock','label','desc','associatedNodeID','no_idea','no_idea2'])

    df_star = df_mod[["X", "Y", "Z"]]
    dfs_save.append(df_star)

    poly = pv.PolyData(np.array([df_mod.X, df_mod.Y, df_mod.Z]).T)
    polys.append(poly)

    p.add_mesh(poly, color=cmap(count)[0:3])
p.show()



#%% overwrite the last timestep with the first

df_periodic = dfs_save.copy()
# df_periodic[-1] = df_periodic[0]
# df_periodic = np.append(df_periodic, np.atleast_3d(np.transpose(np.stack(np.atleast_3d(df_periodic[0]), axis = 1))), axis = 0)
# df_periodic = [df_periodic] + [np.transpose(np.stack(np.atleast_3d(df_periodic[0]), axis = 1))]
# df_periodic = df_periodic + [df_periodic[0]]

#%% Stack all of the control points into arrays

n_images = len(df_periodic)
n_points = len(df_periodic[0])

dt = 0.1
time = np.linspace(0, (n_images-1)*dt, n_images)

x_all = np.zeros((n_images, n_points))
y_all = np.zeros((n_images, n_points))
z_all = np.zeros((n_images, n_points))


for count,df in enumerate(df_periodic):
    x = df.X
    y = df.Y
    z = df.Z
    
    x_all[count,:] = x
    y_all[count,:] = y
    z_all[count,:] = z


#%% Run the interpolation
       
dt_cfd = 0.00025
start_time = 0
period_length = 0.1 # amount of time for each cycle
dt_ct = 0.1
new_time = np.arange(start_time, period_length+0.0000001, dt_cfd)    
x_new2, y_new2, z_new2 = interpolate_controlPoints_time(df_periodic, dt_ct, new_time, show=True)


#%%replace x_new with x_new2

x_new_ori, y_new_ori, z_new_ori = x_new, y_new, z_new

for i in range(len(x_new2)):
    for j in range(len(x_new2[0])):
        x_new[200 + i, j] = x_new2[i, j]
        x_new[200 + i, j] = x_new2[i, j]
        x_new[200 + i, j] = x_new2[i, j]



#%% create new df structure for repeating and saving - Total Displcement


df_periodic_star = periodic_star_table_totalDisp_fromArraysV2(x_new, y_new, z_new, dt=0.01, n_cycles=3)  
df_periodic_star = df_periodic_star*1e-3
df_periodic_star.to_csv(work_dir +"StarControlPoinstFull_2mmSpacing_Periodic_5Cycles_Total_interpolated_01s_append.csv", index=False)

#%% Create the datafrom for star - incremental displacement



# df_periodic_star = periodic_star_table_IncDisp_fromArraysV2(x_new, y_new, z_new, dt=dt_cfd, n_cycles=1, start_time=0.7)  
# df_periodic_star = df_periodic_star*1e-3
# df_periodic_star.to_csv(work_dir +f"StarControlPoinstFull_2mmSpacing_Periodic_5Cycles_Inc_interpolated_0001s_cycle_whole2.csv", index=False)
work_dir = 'D:/Jalori/RobinSequence/subj14/cp_csv_dt001/'
end_time = 3.0
start_time_split = 0.0
dt_file = 0.01
dt_cfd = 0.001
iter = np.arange(start_time_split, end_time, dt_file)

for i in iter:
    df_periodic_star = reference_periodic_star_table_IncDisp_fromArrays_split_01(x_new, y_new, z_new, dt=dt_cfd, n_cycles=1, start_time=i, div=100, cycle_len=1.0)
    df_periodic_star = df_periodic_star*1e-3
    df_periodic_star.to_csv(work_dir +f"StarControlPoinstFull_2mmSpacing_Periodic_5Cycles_Inc_interpolated_reference_subj14_interface_001s_cycle{int(i*100+0.001)}.csv", index=False)

# df_periodic_star = periodic_star_table_IncDisp_fromArrays_split(x_new, y_new, z_new, dt=dt_cfd, n_cycles=1, start_time=0.7, div=7, cycle_len=0.7)
# df_periodic_star = df_periodic_star*1e-3
# df_periodic_star.to_csv(work_dir +f"StarControlPoinstFull_2mmSpacing_Periodic_5Cycles_Inc_interpolated_0001s_cycle7.csv", index=False)


#%% Create the datafrom for star - incremental displacement()

work_dir = 'D:/Jalori/RobinSequence/subj14/cp_sparse_csv_dt001_2/'
end_time = 3.0
start_time_split = 0.0
dt_file = 0.001
dt_cfd = 0.001
iter = np.arange(start_time_split, end_time, dt_file)

def star_motion_table_split_incDisp(X, Y, Z, start_time=0.0, dt=0.1, dim=3, n_cycles=3, div_per_cycle=10, cycle_len=1.0):
    """
    Modified function for exporting StarCCM motion table - 1/16/2025
    This version of the function exports a sparse version of the CP where the CP locations are updated as new rows for every time-step. StarCCM does not keep 
    row correspondence with CP ids

    Input is X,Y,Z arrays X[time, positions]
    dt: delta time between each defined cp
    n_cycles: number of cycles to repeat for periodic simulation
    cycle_len: period length (s)
    Start_time: start time of this table. recomend that tables are split to only contain 0.01s of data. Otherwise files get too large.


    """
    
    n_time_points = int(len(X[:,0]) * (1/div_per_cycle)) + 1
    n_control_points = len(X[0,:])
    print(n_time_points)
    
    
    if start_time >= cycle_len:
        start_time_local = start_time % cycle_len
        start_time_index = int((start_time_local/cycle_len)*len(X[:,0]))
        print(start_time_index)

    else:
        start_time_index = int((start_time/cycle_len)*len(X[:,0]))
        print(start_time_index)
    
    
    position_list = []
    position_list.append(pd.DataFrame(data=X[start_time_index:start_time_index+n_time_points-1,:].flatten(), columns=['X']))
    position_list.append(pd.DataFrame(data=Y[start_time_index:start_time_index+n_time_points-1,:].flatten(), columns=['Y']))
    position_list.append(pd.DataFrame(data=Z[start_time_index:start_time_index+n_time_points-1,:].flatten(), columns=['Z']))
    
    df_positions = pd.concat(position_list, axis=1)
    # print(df_positions)
    
    disp_table = np.zeros(((n_time_points-1) * n_control_points, n_time_points * dim))
    column_list = []
    
    column_x = "X[t={:1.5f}s]".format(start_time)
    column_y = "Y[t={:1.5f}s]".format(start_time)
    column_z = "Z[t={:1.5f}s]".format(start_time)
    
    column_list.append(column_x)
    column_list.append(column_y)
    column_list.append(column_z)
    
    
    for count in range(1, n_time_points):
        
        if start_time >= cycle_len:
            start_time_local = start_time % cycle_len
            time_count = count + int((start_time_local/cycle_len)*len(X[:,0]))
        else:
            time_count = count + int((start_time/cycle_len)*len(X[:,0]))
        time =  count * dt
        
        
        time = count * dt
        cp_range_start = count  
        
        
        column_x = "X[t={:1.5f}s]".format(time + start_time)
        column_y = "Y[t={:1.5f}s]".format(time + start_time)
        column_z = "Z[t={:1.5f}s]".format(time + start_time)
        
        column_list.append(column_x)
        column_list.append(column_y)
        column_list.append(column_z)
        
        # calculate displacement
        dx = X[time_count,:] - X[time_count-1,:]
        dy = Y[time_count,:] - Y[time_count-1,:]
        dz = Z[time_count,:] - Z[time_count-1,:]
        
        # populate displacement table
        disp_table[(count-1)*n_control_points:(count-1)*n_control_points + n_control_points, count * dim] = dx
        disp_table[(count-1)*n_control_points:(count-1)*n_control_points + n_control_points, count * dim + 1] = dy
        disp_table[(count-1)*n_control_points:(count-1)*n_control_points + n_control_points, count * dim + 2] = dz
    
        
        
    df_motion = pd.DataFrame(disp_table, columns = column_list)
    
    df_full = pd.concat([df_positions, df_motion], axis=1)
    
    
    return df_full

for i in iter:

    df_periodic_star = star_motion_table_split_incDisp(x_new, y_new, z_new, dt=dt_cfd, start_time=i, div_per_cycle=1000, cycle_len=1.0)
    df_periodic_star = df_periodic_star*1e-3
    df_periodic_star.to_csv(work_dir + f"subj14_sparse_cp_function_{int(i*1000+0.001)}.csv", index=False)
#%% Create the datafrom for star - target pos

work_dir = 'D:/Jalori/RobinSequence/4DCT/Subj05/reference_update_cp_csv_target_pos/'
end_time = 2.1
start_time_split = 0.0
iter = np.arange(start_time_split, end_time, dt_ct)

for i in iter:
    df_periodic_star = target_pos_periodic_star_table_IncDisp_fromArrays_split(x_new, y_new, z_new, dt=dt_cfd, n_cycles=1, start_time=i, div=7, cycle_len=0.7)
    df_periodic_star = df_periodic_star*1e-3
    df_periodic_star.to_csv(work_dir +f"StarControlPoinstFull_2mmSpacing_Periodic_5Cycles_target_pos_subj05_01s_cycle{i*10}.csv", index=False)

