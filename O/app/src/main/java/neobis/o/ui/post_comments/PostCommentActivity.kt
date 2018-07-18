package neobis.o.ui.post_comments

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_comments.*
import neobis.alier.bilimkana.ui.BaseActivity
import neobis.o.R
import neobis.o.model.Comment
import neobis.o.model.Post

class PostCommentActivity : BaseActivity(), PostCommentContract.View {
    private var presenter: PostCommentContract.Presenter? = null
    private var adapter: CommentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        adapter = CommentAdapter(ArrayList())
        recyclerView.adapter = adapter
        presenter = PostCommentPresenter(this)
        val post = intent?.getParcelableExtra<Post>("post")
        presenter?.loadComments(post?.id)

        postTitle.text = post?.title
        postDescription.text = post?.body
    }

    override fun onSuccess(result: List<Comment>) {
        adapter?.setPostCommentList(result)
    }
}