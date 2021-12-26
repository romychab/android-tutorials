package ua.cn.stu.room.model.boxes.room.views

// todo #8: Create a database view named SettingsDbView with columns 'account_id', 'box_id', 'color_name', 'color_value'
//          and 'is_active' ('color_name' and 'color_value' from 'boxes' table, other columns
//          from 'accounts_boxes_settings' table). The view should contain rows which are absent
//          in 'accounts_boxes_settings' table filled with default 'is_active' value (is_active = 1)
//          Hints:
//            1) use JOIN and LEFT JOIN
//            2) use ifnull() SQLite-function for assigning default value
//            3) use @DatabaseView annotation to create a view
//            4) use 'DB Browser for SQLite' app to test SQL-queries.

// todo #13: Remove 'color_name' and 'color_value' columns from the database view. Make the structure
//           of the view the same as in 'accounts_boxes_settings' table. The only difference: view
//           should also contain rows which are absent in 'accounts_boxes_settings' table filled
//           with default 'is_active' value (is_active = 1)