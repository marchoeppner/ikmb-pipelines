vep = {

	doc about: "Variant effect prediction",
	description: "Runs the EnsEMBL VEP on a VCF file",
    	constraints: "Requires EnsEMBL VEP and local cache matching the genome assembly",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 16		// Number of cores to use
	var directory : ""	// Allows specifying an output directory
	var canonical : true 	// Set flag for canonical annotation
	var name : ""		// Allow to manipulate output name if the default won't work. 
	var everything : false  // Switch to run every meta analysis

    	// requires here
	requires ENSEMBLCACHE : "Must provide root directory for EnsEMBL VEP cache"
	requires ASSEMBLY : "Must provide assembly version to use (GRCh37 or GRCh38)"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}

	def options

	if (canonical) {
		options += "--canonical "
	}

	if (everything) {
		options = "--everything"
	}

    	// Running a command

	def vep_result 

	if (name) {
		vep_result = branch.sample + "." + name + ".vep"
	} else {
		vep_result = input.prefix + ".vep"
	}
	
	produce(vep_result) {
	    	exec "$VEP $options --format vcf -i $input --no_progress --assembly $ASSEMBLY --fork $procs --output_file $output --stats_text --cache --dir_cache $ENSEMBLCACHE --offline --maf_1kg --maf_exac","vep"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
