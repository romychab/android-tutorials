package ua.cn.stu.tabs.model.settings

interface AppSettings {

    /**
     * Get account ID of the current logged-in user or [NO_ACCOUNT_ID] otherwise.
     */
    fun getCurrentAccountId(): Long

    /**
     * Set account ID of the logged-in user or [NO_ACCOUNT_ID] if user is not logged-in.
     */
    fun setCurrentAccountId(accountId: Long)

    companion object {
        /**
         * Indicates that there is no logged-in user.
         */
        const val NO_ACCOUNT_ID = -1L
    }

}