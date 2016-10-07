vcffilter = {

	doc about: "VCFfilter",
	description: "Filter a VCF file by some criterion (e.g. QUAL)",
    	constraints: "Part of the Freebayes dtistribution, must be in PATH",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var directory : ""	// Allows specifying an output directory
	var qual : 20 		// Quality filter argument

    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command

	filter("filtered") {	
	    	exec """
			vcffilter -f "QUAL > 20" $input > $output
		"""
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
