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
