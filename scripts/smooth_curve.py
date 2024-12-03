# -*- coding: utf-8 -*-
"""
Created on Tue Nov 12 20:57:26 2024

@author: jalori
"""

import numpy as np
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from scipy.signal import savgol_filter

# Load data from Excel
file_path = 'D:/Jalori/RobinSequence/close_valve/new_cylinder_motion/new_approach/moving_mesh_scaled_expiration/massflowrate.csv'
df = pd.read_csv(file_path)  # Use read_csv instead of read_excel

# Assuming your data has columns 'X' and 'Y' for the plot
x = df['Time'].values
y = df['mass_flow'].values

# Apply Savitzky-Golay filter to smooth the curve
window_size = 101  # Window size (should be an odd number)
poly_order = 4    # Polynomial order (usually 2 or 3)
y_smooth = savgol_filter(y, window_size, poly_order)

# Add smoothed data as a new column in the DataFrame
df['Y_Smooth'] = y_smooth

# Save the updated DataFrame back to the same file or a new file
output_file_path = r'D:/Jalori/RobinSequence/close_valve/new_cylinder_motion/new_approach/moving_mesh_scaled_expiration/massflowrate_smooth.csv'
df.to_csv(output_file_path, index=False)

# Plot the results
plt.figure(figsize=(10, 6))
# plt.plot(x, y, label='Original Data', color='gray', linestyle='--')
plt.plot(x, y_smooth, label='Smoothed Curve', color='red')
plt.legend()
plt.xlabel('X')
plt.ylabel('Y')
plt.title('Curve Smoothing with Savitzky-Golay Filter')
plt.show()


