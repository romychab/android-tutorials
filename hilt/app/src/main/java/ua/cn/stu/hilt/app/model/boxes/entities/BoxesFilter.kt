package ua.cn.stu.hilt.app.model.boxes.entities

enum class BoxesFilter {
    /**
     * Fetch all boxes, both active and inactive
     */
    ALL,

    /**
     * Fetch only active boxes
     */
    ONLY_ACTIVE
}