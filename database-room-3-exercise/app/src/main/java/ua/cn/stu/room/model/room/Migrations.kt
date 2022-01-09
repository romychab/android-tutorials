package ua.cn.stu.room.model.room

// todo #8: create an AutoMigrationSpec1To2 class derived from the Room's AutoMigrationSpec class.

// todo #10: use RenameColumn annotation to tell Room that you want to rename the 'password'
//           column into a 'hash' column;
//           Note: if you need to rename more than 1 column within one migration then you need
//                 to use 'RenameColumn.Entries' annotation instead of 'RenameColumn'.

// todo #11: Override 'onPostMigrate' method in order to convert plain passwords of old accounts
//           into 'hash' and 'salt'. Use SQL-queries and Cursor for querying data and ContentValues
//           for updating data. SecurityUtils class instance is located in Repositories.securityUtils.

// todo #12: Now do not build and run the project. Just check the database content of the already installed
//           app in the emulator/device. Then build and run the project again and compare it with a new
//           upgraded database after migration.

// todo #16: Create a migration object derived from Migration class. Specify 'from' (2) and 'to' (3)
//           versions you want to migrate. Override 'migrate' method and use 'ALTER TABLE' SQL-query
//           in order to add a new 'phone' column to the 'accounts' table.

// todo #18: Before running the project check the database of the installed app; then run the project,
//           check and compare the databases again.

// todo #19: Do not forget to update pre-packaged database in the assets. It's schema should be the same
//           as the latest schema of your current database (version 3, with 'hash' and 'salt' columns
//           instead of 'password' column and with 'phone' column).