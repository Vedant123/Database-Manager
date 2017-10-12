This is a framework class that offers some basic functionality into the Java Database Connectivity (JDBC). It features the following methods or function: 

<ol>
<li><em><bold>setName(String tableName)</bold></em> - Setter function for the variable 'databaseName'</li>
<li><em><bold>getName()</bold></em> - Getter function for the variable 'databaseName'</li>
<li><em><bold>getDatabaseMatrix(String tableName)</bold></em> -  Returns a two-dimensional vector of all the items in a specified table within the current database.. </li>
<li><em><bold>addToDatabase(ArrayList<Object> rowList, String tableName)</bold></em> - Calls another private function that formats an SQL statement to append a row to the database, and runs the JDBC code tied to running and executing this formatted statement
<li><em><bold>updateDatabaseRow(ArrayList<Object> rowList, String tableName, int id)</bold></em> - Similar to the above function, this function calls upon another local method to parse through the Object array rowList and formats it into an SQL statement that is then exeucting and run by this function
</ol>

Through these methods, the manual processing involved with the JDBC is condensed into simple function call. Instead of repeatedly connecting and formatting SQL statements manually, this class simply offers a more clean and concise means to connect to a Database and control its Input and Output streams. 


