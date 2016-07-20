augustus = {

	doc about: "AUGUSTUS - a gene finder tool",
	description: "Runs the AUGUSTUS gene finder",
    	constraints: "Expects hints from the HINTS variable",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var directory : ""	// Allows specifying an output directory
	var config_file : ""
	var alternatives : false
	var noncanonical : false

    	if (branch.sample_dir) { sample_dir = true }
    	// requires here
	def options = ""

	if ( config_file.length() > 0 ) {
		options += "--extrinsicCfgFile=$config_file"
	}

	if (alternatives) {
		options += " --alternatives-from-evidence=true"
	}

	if (noncanonical) {
		options += " --allow_hinted_splicesites=atac"
	}

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
        def prediction = branch.name + ".augustus.gff"
        produce(prediction) {
                exec "augustus --uniqueGeneId=true --softmasking=1 --species=chicken --strand=both --hintsfile=$branch.hints --gff3=on $options $input > $output"
        }
	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}
