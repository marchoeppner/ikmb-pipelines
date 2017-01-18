vcf_passed_variants_from_gz = {

	doc about: "A generic module that needs a description",
	description: "Description here",
    	constraints: "Information on constraints here",
    	author: "WhoWroteThis"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
	transform("gz") to ("passed.vcf") {
	    	exec "zcat $input | awk '/^#/ || \$7 == "PASS"' > $output"
	}

	// Validation here?
}

vcf_concat = {

	doc about: "vcf-concat from the VCFtools package",
        description: "Concatenate VCF files split by chromosome",
        constraints: "None",
        author: "mphoeppner@gmail.com"

	var procs : 1

	requires "VCFCONCAT" : "Must specifiy location of vcf-concat tool from VCFtools"

	def merged_vcf = branch.sample + ".merged.vcf" 

	from("*vcf") produce(merged_vcf) {
		exec "$VCFCONCAT $inputs > $output"
	}

}
