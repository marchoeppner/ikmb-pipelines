manta_configure = {

	doc about: "Generating the config file for MANTA",
	description: "Sets uf a MANTA run for a single BAM file",
    	constraints: "BAM file must be indexed an sorted according to HTSlib standards",
    	author: "m.hoeppner@ikmb.uni-kiel.de"

    	// requires here
	requires REF : "Must provide reference FASTA file"


	output.dir = "manta_" + branch.sample
	
    	// Running a command
	
	produce("runWorkflow.py") {
	    	exec "configManta.py --bam $input.bam --referenceFasta $REF --runDir $output.dir"
	}

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

manta = {

	doc about: "Detection of structural variants using MANTA",
        description: "MANTA is a tool to detect structural genomic variants from aligned reads in BAM format",
        constraints: "Must run the configuration step first and pass the output",
        author: "m.hoeppner@ikmb.uni-kiel.de"

	var procs : 16

	output.dir = "manta_" + branch.sample + "/results/variants"

	produce("candidateSV.vcf.gz","candidateSmallIndels.vcf.gz","diploidSV.vcf.gz") {
		exec "$input -m local -j $procs","manta"
	} 

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }

	
}
