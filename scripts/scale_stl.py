# -*- coding: utf-8 -*-
"""
Created on Mon Sep 23 17:09:52 2024

@author: jalori
"""

from stl import mesh

# Load the STL file


# your_mesh = mesh.Mesh.from_file('D:/Jalori/RobinSequence/close_valve/cylinder_cosine_constriction/extended_cylinder/scaled/extended_cylinder.stl')
# your_mesh = mesh.Mesh.from_file('D:/Jalori/RobinSequence/close_valve/cylinder.stl')
# your_mesh = mesh.Mesh.from_file('D:/Jalori/RobinSequence/4DCT/Subj05/Airway_18_clip2.stl')

# file_path = 'D:/Jalori/RobinSequence/close_valve/cylinder.stl'

# def check_stl_format(file_path):
#     with open(file_path, 'rb') as file:
#         header = file.read(80)
#         if header.strip().startswith(b'solid'):
#             print("ASCII STL file detected.")
#         else:
#             print("Binary STL file detected.")

# check_stl_format(file_path)

# 0.07853981107473373 m


# # Scale factor
# scale_factor = 0.001

# # Scale the mesh
# your_mesh.vectors *= scale_factor

# # Save the scaled STL file
# # your_mesh.save('D:/Jalori/RobinSequence/close_valve/cylinder_cosine_constriction/extended_cylinder/scaled/extended_cylinder_scaled.stl')

try:
    # Attempt to load the STL file
    your_mesh = mesh.Mesh.from_file('D:/Jalori/RobinSequence/subj14/airway_18/Airway_18_simulation_clip_extn_cap.stl')
    print("STL file loaded successfully.")
    
    # Proceed with scaling if loading is successful
    scale_factor = 0.001
    your_mesh.vectors *= scale_factor
    your_mesh.save('D:/Jalori/RobinSequence/subj14/airway_18/Airway_18_simulation_scaled.stl')
    print("STL file scaled and saved.")
except Exception as e:
    print(f"An error occurred: {e}")

