isaac_variant_configure = {

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

	output.dir = "isaac_" + branch.sample
	
    	// Running a command
	
	produce("Makefile") {
	    	exec "rm -r ${output.dir} && configureWorkflow.pl --bam=$input --ref=$REF --config=$ISAAC_CONFIG --output-dir=$output.dir"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

isaac_variant = {

        doc about: "Isaac Variant Caller",
        description: "Run the Isaac Variant caller",
        constraints: "Requires the issac_configure module to run first",
        author: "mphoeppner@gmail.com"

	var procs : 8

	output.dir = "isaac_" + branch.sample + "/results"

	// input from previous stage is Makefile, but we need to derive 
	// the output name from the original bam file, so we do this:
	def makefile = input

	transform("bam") to("genome.vcf.gz") {
		exec "make -j $procs -f $makefile","isaac"
	}
}
