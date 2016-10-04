extract_variants = {

	doc about: "A generic module that needs a description",
	description: "Description here",
    	constraints: "Information on constraints here",
    	author: "WhoWroteThis"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
	transform("gz") to("passed.vcf") {
	    	exec """
		zcat $input | extract_variants | awk '/^#/ || \$7 == "PASS"' > $output
		"""
	}

	// Validation here?

}
