verify_binary = {

	var binary : ""

	if("which $binary".execute().waitFor()!=0) {
                fail "The path provided to $binary could not be resolved"
        }

        forward input	
}
	

// Verifications do not require an execute, so they will run on the head node and
// can be grouped as a segment (else it would mean submitting 4 or more jobs...)



