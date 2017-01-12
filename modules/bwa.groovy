bwa_mem = {

	doc about: "BWA mem alignment algorithm",
	description: "Aligns short reads using the BWA mem algorithm",
    	author: "m.hoeppner@ikmb.uni-kiel.de"

	// Variables here

	var procs : 16
	var BWAOPT_MEM : ""
	var paired : true
	var phred_64 : false

    	// requires here
	requires BWA_INDEX : "Must provide location of BWA_INDEX"
	requires BWA : "Must provide location of BWA"

    	// Running a command

	def samfile = branch.name + "-bwa_mem.sam"

	def header = '@RG' + "\\tID:Illumina\\tSM:${branch.name}_${BWA_INDEX}\\tLB:lib_2x100\\tDS:${BWA_INDEX}\\tCN:ICMB,Kiel;Germany"

	def command = ""

	if (paired) {
		command += input1 + " " + input2
	} else {
		command = input
	}


	produce(samfile) {
		exec "$BWA mem -t $procs -M  -R \"$header\" $BWA_INDEX $command > $output", "bwa_mem"
	}

	// validation of output
	
	check {
		exec "[ -s $output ]"
	} otherwise {
		fail "BWA alignment empty, terminating $branch.name"
	}
}


