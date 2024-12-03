# -*- coding: utf-8 -*-
"""
Created on Mon Sep 16 17:26:33 2024

@author: jalori
"""

import numpy as np
import pandas as pd
import math
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

data = pd.read_csv('D:/Jalori/RobinSequence/close_valve/cylinder_initial_control_points.csv')

x = data['X']
y = data['Y']
z = data['Z']

time_step = np.arange(0.1, 0.60001, 0.1)

new_set = pd.DataFrame({
    'X': x,
    'Y': y,
    'Z': z
})

max_a = 0.36

for i in range(len(time_step)):
    a = max_a*(i+1)/len(time_step)
    print(a)# amplitude of cosine wave
    L = np.max(z) - np.min(z)  # length of cylinder 
    r_original = np.sqrt(x**2 + y**2)  # original radius
    m = abs(y/x) #slope of points
    
    # new radius
    nr = 1 - (a - a * np.cos(2 * np.pi * z / L))
    
    # Update X and Y coordinates with the new radius
    x_new_constriction = nr/np.power((1 + m**2),0.5)
    y_new_constriction = m*x_new_constriction
    x_new_constriction = (abs(x)/x)*x_new_constriction
    y_new_constriction = (abs(y)/y)*y_new_constriction
    
    # for i in range(len(y)):
    #     if y[i] == 0.0:
    #         y[i] == 0.1;
    #         print(y[i])
    
    # data.replace(0.0,0)
    
    # Create a new DataFrame for the deformed points with constriction
    # new_set = pd.DataFrame({
    #     'X': x_new_constriction,
    #     'Y': y_new_constriction,
    #     'Z': z
    # })
    column_x = "X[t={:1.3f}s]".format(time_step[i])
    column_y = "Y[t={:1.3f}s]".format(time_step[i])
    column_z = "Z[t={:1.3f}s]".format(time_step[i])

    new_set[column_x] = x_new_constriction
    new_set[column_y] = y_new_constriction
    new_set[column_z] = z
    
cp_file_path = f"D:/Jalori/RobinSequence/close_valve/new_cylinder_motion/constricted_cylinder_control_points_a_0{int(100*a)}.csv"
new_set.to_csv(cp_file_path, index=False)


# a = 0.18  # amplitude of cosine wave
# L = np.max(z) - np.min(z)  # length of cylinder 
# r_original = np.sqrt(x**2 + y**2)  # original radius
# m = abs(y/x) #slope of points

# # new radius
# nr = 1 - (a - a * np.cos(2 * np.pi * z / L))

# # Update X and Y coordinates with the new radius
# x_new_constriction = nr/np.power((1 + m**2),0.5)
# y_new_constriction = m*x_new_constriction
# x_new_constriction = (abs(x)/x)*x_new_constriction
# y_new_constriction = (abs(y)/y)*y_new_constriction

# # for i in range(len(y)):
# #     if y[i] == 0.0:
# #         y[i] == 0.1;
# #         print(y[i])

# # data.replace(0.0,0)

# # Create a new DataFrame for the deformed points with constriction
# # new_set = pd.DataFrame({
# #     'X': x_new_constriction,
# #     'Y': y_new_constriction,
# #     'Z': z
# # })
# column_x = "X[t={:1.3f}s]".format(time_step[i])
# column_y = "Y[t={:1.3f}s]".format(time_step[i])
# column_z = "Z[t={:1.3f}s]".format(time_step[i])

# new_set[column_x] = x_new_constriction
# new_set[column_y] = y_new_constriction
# new_set[column_z] = z
    
# cp_file_path = f"D:/Jalori/RobinSequence/close_valve/new_cylinder_motion/constricted_cylinder_control_points_a_0{int(100*a)}.csv"
# new_set.to_csv(cp_file_path, index=False)

