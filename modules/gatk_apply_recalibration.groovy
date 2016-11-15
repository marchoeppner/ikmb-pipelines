@preserve
gatk_apply_recalibration = {

	doc about: "Applies recalibration to variants using GATK",
	description: "Runs the GATK apply recalibration tool",
    	constraints: "Requires GATK",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 8		// Number of cores to use
	var directory : ""	// Allows specifying an output directory
	var memory : "22"	// Set max memory for GATK
	var mode : "SNP"	// Mode for GATK module

    	// requires here
	requires GATK : "Must provide path to GATK"
	requires REF : "Must provide genome reference"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
		
	from("vcf","tranches","recal") {
		exec """
                        java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                        -T ApplyRecalibration
			-ts_filter_level 99.0
			-mode $mode
			-input $input.vcf
			-tranchesFile $input.tranches
			-recalFile $input.recal
			-o $output.vcf
			-R $REF
			
		"""
	}

	// Validation here?

//        check {
//                exec "[ -s $output.vcf ]"
//        } otherwise {
//                fail "Output empty, terminating $branch.name (expecting: $output.vcf)"
//        }
	
}
