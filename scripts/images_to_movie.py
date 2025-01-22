import os
import re
import cv2

def extract_numeric_value(file_name):
    # Use regex to extract the numeric value in scientific notation
    match = re.search(r'velocity_mag_velocity_mag_(\d+\.\d+e[+-]\d+)', file_name)
    if match:
        return float(match.group(1))
    else:
        raise ValueError(f"Numeric value not found in file name: {file_name}")
 
png_folder = 'D:/Jalori/RobinSequence/Subj14/CFD/no_interface/scenes/velocity_mag'
output_video_path = 'D:/Jalori/RobinSequence/Subj14/CFD/no_interface/scenes/velocity_mag/output_video.mp4'
fps = 50  # Adjust as needed

# Get all PNG files in the folder
png_files = [f for f in os.listdir(png_folder) if f.endswith('.png')]

# Sort files based on the extracted numeric value
png_files.sort(key=extract_numeric_value)

if not png_files:
    print("No PNG files found in the specified folder.")
else:
    # Read the first image to get dimensions
    first_image = cv2.imread(os.path.join(png_folder, png_files[0]))
    height, width, layers = first_image.shape

    # Define the codec and create VideoWriter object
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')  # You can use other codecs as well, e.g., *'XVID'
    video_writer = cv2.VideoWriter(output_video_path, fourcc, fps, (width, height))

    # Write each PNG image to the video
    for png_file in png_files:
        image_path = os.path.join(png_folder, png_file)
        frame = cv2.imread(image_path)
        video_writer.write(frame)

    # Release the VideoWriter
    video_writer.release()
    print(f"Video saved to {output_video_path}")
