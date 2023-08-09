package com.cmc.recipe.presentation.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<T:ViewBinding>(private val inflate: Inflate<T>) : Fragment() {

    private var _binding : T? = null
    val binding get() = _binding!!

    private var imm : InputMethodManager?=null

    abstract fun initFragment()
    open fun observe() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        initFragment()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //키보드 내리기
    fun hideKeyboard(v:View){
        if(v!=null){
            imm?.hideSoftInputFromWindow(v.windowToken,0)
        }
    }

    //Toast Message show
    fun showToastMessage(text:String){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show()
    }

    fun movePage(naviRes:Int){
        findNavController().navigate(naviRes)
    }
}