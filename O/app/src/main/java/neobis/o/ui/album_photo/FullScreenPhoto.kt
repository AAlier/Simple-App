package neobis.o.ui.album_photo

import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_full_photo.*
import neobis.alier.bilimkana.ui.BaseActivity
import neobis.o.R

/**
 * Created by Alier on 18.07.2018.
 */
class FullScreenPhoto : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_photo)

        toolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val url = intent.getStringExtra("url")
        Glide.with(this).load(url).into(imageView)
    }
}