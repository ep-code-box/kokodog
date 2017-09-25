import cv2
import numpy as np
from collections import Counter

img   = cv2.imread('~/2.jpg')
gray  = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
edges = cv2.Canny(gray,50,150,apertureSize=3)

#cv2.imshow("rotated",edges)
#cv2.waitKey(0)
lines = cv2.HoughLines(edges,2,np.pi/180, 1000)
#lines = cv2.HoughLinesP(edges,1,np.pi/180,200,300,10)
print(lines)


degs = Counter(np.reshape(lines[:,:,1], len(lines)))
print(degs)
rote = float(max(degs, key=degs.get))
rote = np.degrees(rote)
if rote >= 45:
 rote = rote-90
print(rote)

rows,cols,tmp= img.shape
M = cv2.getRotationMatrix2D((cols/2,rows/2),rote,1)
dst = cv2.warpAffine(img,M,(cols,rows))

cv2.imwrite("rot_img.jpg",dst)