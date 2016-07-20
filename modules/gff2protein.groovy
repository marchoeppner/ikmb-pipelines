gff2protein = {

    doc about: "A module to extract protein sequences from GFF annotations",
    description: "Reports protein sequences from GFF annotations",
    constraints: "Only works with standard eukaryotic genetic code!",
    author: "marc.hoeppner@bils.se"

//    var directory : "protein"

    // requires here
    requires GENOME_FA : "Must provide a genome sequence (GENOME_FA) > /dev/null"

    // Running a command

    produce(branch.name + ".pep.fasta") {
            exec "$BPIPE_BIN/gff2protein.pl $GENOME_FA $input $branch.name"
    }

}
