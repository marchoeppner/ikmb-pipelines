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
