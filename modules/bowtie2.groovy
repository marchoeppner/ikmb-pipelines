bowtie2_align = {

	doc about: "A generic module that needs a description",
	description: "Description here",
    	constraints: "Information on constraints here",
    	author: "mphoeppner@gmail.com"

	// Variables here
	var procs : 16		// Number of cores to use
	var directory : ""	// Allows specifying an output directory

    	// requires here

	requires BWT2_INDEX 	: "Must provide name of Bowtie2 index BWT2_INDEX"

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
	def samfile = branch.name + ".sam"

	produce(samfile) {
	    	exec "bowtie2 -p $procs -x $BWT2_INDEX -1 $input1 -2 $input2 -S $output"
	}

	// Validation here?

//        check {
 //               exec "[ -s $output ]"
  //      } otherwise {
   //             fail "Output empty, terminating $branch.name"
    //    }
	
}
