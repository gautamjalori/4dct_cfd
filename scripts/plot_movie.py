import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from moviepy.editor import VideoFileClip, CompositeVideoClip


# # Load the data from Excel
# excel_file = "mass_flow_inlet.xlsx"  # Replace with your Excel file name
# data = pd.read_excel(excel_file)

# time = data['Time']
# massflow = data['MassFlow']

# # Create the figure and axis
# fig, ax = plt.subplots()
# ax.set_title("Mass Flow Over Time")
# ax.set_xlabel("Time (s)")
# ax.set_ylabel("Mass Flow")
# ax.grid()

# # Set up the line and pointer
# line, = ax.plot([], [], label="Mass Flow")
# pointer, = ax.plot([], [], 'ro')  # Red dot for the pointer
# ax.legend()

# # Set the axis limits
# ax.set_xlim(time.min(), time.max())
# ax.set_ylim(massflow.min() - 0.1 * abs(massflow.min()), massflow.max() + 0.1 * abs(massflow.max()))

# # Initialization function
# def init():
#     line.set_data([], [])
#     pointer.set_data([], [])
#     return line, pointer

# # Update function
# def update(frame):
#     # Ensure slicing is valid
#     t = time.iloc[:frame + 1]  # Include current frame
#     m = massflow.iloc[:frame + 1]
#     if not t.empty and not m.empty:
#         line.set_data(t, m)
#         pointer.set_data(t.iloc[-1], m.iloc[-1])
#     return line, pointer

# # Animation
# frames = len(time)
# ani = FuncAnimation(fig, update, frames=frames, init_func=init, blit=True)

# # Save animation as a video
# plot_video = "plot_animation.mp4"
# # # ani.save(plot_video, fps=100, extra_args=['-vcodec', 'libx264'])


# # simulation_video = "output_video.mp4"  # The airway simulation video
# # output_video = "combined_video.mp4"  # The output combined video

# # Load the videos
# plot_clip = VideoFileClip(plot_video)
# simulation_clip = VideoFileClip(simulation_video)

# # Ensure the videos have the same duration
# duration = min(plot_clip.duration, simulation_clip.duration)
# plot_clip = plot_clip.subclip(0, duration)
# simulation_clip = simulation_clip.subclip(0, duration)

# # Resize clips if necessary
# height = max(plot_clip.h, simulation_clip.h) 
# simulation_clip = simulation_clip.resize(height=height)  # Adjust height as needed
# plot_clip = plot_clip.resize(height=height)

# # Position the videos side by side
# final_clip = CompositeVideoClip([
#     simulation_clip.set_position(("left", "center")),
#     plot_clip.set_position(("right", "center"))
# ], size=(simulation_clip.w + plot_clip.w, simulation_clip.h))  # Adjust canvas size

# # Write the final combined video
# final_clip.write_videofile(output_video, fps=100, codec='libx264')

# print(f"Combined video saved as {output_video}")


# File paths
# plot_video_path = "plot_animation.mp4"
# simulation_video_path = "output_video.mp4"
# output_video_path = "combined_side_by_side.mp4"

# # Load the videos
# plot_clip = VideoFileClip(plot_video_path)
# simulation_clip = VideoFileClip(simulation_video_path)

# # Ensure the videos have the same width
# width = max(plot_clip.w, simulation_clip.w)  # Use the smaller width
# plot_clip = plot_clip.resize(width=width)
# simulation_clip = simulation_clip.resize(width=width)

# # Combine videos top and bottom
# final_height = simulation_clip.h + plot_clip.h  # Total height for canvas
# final_clip = CompositeVideoClip([
#     simulation_clip.set_position(("center", "top")),
#     plot_clip.set_position(("center", simulation_clip.h))  # Place plot below the simulation
# ], size=(width, final_height))  # Adjust canvas size

# # Write the final combined video
# final_clip.write_videofile(output_video_path, fps=30, codec='libx264')

from moviepy.editor import VideoFileClip, CompositeVideoClip, ColorClip

# File paths
plot_video_path = "plot_animation.mp4"
simulation_video_path = "output_video.mp4"
output_video_path = "combined_top_bottom_with_whitespace.mp4"

# Load the videos
plot_clip = VideoFileClip(plot_video_path)
simulation_clip = VideoFileClip(simulation_video_path)

# Determine the width difference
max_width = max(plot_clip.w, simulation_clip.w)
common_width = max_width

# Add whitespace to the narrower video to match widths
def add_whitespace(clip, target_width):
    if clip.w < target_width:
        padding = (target_width - clip.w) // 2
        white_clip_left = ColorClip(size=(padding, clip.h), color=(255, 255, 255), duration=clip.duration)
        white_clip_right = ColorClip(size=(padding, clip.h), color=(255, 255, 255), duration=clip.duration)
        return CompositeVideoClip([white_clip_left.set_position(("left", "center")), clip.set_position(("center", "center")), white_clip_right.set_position(("right", "center"))], size=(target_width, clip.h))
    return clip

plot_clip_padded = add_whitespace(plot_clip, common_width)
simulation_clip_padded = add_whitespace(simulation_clip, common_width)

# Combine videos top and bottom
total_height = plot_clip_padded.h + simulation_clip_padded.h
final_clip = CompositeVideoClip([
    simulation_clip_padded.set_position(("center", "top")),
    plot_clip_padded.set_position(("center", simulation_clip_padded.h))
], size=(common_width, total_height))

# Write the final combined video
final_clip.write_videofile(output_video_path, fps=30, codec='libx264')

output_video_path



