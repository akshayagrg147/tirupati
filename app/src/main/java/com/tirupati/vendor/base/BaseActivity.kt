package com.tirupati.vendor.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.tirupati.vendor.R
import com.tirupati.vendor.ui.LogoutDialogFragment
import com.tirupati.vendor.utils.MyContextWrapper
import com.tirupati.vendor.views.CustomToolbar
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseActivity<VBinding : ViewBinding> : AppCompatActivity(), View.OnClickListener {

    open var mViewModel: Boolean = false
    private lateinit var mBinding: VBinding
    protected abstract fun getViewBinding(): VBinding

    fun overrideToolbar(customToolbar: CustomToolbar?) {
        customToolbar?.setToolbarClickListener(toolbarClickListener)
    }

    override fun attachBaseContext(newBase: Context?) {


        super.attachBaseContext(MyContextWrapper.wrap(newBase, updateLoacle("en")))


    }

   fun updateLoacle(locale:String):String
   {

       return locale
   }

//    protected lateinit var viewModel: VModel
//    protected abstract fun getViewModelClass(): Class<ViewModel>
//    private fun getViewModelClass(): Class<VModel> {
//        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
//        return type as Class<VModel>
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setUpViews()
        observeData()
        setUpAnimation()


    }

    private fun init() {
        mBinding = getViewBinding()

        setContentView(mBinding.root)
    }
//SNACK BAR

  //  abstract fun attachObservers()

    open fun setUpViews() {}
    open fun observeView() {}
    open fun observeData() {}
    open fun setUpAnimation() {}



//    private val toolbarClickListener: View.OnClickListener = View.OnClickListener { v ->
//        if (!onToolbarClick(v)) {
//            when (v.id) {
//                R.id.ivBack -> {
//                    finish()
//                }
//            }
//        }
//    }



    fun showLogoutDialog(func: (View) -> Unit = {}): DialogFragment {

        val dialogFragment = LogoutDialogFragment()
        dialogFragment.setOnClickListener(View.OnClickListener { (func(it)) })
        dialogFragment.show(this.supportFragmentManager, "dialog")
        return dialogFragment
    }

    private val toolbarClickListener: View.OnClickListener = View.OnClickListener { v ->
        if (!onToolbarClick(v)) {
            when (v.id) {
                R.id.ivBack -> {

//                    Toast.makeText(
//                        this,
//                        "ivBack",
//                        Toast.LENGTH_SHORT
//                    ).show()

                   // DeviceUtils.hideKeyboard(this)
//                    Handler().postDelayed({
//                        onBackPressed()
//                    }, 100)

                }
            }
        }
    }

    open fun onToolbarClick(view: View): Boolean {

        return false
    }

}

