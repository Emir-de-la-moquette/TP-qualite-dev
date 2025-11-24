# README - Nathan's notes to fix devcontainer setup issues
# Problems list
- database container created but not setup
- prdts-registry-domaine-service wont start because database is not setup
- cant access services on port 8080, 8091, 4200 from host machine
- can't access angular front application on port 4200
- angular can't reach backend services (need to start all gradle apps first), got error 500 connection refused


# Postgres container logs (Before applying solution) : 
![](images_readme_nathan/screenshot-pg-container.png)

Logs shows that database init script is missing (but the file is present in the folder /docker-entrypoint-initdb.d)
. It may be a permission issue or the file is not executed for some reason.
 

# Probleme sources and solutions

Below are listed the lproblems lsited above and the way I solved them :

##  Database setup

See previous screenshot of postgres container logs

### Probleme source
Postgres container failed to execute inint script to setup database and role needed by the application. Currently the database is not setup and the role older_flow is missing + no migrations performed.

Missing init script in /docker-entrypoint-initdb.d to setup the database and role needed by the application

### Solution 

#### Iinside the postgres container execute the init script manually
- connect to container using windows terminal. (reminder `docker container exec -it <container_id> /bin/bash`)
- execute the following command to check if the init script is present in the folder /docker-entrypoint-initdb.d : `ls -l /docker-entrypoint-initdb.d`
- execute the following command to run database creation script : `bash /docker-entrypoint-initdb.d/init.sh`

![](images_readme_nathan/create-role.png)


- run the init.sh script in /docker-entrypoint-initdb.d to setup the environment older_flow role 
- (optionnal) verification

#### Inside the liquibase container run database migrations

 **Note : postgres database has to be set up and running**
- connect to luiquibase continer and run `liquibase --changeLogFile=master.xml --search-path=/liquibase/changelog update`

![](images_readme_nathan/liquibase-migrate.png)

## Service not reachable from host machine : 

### Probleme source
Missing port forwading

### Solution

#### add port forwarding to devcontainer from vscode:

use vscode port forwading tool to forward the ports needed (8080, 8091, 8092)

![](images_readme_nathan/manual-forward-port.png)

Angular runs dev server on 0.0.0.0 ==> no need to forward port manually

## Angular can't access backend services (services not started) :

### Probleme source

Backend services not started

### Solution
- start all backend services using gradle tasks in the following order :
- Dont forgive to forward services ports if not done yet see previous section 
  1. Commande (écriture) : Product Registry Command Service (8091)
  2. Query (lecture) : Product Registry Read Service (8092)
  3. BFF (Backend For Frontend) : Order Flow BFF (8080)
  4. Front de gestion de magasin : Store Front (4200)

# Just Notes for me : Nathan => use previous content to fix issues faced when setting up the devcontainer for older_flow project

## Action taken to setup the environment
- log in to dev container shell with cmd and executed the init.sh script in /docker-entrypoint-initdb.d to setup the environment older_flow role and db (postgres)

- Added port 8091, 8080 and 4200 qto dev container via docker-composse.yml or vscode port forwaring tool

## Status of what is running in the container
- quarkus dt_socket running on port 5005 for remote debugging

- quarkus welcome running on
[quarkus welcome](http://localhost:8080/q/dev-ui/welcome)
[quarkus welcome](http://localhost:8091/q/dev-ui/welcome)
[quarkus welcome](http://localhost:8092/q/dev-ui/welcome)

- quarkus swagger ui running on
[quarkus swagger](http://localhost:8080/q/swagger-ui/)
[quarkus swagger](http://localhost:8091/q/swagger-ui/)
[quarkus swagger](http://localhost:8092/q/swagger-ui/)



- angular application running on port 4200
[angular app](http://localhost:4200/)   

