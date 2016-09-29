gatk_pipeline = segment {
                "%_%_%_*.fastq.gz" * [

                        pipeline_prepare
                        + "%_%_%_%_read*.fastq.gz" * [
                                trimmomatic.using(procs:16,minlen:36)
                                + bwa_mem
                                + samtools_bam_sort
                                + samtools_index
                        ]

                        + "*.bam" * [
                                samtools_merge
                                + samtools_index
                        ]

                        + gatk_local_realign_target
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
                ]
        ]
}
