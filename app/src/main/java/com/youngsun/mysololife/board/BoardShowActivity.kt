package com.youngsun.mysololife.board


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.ActivityBoardShowBinding
import com.youngsun.mysololife.utils.FbAuth
import com.youngsun.mysololife.utils.FbRef
import java.lang.Exception

class BoardShowActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardShowBinding

    private val TAG = BoardShowActivity::class.java.simpleName


    private lateinit var boardUid : String  // 게시글 작성자 uid
    private lateinit var authUid : String   // 사용자 uid

    private lateinit var key : String    // 게시글 key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView( this, R.layout.activity_board_show)

        /*
        val titleArea = binding.boardTitle
        val contentsArea = binding.boardContents
        val imgArea = binding.boardImg
        val timeArea = binding.boardWriteTime
         */

        // 첫 번째 방법 - Intent 로 전달받은 값을 Setting 한다.
//        val title = intent.getStringExtra("title")
//        val contents = intent.getStringExtra("contents")
//        val time = intent.getStringExtra("time")
//
//        titleArea.text = title.toString()
//        contentsArea.text = contents.toString()
//        timeArea.text = time.toString()

        key = intent.getStringExtra("key").toString()

        // 두 번째 방법 - 게시글 key 를 통해 Firebase 로 받아오는 방법.
        getBoardData( key )

        authUid = FbAuth.getUid()

        // 이미지가 있다면 ? -> 이미지 받아오기
        getBoardImgData( key )

        binding.boardSettingIcon.setOnClickListener {
            // 다이얼로그 창 띄워서 게시글 수정, 삭제 버튼 보여주기.
            BoardDialogSetting()
        }

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

            } else{

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

            Glide
                .with(this)
                .load( it )
                .into(boardImageView)

        }.addOnFailureListener {
            // 게시글의 key 값에 따른 이미지가 다운로드 없다면 ? default 값으로.
        }
    }

    private fun getBoardData( key : String ) {
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
}