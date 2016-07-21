isaac_configure = {

	doc about: "Isaac configuration step",
	description: "Create a configuration for the Isaac Variant caller",
    	constraints: "Requires aligned, indexed and deduped BAM file",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use

    	// requires here
	requires ISAAC_CONFIG : "Must provide config to ISAAC, located in ${ICV_INSTALL_DIR}"
	requires REF : "Must provide reference file for ISAAC"

	// Set a different output directory
	def outdir = "isaac_" + branch.name
	output.dir = "isaac_" + branch.name
	
    	// Running a command
	
	produce("Makefile") {
	    	exec "configureWorkflow.pl --bam=$input --ref=$REF --config=$ISAAC_CONFIG --output-dir=$output"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

isaac = {

        doc about: "Isaac Variant Caller",
        description: "Run the Isaac Variant caller",
        constraints: "Requires the issac_configure module to run first",
        author: "mphoeppner@gmail.com"

	exec "make -j 8 -f $input"
}
