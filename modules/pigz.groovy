pigz = {

	doc about: "A generic module that needs a description",
	description: "Description here",
    	constraints: "Information on constraints here",
    	author: "WhoWroteThis"

	// Variables here
	var procs : 16		// Number of cores to use

    	if (branch.sample_dir) { sample_dir = true }
    	// requires here
	
    	// Running a command
	
	produce(input + ".gz") {
    		exec "pigz -p $procs $input","pigz"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
