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
