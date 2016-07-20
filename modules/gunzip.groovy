@intermediate
gunzip = {
	doc about: "Decompress a gzipped file",
	description: "Uses Gzip to uncompress an archive",
    	constraints: "None",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 1		// Number of cores to use
	var paired : true
	var directory : "gunzip"
    	// requires here

	// Set a different output directory
	
    	// Running a command
	
	output.dir = directory

	def products

	if(paired) {
		products = [ 
			directory + "/" + file(input1).name.replaceAll(/\.gz/, ""),
			directory + "/" + file(input2).name.replaceAll(/\.gz/, "")
		]
		produce(products) {	
    			multi  "gunzip -c $input1 > $output1", "gunzip -c $input2 > $output2"
		}
	} else {
		produce(input.prefix) {
			exec "gunzip -c $input > $output"
		}
	}
	// Validation here?
	
}
