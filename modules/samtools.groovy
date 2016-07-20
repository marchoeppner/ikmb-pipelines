@preserve
samtools_bam_sort = {

	doc about: "A stage to convert sam files into a sorted bam file",
	description: "Converts .sam to sorted.bam",
    	constraints: "Must have samtools in PATH",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command

        def fasta_index = REF + ".fai"

	transform(".sam") to(".sorted.bam") {
	    	exec "samtools view -uhSt $fasta_index $input | samtools sort -f -m 16G - $output","samtools_bam_sort"
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

        transform(".bam") to(".bam.bai") {
                exec "samtools index $input"
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


        def bam_file = branch.sample + ".bam"

        produce(bam_file) {
                exec "samtools merge $bam_file $inputs"
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

	def options = ""

	if (quality.length() > 0 ) {
		options += " -q ${quality}"
	}

        filter("view") {

                exec "samtools view -b $options $input > $output & samtools index $output"

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


        transform("bamstats") {

                exec "samtools flagstat $input > $output"

        }
}
