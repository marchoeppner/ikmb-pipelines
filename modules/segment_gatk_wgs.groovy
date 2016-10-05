
gatk_wgs_pipeline = segment {

	// Deduping is done upstream via Samblaster for performance reasons

	 gatk_base_recalibrate + gatk_haplotype_caller_wgs

}
