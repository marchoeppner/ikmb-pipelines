pibase_bamref = {

	doc about: "Generate input for pibase_consensus genotyping",
	description: "Pibase performs high-quality genotyping for individual loci",
    	constraints: "Must have Pibase in PATH",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	// requires here
	requires PIBASE_LR : "Must provide read length for Pibase (PIBASE_LR)"
	requires REF : "Must provide reference file used during read alignment (REF)"
	requires PIBASE_VCF : "Must provide a VCF file with loci to genotype (PIBASE_VCF)"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
    	exec "pibase_bamref $PIBASE_VCF $REF $input $output $PIBASE_LR"

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

pibase_consensus = {

	doc about: "Genytype loci using Pibase",
        description: "Pibase performs high-quality genotyping for individual loci",
        constraints: "Must have Pibase in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1           // Number of cores to use
        var directory : ""      // Allows specifying an output directory

        // requires here

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

        // Running a command

        exec "pibase_consensus $input $output"

        // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }


}
