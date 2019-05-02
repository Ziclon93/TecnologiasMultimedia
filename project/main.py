#!/usr/bin/python3

import sys, getopt
from zipfile import ZipFile


def main(argv):

    inputfile = ' '
    outputfile = ' '

    try:
      opts, args = getopt.getopt(argv,"i:o:",[])

    except getopt.GetoptError:

      sys.exit(2)

    for opt, arg in opts:

        if opt == '-i':
            inputfile = arg

        elif opt == '-o':
            outputfile = arg

    zip_File = ZipFile(inputfile, "r")
    imgdata = zip_File.read('Cubo00.png')

    with ZipFile(outputfile, 'w') as zip:
        zip.write(imgdata)

if __name__ == "__main__":
   main(sys.argv[1:])