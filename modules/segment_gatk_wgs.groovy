
gatk_wgs_pipeline = segment {

	// Deduping is done upstream via Samblaster for performance reasons

	 gatk_base_recalibrate + gatk_print_reads + gatk_haplotype_caller.using(exome:false)

}
