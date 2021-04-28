package ua.cn.stu.navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ua.cn.stu.navigation.R
import ua.cn.stu.navigation.contract.HasCustomTitle
import ua.cn.stu.navigation.contract.navigator
import ua.cn.stu.navigation.databinding.FragmentBoxBinding

class BoxFragment : Fragment(), HasCustomTitle {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentBoxBinding.inflate(inflater, container, false).apply {
        toMainMenuButton.setOnClickListener { onToMainMenuPressed() }
    }.root

    private fun onToMainMenuPressed() {
        navigator().goToMenu()
    }

    override fun getTitleRes(): Int = R.string.box

}