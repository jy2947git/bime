import getopt
import sys
import os
import time
import shutil

def reconfig(config_dir):
    """recreate the haproxy configuration from template."""
    templatename=config_dir+"/"+ "haproxy.cfg.template"
    print "checking " + templatename +  "..."
    if(os.path.exists(templatename)):
        print "found "
        
        print "file generated:" + "haproxy.cfg"
    else:
        print "tempalte file not found"
    print "bye"
    sys.exit(0)

def recreate_configuration(template_name,config_dir):
    """recreate the haproxy configuration file based on template and files in config directory"""

    #first back up the file
    focaplo_files.backup("/etc/haproxy/haproxy.cfg")
   
    #search the ACL files in the configuration directory
     
def main():
    opts,args=getopt.getopt(sys.argv[1:],":")
    if args:
        # print "input:" + str(args)
        backup(args[0])
    else:
        print "usage: python my.py config_path"
        sys.exit(2)

if __name__ == "__main__":
    main()


