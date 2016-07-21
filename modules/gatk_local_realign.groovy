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
