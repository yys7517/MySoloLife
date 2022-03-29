package com.youngsun.mysololife.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.ActivityBoardEditBinding
import com.youngsun.mysololife.utils.FbAuth
import com.youngsun.mysololife.utils.FbRef
import com.youngsun.mysololife.utils.TimeUtil
import java.io.ByteArrayOutputStream
import java.lang.Exception

class BoardEditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardEditBinding

    private val TAG : String = BoardEditActivity::class.java.simpleName

    private lateinit var boardImageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit )
        boardImageView = binding.imageView

        val key = intent.getStringExtra("key").toString()

        // 게시글의 key 값을 통해 기존 게시글의 데이터 값을 받아온다.
        getBoardData( key )
        getBoardImgData( key )

        // 이미지 뷰를 클릭 시 이미지를 다시 수정할 수 있게 해야한다.
        boardImageView.setOnClickListener {
            // 1. 기존 이미지 삭제.

            val gallery = Intent( Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI )
            startActivityForResult( gallery, 100 )
        }

        // 수정 완료 버튼 클릭 시
        binding.btnSubmit.setOnClickListener {

            // 수정된 게시글 데이터를 업로드.
            val title = binding.edtBoardTitle.text.toString()
            val contents = binding.edtContent.text.toString()

            FbRef.boardRef.
            child(key).
            setValue( BoardModel( title, contents, FbAuth.getUid() , TimeUtil.getTime() ) )

            // 게시글 이미지가 하나 뿐 이므로..
            // 1. 기존 이미지 삭제
            FbRef.storageRef.child( "${key}.png").delete()

            // 2. 이미지 업로드
            imageUpload(key)

            Toast.makeText(this, "게시글 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()

            finish()

        }
    }

    private fun getBoardImgData(key: String) {
        val boardImgRef = FbRef.storageRef.child("${key}.png")

        boardImgRef.downloadUrl.addOnSuccessListener {
            // 기존 게시글 이미지를 다운로드해서 Glide를 통해 boardImageView에 적용한다.

            if( !this.isFinishing ) {
                Glide
                    .with(this)
                    .load( it )
                    .into(boardImageView)
            }
        }
    }

    private fun getBoardData( key : String ) {
        val getBoardListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    Log.d(TAG, dataSnapshot.toString())
                    // DataSnapshot { key = -Mz-t9-fXqtI2T_sjtzm, value = {uid=FE4EMT7tIthxHrDQhke7TUyCVv02, contents=i am a boy . you are a girl., time=2022.03.25 20:44:45, title=i am a boy } }
                    val board = dataSnapshot.getValue( BoardModel::class.java )

                    Log.d("게시글수정", board!!.title)
                    Log.d("게시글수정", board!!.contents)

                    // EditText의 경우 .text로 값을 넣을 수 없다.
                    // binding.edtBoardTitle.text = board!!.title

                    // 수정 페이지 제목 입력 창
                    binding.edtBoardTitle.setText( board!!.title )
                    // binding.edtContent.text = board!!.contents

                    // 수정 페이지 내용 입력 창
                    binding.edtContent.setText( board!!.contents )

                }catch ( e : Exception) {
                    Log.d(TAG, "삭제완료")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FbRef.boardRef.child(key).addValueEventListener(getBoardListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( resultCode == RESULT_OK && requestCode == 100 ) {
            // 선택한 이미지가 게시글 작성화면에 보이게한다.
            boardImageView.setImageURI( data?.data )
        }
    }

    private fun imageUpload( key : String ) {

        boardImageView.isDrawingCacheEnabled = true
        boardImageView.buildDrawingCache()
        val bitmap = (boardImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // root \ 게시글 key 값.png

        var uploadTask = FbRef.storageRef.child( "${key}.png").putBytes(data)
        uploadTask.addOnFailureListener {
            // 업로드 실패 시
            Log.d("게시글 수정", "이미지 업로드 실패")

        }.addOnSuccessListener { taskSnapshot ->
            // 업로드 성공 시
            Log.d("게시글 수정", "이미지 업로드 성공")
        }
    }

}