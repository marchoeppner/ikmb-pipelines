// Expects a BAM file to go in (possibly merged if multiple files per sample were used)

gatk_pipeline = segment {

	gatk_local_realign_target
        + gatk_local_realign + [
        	samtools_flagstat ,
                gatk_base_recalibrate
                + gatk_print_reads
                + picard_dedup
                + samtools_view.using(quality:"1")
                + samtools_index
                + samtools_flagstat
                + picard_coverage
                + gatk_haplotype_caller + [
                	gatk_select_variants.using(select:"SNP") + gatk_variant_recalibrator.using(mode:"SNP"),
                        gatk_select_variants.using(select:"INDEL") + gatk_variant_recalibrator.using(mode:"INDEL")
               ]
        ]
}

