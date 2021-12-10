package ua.cn.stu.room.model.boxes.room.entities

// todo #16: Add AccountBoxSettingDbEntity
//          - fields: accountId, boxId, isActive.
//          - use composite primary key: (accountId + boxId);
//            hint: use 'primaryKeys' parameter of @Entity annotation.
//          - add index for the second column of composite primary key (boxId);
//            hint: use 'indices' parameter of @Entity annotation.
//          - add foreign keys for accountId and boxId fields. Foreign keys should point to the
//            AccountDbEntity and BoxDbEntity with CASCADE option for update/delete actions.
//            hint: use 'foreignKeys' parameter of @Entity annotation.
//          - DO NOT FORGET to add this entity to the @Database annotation of AppDatabase class
class AccountBoxSettingDbEntity
