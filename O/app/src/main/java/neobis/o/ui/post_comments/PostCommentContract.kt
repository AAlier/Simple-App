package neobis.o.ui.post_comments

import neobis.alier.bilimkana.util.IProgressBar
import neobis.alier.bilimkana.util.IResult
import neobis.o.model.Comment

/**
 * Created by Alier on 18.07.2018.
 */
interface PostCommentContract {
    interface View : IProgressBar, IResult<List<Comment>>

    interface Presenter {
        fun loadComments(postId: Int?)
    }
}