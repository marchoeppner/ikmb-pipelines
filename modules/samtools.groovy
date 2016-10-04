@preserve
samtools_bam_sort = {

	doc about: "A stage to convert sam files into a sorted bam file",
	description: "Converts .sam to sorted.bam",
    	constraints: "Must have samtools in PATH",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory
	var mem : 16
    	// requires here

	requires SAMTOOLS : "Must provide location of Samtools"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command

        def fasta_index = REF + ".fai"

	transform(".sam") to(".sorted.bam") {
	    	exec "$SAMTOOLS view -uhSt $fasta_index $input | samtools sort -f -m ${mem}G - $output","samtools_bam_sort"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

samtools_sort = {

        doc about: "A stage to convert sam files into a sorted bam file",
        description: "Converts .bam to sorted.bam",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1           // Number of cores to use
        var directory : ""      // Allows specifying an output directory
	var mem : "15"

        // requires here
	requires SAMTOOLS : "Must provide location of samtools"

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

	def sorted_file = ""
	if (branch.sample.length() > 0) {
		sorted_file = branch.sample
	} else {
		sorted_file = branch.name
	}

        // Running a command

	filter("sorted") {
                exec "$SAMTOOLS sort -T $sorted_file -O bam -m ${mem}G $input > $output","samtools_sort"
        }

        // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }

}

samtools_index = {

	doc about: "Indexing a BAMfile",
        description: "Creates an index for a BAM file using samtools",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1           // Number of cores to use
        var directory : ""      // Allows specifying an output directory

	requires SAMTOOLS : "Must provide location of Samtools"

        transform(".bam") to(".bam.bai") {
                exec "$SAMTOOLS index $input"
        }

        forward input

}

@preserve
samtools_merge = {

	doc about: "A stage to merge bam files",
        description: "Merges multiple BAM files",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1           // Number of cores to use
        var directory : ""      // Allows specifying an output directory

        requires SAMTOOLS : "Must provide location of Samtools"

        def bam_file = branch.sample + ".bam"

        produce(bam_file) {
                exec "$SAMTOOLS merge $bam_file $inputs"
        }

}

samtools_view = {


        doc about: "A stage to merge bam files",
        description: "Merges multiple BAM files",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1           // Number of cores to use
        var directory : ""      // Allows specifying an output directory
	var quality : ""

        requires SAMTOOLS : "Must provide location of Samtools"

	def options = ""

	if (quality.length() > 0 ) {
		options += " -q ${quality}"
	}

        filter("view") {

                exec "$SAMTOOLS view -b $options -o $output $input"

        }

}

samtools_flagstat = {


        doc about: "A stage to generate statistics from a BAM file",
        description: "Generates BAM statistics using Samtools flagstat command",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1           // Number of cores to use
        var directory : ""      // Allows specifying an output directory

        requires SAMTOOLS : "Must provide location of Samtools"

        transform("bamstats") {

                exec "$SAMTOOLS flagstat $input > $output"

        }

	forward input
}
