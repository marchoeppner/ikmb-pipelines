@preserve
bwa_mem_samblaster = {

	doc about: "BWA mem alignment algorithm",
	description: "Aligns short reads using the BWA mem algorithm",
    	author: "m.hoeppner@ikmb.uni-kiel.de"

	// Variables here

	var procs : 16
	var BWAOPT_MEM : ""
	var paired : true
	var phred_64 : false
	var read_length : 100
	var cram : false

    	// requires here
	requires BWA_INDEX : "Must provide location of BWA_INDEX"
	requires BWA : "Must provide location of BWA"
	requires SAMBLASTER : "Must provide location of Samblaster"
	requires SAMTOOLS : "Must provide location of samtools"

    	// Running a command

	def samfile
	def sam_options = ""
	if (cram) {
		samfile = branch.name + "-bwa_mem.samblaster.cram"
		sam_options += "-C -T $BWA_INDEX"
	} else {
		samfile = branch.name + "-bwa_mem.samblaster.bam"
		sam_options += "-b"
	}

	def rg_id = ""
	if (branch.sample) 	{
		rg_id = branch.sample
	} else {
		rg_id = branch.name
	}

	def header = '@RG' + "\\tID:${rg_id}\\tPL:Illumina\\tSM:${rg_id}_${BWA_INDEX}\\tLB:lib_2x${read_length}\\tDS:${BWA_INDEX}\\tCN:ICMB,Kiel;Germany"

	def command = ""

	if (paired) {
		command += input1 + " " + input2
	} else {
		command = input
	}

	produce(samfile) {
		exec "$BWA mem -t $procs -M -R \"$header\" $BWA_INDEX $command | $SAMBLASTER -M | $SAMTOOLS view -S $sam_options - > $output", "bwa_mem"
	}

	// validation of output
	
	check {
		exec "[ -s $output ]"
	} otherwise {
		fail "BWA alignment empty, terminating $branch.name"
	}
}


