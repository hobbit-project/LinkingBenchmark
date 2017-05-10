#!/bin/bash

IFS='-' read -r -a to_docker <<< "$1"
YELLOW='\033[0;33m'
NC='\033[0m'

docker stop $(docker ps -aqf "name=linking_")
docker rm $(docker ps -aqf "name=linking_")

for element in "${to_docker[@]}"; do
   if [ "$element" == "b" ] || [ "$element" == "all"  ]; then
      echo -e ${YELLOW}Docker build: linking_benchmark-controller${NC}
      docker build -t linking_benchmark-controller -f linkingbenchmarkcontroller.docker .
   fi
   if [ "$element" == "d"  ] || [ "$element" == "all"  ]; then
      echo -e ${YELLOW}Docker build: linking_data-generator${NC}
      docker build -t linking_data-generator -f linkingdatagenerator.docker .
   fi
   if [ "$element" == "t"  ] || [ "$element" == "all"  ]; then
      echo -e ${YELLOW}Docker build: linking_task-generator${NC}
      docker build -t linking_task-generator -f linkingtaskgenerator.docker .
   fi
   if [ "$element" == "e"  ] || [ "$element" == "all"  ]; then
      echo -e ${YELLOW}Docker build: linking_evaluation-module${NC}
      docker build -t linking_evaluation-module -f linkingevaluationmodule.docker .
   fi
   if [ "$element" == "s"  ] || [ "$element" == "all"  ]; then
      echo -e ${YELLOW}Docker build: linking_system${NC}
      docker build -t linking_system -f linkingsystemadapter.docker .
   fi
   
done
