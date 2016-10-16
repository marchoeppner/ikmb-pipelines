gatk_print_reads = {

	 doc about: "Printing reads using GATK",
        description: "prints reads using the PrintReads module of GATK",
        constraints: "Must have GATK in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 16          // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var memory : "22"

        // Requires
        requires GATK : "Must provide path to GATK"
        requires REF  : "Must provide reference file for GATK"

	// does not detect whether input is cram or bam, but always prints bam format
	// so we need to be flexible with the input suffix.
        produce(input.prefix + ".recal.bam") {

                exec """
                        java -XX:ParallelGCThreads=1 -jar -Xmx${memory}g $GATK
                                -T PrintReads
				-nct $procs
                                -I $input
                                -BQSR ${branch.bqsr}
                                -o $output
                                -R $REF
                                -l INFO
                ""","gatk_print_reads"
        }

}
