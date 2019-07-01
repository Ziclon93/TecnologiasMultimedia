#!/usr/bin/python3

"""
Tecnologies Multimedia, 2019
Universitat de Barcelona

Pedro Pizarro Huertas
Lluís Montabes García

Dependencies:
- matplotlib
- Pillow
- scikit-image
- scipy
"""

import os
import sys, argparse
import zipfile

import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation
from PIL import Image, ImageFile
import skimage

# Dimensions of tiles to divide into
TILE_W = 16
TILE_H = 16

# Initial ranges
SEEK_RANGE = 10
MAX_DIFF = 50

class Filters:

    def negate(v):
        return v * -1

    def binarize(v, b):
        return 255 if v > b else 0

class Filter:
    """
    Defines an operation that can be applied to an image
    """

    def __init__(self, operation, tile_size = 0):
        self.operation = operation
        self.tile_size = tile_size

    def apply_to(self, img):
        """
        Apply filter to specified image
        """
        for x in range(img.shape[0]):
            for y in range(img.shape[1]):
                for z in range(img.shape[2]):
                    img[x, y, z] = self.operation(img[x, y, z])

    def apply_to_all(self, img_arr):
        """
        Apply filter to image array
        """
        for img in img_arr:
            self.apply_to(img)

def parse_args(args):
    """
    Parse arguments from args string

    :param args: Argument string to parse
    :returns: Object containing parsed arguments
    """

    # Initialize argument parser
    parser = argparse.ArgumentParser()

    parser.add_argument('-i', '--input',
        action="store",
        dest="input_file",
        help="Input zip file to retrieve frames from",
    )

    parser.add_argument('-o', '--output',
        action="store",
        dest="output_file",
        help="Output zip file to save frames to",
    )

    parser.add_argument('-e', '--encode',
        action="store_true",
        dest="encode",
        help="Whether to encode frames",
    )

    parser.add_argument('-d', '--decode',
        action="store_true",
        dest="decode",
        help="Whether to decode frames",
    )

    parser.add_argument('--fps',
        action="store",
        dest="fps",
        type=int,
        help="Frames per second at which video will run",
    )

    parser.add_argument('--binarization',
        action="store",
        dest="binarization",
        type=int,
        help="Threshold value for binarization filter",
    )

    parser.add_argument('--negative',
        action="store_true",
        dest="negative",
        help="Apply negative filter to frames",
    )

    parser.add_argument('--averaging',
        action="store",
        dest="averaging",
        type=int,
        help="Apply an average filter in defined width square tiles",
    )

    parser.add_argument('--nTiles',
        action="store",
        dest="nTiles",
        type=int,
        help="Amount of tiles in which to divide images",
    )

    parser.add_argument('--seekRange',
        action="store",
        dest="seekRange",
        type=int,
        help="Max range in which to look for coinciding tiles",
    )

    parser.add_argument('--GOP',
        action="store",
        dest="GOP",
        type=int,
        help="Amount of frames inbetween reference frames",
    )

    parser.add_argument('--quality',
        action="store",
        dest="quality",
        type=int,
        help="Quality factor that will determine coinciding tiles",
    )

    parser.add_argument('-b', '--batch',
        action="store_true",
        dest="b",
        help="No video playback mode",
    )

    parser.add_argument('--debug',
        action="store_true",
        dest="debug",
        help="Print debug information",
    )

    # Parse arguments and get input file name
    return parser.parse_args()

def zipdir(path, ziph):
    # ziph is zipfile handle
    for root, dirs, files in os.walk(path):
        for file in files:
            ziph.write(os.path.join(root, file))

def main(argv):
    """
    Main application routine
    """

    # Retrieve arguments
    args = parse_args(argv)

    # Get input file from speficied arguments
    input_file_name = args.input_file

    print("Reading from", input_file_name)

    # Open and read from zip
    zip_file = zipfile.ZipFile(input_file_name, "r")

    # Set up display
#    anim_fig = plt.figure()

    frames = []

    # Iterate over all files contained in zip_file
    for entry in zip_file.infolist():

        # Open file as Image, print metadata and show
        file = zip_file.open(entry)
        img = Image.open(file)
        if args.debug: print(img.size, img.mode, len(img.getdata()))

        # Convert image to numpy array and save to
        # frames array
        frames.append(np.array(img))

    # Define filters to implement
    filter_negative = Filter(lambda x: Filters.negate(x))
    filter_binary = Filter(lambda x: Filters.binarize(x, args.binarization))

    # Implement specified filters
    if args.negative:
        filter_negative.apply_to_all(frames)

    if args.binarization:
        filter_binary.apply_to_all(frames)

    # Create output folder if it doesn't exist
    if not os.path.exists('output'):
        os.makedirs('output')

    # Save each frame as JPEG
