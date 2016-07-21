print_file_name = {

	doc about: "Prints the name if the input file(s)",
	description: "A debugging module that prints file names",
    	constraints: "None",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
	names = inputs.join(";")

    	exec "echo $inputs > $output"

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }

	forward inputs	
}
