split_fasta_file = {

	doc about: "Uses a ruby script to generate n number of roughly equal FASTA files",
	description: "Split a FASTA file into n CHUNKS",
    	constraints: "Requires Ruby and BioRuby",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory
	var suffix : "fastadb"
	var chunks : "500"

    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
        var chunks : 4
        def products = []

        def chunkfiles = []

        for ( value in (1..(chunks.toInteger())) ) {
                chunkfiles.push(input +"_chunk_${value}." + suffix)
        }

        produce(chunkfiles) {
                exec "$BPIPE_BIN/split_fasta_file.rb -i $input -c $chunks -s $suffix"
        }

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
