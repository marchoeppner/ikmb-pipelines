gatk_haplotype_caller = {

        doc about: "Call haplotypes from BAM files",
        description: "Compute haplotypes from a list of BAM files using GATK",
        constraints: "Must have GATK in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 16          // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var memory : "22"
	var exome : false 	// Run on the exome only, ecpects TARGET_FILE

        // Requires
        requires GATK : "Must provide path to GATK"
        requires REF  : "Must provide reference file for GATK"
        requires SNP_REF : "Must provide reference SNPS for GATK"
        requires DBSNP_REF : "Must provide dbSNP reference"

	def options = ""
	if (exome) {
		requires TARGET_FILE : "Must provide the Exome target file"
		options += " -L $TARGET_file -L chrM"

	}

	if (chr){
		options += " -L $chr"
	}
 		
	transform("gatk.raw.vcf") {
		exec """
			java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
               		-T HaplotypeCaller
			-nct $procs
			-minPruning 4 -minReadsPerAlignStart 10
			-R $REF
			-I $input
			--dbsnp $DBSNP_REF
			-stand_call_conf 50.0
			-stand_emit_conf 10.0
			-mbq 10 $options
			-o $output
		""","gatk_haplotype_caller"
	}

	 // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
}

gatk_haplotype_caller_wgs = {

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
        requires DBSNP_REF : "Must provide dbSNP reference"

        def vcf_file = ""
        if (branch.sample) {
                vcf_file = branch.sample + ".gatk.hc.gvcf"
        } else {
                vcf_file = branch.name + ".gatk.hc.gvcf"
        }

        produce(vcf_file) {
                exec """
                        java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                        -T HaplotypeCaller
                        -nct $procs
                        -R $REF
			--emitRefConfidence GVCF
                        -I $input.bam
			-variant_index_type LINEAR -variant_index_parameter 128000
                        --dbsnp $DBSNP_REF
                        -o $output
                ""","gatk_haplotype_caller"
        }

         // Validation here?
}
