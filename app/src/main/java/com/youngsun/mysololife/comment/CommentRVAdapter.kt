package com.youngsun.mysololife.comment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.youngsun.mysololife.R
import com.youngsun.mysololife.utils.FbAuth
import com.youngsun.mysololife.utils.FbRef
import com.youngsun.mysololife.utils.TimeUtil

class CommentRVAdapter(
    private val items: ArrayList<CommentModel>,
    private val keyList: ArrayList<String>,
    val writerList: ArrayList<String>,
    val boardKey: String
) : RecyclerView.Adapter<CommentRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRVAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_rv_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position], keyList[position], writerList[position] )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: CommentModel, commentKey: String, writer: String) {

            val txtComment: TextView = itemView.findViewById(R.id.txtComment)
            val txtTime: TextView = itemView.findViewById(R.id.txtCommentTime)

            val edtComment: EditText = itemView.findViewById(R.id.edtComment)
            val btnSubmit: ImageView = itemView.findViewById(R.id.btnSubmit)

            val editCommentBtn: TextView = itemView.findViewById(R.id.btnEditComment)
            val delCommentBtn: TextView = itemView.findViewById(R.id.btnDelComment)

            val commentSetLayout: LinearLayout = itemView.findViewById(R.id.commentSetLayout)

            val context = itemView.context

            // 댓글 작성자이면, 댓글 수정 삭제 버튼이 보이게 하자.
            if (writer == FbAuth.getUid()) {
                commentSetLayout.visibility = View.VISIBLE
            }

            // 댓글 정보 바인딩
            txtComment.text = item!!.comment
            txtTime.text = item!!.time

            // 수정 버튼
            editCommentBtn.setOnClickListener {

                Toast.makeText(context,"댓글을 수정합니다." , Toast.LENGTH_SHORT).show()

                // 기존 댓글 레이아웃 안보이게
                txtComment.visibility = View.GONE
                txtTime.visibility = View.GONE
                commentSetLayout.visibility = View.GONE

                // 수정 레이아웃 보이게
                edtComment.visibility = View.VISIBLE
                btnSubmit.visibility = View.VISIBLE

                // 기존 댓글 내용 불러오기
                edtComment.setText(txtComment.text.toString())

                // 입력(수정) 완료 버튼 눌렀을 때
                btnSubmit.setOnClickListener {
                    val comment = edtComment.text.toString()
                    val time = TimeUtil.getTime()

                    // 댓글 수정
                    FbRef.commentRef
                        .child(boardKey)
                        .child(commentKey)
                        .setValue(
                            CommentModel(comment, time, FbAuth.getUid() )
                        ).addOnSuccessListener {
                            // 수정 성공 시
                            edtComment.visibility = View.GONE   // 수정 댓글 입력 창 없어지게
                            btnSubmit.visibility = View.GONE    // 수정 완료 버튼 없어지게

                            txtComment.visibility = View.VISIBLE    // 댓글 창 보이게
                            txtTime.visibility = View.VISIBLE       // 댓글 작성 시간 보이게

                            Toast.makeText(itemView.context, "댓글이 수정되었습니다.", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener{
                            // 수정 실패 시
                            Toast.makeText(itemView.context, "댓글 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            // 삭제 버튼
            delCommentBtn.setOnClickListener {

                // 다이얼로그 띄울까 ?
                val mDialogView =
                    LayoutInflater.from(context).inflate(R.layout.comment_delete_dialog, null)
                val mBuilder = AlertDialog.Builder(context)
                    .setView(mDialogView)
                    .setTitle("댓글 삭제")

                val alerDialog = mBuilder.show()

                val btnSubmit = alerDialog.findViewById<Button>(R.id.btnSubmit)!!
                val btnCancel = alerDialog.findViewById<Button>(R.id.btnCancel)!!

                btnSubmit.setOnClickListener {
                    // 게시글 작성자와 사용자가 같은 사용자라면 게시글 삭제를 진행
                    FbRef.commentRef
                        .child(boardKey)
                        .child(commentKey)
                        .removeValue().addOnSuccessListener {
                            alerDialog.dismiss()
                            Log.d("댓글 삭제", "댓글 삭제 성공")
                            Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()


                        }.addOnFailureListener {
                            alerDialog.dismiss()
                            Log.d("댓글 삭제", "댓글 삭제 실패")
                            Toast.makeText(context, "댓글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                }

                btnCancel!!.setOnClickListener {
                    alerDialog.dismiss()    // 다이얼로그 닫기
                }


            }
        }
    }
}