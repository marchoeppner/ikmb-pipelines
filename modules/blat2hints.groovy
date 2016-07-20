blat2hints = {

	doc about: "Converts a PSL file to AUGUSTUS hints",
	description: "Usees the AUGUSTUS utility blat2hints to make hints",
    	constraints: "Requires the AUGUSTUS utilities to be in Path",
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
	
	 transform("hints.E.gff") {

                exec "blat2hints.pl --in=$input --out=$output","hints"
        }

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
