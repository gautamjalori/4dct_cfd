# -*- coding: utf-8 -*-
"""
Created on Sat Jul  6 18:35:32 2024

@author: jalori
"""

import pandas as pd
import matplotlib.pyplot as plt

import numpy as np

def moving_average(data, window_size):
    """
    Smooth an array of data using a moving average.

    Parameters:
    - data: array-like, the input data to be smoothed
    - window_size: int, the size of the moving window

    Returns:
    - smoothed_data: numpy array, the smoothed data
    """
    if window_size < 1:
        raise ValueError("Window size must be at least 1.")
    if window_size > len(data):
        raise ValueError("Window size must not be larger than the data length.")
    
    pad_width = window_size // 2
    padded_data = np.pad(data, (pad_width, pad_width), mode='edge')
    smoothed_data = np.convolve(padded_data, np.ones(window_size) / window_size, mode='valid')
    return smoothed_data

def calculate_derivative(time, values):
    # Ensure the arrays are numpy arrays
    time = np.array(time)
    values = np.array(values)
    
    # Calculate the differences
    dt = np.diff(time)
    dv = np.diff(values)
    
    # Calculate the derivative (rate of change)
    derivative = dv / dt
    
    # For the same length as the input arrays, we can average the derivative values
    # This step is optional and depends on how you want to handle the endpoints
    derivative = np.concatenate(([derivative[0]], (derivative[:-1] + derivative[1:]) / 2, [derivative[-1]]))
    
    return derivative

# Replace 'your_file.csv' with the path to your CSV file
file_path = 'D:/Jalori/RobinSequence/4dctSubj10/Time_Raw_Stabilized_Magnified.csv'

# Replace 'column1', 'column2' with the names of the columns you want to read
columns_to_read = ['time', 'real_time', 'the motion magnified chest position']

# Read the specific columns from the CSV file
data = pd.read_csv(file_path, usecols=columns_to_read)

# Extract columns into separate arrays
time = data['time'].to_numpy()
real_time = data['real_time'].to_numpy()
c_motion = data['the motion magnified chest position'].to_numpy()

# Define the window size for the moving average
window_size = 5

# Calculate the moving average for column2_array
c_motion_smooth = moving_average(c_motion, window_size)

dc_motion_smooth_dt = np.gradient(c_motion_smooth, time)


# Assuming column1 is the x-axis and column2 is the y-axis
plt.plot(real_time, c_motion, label='time vs chest motion')
# plt.plot(real_time, c_motion_smooth, color='r', label='Smoothed Chest Motion')
plt.plot(real_time, dc_motion_smooth_dt, color='b', label='Grad Smoothed Chest Motion')

plt.xlabel('time')
plt.ylabel('chest motion')
plt.title('Plot of time vs chest motion')
plt.legend()

# Display the plot
plt.show()