#    i = 0
#    for f in frames:
#        filtered_img = Image.fromarray(f)
#        filtered_img.save('output/frame_' + str(i) + '.jpg','JPEG')
#        i += 1

    # Show result animation
    # plt.show()

    if args.encode and args.decode:
        enc = encode(frames)
        print ("Encoded")

        dec = decode(enc)

        anim_fig = plt.figure()

        # Display frames
        img = plt.imshow(dec[0])

        global i
        i = 0
        def updatefig(*args):
            global i
            i = (i + 1) % len(dec)
            img.set_array(dec[i])
            return img,

        # Calculate frequency at which to update frame
        # or leave as 50 if FPS are undefined
        freq = 1000 / args.fps if args.fps else 50

        ani = animation.FuncAnimation(anim_fig, updatefig, interval=freq, blit=True)
        plt.show()

def encode(frames):
    """
    Encode given frames using motion estimation
    """

    encoded = [frames[0]]

    for i, f in enumerate(frames):
        if i + 1 < len(frames):
            # Get current and next frames
            current_frame = f
            next_frame = frames[i + 1]

            # Find motion vectors between current and next frames
            mv = find_motion_vectors(current_frame, next_frame)
            encoded.append(mv)

    return encoded

def decode(encoded):
    """
    Decode given data
    """

    decoded = [encoded[0]]

    for i, c in enumerate(encoded[1:]):
        r = reconstruct_frame(decoded[i], c)
        decoded.append(r)

    return decoded

def get_matrix_difference(m1, m2):
    """
    Compute euclidean distance between two matrices.
    Returns None if distance is above defined threshold.
    """

    d = np.linalg.norm(m1 - m2)
    return d if d > MAX_DIFF else None

def split_into_tiles(im, w, h):
    """
    Split given image into tiles of w * h dimensions
    """

    tiles = np.array([im[x:x + h, y:y + w] for x in range(0, im.shape[0], h) for y in range(0, im.shape[1], w)])
    rows = np.split(tiles, im.shape[0] / w)
    result = np.stack(rows)
    return result

def find_motion_vectors(frame1, frame2):
    """
    Find motion vectors between 2 frames using diamond search
    """

    def large_diamond(p):
        i = p[0]
        j = p[1]
        return [
            (i, j),        # center
            (i, j-2),      # top
            (i-1, j-1),    # up left
            (i-2, j),      # left
            (i-1, j+1),    # down left
            (i, j+2),      # down
            (i+1, j+1),    # down right
            (i+2, j),      # right
            (i+1, j+1)     # up right
        ]

    def small_diamond(p):
        i = p[0]
        j = p[1]
        return [
            (i, j),     # center
            (i, j-1),   # top
            (i-1, j),   # left
            (i, j+1),   # bottom
            (i+1, j)    # right
        ]

    # Split frames into tiles of TILE_W * TILE_H dimensions
    tiles1 = split_into_tiles(frame1, TILE_W, TILE_H)
    tiles2 = split_into_tiles(frame2, TILE_W, TILE_H)

    motion_vectors = {}

    for i in range(tiles1.shape[0]):
        for j in range(tiles1.shape[1]):

            # Compute distance to each tile in next frame
            # in large diamond shape
            current_position = (i, j)
            last_position = None

            while current_position != last_position:

                last_position = current_position
                distances = {}

                for pos in large_diamond(current_position):
                    try:
                        distances[pos] = get_matrix_difference(tiles1[(i, j)], tiles2[pos])
                    except IndexError:
                        pass

                current_position = min(distances, key=distances.get)

            # Apply small diamond
            distances = {}
            for pos in small_diamond(current_position):
                try:
                    distances[pos] = get_matrix_difference(tiles1[(i, j)], tiles2[pos])
                except IndexError:
                    pass

                current_position = min(distances, key=distances.get)

            # Obtain final motion vector
            motion_vector = (current_position[0] - i, current_position[1] - j)
            motion_vectors[(i, j)] = motion_vector

    return motion_vectors

def reconstruct_frame(frame, mv):
    """
    Reconstruct an image given previous frame tiles and movement vectors
    """

    tiles = split_into_tiles(frame, TILE_W, TILE_H)
    rec = np.zeros((tiles.shape[0] * 16, tiles.shape[1] * 16, 3))

    for k, v in mv.items():
        try:
            rec[
                k[0] * TILE_W : (k[0] + 1) * TILE_W,
                k[1] * TILE_H : (k[1] + 1) * TILE_H
            ] = skimage.img_as_float(tiles[k[0] + v[0], k[1] + v[1]])
        except:
            pass

    return rec

if __name__ == "__main__":
   main(sys.argv[1:])
