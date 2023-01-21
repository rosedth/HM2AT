# Executing Python scripts to prepare files for download

import sys
import os
import shutil


# Path to implementation folder and Tmp folder
implementationFolder = sys.argv[1]
tmpFolder= sys.argv[2]


# create a HM2AT-Download folder
downloadFolder =os.path.join(tmpFolder,"HM2AT-Download")
os.mkdir(downloadFolder)

# copy to /downloadFolder all the files in the implementation/src folder
srcFolder =os.path.join(downloadFolder,"src")
os.mkdir(srcFolder)
shutil.copytree(implementationFolder+"\\src", srcFolder,dirs_exist_ok=True)

# copy to /downloadFolder all the files in the implementation/src folder
newGenFolder =os.path.join(downloadFolder,"gen")
os.mkdir(newGenFolder)

# copy to /downloadFolder all the files in the implementation/scripts/gen folder
genFolder=shutil.copytree(implementationFolder+"\\scripts\\gen",newGenFolder,dirs_exist_ok=True)


genFolder=genFolder+"\\org\\rossedth"
genFolder=genFolder+"\\"+os.listdir(genFolder)[0]

# replace the files .java to downloadFolder
# fetch all files at tmp
for file_name in os.listdir(tmpFolder):
    # construct full file path
    source = tmpFolder+"\\"+file_name
    isFile= os.path.isfile(source)
    isUpdatedFile= file_name.lower().startswith("upd")
    
    if (isFile and isUpdatedFile):
        prefix,name = file_name.split("-")
        #remove OLD version of the file with name
        os.remove(genFolder+"\\"+name)
        shutil.copy(source, genFolder+"\\"+name)

   
# zip contend of /downloadFolder
shutil.make_archive(downloadFolder, 'zip', root_dir=downloadFolder)

# remove intermediary files
for file_name in os.listdir(tmpFolder):
    source = tmpFolder+"\\"+file_name
    isFile= os.path.isfile(source)
    if(not(isFile)):
        shutil.rmtree(source)
    else:
        isZipFile= file_name.lower().endswith(".zip")
        if (not(isZipFile)):
            os.remove(source)
