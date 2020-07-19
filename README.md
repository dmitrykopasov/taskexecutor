Required to build: java1.8/maven/node-v12/npm-v6
Required to run: linux/java1.8/postgres

Steps to launch:
1. Set database URL and connection credentials in application.properties file
2. Build with maven: "mvn clean install"
3. Run as follows: "java -jar taskexecutor-1.0-SNAPSHOT.jar"

A new task is created with POST request like below:
{
  "path": "/home/scripts/script5.sh",  // executable script path
  "priority": 10,  // arbitrary integer 
  "cpu": 75,  // percent rate, 0.0 - 100.0
  "memory": 0.1, // percent rate, 0.0 - 100.0
  "execTime": 120  // 
}

Sample scripts are placed in /scripts directory. Some of the scripts are using "stress" utility.
 
 
