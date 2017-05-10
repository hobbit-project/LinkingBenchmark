#!/bin/bash

while true; do
echo "$(date) =====================================================================================" >> linking_benchmark-controller.log
echo "$(date) =====================================================================================" >> linking_data-generators.log
echo "$(date) =====================================================================================" >> linking_task-generators.log
echo "$(date) =====================================================================================" >> linking_evaluation-module.log
echo "$(date) =====================================================================================" >> linking_system.log



data_gens=( $(docker ps -aqf "name=linking_data-generator") )
task_gens=( $(docker ps -aqf "name=linking_task-generator") )
bench=( $(docker ps -aqf "name=linking_benchmark-controller") )
eval_mod=( $(docker ps -aqf "name=linking_evaluation-module") )
system=( $(docker ps -aqf "name=linking_system") )
virt_gs=( $(docker ps -aqf "name=tenforce") )

for data_gen in "${data_gens[@]}"; do
   docker logs $data_gen  >> linking_data-generators.log
   echo "-------------------------------------------------------------------------------------" >> linking_data-generators.log
   echo Data generators logs saved.
done

for task_gen in "${task_gens[@]}"; do
   docker logs $task_gen  >> linking_task-generators.log
   echo "-------------------------------------------------------------------------------------" >> linking_task-generators.log
   echo Task generators logs saved.
done

for ben in "${bench[@]}"; do
   docker logs $ben  >> linking_benchmark-controller.log
   echo "-------------------------------------------------------------------------------------" >> linking_benchmark-controller.log
   echo Benchmark controller logs saved.
done


for ev_mod in "${eval_mod[@]}"; do
   docker logs $ev_mod  >> linking_evaluation-module.log
   echo "-------------------------------------------------------------------------------------" >> linking_evaluation-module.log
   echo Evaluation module logs saved.
done

for sys in "${system[@]}"; do
   docker logs $sys  >> linking_system.log
   echo "-------------------------------------------------------------------------------------" >> linking_system.log
   echo Test system logs saved.
done

sleep 1
done
