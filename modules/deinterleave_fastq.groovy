deinterleave_fastq = {

	doc about: "De-interleave a fastq file",
	description: "Runs a custom awk script to de-interlave a fastq file",
    	constraints: "None",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 8		// Number of cores to use
	var directoy : ""	// Allows specifying an output directory

    	if (branch.sample_dir) { sample_dir = true }
    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
	def left_read = branch.name + "_left.fastq.gz"
        def right_read = branch.name + "_right.fastq.gz"

        produce(left_read,right_read) {
	    	exec "bash $BPIPE_BIN/deinterleave_fastq.sh < $input $output1 $output2 compress"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
