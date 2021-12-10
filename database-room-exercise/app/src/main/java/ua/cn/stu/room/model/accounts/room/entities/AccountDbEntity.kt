package ua.cn.stu.room.model.accounts.room.entities

import ua.cn.stu.room.model.accounts.entities.Account
import ua.cn.stu.room.model.accounts.entities.SignUpData

// todo #5: Define AccountDbEntity.
//          - fields: id, email, username, password, createdAt.
//          - annotate class with @Entity annotation:
//            - use 'indices' parameter to add UNIQUE constraint for 'email' field;
//            - use 'tableName' to customize the table name for this entity;
//          - use @PrimaryKey annotation for the identifier field:
//            - use 'autoGenerate' parameter if you don't want to specify ID every time upon creating a new account;
//          - use @ColumnInfo(name='...') to customize column names and add additional
//            parameters (e.g. 'collate = ColumnInfo.NOCASE').
//          - add toAccount() method for mapping AccountDbEntity objects to Account objects.
//          - add companion object with fromSignUpData(signUpData: SignUpData) method for creating a new
//            instance of AccountDbEntity from SignUpData object.
class AccountDbEntity {

    fun toAccount(): Account = TODO()

    companion object {
        fun fromSignUpData(signUpData: SignUpData): AccountDbEntity = TODO()
    }

}
