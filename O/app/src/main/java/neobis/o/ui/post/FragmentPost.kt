package neobis.o.ui.post

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.fragment_post.*
import neobis.o.R
import neobis.o.model.Comment
import neobis.o.model.Post
import neobis.o.model.WeatherInfo
import neobis.o.ui.post_comments.CommentAdapter
import neobis.o.ui.post_comments.PostCommentActivity
import neobis.o.utils.Constant
import neobis.o.utils.FileLog

/**
 * Created by Alier on 18.07.2018.
 */
class FragmentPost : Fragment(), FragmentContract.View, Adapter.Listener {
    private var presenter: FragmentContract.Presenter? = null
    private var adapter: Adapter? = null
    private var commentsAdapter: CommentAdapter? = null
    private var list: MutableList<Any> = ArrayList()
    private var isSelectedElements = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        adapter = Adapter(list, this)
        recyclerView.adapter = adapter
        presenter = FragmentPresenter(this)
        presenter?.loadInformation()
        getWeatherFor()

        swipeRefresh.setOnRefreshListener {
            presenter?.loadInformation()
            getWeatherFor()
        }
    }

    override fun onPostClicked(post: Post) {
        val intent = Intent(context, PostCommentActivity::class.java)
        intent.putExtra("post", post)
        startActivity(intent)
    }

    override fun onWeatherClicked() {
        startActivity(Intent(context, WebViewActivity::class.java))
    }

    override fun onSuccess(result: MutableList<Post>) {
        this.list.addAll(result)
        adapter?.setPostList(this.list)
        recyclerView.adapter = adapter
    }

    override fun onError(message: String?) {
        FileLog.showError(context, message)
    }

    override fun showProgress() {
        swipeRefresh?.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefresh?.isRefreshing = false
        isSelectedElements = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.extract_elements, menu)
    }

    override fun onWeatherResponse(result: WeatherInfo) {
        adapter?.addAtRandom(result)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.nElement -> {
                adapter?.setPostList(Constant.selectTenElements(list))
                isSelectedElements = true
            }
            R.id.nComments -> {
                if(isSelectedElements){
                    presenter?.loadComments(adapter?.list)
                }else
                    FileLog.showError(context, getString(R.string.error_select_first))
                isSelectedElements = false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getWeatherFor() {
        val bishkekId = 1528334 // Bishkek
        val osh = 1346798       // Osh
        val naryn = 1527592     // Нарын

        presenter?.getWeatherForCity(bishkekId)
        presenter?.getWeatherForCity(osh)
        presenter?.getWeatherForCity(naryn)
    }

    override fun onSelectComments(commentlist: ArrayList<Comment>) {
        if(commentsAdapter == null)
            commentsAdapter = CommentAdapter(commentlist)
        else
            commentsAdapter?.setPostCommentList(commentlist)
        recyclerView.adapter = commentsAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
        commentsAdapter = null
    }
}