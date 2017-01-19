dummy = {

	doc about: "A generic module that needs a description",
	description: "Description here",
    	constraints: "Information on constraints here",
    	author: "WhoWroteThis"

	// Variables here
	var procs : 16		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command

	def dummy_file = branch.name + ".dummy"

	produce(dummy_file) {
	    	exec """
			echo "That worked, hooray" >>  $output
		"""
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

dimmy = {

        doc about: "A generic module that needs a description",
        description: "Description here",
        constraints: "Information on constraints here",
        author: "WhoWroteThis"

        // Variables here
        var procs : 16          // Number of cores to use
        var directory : ""      // Allows specifying an output directory

        // requires here

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

        // Running a command

        def dummy_file = branch.name + ".dimmy"

        produce(dummy_file) {
                exec """
                        echo "That worked, hooray" >>  $output
                """
        }

        // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }

}
