package com.tirupati.vendor.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.tirupati.vendor.helper.interfaces.GetToolbarCallback
import com.tirupati.vendor.fragmnts.ProgressDialogFragment
import com.tirupati.vendor.views.CustomToolbar
import com.tirupati.vendor.views.ToolbarConfig


//import com.viviroomsmain.app.extensions.searchProds


//abstract class BaseFragment<VModel:ViewModel,VBinding : ViewBinding> : Fragment() {
abstract class BaseFragment<VBinding : ViewBinding> : Fragment(), View.OnClickListener {

    open var mViewModel: Boolean = false
    private lateinit var mBinding: VBinding
    protected abstract fun getViewBinding(): VBinding

//    protected lateinit var viewModel: VModel
//    protected abstract fun getViewModelClass(): Class<ViewModel>

    protected var baseActivity: BaseActivity<VBinding>? = null
    private var getToolbarCallback: GetToolbarCallback? = null
    private var customToolbar: CustomToolbar? = null
    private var toolbarConfig: ToolbarConfig? = null
    var isFirstTimeLoad: Boolean = true


    /**
     * ProgressDialogFragment showing methods
     */
    var progressDialogFragment: ProgressDialogFragment? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        baseActivity = activity as BaseActivity<VBinding>?
        init()
    }


    private fun init() {
        mBinding = getViewBinding()

//        viewModel = if (mViewModel) {
//            ViewModelProvider(requireActivity())[getViewModelClass()]
//        } else {
//            ViewModelProvider(this)[getViewModelClass()]
//        }

        progressDialogFragment = ProgressDialogFragment.newInstance()



    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnlineCheck(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")

                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun showProgressDialog(isShow: Boolean) {
        try {
            if (isShow == true) {
                if (progressDialogFragment?.dialog == null || progressDialogFragment?.dialog?.isShowing == false)
                    progressDialogFragment?.show(
                        activity?.supportFragmentManager!!,
                        javaClass.simpleName
                    )
            } else {
                if (progressDialogFragment?.fragmentManager != null)
                    progressDialogFragment?.dismissAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




    /**
     * Used when we dont want to create/inflate view if same fragment instance is used.
     */
    var prevViewDataBinding: ViewDataBinding? = null
    fun <T : ViewDataBinding> createOrReloadView(
        inflater: LayoutInflater,
        resLayout: Int,
        container: ViewGroup?
    ): T {
        isFirstTimeLoad = false
        if (prevViewDataBinding == null) {
            prevViewDataBinding = DataBindingUtil.inflate(inflater, resLayout, container, false)
            isFirstTimeLoad = true
        } else if (prevViewDataBinding?.root?.parent != null) {
            container?.removeView(prevViewDataBinding?.root)
        }
        return prevViewDataBinding as T
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GetToolbarCallback) {
            getToolbarCallback = context

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        manageToolbar()
       // manageBottombar()
        setUpViews()
        observeData()
        fittingRoomDisplayStar()

    }


    open fun setUpViews() {
    }

    open fun observeView() {}
    open fun observeData() {}
    open fun fittingRoomDisplayStar(){}


    //BOTTOM BAR
    fun withoutBottomBar(): BaseFragment<VBinding>? {
        var bundle: Bundle? = Bundle()
        if (arguments != null) {
            bundle = arguments
        }

        bundle?.putBoolean("withoutbottombar", true)
        arguments = bundle
        return this
    }

    //BOTTOM BAR
    fun withBottomBar(): BaseFragment<VBinding>? {
        var bundle: Bundle? = Bundle()
        if (arguments != null) {
            bundle = arguments
        }
        bundle?.putBoolean("withoutbottombar", false)
        arguments = bundle
        return this
    }



    //show and hide bottombar with animation method
    fun showBottomBar(boolean: Boolean) {
        try {
            if (boolean) {

              //  (activity as HomeTabActivityBottomBar1).setBottomBarVisibilityWithAnimation(true)
              //  (activity as SelectMainRolesActivity).setBottomBarVisibility(true)
            } else {
               // (activity as HomeTabActivityBottomBar1).setBottomBarVisibilityWithAnimation(false)
              //  (activity as SelectMainRolesActivity).setBottomBarVisibility(false)
            }
        } catch (e: Exception) {

        }
    }





    //TOOLBAR RELATED CODEback
    fun withToolbarConfig(toolbarConfig: ToolbarConfig): BaseFragment<VBinding>? {
        var bundle: Bundle? = Bundle()
        if (arguments != null) {
            bundle = arguments
        }
        bundle?.putParcelable("toolbarconfig", toolbarConfig)
        arguments = bundle
        Log.i("BaseFragment", "withToolbarConfig: " + arguments.toString())


        return this
    }


    private fun manageToolbar() {
        if (arguments != null && arguments?.containsKey("toolbarconfig") == true) {
            toolbarConfig = requireArguments().getParcelable("toolbarconfig")
            //customToolbar?.binding?.tvCartCount?.text="18"


            if (toolbarConfig != null && getToolbarCallback != null) {
                customToolbar = getToolbarCallback?.toolbar
                customToolbar?.setConfig(toolbarConfig)


            }
        }
    }

    fun overrideToolbar(toolbar: CustomToolbar) {
        if (getToolbarCallback != null) {
            getToolbarCallback?.toolbar?.visibility = View.GONE
        }
        this.customToolbar = toolbar
        if (toolbarConfig != null) {
            toolbar.setConfig(toolbarConfig)
        }



        this.customToolbar?.setToolbarClickListener(toolbarClickListener)
    }






    /*fun addFragment(mFrag: Fragment?) {
        if (activity is SIgnInRegisterActivity) {
            (activity as SIgnInRegisterActivity).addFragment(mFrag)
        }
    }*/



    private val toolbarClickListener: View.OnClickListener = View.OnClickListener { v ->
        if (!onToolbarClick(v)) {
            when (v.id) {
/*                R.id.ivBack -> {
                    if (activity != null && activity is SelectMainRolesActivity) {
                        (activity as SelectMainRolesActivity).onBackPressed()
                    } else {
                        onBackPressed()
                    }
                }*/

            }
        }
    }

    open fun onToolbarClick(view: View): Boolean {
        return false
    }


    fun popBackStack(popDepth: Int = 0, needsAnimation: Boolean = true) {

        Log.d("BaseFragment", "popBackStack popDepth:" + popDepth)
    }

    override fun onDetach() {
        super.onDetach()
        getToolbarCallback = null
    }


}

