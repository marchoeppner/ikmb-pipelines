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
