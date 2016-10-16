gatk_variant_recalibrator = {

	doc about: "Recalibrate variants using GATK",
	description: "Runs the GATK variant recalibration tool",
    	constraints: "Requires GATK",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 16		// Number of cores to use
	var directory : ""	// Allows specifying an output directory
	var memory : "64"	// Set max memory for GATK
	var mode : "SNP"	// Mode for GATK module
	var exome : false	// Exome mode?

    	// requires here
	requires GATK : "Must provide path to GATK"
	requires REF : "Must provide genome reference"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command

	// Impement two modes - SNP or INDEL
	def extension = ""
	def options = ""
	if (mode == "SNP") {
		extension = "snps"
		requires HAPMAP_REF : "Must provide path to HAPMAP reference file"
	        requires DBSNP_REF : "Must provide dbSNP reference file"
        	requires OMNI_REF : "Must provide g1k omni reference file"
		options += "-resource:hapmap,VCF,known=false,training=true,truth=true,prior=15.0 $HAPMAP_REF "
 	        options += "-resource:omni,VCF,known=false,training=true,truth=false,prior=12.0 $OMNI_REF "
                options += "-resource:dbsnp,VCF,known=true,training=false,truth=false,prior=2.0 $DBSNP_REF "
	} else if (mode == "INDEL") {
		extension =" indels"
		requires MILLS_REF : "Must provide path to MILLS reference file"
		options += "-resource:mills,known=true,training=true,truth=true,prior=12.0 $MILLS_REF"
	} else {
		fail "Do not understand the mode $mode, aborting"
	}

	if (exome) {
		options += " -an DP -an QD -an FS -an MQRankSum -an ReadPosRankSum -an MQ -minNumBad 1000"
	} else {
		options += " -an QD -an MQ -an MQRankSum -an ReadPosRankSum -an FS -an SOR -minNumBad 1000"
	}

    	transform(".vcf") to(".recal",".tranches",".plot") {
		exec """
                        java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                        -T VariantRecalibrator
			-nt $procs
			-mode $mode
			--maxGaussians 4
			-input $input
			$options
			-recalFile $output1
			-tranchesFile $output2
			-rscriptFile $output3
			-R $REF
			
		""","gatk_variant_recalibrator"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
