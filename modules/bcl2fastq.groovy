bcl2fastq = {

        doc title: "Demultiplexing raw sequencing data",
        desc: """
            Runs bcl2fastq to demultiplex a raw illumina sequencing run
        """,
        constraints: "Assumes that the raw data directroy contains the SampleSheet.csv",
        author: "m.hoeppner@ikmb.uni-kiel.de"

        // Defining accessible variables to run the tool

        var r : 4
        var d : 8
        var p : 8
        var w : 4
        var barcodemismatches : 1
	var directory : "/mnt/ng_backup/illumina/"

        // Info: input.dir is a Bpipe convention that allows to pass a folder
        // as input, not a file

        def samplesheet = input.dir + "/SampleSheet.csv"

        if (file(samplesheet).exists() == false ) {
                fail "Could not find Samplesheet in directory $input.dir"
        }

        // Determines run_id from the input path
        def run_id = input.split("/")[-1]

        // Concatenate the different options into one string
        def options
        options = "-r $r -d $d -p $p -w $w --barcode-mismatches $barcodemismatches"

        // Directory to write output to
        // Note that output.dir is a Bpipe convention to carry over folder information
        output.dir = directory + run_id

        // Runs the actual tool - and assigns a shorthand (bcl2fastq) for bpipe.config
        exec "bcl2fastq -R $input.dir -o $output.dir --sample-sheet $samplesheet $options" , "bcl2fastq"
}
