package com.paypay.android.task.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        setupView()
        setupViewModelObservers()
    }

    abstract fun getViewBinding(): B


    abstract fun setupView()
    abstract fun setupViewModelObservers()

}