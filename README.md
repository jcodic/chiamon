# chiamon
chia farming monitoring &amp; stats

---------------------
Installing postgresql
---------------------
1. Download postgresql from postgresql.org
2. Init db by executing: initdb.exe -D <db_folder>
3. In <db_folder> edit pg_hba.conf change all METHOD to md5 as follows:
```
# IPv4 local connections:
host    all             all             127.0.0.1/32            md5
```
4. Execute: psql.exe -U postgres and enter your password, enter following commands:
ALTER USER postgres WITH encrypted password 'your postgres password';
CREATE DATABASE chiamon;
\q
5. Stop and start postgresql service
6. Execute: psql.exe -U postgres chiamon < chiamon\sql\db_1.sql

-----------------
Installing tomcat
-----------------
1. Download tomcat from https://tomcat.apache.org as zip archive and extract all
2. Goto tomcat\webapps directory and remove all files and folders here
3. Create folder tomcat\webapps\ROOT\WEB-INF\lib and put jar from chiamon\target folder here
4. Copy chiamon\cfg-tomcat\web.xml to tomcat\webapps\ROOT\WEB-INF
5. Create folder tomcat\key. Now we are ready to create self signed certificate for your chiamon server. Find tool 'keytool' if you have java installed it must be on your path, if not, install java, go to java\bin. Execute following script: keytool -genkey -alias tomcat -keyalg RSA -keystore tomcat\key\mykey
Remember your keystore password. Command will prompt 'Enter key password for <tomcat>' just press enter.
6. Now open tomcat\conf\server.xml and add new connector:
```
<Connector
           protocol="org.apache.coyote.http11.Http11NioProtocol"
           sslImplementationName="org.apache.tomcat.util.net.jsse.JSSEImplementation"
           port="8443" maxThreads="200"
           scheme="https" secure="true"
	   SSLEnabled="true">
<SSLHostConfig>
    <Certificate
      certificateKeystoreFile="tomcat\key\mykey"
      certificateKeystorePassword="your keystore password"
      type="RSA"
      />
</SSLHostConfig>
</Connector>
```
7. Open tomcat\context.xml and add following inside <Context>:
```  
<Resource name="jdbc/postgres" auth="Container"
          type="javax.sql.DataSource" driverClassName="org.postgresql.Driver"
          url="jdbc:postgresql://localhost:5432/chiamon"
          username="postgres" password="your postgres password" maxTotal="20" maxIdle="10" maxWaitMillis="-1"/>
```
8. Create password for your tomcat user by executing:
tomcat\bin\digest.bat -a SHA-256 -h org.apache.catalina.realm.MessageDigestCredentialHandler your_password
you will get following result your_password:your_password_hash
copy your_password_hash and edit file tomcat\conf\tomcat-users.xml by insert inside <tomcat-users>:
```
<role rolename="admin"/>
<user username="myuser" password="your_password_hash" roles="admin"/>
```
comment all other users and roles
9. Open tomcat\conf\server.xml
Find and modify line as follows:
```  
	<Realm className="org.apache.catalina.realm.LockOutRealm">
        <!-- This Realm uses the UserDatabase configured in the global JNDI
             resources under the key "UserDatabase".  Any edits
             that are performed against this UserDatabase are immediately
             available for use by the Realm.  -->
        <Realm className="org.apache.catalina.realm.UserDatabaseRealm"
               resourceName="UserDatabase">
			<CredentialHandler className="org.apache.catalina.realm.MessageDigestCredentialHandler" algorithm="sha-256" />
	</Realm>
      </Realm>
```
10. Download postgresql driver for tomcat at 'https://jdbc.postgresql.org/download.html' and put it in tomcat\lib
11. Tomcat is ready! Start it by typing: tomcat\bin\catalina.bat run
12. Check if service is available by connecting from browser:
https://127.0.0.1:8443/servlet/clientservice?cmd=db
type 'admin' as username and your_password
if everything is correct return should be like this "PostgreSQL 9.6.22, compiled by Visual C++ build 1800, 64-bit"
Now your server is running and ready to accept connections from harvesters.

------------------------
Run harvester monitoring
------------------------
1. On each harvester node create folder 'chiamon' with following structure:
chiamon\logs - directory for logging
chiamon\target - directory for application, put main jar here
chiamon\node_log.xml - configuration file for logging
chiamon\harvester_node.ini - monitoring configuration
Configuring monitoring configuration file harvester_node.ini:
[init]
node_id - is used to identify each harvester
main_node_url - address of chiamon main server
main_node_user - user defined in tomcat, admin by default
main_node_pwd - encrypted password for user defined in tomcat
(to get encrypted password run chiamon\convert_password.bat your_password and you will receive encrypted password
example: your password is 'secret'
execute: convert_password.bat secret
your receive following string
Password [secret] -> [BNrtrFijAiM=]
in ini file set:
main_node_pwd = BNrtrFijAiM=
)
main_node_send_stats_interval - how often your harvester will send stats on main node
[vars]
io_time_max_scan - max time used for disk scanning, if disk scan is not completed in time it will be stopped and result will return error
io_time_max_read - max time used for read file operations
http_time_max_send - max time used for send result to remote node
[logs]
path - path to chia main node logs
file_filter - read all logs with this file filter
rescan_interval - logs rescan interval
rescan_time_max - max time used for read logs operation
[disks]
this section is common for all disks and is used as parent for all disks
rescan_interval - disk rescan interval
file_filter - read files with this file filter
read_interval - read interval for reading files
read_files - number of files to read per iteration
read_block_size - size to read on each file
smarts_interval - disk smart read interval (0 if smart is disabled)
smarts_cmd - command to call smartctl tool from shell
if your want to read disks S.M.A.R.T. info you have to install smartctl tools
https://www.smartmontools.org/wiki/Download
it's available for windows and linux
make smartctl available on path or write full path in cmd
use for linux:
```							       
smarts_cmd = dev=$(df -P {path} | awk 'END{print $1}') && sudo smartctl -a $dev
```
use for windows:
							       ```
smarts_cmd = smartctl -a {path}
							       ```
also you can set smarts_cmd for each disk separately
smartctl requires admin rights for reading disks S.M.A.R.T.
so under linux you have to run sudo ./run_harvester_node.sh
undex windows you may run run_harvester_node.bat under admin if you want all info
windows do not require admin rights, but can skip some data if not in admin mode
[disk_N]
define each disk use have on this node
id - unique identifier through all nodes you have
name - visible name
path - path to disk where plots are
for linux path could be like that:
/path
/plots/{id}
for windows path could be like that:
c:\\
c:\\plots\\
size - declared size of disk, example: 10tb

run harvester monitoring by executing:
./run_harvester_node.sh
or
run_harverster_node.bat
check logs if everything works well

-------------
Run UI client
-------------
From any remote location you can access your harvesters. 
Create create folder 'chiamon' with following structure:
chiamon\logs - directory for logging
chiamon\target - directory for application, put main jar here
chiamon\client_log.xml - configuration file for logging
chiamon\client.ini - UI configuration
Configuring UI configuration file client.ini:
[init]
client_id - client unique identifier
main_node_url - url where tomcat is running
main_node_user - look harvester_node.ini
main_node_pwd - look harvester_node.ini
[vars]
stats_collect_time - display data in this interval
[ui]
autoscript - automatically open chart windows on start
you can select multiple, separated by comma, like this:
autoscript = logsfarm_plots_total_chart,logsfarm_plots_farmed_chart
	
--------------------------
Build project from sources
--------------------------
mvn clean compile assembly:single	
	
or user compiled version from target folder
	
	
