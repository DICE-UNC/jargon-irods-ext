galleryBrowse {
#Input parameters are:
#  iRODS absolute path
#  offset
#  length
#  
#Output parameter is:
#  String (a JSON array of results)
   irods_policy_list_thumbnails_for_logical_path(*absPath,*offset,*length, *out);
}
INPUT *absPath="/",*offset="0",*length="100"
OUTPUT ruleExecOut,*out