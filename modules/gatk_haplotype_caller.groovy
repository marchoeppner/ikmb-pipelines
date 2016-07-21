gatk_haplotype_caller = {

        doc about: "Call haplotypes from BAM file",
        description: "Compute haplotypes from BAM file using GATK",
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

	exec """
		 java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                 -T BaseRecalibrator
	"""
}
