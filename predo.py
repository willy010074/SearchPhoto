
# coding: utf-8

import cv2
import numpy as np
from matplotlib import pyplot as plt
from matplotlib import image as mpimg
import sys
import os
import time

def process_image (path,out_path):
    im = cv2.imread(path,0)
    retval,im = cv2.threshold(im, 155 ,255 , cv2.THRESH_BINARY_INV)

    #plt.imshow(im)
    #plt.show()

    start = time.time()
    for i in xrange(len(im)):
        for j in xrange(len(im[i])):
            if im[i][j] == 255:
                count = 0 
                for k in range(-2, 3):
                    for l in range(-2, 3):
                        try:
                            if im[i + k][j + l] == 255:
                                count += 1
                        except IndexError:
                            pass
                # 這裡 threshold 設 4，當周遭小於 4 個點的話視為雜點
                if count <= 4:
                    im[i][j] = 0
    end = time.time()
    execute_time = end - start
    print "Time taken : ",execute_time," s"
    #plt.imshow(im)
    #plt.show()

    im = cv2.dilate(im,(2,2),iterations=1)
    cv2.imwrite(out_path,im)


if len(sys.argv) == 2 and '*' in sys.argv[1]:
    files = glob.glob(sys.argv[1])
    random.shuffle(files)
else:
    files = sys.argv[1:]
    
for path in files:
    out_path = path.replace('test','result')
    if os.path.exists(out_path): continue
    try:
        process_image(path, out_path)            
    except Exception as e:
        print '%s %s' % (path, e)
