#!/bin/sh

#move to tmp
cd tmp

#read image from tmp
ls *.jpg > jpg.tmp
ls *.png > png.tmp

file_jpg=jpg.tmp
file_png=png.tmp

while read line
do
	tesseract $line $line -l eng+chi_tra
	mv $line ../image/
done < $file_jpg
rm -rf jpg.tmp

while read line
do
	tesseract $line $line -l eng+chi_tra
	mv $line ../image/
done < $file_png
rm -rf png.tmp

ls *.txt >txt.tmp
file_txt=txt.tmp

while read line
do
	mv $line ../after_tesseract/
done < $file_txt
rm -rf txt.tmp
