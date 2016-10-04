bwa_pipeline = segment {

	bwa_mem_samblaster.using(procs:16) + samtools_sort + samtools_index

}
