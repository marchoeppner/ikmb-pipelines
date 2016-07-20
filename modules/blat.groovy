blat = {

	doc about: "Runs the UCSC Blat aligner",
	description: "Wrapper for BLAT",
    	constraints: "Needs Blat to be in PATH",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var identity: 92
	var directory : "blat"	// Allows specifying an output directory

    	if (branch.sample_dir) { sample_dir = true }
    	// requires here

	requires BLAT_DB : "Specify a Blat db file (fasta file)"

	// Set a different output directory
	output.dir = directory
	
    	// Running a command
	
 	transform("psl") {
                exec "blat -mask=lower -noHead -minIdentity=92 $input $BLAT_DB $output"
        }

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

psl_sort = {

	doc about: "Sort multiple Blat PSL files",
        description: "Uses Bash to sort multiple PSL files into one",
        constraints: "None",
        author: "mphoeppner@gmail.com"

	var directory : "blat"
	

	produce("blat.merged.psl") {

                exec "sort -k 10 $inputs > $output"
        }

}


