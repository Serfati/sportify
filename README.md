<img src="https://in.bgu.ac.il/marketing/graphics/BGU.sig3-he-en-white.png" height="48px" align="right" />  
<img src=https://res.cloudinary.com/serfati/image/upload/v1589354855/icon_iqs8if.png height="120"/>  

  
![](https://codeclimate.com/github/JonSn0w/Hyde/badges/gpa.svg)   ![](https://img.shields.io/badge/version-0.1.0-blueviolet)  ![](https://img.shields.io/apm/l/atomic-design-ui.svg?)  
# Description  
  
**Sportify** is a JavaFX desktop application that collects and presents information on the top European football leagues using JPA Persistence API for database management. Database hosted on Microsoft Azure server managed by PostgreSQL.  

## ⚠️ Prerequisites  
  
- [JDK 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)  
- [Apache Maven 3.6.0](https://maven.apache.org/download.cgi)  
- [PostgreSQL 12.0](https://www.postgresql.org/download/)  
- [Git 2.26](https://git-scm.com/downloads/)  
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (recommend)  
- other dependencies you can see in [pom.xml](https://github.com/attiasas/FootballAssociationSystem/blob/master/pom.xml)  
  
## 📦 How To Install  
  
You can modify or contribute to this project by following the steps below:  
  
**1. Clone the repository**  
  
- Open terminal ( <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>T</kbd> )  
  
- [Clone](https://help.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository) to a location on your machine.  
 ```bash  
 # Clone the repository 
 $> git clone https://github.com/attiasas/sportify.git  

 # Navigate to the directory 
 $> cd sportify
  ```  

**2. Database connection (locally)**  
  
- Create an empty base  
  
```bash 
 $> psql postgres 
 
 # Create empty bases 
 postgres=# CREATE DATABASE sportify; 
 postgres=# CREATE DATABASE sportify_dev; 
 postgres=# \c sportify; 
 ```  
- [Connecting it to IntelliJ IDEA](https://www.jetbrains.com/help/idea/working-with-the-database-tool-window.html#create_data_source)  
  
- Set up a connection for JPA  
  
 ```bash  
 # Navigate to the directory 
 $> cd sportify  
 
 # Open config file 
 $> nano src/main/resources/config.properties  
 
 # Edit with the correct values 
	 ... 
	 db.server={localhost or postgres server} 
	 db.port=5432 
	 db.user={Enter username} 
	 db.password={Enter password} 
	 db.database=sportify 
	 ... 
 ```  
 
**3. Assembly and launch of the project**  
  
- Open terminal ( <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>T</kbd> )  
  
 ```bash  
 # Navigate to the directory 
 $> cd sportify  
 
 # compile and run 
 $> mvn clean package && java -jar target/sportify-0.0.1-SNAPSHOT-jar-with-dependecies.jar 
 ```  
**_Note to IntelliJ Users_**  
  
This project uses the lombok library in some of the model classes to cut down on boiler plate code. IntelliJ requires that you enable annotation processing to prevent it showing errors from unimplemented methods. You can find this in _Settings->Build, Execution, Deployment->Compiler->Annotation Processors_ - Or simply search for **_"enable annotation processing"_** in the settings search bar.  
  
**Run** the main application using the appropriate process for your set-up by IntelliJ Run/Debug.  
  
## Test Instructions  
- Various testing methods such as:  
  - Unit testing  
  - Integration Testing  
  - Functional Testing  
  
* Run tests with Maven from the command line.  
  
 ```bash  
 $> mvn verify 
 ```  
## Aditional Links  
  
- [System Demo]() - TODO  
- [Final Presentation]() - TODO  
- [Setup Demo]() - TODO  
- [Trace Google Sheet](https://docs.google.com/spreadsheets/u/1/d/17n6JLLVUFWz8y_0te5-axufSroGrtFTEjjgtUX7Dgyw/edit?usp=drive_web&ouid=104494091826522493400)  
- [Trello Board](https://trello.com/b/tQHRhOYQ/sportify-v3)  
  
**Design Patterns and Techniques**  
  
- Singleton  
- Façade  
- Dependency Injection  

## Screenshots
<img src=https://res.cloudinary.com/serfati/image/upload/v1603181422/Screenshot_from_2020-10-20_10-55-23_uhq5tg.png height="500" align="center"/>  
<img src=https://res.cloudinary.com/serfati/image/upload/v1603181422/Screenshot_from_2020-10-20_10-55-39_hef0fi.png height="500" align="center"/>  
<img src=https://res.cloudinary.com/serfati/image/upload/v1603181424/Screenshot_from_2020-10-20_10-58-04_kzyoqu.png height="500" align="center" />  
  
---  
> Author Serfati
  
## ⚖️ License  
  
This program is free software: you can redistribute it and/or modify it under the terms of the **MIT LICENSE** as published by the Free Software Foundation.  
  
**[⬆ back to top](#description)**
