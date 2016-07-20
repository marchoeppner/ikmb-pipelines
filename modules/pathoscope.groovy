pathoscope_map = {

	doc about: "A generic module that needs a description",
	description: "Description here",
    	constraints: "Information on constraints here",
    	author: "WhoWroteThis"

	// Variables here
	var procs : 1		// Number of cores to use
	var directoy : ""	// Allows specifying an output directory

    	// requires here
	
	requires INDEX_DIR : "Must provide path to Pathoscope Bowtie2 indices"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
 	def samfile = branch.name + ".sam"

	// Evil hack below... pathoscope does not properly deal with paths so we construct a bpipe compatible output to pass on
	// but to pathscope we provide the broken down info (or else it will combine outdir and output to generate messed up paths)
        produce(samfile) {
                exec "pathoscope2.py MAP -1 $input1 -2 $input2 -indexDir $INDEX_DIR -filterIndexPrefixes hg19_rRNA -targetIndexPrefix A-Lbacteria.fa,M-Zbacteria.fa,virus.fa -outDir $directory -outAlign $samfile -expTag $branch.name -numThreads $procs","pathoscope_map"
        }
	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

pathoscope_id = {

	doc about: "A generic module that needs a description",
        description: "Description here",
        constraints: "Information on constraints here",
        author: "WhoWroteThis"

        // Variables here
        var procs : 1           // Number of cores to use
        var directoy : ""       // Allows specifying an output directory

	// Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

	// we need to attach something to the beginning of the file, so just using
	// string methods won't work. We need to use Groovy's file shorthand. 
	updated_sam = "updated_" + file(input).name

	produce(updated_sam) {
		exec "pathoscope2.py ID -alignFile $input -fileType sam -outDir $output.dir -expTag $branch.name","pathoscope_id"
	}

	check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }

}
