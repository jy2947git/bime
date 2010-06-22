import getopt
import sys
import os
import time
import shutil
import fnmatch
import UserString

def concat(input_file_dir,name_pattern):
    """search in the input file directory, and contact the found files to string"""
    str_list=UserString.MutableString.MutableString()
    filenames = os.listdir(input_file_dir)
    for filename in filenames:
        if fnmatch.fnmatch(filename,name_pattern):
            print filename
            str_list+=open(filename).read()
    return str_list
    
def backup(output_file):
    """back up file if it exists."""
    backupname=output_file+"."+str(time.time())
    print "checking " + output_file +  "..."
    if(os.path.exists(output_file)):
        print "found "
        shutil.copyfile(output_file,backupname)
        print "file copied:" + backupname
    else:
        print "file not found"
    print "bye"
    sys.exit(0)

def main():
    opts,args=getopt.getopt(sys.argv[1:],":")
    if args:
        # print "input:" + str(args)
        backup(args[0])
    else:
        print "usage: python my.py file-name"
        sys.exit(2)

if __name__ == "__main__":
    main()


