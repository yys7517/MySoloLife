package com.youngsun.mysololife.board


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.youngsun.mysololife.R
import com.youngsun.mysololife.auth.IntroActivity
import com.youngsun.mysololife.comment.CommentModel
import com.youngsun.mysololife.comment.CommentRVAdapter
import com.youngsun.mysololife.databinding.ActivityBoardShowBinding
import com.youngsun.mysololife.utils.FbAuth
import com.youngsun.mysololife.utils.FbRef
import com.youngsun.mysololife.utils.TimeUtil
import java.lang.Exception

class BoardShowActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardShowBinding

    private val TAG = BoardShowActivity::class.java.simpleName


    private lateinit var boardUid : String  // 게시글 작성자 uid
    private lateinit var authUid : String   // 사용자 uid

    private lateinit var key : String    // 게시글 key

    private lateinit var commentRVAdapter : CommentRVAdapter

    private val commentDataList = ArrayList<CommentModel>()
    private val commentKeyList = ArrayList<String>()
    private val commentWriterList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView( this, R.layout.activity_board_show)

        /*
        val titleArea = binding.boardTitle
        val contentsArea = binding.boardContents
        val imgArea = binding.boardImg
        val timeArea = binding.boardWriteTime


        // 첫 번째 방법 - Intent 로 전달받은 값을 Setting 한다.
//        val title = intent.getStringExtra("title")
//        val contents = intent.getStringExtra("contents")
//        val time = intent.getStringExtra("time")
//
//        titleArea.text = title.toString()
//        contentsArea.text = contents.toString()
//        timeArea.text = time.toString()

         */

        key = intent.getStringExtra("key").toString()



        // 두 번째 방법 - 게시글 key 를 통해 Firebase 로 받아오는 방법.
        getBoardData( key )

        // 이미지가 있다면 ? -> 이미지 받아오기
        getBoardImgData( key )

        authUid = FbAuth.getUid()

        binding.boardSettingIcon.setOnClickListener {
            // 다이얼로그 창 띄워서 게시글 수정, 삭제 버튼 보여주기.
            BoardDialogSetting()
        }

        // 댓글 작성 완료 버튼
        binding.btnSubmit.setOnClickListener {
            InsertComment( key )
        }

        // 댓글 RecyclerView
        val commentRV = binding.commentsRV
        commentRVAdapter = CommentRVAdapter( commentDataList, commentKeyList , commentWriterList, key )    // 댓글목록, 댓글 key 목록, 게시글 key
        commentRV.adapter = commentRVAdapter
        commentRV.layoutManager = LinearLayoutManager( this )

        // 댓글 목록 가져오기
        getCommentData( key )



    }

    private fun InsertComment( boardKey : String ) {

        val comment = binding.edtComment.text.toString()
        val time = TimeUtil.getTime()

        // 댓글 삽입
        FbRef.commentRef
            .child(boardKey)
            .push()
            .setValue(
                CommentModel( comment, time, FbAuth.getUid() )      // 댓글 작성자.
            )

        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()

        binding.edtComment.setText("")
    }

    // 게시글 수정, 삭제 다이얼로그 창 띄워주기.
    private fun BoardDialogSetting() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.board_setting_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정 / 삭제")

        val alerDialog = mBuilder.show()

        val btnEdit = alerDialog.findViewById<Button>(R.id.btnEdit)!!
        val btnDel = alerDialog.findViewById<Button>(R.id.btnDel)!!


        // 게시글 수정 버튼 클릭
        btnEdit.setOnClickListener {
            if( boardUid == authUid ) {
                Toast.makeText(this, "게시글을 수정합니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent( this, BoardEditActivity::class.java )
                // 기존 게시글의 제목, 내용, 이미지 정보를 수정 페이지에 넘겨줘야 한다.
                // 게시글의 key 값을 넘겨주어 key 값을 바탕으로 정보를 가져오게 하자
                intent.putExtra("key", key)
                startActivity(intent)

                alerDialog.dismiss()

            } else{
                Toast.makeText(this, "게시글 작성자만 게시글을 수정하거나 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        // 게시글 삭제 버튼 클릭
        btnDel.setOnClickListener {
            if( boardUid == authUid ) {
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.board_delete_dialog, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setTitle("게시글 삭제")

                val alerDialog = mBuilder.show()

                val btnSubmit = alerDialog.findViewById<Button>(R.id.btnSubmit)!!
                val btnCancel = alerDialog.findViewById<Button>(R.id.btnCancel)!!

                btnSubmit.setOnClickListener {
                    // 게시글 작성자와 사용자가 같은 사용자라면 게시글 삭제를 진행
                    FbRef.boardRef.child(key).removeValue().addOnSuccessListener {
                        Log.d(TAG, "게시글 삭제 성공")
                    }.addOnFailureListener {
                        Log.d(TAG, "게시글 삭제 실패")
                    }


                    // 게시글에 이미지가 있다면 이미지 또한 삭제
                    FbRef.storageRef.child("${key}.png").delete().addOnSuccessListener {
                        Log.d(TAG, "이미지 삭제 성공")
                    }.addOnFailureListener {
                        Log.d(TAG, "이미지 삭제 실패")
                    }

                    Toast.makeText(this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }

                btnCancel!!.setOnClickListener {
                    alerDialog.dismiss()    // 다이얼로그 닫기
                }

            }
            else {
                Toast.makeText(this, "게시글 작성자만 게시글을 수정하거나 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun getBoardImgData(key: String) {
        val boardImageView = binding.boardImg

        val boardImgRef = FbRef.storageRef.child("${key}.png")

        boardImgRef.downloadUrl.addOnSuccessListener {
            // 게시글의 key 값에 따른 이미지가 다운로드 되었다면 ?
            // 이미지를 다운로드해서 Glide를 통해 ImageView에 적용한다.

            binding.boardImg.visibility = View.VISIBLE

            if( !this.isFinishing ) {
                Glide
                    .with(this)
                    .load( it )
                    .into(boardImageView)
            }

        }.addOnFailureListener {
            // 게시글의 key 값에 따른 이미지가 다운로드 없다면 ? default 값으로.
        }
    }

    private fun getBoardData( key : String )  {
        val getBoardListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    Log.d(TAG, dataSnapshot.toString())
                    // DataSnapshot { key = -Mz-t9-fXqtI2T_sjtzm, value = {uid=FE4EMT7tIthxHrDQhke7TUyCVv02, contents=i am a boy . you are a girl., time=2022.03.25 20:44:45, title=i am a boy } }
                    val board = dataSnapshot.getValue( BoardModel::class.java )

                    binding.boardTitle.text = board!!.title
                    binding.boardContents.text = board!!.contents
                    binding.boardWriteTime.text = board!!.time

                    boardUid = board!!.uid

                    // 내가 쓴 글이면 게시글 수정 / 삭제 버튼이 보이게.
                    if( boardUid == FbAuth.getUid() ) {
                        binding.boardSettingIcon.visibility = View.VISIBLE
                    }

                }catch ( e : Exception ) {
                    Log.d(TAG, "삭제완료")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FbRef.boardRef.child(key).addValueEventListener(getBoardListener)
    }

    private fun getCommentData( boardKey: String ) {
        val getCommentListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    commentDataList.clear()
                    commentKeyList.clear()
                    commentWriterList.clear()

                    Log.d("댓글보기", dataSnapshot.toString())

                    for ( dataModel in dataSnapshot.children ) {
                        val comment = dataModel.getValue( CommentModel::class.java )
                        val commentKey = dataModel.key.toString()

                        Log.d("댓글key", commentKey)

                        commentDataList.add( comment!! )
                        commentKeyList.add( commentKey!! )
                        commentWriterList.add( comment!!.writer )
                    }
                    // 시간 내림차순으로 정렬하기 위해, 삽입된 순서의 반대로 정렬.
                    commentDataList.reverse()
                    commentKeyList.reverse()
                    commentWriterList.reverse()

                    binding.commentsCount.text = "댓글 (${commentRVAdapter.itemCount})"

                    commentRVAdapter.notifyDataSetChanged()

                }catch ( e : Exception ) {
                    Log.d(TAG, "삭제완료")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FbRef.commentRef.child(boardKey).addValueEventListener(getCommentListener)
    }

    // 수정을 마친 후 돌아왔을 때, Firebase Storage 에서 이미지 파일을 지웠다가 새로 업로드하기 때문에
    // 딜레이를 주고 게시글 정보를 새로 고침 해준다.
    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            getBoardData(key)
            getBoardImgData(key)
            getCommentData(key)
        },4000)

    }

}