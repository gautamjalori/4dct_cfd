# -*- coding: utf-8 -*-
"""
Created on Sun Dec  1 20:35:20 2024

@author: jalori
"""

import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from moviepy.editor import VideoFileClip, concatenate_videoclips
import numpy as np

# Load the data from Excel
excel_file = "airway_data.xlsx"  # Replace with your Excel file name
data = pd.read_excel(excel_file)

time = data['Time']
massflow = data['MassFlow']

# Create the figure and axis
fig, ax = plt.subplots()
ax.set_title("Mass Flow Over Time")
ax.set_xlabel("Time (s)")
ax.set_ylabel("Mass Flow")
ax.grid()

# Set up the line and pointer
line, = ax.plot([], [], label="Mass Flow")
pointer, = ax.plot([], [], 'ro')  # Red dot for the pointer
ax.legend()

# Set the axis limits
ax.set_xlim(time.min(), time.max())
ax.set_ylim(massflow.min() - 0.1 * abs(massflow.min()), massflow.max() + 0.1 * abs(massflow.max()))

# Initialization function
def init():
    line.set_data([], [])
    pointer.set_data([], [])
    return line, pointer

# Update function
def update(frame):
    t = time.iloc[:frame]
    m = massflow.iloc[:frame]
    line.set_data(t, m)
    pointer.set_data(t.iloc[-1], m.iloc[-1])
    return line, pointer

# Animation
frames = len(time)
ani = FuncAnimation(fig, update, frames=frames, init_func=init, blit=True)

# Save animation as a video
plot_video = "plot_animation.mp4"
ani.save(plot_video, fps=30, extra_args=['-vcodec', 'libx264'])

# Combine with airway simulation video
simulation_video = "airway_simulation.mp4"  # Replace with your airway video name
output_video = "combined_video.mp4"

# Load videos and combine
plot_clip = VideoFileClip(plot_video).resize(height=300)  # Resize if needed
simulation_clip = VideoFileClip(simulation_video)

# Position the plot beside the simulation
combined = concatenate_videoclips([simulation_clip, plot_clip.set_position(("right", "center"))], method="compose")
combined.write_videofile(output_video, fps=30, codec='libx264')

print(f"Combined video saved as {output_video}")
