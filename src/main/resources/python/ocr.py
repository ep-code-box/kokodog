#허프 변환으로 선을 뚜렷하게 하기
#blur 를 통해 텍스트 위치 명확하게 네모치기
from PIL import Image
from pytesseract import *
import cv2
import numpy as np
from collections import Counter
import sys
from binascii import unhexlify
import json

def OCR(img, lang='kor'):
 txt = image_to_string(img, lang=lang).replace(' ','')
 txt = txt.split('\n')[0]
 txt = txt.split('(')[0]
 txt = txt.split('-')[0]
 return txt
def histo(img):
 #show histogram
 pimg = np.zeros((300,256,3))
 bins = np.arange(256).reshape(256,1)
 hisc = cv2.calcHist([img],[0],None,[256],[0,256])
 cv2.normalize(hisc, hisc, 0,255,cv2.NORM_MINMAX)
 hist = np.int32(np.around(hisc))
 ptsh = np.column_stack((hist,bins))
 cv2.polylines(pimg,[ptsh],False,(0,255,0) )
 cv2.imshow("family",pimg)
 cv2.waitKey(0)
def raImg(img, pts1, pts2=np.float32([[0,0],[0,2970],[2100,2970],[2100,0]])): #이미지 재 정렬 2970 2100
 #np를 재 구성하여 꼭지점 맵핑을 한다.
 #네 귀퉁이가 온전해야함...
 pts1 = pts1.reshape((4,2)).astype('float32')
 M    = cv2.getPerspectiveTransform(pts1,pts2)
 dst  = cv2.warpPerspective(img,M,(2100,2970))
 cv2.imshow("rotated",dst)
 cv2.waitKey(0)
 cv2.imwrite("rot_img.jpg",dst)
 return dst
def findH(img):
 gray  = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
 edges = cv2.Canny(gray,50,150,apertureSize=3)
 lines = cv2.HoughLines(edges,2,np.pi/180, 1000)
 degs = Counter(np.reshape(lines[:,:,1], len(lines)))
 #rote = float(max(degs, key=degs.get))
 rote = max(degs, key=degs.get)
 rote = np.degrees(rote)
 if rote >= 45:
  rote = rote-90
 rows,cols,tmp= img.shape
 M = cv2.getRotationMatrix2D((cols,rows),rote,1)
 dst = cv2.warpAffine(img,M,(cols,rows))
 return dst
def findR(img, th_val, th_area, deg):
 img = findH(img)
 kernel = np.ones((2,2),np.uint8)
 imgG = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
 imgE = cv2.equalizeHist(imgG)
 imgB = cv2.erode (imgE,kernel,iterations = 1)
 ret,tt_img = cv2.threshold(imgB,th_val,255,cv2.THRESH_BINARY) # 66 쓰레 숄더가 가장 안정적인듯
 imgB = cv2.erode (imgE,kernel,iterations = 3)
 ret,th_img = cv2.threshold(imgB,th_val,255,cv2.THRESH_BINARY) # 66 쓰레 숄더가 가장 안정적인듯
 rimg, contours, hierarchy = cv2.findContours(th_img,cv2.RETR_CCOMP,cv2.CHAIN_APPROX_SIMPLE)
 
 output = []
 idx = 0
 hierarchy = hierarchy[0]
 for i in range(len(contours)-1,0, -1):
  cnt    = contours[i]
  hier   = hierarchy[i]
  approx = cv2.approxPolyDP(cnt,0.01*cv2.arcLength(cnt,True),True)
  area   = cv2.contourArea(cnt)
  if (hier[0] == -1 and hier[3] == -1) or (hier[3] == -1 and len(approx) == 4 and area > th_area):
   #print(idx," : ",area," : ",cnt)
   #cv2.drawContours(th_img,[cnt],-1,(255,255,255), 4)
   #cv2.drawContours(img,[cnt],-1,(0,0,255), 1)
   idx += 1
   x,y,w,h = cv2.boundingRect(cnt)
   
   if hier[0] == -1 :
    #cv2.imwrite("Img"+str(idx)+".jpg", tt_img[y: y + h, x: x + w])
    c_img1 = Image.fromarray(tt_img[y: y + h, x: x + w])
   else :
    #cv2.imwrite("Img"+str(idx)+".jpg", imgG[y: y + h, x: x + w])
    c_img1 = Image.fromarray(   img[y: y + h, x: x + w])
   if deg >= 4:
    tmp_output = {"hier": str(hier), "cord": cv2.boundingRect(cnt), "text": OCR(c_img1)}
    output.append(tmp_output)
   cv2.rectangle(img,(x,y),(x+w,y+h),(0,255,0),2)
   cv2.putText(img,str(idx), (x,y+20), cv2.FONT_HERSHEY_SIMPLEX, 0.8, 4, 2)
 return output
# if deg > -1 :
#  cv2.imshow("family",img)
#  cv2.waitKey(0)
# cv2.imwrite("che_img.jpg",img)
cvimg = cv2.imread(sys.argv[1])
#cvimg = cv2.imdecode(unhexlify(sys.argv[1]), 1)
print(json.dumps(findR(cvimg, 67, 3000, 4)))
#img = Image.fromarray(cvimg)
#OCR(img)
