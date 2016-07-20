hints_from_fasta = {

	doc about: "Grep hints from a hint file for a list of sequences",
	description: "Allows to fetch hints that match the sequences in a fasta file",
    	constraints: "Requires a hint file through HINTS",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	if (branch.sample_dir) { sample_dir = true }
    	// requires here

	requires HINTS : "An Augustus hints file"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
        def hintsfile = branch.name + ".hints"
        branch.hints = hintsfile

        produce(hintsfile) {
                exec "ruby $BPIPE_BIN/hints_from_fasta.rb -i $input -t $HINTS > $output","hints"
        }

        forward input
	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
