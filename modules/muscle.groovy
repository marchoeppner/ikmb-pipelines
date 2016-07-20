muscle = {

	doc about: "Muscle sequence aligner",
	description: "Runs the muscle aligner on a fasta input file",
    	author: "m.hoeppner@ikmb.uni-kiel.de"

	// Variables here
	var format : "clwstrict"

	def options
	options = "-" + format

    	// requires here

    	// Running a command

        transform(".fasta") to (".aln") {
                exec "muscle $options -in $input -out $output"
        }

	// Validation here?
	
}
