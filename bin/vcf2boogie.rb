#!/usr/bin/ruby
# == NAME
# script_skeleton.rb
#
# == USAGE
# ./this_script.rb [ -h | --help ]
#[ -i | --infile ] |[ -o | --outfile ] | 
# == DESCRIPTION
# A skeleton script for Ruby
#
# == OPTIONS
# -h,--help Show help
# -i,--infile=INFILE input file
# -o,--outfile=OUTFILE : output file

#
# == EXPERT OPTIONS
#
# == AUTHOR
#  Marc Hoeppner, mphoeppner@gmail.com

require 'optparse'
require 'ostruct'


### Define modules and classes here


### Get the script arguments and open relevant files
options = OpenStruct.new()
opts = OptionParser.new()
opts.banner = "A script description here"
opts.separator ""
opts.on("-i","--infile", "=INFILE","Input file") {|argument| options.infile = argument }
opts.on("-o","--outfile", "=OUTFILE","Output file") {|argument| options.outfile = argument }
opts.on("-h","--help","Display the usage information") {
 puts opts
 exit
}

opts.parse! 

options.infile ? input_stream = File.open(options.infile,"r") : input_stream = $stdin
options.outfile ? output_stream = File.new(options.outfile,'w') : output_stream = $stdout

while (line = input_stream.gets)
  
  # Don't care about comment lines
  next if line.match(/^#.*/)
  
  e = line.strip.split("\t")
  
  seq = e[0]
  start = e[1].to_i-1
  ref = e[3]
  alt = e[4]
  stop = start + (alt.length-1)
  
  next if alt.include?(",")
  
  gt = e[9].split(":")[0].split("/")
  
  gt[0] == gt[1] ? zygosity = "hom" : zygosity = "het" 
  
  puts "#{seq} #{start} #{stop} #{ref} #{alt} #{zygosity}"
  
end


input_stream.close
output_stream.close