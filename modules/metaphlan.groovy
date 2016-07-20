metaphlan = {

  	doc about: "Metaphlan short read profile",
    	description: "Metaphlan matches short reads to taxonomic groups",
   	author: "mphoeppner@gmail.com"

        var procs : 10
    	var directory : "metaphlan"

        requires MPA_PKL : "Must provide path to Metaphlan PKL file"
        requires MPA_DB : "Must provide path to Metaphlan Bowtie2 DB"

        output.dir = directory

        transform("metaphlan.out") {

		def bowtie_out = "$output" + ".bowtie"

                exec """
                        metaphlan2.py --mpa_pkl $MPA_PKL --input_type fastq --bowtie2db $MPA_DB -o $output --bowtie2out $bowtie_out --nproc $procs $input1,$input2
                ""","metaphlan"

        }

}
