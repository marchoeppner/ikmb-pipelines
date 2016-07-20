repeatmasker = {

	doc about: "Repeatmask a multi-fasta file",
	description: "Runs RM and softmasks the input fasta file",
    	constraints: "Information on constraints here",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 8		// Number of cores to use

    	// requires here
	requires RM_SPECIES : "Must provide a RepeatMasker species/taxon group"

    	// Running a command
	
	produce(input + ".masked") {
    		exec "RepeatMasker -pa $procs -xsmall -species $RM_SPECIES $input","repeatmasker"
	}

	// Validation here?

//        check {
//                exec "[ -s $output ]"
//        } otherwise {
//                fail "Output empty, terminating $branch.name"
//        }
}
