gatk_select_variants = {

        doc about: "Select variants from vcf file",
        description: "Run the SelectVariants method from GATK",
        constraints: "Must have GATK in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1          // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var memory : "22"
	var select : "SNP"

        // Requires
        requires GATK : "Must provide path to GATK"
        requires REF  : "Must provide reference file for GATK"

	def extension = ""

	if (select == "SNP") {
		extension = "snps"
	} else if (select == "INDEL") {
		extension = "indels"
	} else {
		fail "SelectType (select) must be either SNP or INDEL"
	}

	filter(extension) {
		 exec """
        	                java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                	                -T SelectVariants
					-selectType $select
                        	        -V $input.vcf
	                                -o $output
        	                        -R $REF
	                ""","gatk_select_variants"
	}

        // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
}
