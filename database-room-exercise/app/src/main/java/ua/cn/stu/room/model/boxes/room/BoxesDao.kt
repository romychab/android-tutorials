package ua.cn.stu.room.model.boxes.room

// todo #17: Create a DAO interface for working boxes & settings.
//           - annotate this interface with @Dao
//           - add method for activating/deactivating box for the specified account;
//             hint: method should have 1 argument of AccountBoxSettingDbEntity type and should
//                   be annotated with @Insert(onConflict=OnConflictStrategy.REPLACE);
//           - add method for fetching boxes and their settings by account ID;
//             hint: there are at least 4 options, the easiest one is to
//                   use Map<BoxDbEntity, AccountBoxSettingDbEntity> as a return type
interface BoxesDao
