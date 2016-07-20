cmsearch = {

	doc about: "Infernal predicts non-coding RNAs using CM profiles",
	description: "Runs Infernal using a CM profile",
    	constraints: "Must use a CM set das matches the versio of Infernal",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 8		// Number of cores to use
	var directory : "cmsearch"	// Allows specifying an output directory

    	if (branch.sample_dir) { sample_dir = true }
    	// requires here

	requires CM_FILE : "Must provide a CM file for Infernal (CM_FILE)"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	produce(input.prefix + ".tblout",input.prefix + ".cmsearch") {
	    	exec "cmsearch --cut_tc --rfam --cpu $procs --tblout $output1 -o $output2 $CM_FILE $input"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
