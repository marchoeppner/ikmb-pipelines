psl_filter = {

	doc about: "Uses Augustus utilities to filter PSL file",
	description: "Filters a PSL file for unique mappings",
    	constraints: "Requires AUGUSTUS utility scripts",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	if (branch.sample_dir) { sample_dir = true }
    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
	filter("filtered") {

                exec "pslCDnaFilter -maxAligns=1 $input $output","filter"

        }

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
