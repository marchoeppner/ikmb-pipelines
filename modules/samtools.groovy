bam2cram = {

        doc about: "A stage to convert BAM files into CRAM files",
        description: "Converts .cram to .bam",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 8           // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var mem : 16
        // requires here

        requires SAMTOOLS : "Must provide location of Samtools"

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

        // Running a command

        def fasta_index = REF + ".fai"

        transform("cram") {
                exec "$SAMTOOLS view -@ $procs -C -o $output $input","bam2cram"
        }

        // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }

}

cram2bam = {

	doc about: "A stage to convert CRAM files into BAM files",
        description: "Converts .cram to .bam",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 8           // Number of cores to use
        var directory : ""      // Allows specifying an output directory
        var mem : 16
        // requires here

        requires SAMTOOLS : "Must provide location of Samtools"

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

        // Running a command

        def fasta_index = REF + ".fai"

        transform("bam") {
                exec "$SAMTOOLS view -@ $procs -b -o $output $input","cram2bam"
        }

        // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
	}

}


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
	    	exec "$SAMTOOLS view -uhSt $fasta_index $input | samtools sort -m ${mem}G - -o $output","samtools_bam_sort"
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
        var procs : 8           // Number of cores to use
        var directory : ""      // Allows specifying an output directory
	var mem : "12"		// this is PER thread
	var cram : false		// Specify if to use CRAM or BAM; default CRAM

        // requires here
	requires SAMTOOLS : "Must provide location of samtools"
	requires REF : "Must provide location of alignment reference"

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

	def sorted_file = branch.name

	def options = ""

	if (cram) {
		options += "-O CRAM --reference $REF"
	}	
	

        // Running a command

	filter("sorted") {
                exec "$SAMTOOLS sort -@ $procs -T $sorted_file $options -m ${mem}G $input > $output","samtools_sort"
        }

        // Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }

}

samtools_index = {

	doc about: "Indexing a BAM/CRAM file",
        description: "Creates an index for a BAM/CRAM file using samtools",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1           // Number of cores to use
        var directory : ""      // Allows specifying an output directory

	requires SAMTOOLS : "Must provide location of Samtools"

	def index_file
	def dot = file(input).name.lastIndexOf(".")
	def extension = file(input).name.substring(dot + 1)
	def options_flag = "-b"
	
	if (extension == "cram") {
		index_file = input + ".crai"
		options_flag = "-c"
	} else {
		index_file = input + ".bai"
	}
	
        produce(index_file) {
                exec "$SAMTOOLS index $options_flag $input"
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
        var procs : 8           // Number of cores to use
        var directory : ""      // Allows specifying an output directory
	var cram : false		// Use CRAM format

        requires SAMTOOLS : "Must provide location of Samtools"
	requires REF : "Must provide location of alignment reference"

	def options = ""
	def bam_file 
        if (cram) {
                options += "-O cram --reference $REF"
		bam_file = branch.sample + ".cram"
        } else {
                options += ""
		bam_file = branch.sample + ".bam"
        }

        from("bam") produce(bam_file) {
                exec "$SAMTOOLS merge -@ $procs $options $bam_file $inputs","samtools_merge"
        }

}

samtools_view = {


        doc about: "A stage to convert SAM formats",
        description: "Converts SAM to BAM/CRAM",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1           // Number of cores to use
        var directory : ""      // Allows specifying an output directory
	var quality : ""
	var cram : false 	// Use CRAM format as output

        requires SAMTOOLS : "Must provide location of Samtools"


	def options = ""

	if (cram) {
		options += "-C --reference $REF"
	} else {
		options += "-b"
	}

	if (quality.length() > 0 ) {
		options += " -q ${quality}"
	}

        filter("view") {

                exec "$SAMTOOLS view $options -o $output $input"

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

samtools_calmd = {


        doc about: "A stage to generate a BAM file with MD calls",
        description: "Generates MD calls on BAM file using samtools calmd",
        constraints: "Must have samtools in PATH",
        author: "mphoeppner@gmail.com"

        // Variables here
        var procs : 1           // Number of cores to use
        var directory : ""      // Allows specifying an output directory

        requires SAMTOOLS : "Must provide location of Samtools"
	requires REF : "Must provide location of matching FASTA sequence"

        filter("md") {

                exec "$SAMTOOLS calmd -b $input $REF > $output.bam"

        }

        forward input
}
