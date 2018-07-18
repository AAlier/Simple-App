package neobis.o.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import neobis.o.R
import neobis.o.ui.album.FragmentAlbum
import neobis.o.ui.post.FragmentPost


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.post -> {
                    loadFragment(FragmentPost())
                }
                R.id.album -> {
                    loadFragment(FragmentAlbum())
                }
            }
            false
        }
        bottomNavigationView.selectedItemId = R.id.post
    }

    //switching fragment
    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            return true
        }
        return false
    }
}
