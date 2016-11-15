gatk_merge_vcf_files = {

	doc about: "A generic module that needs a description",
	description: "Description here",
    	constraints: "Information on constraints here",
    	author: "WhoWroteThis"

	// Variables here
	var procs : 16		// Number of cores to use
	var directory : ""	// Allows specifying an output directory
	var memory : 22

    	// requires here
	requires REF : "Must provide location of genome reference (REF)"

    	// Running a command
	
	def vcf_merged = branch.sample + ".merged.vcf"

	produce(vcf_merged) {
	    	exec """
			java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK 
			-T CombineVariants
			--genotypemergeoption UNSORTED
			-V $input1
			-V $input2
			-o $output
			-R $REF
		""", "gatk_combine_variants"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
