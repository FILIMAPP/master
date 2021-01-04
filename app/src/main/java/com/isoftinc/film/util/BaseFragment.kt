package com.isoftinc.film.util
import android.os.Bundle
import android.view.WindowManager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.isoftinc.film.activity.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


open class BaseFragment : Fragment(){

    protected var TAG = "OttApp"
    protected lateinit var activity: BaseActivity
    protected var bottomNavigationView : BottomNavigationView? = null
    protected  var sideMenu : NavigationView? = null
    protected  var drawerLayout : DrawerLayout? = null



  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity() as BaseActivity
      if (activity is MainActivity)
       bottomNavigationView = activity.bottom_navigation
      sideMenu = activity.navigationView
      drawerLayout = activity.drawerLayout

        activity.window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
        TAG = javaClass.simpleName

    }






   open fun onBackPressed() {
        try {
            AndroidUtilities().hideKeyboard(view)
            fragmentManager!!.popBackStack()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
