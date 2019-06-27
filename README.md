

Step 1
Update the code with your service name and the other services that you want to
message

Step 2
Build your artifact 
  scala: 
    sbt universal:packageZipTarball
  java:
    gradle clean build

Step 3
Build your docker image
  docker build . --tag=servicename

Step 4 (only if you don't have image repository AKA hack)
Deploy to all kubernetes nodes:
  docker save -o ./images/{servicename}.tar servicename
  foreach node in kubenodes:
    rsync ./images/{servicename}.tar user@node-ip:~/servicename/
    ssh user@node-ip 'sudo docker load -i ~/servicename/servicename.tar

Step 5
execute kubectl deploy
  kubectl apply -f service-deploy.yaml

Step 6
  test this puppy out
