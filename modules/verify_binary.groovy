verify_binary = {

        var binary : ""

        if("which $binary".execute().waitFor()!=0) {
                fail "The path provided to $binary could not be resolved"
        }

        forward input
}
