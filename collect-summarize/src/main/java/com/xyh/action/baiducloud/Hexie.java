package com.xyh.action.baiducloud;

/**
 * 
 * @author hcxyh  2018年8月13日
 *
 */
public class Hexie {

	/**
	 * 百度大概有种几种手段来判断视频问题
		1、安排人员去做视频排查，查一个标记一个。
		2、抽查一些视频，利用视频数据帧截取一些视频图片，再进行排查
		3、比较视频的MD5码
	   1.有些文件秒传上去就会被判定违规,所以应该是根据MD5ma来进行判断.
	   		因此,修改文件的名称是没有作用的.MD5不包含文件的属性和名称.
	   2.打开文件,在后面添加两行(不能影响文件的正常播放)

附上一个python 
# -*- coding: utf-8 -*-

import hashlib
import shutil
import os
from os import walk
from os import listdir


def get_filepaths(directory):
    file_paths = []  # List which will store all of the full filepaths.

    for root, directories, files in os.walk(directory):
        for filename in files:
            filepath = os.path.join(root, filename)
            extension = os.path.splitext(filename)[1][1:]
            if extension in ("jpg", "png", "torrent"):
                os.remove(filepath)
            else:
                file_paths.append(filepath)  # Add it to the list.

    return file_paths  

dir = "D://xunleidown//private" // your dir 
new_file_name = []

files = get_filepaths(dir)

for file in files:
    filename, file_extension = os.path.splitext(file)
    new_file_name = filename + "bak" + file_extension

    with open(file, "a") as testFile:
        testFile.write("ah")

print "done"
	   
	 */
	
}
