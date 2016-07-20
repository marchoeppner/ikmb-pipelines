signalp = {

	doc about: "SignalP detects pteptide cleavage sites",
	description: "A tool to analyse proteins for cleavage sites",
    	constraints: "Requires a protein FASTA file",
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

	transform("signalp")	 {
	    	exec "signalp -f short -n $output $input"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
