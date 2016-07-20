psl_sort = {

        doc about: "Sort multiple Blat PSL files",
        description: "Uses Bash to sort multiple PSL files into one",
        constraints: "None",
        author: "mphoeppner@gmail.com"

        var directory : "blat"


        produce("blat.merged.psl") {

                exec "sort -k 10 $inputs > $output"
        }

}


