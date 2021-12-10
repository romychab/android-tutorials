package ua.cn.stu.room.model.accounts.room.entities

// todo #6: Create a tuple for fetching account id + account password.
//          Tuple classes should not be annotated with @Entity but their fields may be
//          annotated with @ColumnInfo.
class AccountSignInTuple

// todo #7: Create a tuple for updating account username.
//          Such tuples should contain a primary key ('id') in order to notify Room which row you want to update
//          and fields to be updated ('username' is this case).
class AccountUpdateUsernameTuple