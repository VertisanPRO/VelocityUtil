#!/bin/bash
# VelocityUtil example script by GIGABAIT

# Command: bash run shell <command>
echo "&6--------------------------------------------------------------"
if [[ $1 == "shell" ]]; then
  $2
  echo "&6--------------------------------------------------------------"
  exit
fi

str=""
for arg in "$@"; do
  str="$str $arg"
done
echo "&7You have run the script: &3&l$0
&7With arguments:&3&l$str"
echo "&6--------------------------------------------------------------"
