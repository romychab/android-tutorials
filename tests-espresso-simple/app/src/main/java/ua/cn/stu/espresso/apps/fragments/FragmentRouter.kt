package ua.cn.stu.espresso.apps.fragments

interface FragmentRouter {

    /**
     * Go back to the previous screen
     */
    fun goBack()

    /**
     * Launch cat details screen
     */
    fun showDetails(catId: Long)

}