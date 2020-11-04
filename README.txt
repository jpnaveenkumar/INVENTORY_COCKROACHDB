# COCKROACHDB README
 
### 1) COCKROACHDB INSTALLATION
 
#### a) Download CockroachDB and Unzip
 
> wget -qO- https://binaries.cockroachdb.com/cockroach-v19.2.11.linux-amd64.tgz | tar  xvz

#### b) Setting path for CockroachDB

> export PATH=$PATH:'COCKROACHDB_INSTALLATION_DIRECTORY_HERE/cockroachDB'

### 2) Building project source code for running

#### a) Download Maven and unzip

> https://apachemirror.sg.wuchna.com/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
> tar xzvf apache-maven-3.6.3-bin.tar.gz

#### b) Setting path for Maven

> export PATH=$PATH:'COCKROACHDB_INSTALLATION_DIRECTORY_HERE/maven/bin/'

#### c) Generate jar file of the project using Maven

Execute the following command in the project root directory

To download all the dependencies of the project run the below command :
> mvn dependency:resolve

To generate executable jar file of the project
> mvn package

The above command generates INVENTORY_COCKROACH-1.0-SNAPSHOT-jar-with-dependencies.jar inside a folder named target

### 3) Running project

a) Get into the target directory containing the INVENTORY_COCKROACH-1.0-SNAPSHOT-jar-with-dependencies.jar generated in step 2 and run the following command :

>java -jar INVENTORY_COCKROACH-1.0-SNAPSHOT-jar-with-dependencies.jar

b) Executing the above command will ask for following inputs :

    Enter the Experiment Number (5 to 8):
    Enter the Server Id (from 1 to 5):
    Do you want to refresh Database State (yes/no) ? 
c) Enter the Experiment Number (5 to 8) => Enter any experiment number between 5 to 8 to run.
d) Enter the Server Id (from 1 to 5) => Enter any number between 1 to 5 (this is to decide what set of client files to execute on the current instance).
e) Do you want to refresh Database State (yes/no) ? yes to refresh the data in database and no if not.
