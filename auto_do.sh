#!/bin/sh

#move to Desktop
cd ~/Desktop/testing

#read image name and put it into input.tmp
ls *.jpg > input.tmp

#do crop
file=input.tmp
crop=~/Desktop/crop_morphology.py

while read line
do
	python $crop $line
done < $file

test -e tmp_crop && rm -rf tmp_crop
mkdir tmp_crop
mv *.crop.jpg tmp_crop
rename 's/(.crop.jpg)/.jpg/' tmp_crop/*

#do crop second
ls tmp_crop/*.jpg > $file

while read line
do
	python $crop $line
done < $file

test -e after_crop && rm -rf after_crop
mkdir after_crop
mv tmp_crop/*.crop.jpg after_crop

