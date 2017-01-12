vcf2boogie = {

	doc about: "Converts a VCF formatted file to Boogie input",
	description: "Conversion of a VCF file to Boogie genotype data",
    	constraints: "None",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	// requires here

    	// Running a command
	
	transform("gt.txt") {
	    	exec "ruby $BPIPE_BIN/vcf2boogie.rb -i $input.vcf > $output"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

boogie = {

	doc about: "Module to run Boogie",
        description: "Boogie predicts bloodtypes from genotype data",
        constraints: "None",
        author: "mphoeppner@gmail.com"

	var procs : 1

	filter("boogie") {
		exec """
			boogie $input | grep "^Chromatid" > $output
		"""
	}

     	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }

}

boogie_pipeline = segment {
        vcf2boogie + boogie
}
