#!/usr/bin/python3

import sys, argparse
from zipfile import ZipFile

# $ pip install -U matplotlib
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation

# $ pip install Pillow
from PIL import Image

def parse_args(args):
    """
    Parse arguments from args string.

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

    # Parse arguments and get input file name
    return parser.parse_args()

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
    zip_file = ZipFile(input_file_name, "r")

    # Set up display
    fig = plt.figure()

    frames = []

    # Iterate over all files contained in zip_file
    for entry in zip_file.infolist():

        # Open file as Image, print metadata and show
        file = zip_file.open(entry)
        img = Image.open(file)
        print(img.size, img.mode, len(img.getdata()))

        # Convert image to numpy array and save to
        # frames array
        frames.append(np.array(img))

    # Display frames
    img = plt.imshow(frames[0])
    global i
    i = 0

    def updatefig(*args):
        global i
        i += 1
        img.set_array(frames[i])
        return img,

    ani = animation.FuncAnimation(fig, updatefig, interval=50, blit=True)
    plt.show()

if __name__ == "__main__":
   main(sys.argv[1:])
