Installation instruction
------------------------

1) Prerequisites

Bpipe (http://github.com/ssadedin/bpipe/)

Ruby 2.0 or higher (for the pipeline configuration script)

Plus any tool you wish to pipeline. 

2) Installing this pipeline

Check out the code to a location that is visible across your cluster nodes

Execute the setup script and follow the instructions (setup.sh)

Refresh your shell to make sure that the new environment variables are set properly (rerun the setup script to verify).

3) Setting up individual pipelines

First, you can get a list of available pipelines by doing (where PIPELINE_HOME is the directory to which you cloned this code):

$PIPELINE_HOME/config/bpipe_config -l

To prepare to run a pipeline, get the name from the previous step and
$PIPELINE_HOME/config/bpipe_config -p <name_of_pipeline> -c
$PIPELINE_HOME/config/bpipe_config -b

This will create two files - pipeline.config.template and bpipe.config.template

Rename these files to remove the .template suffix and fill out the relevant values. 

For instructions on how to configure a bpipe.config file, please see http://docs.bpipe.org/Guides/ResourceManagers/

NOTE: Not alpipelines actually need a pipeline.config file, so if the file is empty, you may as well delete it. 

4) Running a pipeline

Assuming you filled out the config file(s), you can next test if the pipeline recognizes your input and see what commands it will run first:

bpipe test /path/to/pipeline_file input files

If this looks ok and no errors are thrown, you can run the pipeline:

bpipe run -n <allowed number of cores> /path/to/pipeline_file input_files

The allowed_number_of_cores controls how many compute cores the pipeline is allowed to use at most across your cluster. 

5) Additional reading:

Each pipeline has a detailed description in a companion file ending on .info. 

A detailed documentation for Bpipe can be found: http://docs.bpipe.org/

If you have questions about these particular pipelines: mphoeppner@gmail.com


