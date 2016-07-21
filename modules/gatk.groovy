gatk_local_realign_target = {

	doc about: "Create targets for Local realignment using GATK",
	description: "Generates a list of targets for local realignment in GATK",
    	constraints: "Must have GATK in PATH",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 4		// Number of cores to use
	var directory : ""	// Allows specifying an output directory
	var memory : "25"

    	// requires here

	requires GATK : "Must provide path to GATK"
  	requires SNP_REF : "Must provide reference SNPS for GATK"
  	
	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
        transform("indel_realignment.list") {

                branch.realignment_target = output

                exec """
                        java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                                -T RealignerTargetCreator
                                -nt $procs
                                -known $SNP_REF
                                -I $input
                                -o $output
                                -R $REF
                ""","gatk_local_realign_target"
        }

        forward input

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

gatk_local_realign = {

	doc about: "Local realignment using GATK",
        description: "Runs the local realignment module of GATK",
        constraints: "Must have GATK in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1          // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var memory : "22"

	// Requires
	requires GATK : "Must provide path to GATK"
	requires REF  : "Must provide reference file for GATK"
	requires SNP_REF : "Must provide reference SNPS for GATK"

        transform("realigned.bam") {

                exec """
                        java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                                -T IndelRealigner
                                -known $SNP_REF
                                -I $input
                                -o $output
                                -R $REF
                                -targetIntervals ${branch.realignment_target}
                ""","gatk_local_realign"
        }
}

gatk_base_recalibrate = {

	 doc about: "Base calibration using GATK",
        description: "Runs the base calibration module of GATK",
        constraints: "Must have GATK in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1          // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var memory : "22"

	// Requires
        requires GATK : "Must provide path to GATK"
        requires REF  : "Must provide reference file for GATK"
        requires SNP_REF : "Must provide reference SNPS for GATK"
	requires DBSNP_REF : "Must provide dbSNP reference"	

        transform("recal_data.grp") {

                branch.bqsr = output

                exec """
                        java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                                -T BaseRecalibrator
                                -knownSites $SNP_REF
                                -knownSites $DBSNP_REF
                                -I $input
                                -o $output
                                -R $REF
                                -l INFO
                                -dP ILLUMINA
                ""","gatk_base_recalibrate"
        }

        forward input
}

gatk_print_reads = {

	 doc about: "Printing reads using GATK",
        description: "prints reads using the PrintReads module of GATK",
        constraints: "Must have GATK in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1          // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var memory : "22"

        // Requires
        requires GATK : "Must provide path to GATK"
        requires REF  : "Must provide reference file for GATK"

        filter("recal") {

                exec """
                        java -XX:ParallelGCThreads=1 -jar -Xmx4g $GATK
                                -T PrintReads
                                -I $input
                                -BQSR ${branch.bqsr}
                                -o $output
                                -R $REF
                                -l INFO
                ""","gatk_print_reads"
        }

}

gatk_haplotype_caller = {

        doc about: "Call haplotypes from BAM files",
        description: "Compute haplotypes from a list of BAM files using GATK",
        constraints: "Must have GATK in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 16          // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var memory : "22"

        // Requires
        requires GATK : "Must provide path to GATK"
        requires REF  : "Must provide reference file for GATK"
        requires SNP_REF : "Must provide reference SNPS for GATK"
        requires DBSNP_REF : "Must provide dbSNP reference"

	def vcf_file = branch.name + ".gatk.raw.vcf"

	produce(vcf_file) {
		exec """
			java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                	-T BaseRecalibrator
			-nct $procs
			-minPruning 4 -minReadsPerAlignStart 10
			-R $REF
			-I $input
			--dbsnp $DBSNP_REF
			-stand_call_conf 50.0
			-stand_emit_conf 10.0
			-mbq 10
			-TARGET_FILE $TARGET_FILE
			-L chrM
			-o $output
		"""
	}

	 // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
}

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
                        	        -V $input
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

