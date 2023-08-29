import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmc.recipe.R
import com.cmc.recipe.data.model.response.CommentContent
import com.cmc.recipe.presentation.ui.common.CommentAdapter
import com.cmc.recipe.utils.highlightText
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetCommentFragment(context: Context, private val layoutResourceId: Int,  private val adapter: CommentAdapter, private val newList: List<CommentContent>) :
    BottomSheetDialog(context) {

    private lateinit var rvComment: RecyclerView
    private lateinit var bottomLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)

        val text = "댓글 ${adapter.getData().size}개"
        val tvCommentSheetCnt: TextView? = findViewById(R.id.tv_comment_sheet_cnt)
        tvCommentSheetCnt?.text = context.highlightText(text,"${adapter.getData().size}")

        val bottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet_comment)
        val bottomLayout = findViewById<ConstraintLayout>(R.id.constraintLayout_bottom)

        bottomSheet?.post {
            val bottomSheetVisibleHeight = bottomSheet.height - bottomSheet.top
            bottomLayout?.y =
                (bottomSheetVisibleHeight - bottomLayout?.height!!).toFloat()
        }


        rvComment = findViewById<RecyclerView>(R.id.rv_comment)!!
        rvComment?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvComment?.adapter = adapter
        adapter.replaceData(newList)

        val etComment = findViewById<EditText>(R.id.et_comment)
        val ibSend = findViewById<ImageButton>(R.id.ib_send)

        ibSend?.setOnClickListener {

        }

        var isInitialState = true
        this.behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    val layoutParams = rvComment?.layoutParams
                    layoutParams?.height = bottomSheet.height - bottomLayout?.height!!
                    rvComment?.layoutParams = layoutParams
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        this.behavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    override fun show() {
        super.show()

        if (this.behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            val layoutParams = rvComment.layoutParams
            layoutParams.height = findViewById<LinearLayout>(R.id.bottom_sheet_comment)?.height!! - bottomLayout.height
            rvComment.layoutParams = layoutParams
        }

        this.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}

