cufflinks = {

	 doc title: "Build transcripts from aligned reads using cufflinks",
                desc: """
                        Reconstructs transcripts from aligned RNA-seq reads in BAM format.

                        Input:
                        A read alignment in BAM format

                        Output:
                        transcripts.gtf

                        Requires:
                        GENOME_FA : Genome sequence in FASTA format
			CUFFLINKS : Path to the cufflinks binary

                        Stage options:
                        sample_dir : true or false, determines whether output will be written to
                                a sample-specific subfolder (true) or not (false)
                        j : set the isoform fraction cut-off
                        F : set the intra-splice-junction read cut-off
                """,
	        author: "marc.hoeppner@bils.se"
	var procs : 8
	var directory : "cufflinks"
        var cufflinks_j : "0.15"
        var cufflinks_F : "0.1"
	var cufflinks_g : false
	var GENOME_GTF : ""

	requires CUFFLINKS : "Must provide path to cufflinks (CUFFLINKS)"
        requires GENOME_FA : "Please set the GENOME_FA variable"

	def options

	// Checking which variables are set and build options string
	if (cufflinks_g && GENOME_GTF.length() > 5) {	
		options = "-j $cufflinks_j -F $cufflinks_F -g"
	} else {
		options = "-j $cufflinks_j -F $cufflinks_F"
	}

	// Check if an annotation file is passed and modify options if so
	if (GENOME_GTF.length() > 5) { 
  		options += " -G $GENOME_GTF" 	
	} 

        branch.assembly_method = "j" + cufflinks_j + "_F" + cufflinks_F
	
        output.dir = directory + "/" + branch.sample + "_" + assembly_method

        // The file to pass on is generally 'transcripts.gtf' - we use it as output.

	produce("transcripts.gtf") {
		uses(threads:16) {
		        exec "$CUFFLINKS -L ${branch.sample} -o $output.dir -p $threads -u -b $GENOME_FA $options $input 2> $output"+".log","cufflinks"
		}
	}


}
