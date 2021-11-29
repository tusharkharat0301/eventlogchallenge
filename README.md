# eventlogchallenge

Software required to run the programme:
Apache Maven 3.5.2
Jdk 1.8

Checkout git command:
> git clone -b main https://github.com/tusharkharat0301/eventlogchallenge.git

Once checkout is done , Navigate to project and  run below commands 
>mvn clean

To run Junit Test , Please run below command
>mvn test 

To run  programme , change the path to logfile.txt in below command .
for ex. if logfile.txt is located at G://logfile/logfile.txt then Provide path as "G://logfile")
(sample logfile.txt file is located at src/main/resources folder in project )

>mvn compile exec:java -Dexec.mainClass="com.codingchallenge.eventlog.EventLogApplication" "-DlogFilePath=G:/logfile"

**log can be found in project at : log/eventlog.log
**event details to file-based HSQLDB in project at : db-data folder