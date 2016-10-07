Installation instruction
------------------------

1) Prerequisites

Bpipe (http://github.com/ssadedin/bpipe/)

Plus any tool you wish to pipeline. 

2) Installing this pipeline

Check out the code to a location that is visible across your cluster nodes

Execute the setup script and follow the instructions (setup.sh)

Refresh your shell to make sure that the new environment variables are set properly (rerun the setup script to verify).

3) Setting up individual pipelines

Pipelines are located under pipelines/ and have the extension ".bpipe". Along with each pipeline there will usually be several auxilliary files:
- my_pipeline.config (sets the necessary variables, like location of executables and reference data sets)
- my_pipeline.load_modules (optional, will load all the required software packages via the IKMB environment modules)
- my_pipeline.readme (a short description of the pipeline)

Resource requirements for the different pipeline stages are specified in the file bpipe.config (used by all pipelines).

If you have to change variables in the config or change resource usage, you can simply copy the respective file to the location where you 
will run the pipeline and modify this local copy (the local dir has the highest priority when looking for this information)

4) Running a pipeline

If the pipeline has a module loading script, you should run that first:

source /path/to/pipelines/my_pipeline.load_modules

The basic syntax for checking that the pipeline checks out is:

bpipe test /path/to/pipelines/my_pipeline.bpipe input_files

If this looks ok and no errors are thrown, you can run the pipeline:

bpipe run -r -n allowed_number_of_cores /path/to/pipeline_file input_files

The allowed_number_of_cores controls how many compute cores the pipeline is allowed to use at most across your cluster. 
The "-r" flag will generate a report of that pipeline run in doc/index.html - useful for documenting analyses.
Input_files can either be an actual list of files or include wild cards for pattern matching. 

5) Additional reading:

Each pipeline has a detailed description in a companion file ending on .readme. 

A detailed documentation for Bpipe can be found: http://docs.bpipe.org/

If you have questions about these particular pipelines: mphoeppner@gmail.com


