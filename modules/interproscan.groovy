interproscan = {

	doc about: "Interproscan is a pipeline to annotate protein sequences",
	description: "Runs Interproscan on a multi-fasta file",
    	constraints: "Requires interproscan.sh to be in $PATH",
    	author: "mphoeppner@gmail.com"

	// Variables here

	var directory : "interproscan"	// Allows specifying an output directory

    	// requires here

	// Set a different output directory
    	if (directory.length() > 0) {
		output.dir = directory
	}
	
    	// Running a command
	
        produce(input+".gff3",input+".tsv",input+".xml") {	
                exec "interproscan.sh -appl Pfam,ProDom,SuperFamily,PIRSF -i $input -d ${output.dir} -iprlookup -goterms -pa -dp > /dev/null","interproscan"
        }

	// Validation here?

        check {
                exec "[ -s $output ]"
        } otherwise {
                fail "Output empty, terminating $branch.name"
        }
	
}

merge_interpro_xml = {

        doc "Merges the XML output from multiple InterPro searches"

        var directory : "interproscan"

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

        produce(branch.sample+"_interpro.xml") {

                // This is a really stupid hack to merge XML files
                // We take the first lines and the last line from the first file
                // And squeeze the other stuff from all files in between

                def first_file = inputs[0]
                def temp_file = branch.sample + "_interpro.tmp"

                exec "head -n 2 $first_file > $temp_file"

                for (i in inputs) {
                        exec "grep -v xml $i | grep -v protein-matches >> $temp_file"
                }

                exec "tail -n 1 $first_file >> $temp_file"

                exec "mv $temp_file $output"
        }

        branch.ipr_xml = output
}

merge_interpro_tsv = {

        doc "Merges the TSV output from multiple InterPro searches"

        var directory : "interproscan"

        // Set a different output directory
        if (directory.length() > 0) {
                output.dir = directory
        }

        produce(branch.sample+"_interpro.tsv") {
                exec "cat $inputs >> $output"
        }

        // We save the output name since we need it for a later pipeline stage.

        branch.iprtsv = output

        // Pass on the name of the output file

}

interpro2gff = {

        var directory : "interproscan"

        doc "Converts InterPro results into GFF format"

        // This is a horrible way of doing this...
        // We need the name of a file set in an earlier stage,
        // but the variable doesn't carry over, so:

        iprtsv = branch.sample + "_interpro.tsv"

        filter("interpro") {
                exec "ipr_update_gff $input $iprtsv > $output"
        }
}
