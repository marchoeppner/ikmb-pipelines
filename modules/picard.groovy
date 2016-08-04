picard_dedup = {

	doc about: "Marking duplicate reads in a BAM file",
	description: "Marks duplicate reads in a BAM file using Picard",
    	constraints: "Requires Picard to be installed",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 16		// Number of cores to use
	var directory : ""	// Allows specifying an output directory
	var memory : 16

    	// requires here

	requires PICARD : "Must provide path to Picard"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
        filter("nodup") {

                def metrics = "$output" + ".nodup.txt"

                exec """
                        java -Xmx${memory}g -jar $PICARD MarkDuplicates
                        I=$input
                        O=$output
                        METRICS_FILE=$metrics
                        REMOVE_DUPLICATES=true
                        ASSUME_SORTED=true
                        VALIDATION_STRINGENCY=SILENT
                        OPTICAL_DUPLICATE_PIXEL_DISTANCE=10
                ""","picard_dedup"
        }

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

picard_coverage = {

	doc about: "Calculate coverage in BAM file using target interval",
        description: "Uses Picard to calculate coverage in target intervals using a BAM file",
        constraints: "Requires Picard to be installed",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1          // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var memory : 16

        // requires here

        requires PICARD : "Must provide path to Picard"
	requires TARGET_INTERVALS : "Must provide file with target intervals"

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

	produce(input.prefix + ".target_stats", input.prefix + ".target_cov") {
		exec """
			java -Xmx${memory}g -jar $PICARD CalculateHsMetrics 
			BI=$TARGET_INTERVALS
			TI=$TARGET_INTERVALS
			I=$input
			O=$output1
			R=$REF
			PER_TARGET_COVERAGE=$output2
			VALIDATION_STRINGENCY=LENIENT
	
		""","picard_coverage"
	}

	
	// Validation here?

        check {
                exec "[ -s $output1 ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
		
	forward input
}
