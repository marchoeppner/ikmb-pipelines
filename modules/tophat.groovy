// Tophat module

tophat = {

    doc title: "Align RNA-seq reads against a reference with tophat",
        desc: """
            Uses Tophat2/Bowtie2 to align reads against a genome

            Stage options:
                paired : paired-end data (boolean)

        """,
        constraints: """
            Works with fq.gz and fq input files, but assumes paired-end
            reads (if not, set paired to false).
            The first argument is expected to be the left mate, the second
            argument needs to be the right mate
        """,
        author: "marc.hoeppner@bils.se"

    var paired : true // paired-end data?
    var procs : 8
    // Exposed options

    var tophat_r : 50       // mate inner distance
    var tophat_i : 70       // minimum intron length
    var tophat_I : 500000   // maximum intron length
    var tophat_T : false
    var library_method : "fr-firststrand"
    var directory : "tophat"
    var phred : "--solexa-quals"
    use_transcriptome = false

    options = "-r $tophat_r -i $tophat_i -I $tophat_I"

    // Check if an annotation file OR transcriptome index is passed and
    // modify options
    if (GENOME_GTF.length() > 0) {
        options += " -G $GENOME_GTF"
        use_transcriptome = true
    } else if (TRANSCRIPTOME_INDEX.length() > 0) {
        options += " --transcriptome-index $TRANSCRIPTOME_INDEX"
        use_transcriptome = true
    }

    // We enable quantifcation only against known transcripts but only
    // if transcripts were provided
    if (tophat_T && use_transcriptome) {
       options += " -T"
    }

    requires BWT2_INDEX : "Must specify a Bowtie2 index (BWT2_INDEX)"

    // We subsequently need to keep track of folders
    // Here we set a name accessible to all subsequent modules.

    output.dir = directory + "." + branch.name

    // If a basename for this branch was set further upstream

    produce("accepted_hits.bam") {
    		if (paired) {
                	exec "tophat $phred $options -o $output.dir -p $procs --library-type=$library_method $BWT2_INDEX $input1 $input2 >$output.dir/tophat.out","tophat"
            	} else {
                	exec "tophat $phred $options -o $output.dir -p $procs --library-type=$library_method $BWT2_INDEX $input >$output.dir/tophat.out 2>$output.dir/tophat.err && md5sum $output >$output.dir/tophat.md5","tophat"
            	}
    }

    check {
        exec "[ -s $output ]"
    } otherwise {
        succeed "The Tophat output is empty. Stopping this branch ($branch.name)"
    }
}
