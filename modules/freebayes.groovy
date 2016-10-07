freebayes = {

	doc about: "Freebayes - a haplotype-based variant detector",
	description: "Runs Freebayes on a BAM file to report variants",
    	constraints: "Fasta file that matches the alignment index",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 16		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	// requires here

	REF : "Must provide Fasta Reference"
	FREEBAYES : "Must provide location of Freebayes"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	transform("freebayes.vcf") {
	    	exec "$FREEBAYES -f $REF $input > $output","freebayes"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

freebayes_parallel = {

	doc about: "Freebayes - a haplotype-based variant detector",
        description: "Runs Freebayes on a BAM file to report variants",
        constraints: "Fasta file that matches the alignment index",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 16          // Number of cores to use
        var directory : ""      // Allows specifying an output directory

        // requires here

        REF : "Must provide Fasta Reference"
        FREEBAYES_PARALLEL : "Must provide location of freebayes-parallel"

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

        // Running a command
        transform("freebayes.vcf") {
                exec "$FREEBAYES_PARALLEL <(fasta_generate_regions.py $REF" + ".fai 100000) $procs -f $REF $input > $output","freebayes_parallel"
        }

        // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }

}
