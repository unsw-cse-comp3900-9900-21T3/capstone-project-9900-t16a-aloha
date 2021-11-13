# capstone-project-9900-t16a-aloha
capstone-project-9900-t16a-aloha created by GitHub Classroom

**The project is running on virtual machine Lubuntu 20.04.1**
# Front End Server

There are no additional environments for running front-end. 

Chrome is recommended browser to use.

The back-end server needs to start first to run front-end.

Using __python__ to start server is **recommended**.

To run the front-end server useing python, go to web folder, and then run following

`python3 -m http.server`

Visit on either:
  http://127.0.0.1:8000




# Back End Server

## Environment Setup

### Prerequisite

First, download the project folder to the home directory of Lubuntu by using following commands

`cd ~`

`git clone https://github.com/unsw-cse-comp3900-9900-21T3/capstone-project-9900-t16a-aloha.git`

### Install MySQL

Type following commands in terminal to install MySQL Server

`sudo apt-get update`

`sudo apt install mysql-server`

### MySQL Configuration
By default, MySQL must bu login in with `sudo` command, so we need to change the privileges.
Running MySQL shell fisrt by typing

`sudo mysql -uroot -p`

It requires password, just input Enter(empty password).


Then MySQL would run with a `mysql>` prompt.Typing follwing MySQL queries line by line:

`DROP USER 'root'@'localhost';`

`CREATE USER 'root'@'%' IDENTIFIED BY '12345678';`

`GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;`

`FLUSH PRIVILEGES;`

`CREATE DATABASE ecommerce;`

`use ecommerce;`

`source /home/lubuntu/capstone-project-9900-t16a-aloha/ecommerce.sql`

`exit;`

### Install Openjdk@11

`sudo apt-get install openjdk-11-jdk`

### Run backend server

Enter the root of project directory, then type the command in terminal.

`java -jar new.jar`

Done! Now the server is ready to use.

# Recommend 
To generate the recommendation table, 
1. open Jupiter notebook
2. open file generateRecommendProducts.ipynb and run all


It will generate a file called “no_duplicate_10_most_recommend.csv”.
