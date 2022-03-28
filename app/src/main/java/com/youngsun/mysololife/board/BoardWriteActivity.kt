package com.youngsun.mysololife.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.ActivityBoardWriteBinding
import com.youngsun.mysololife.utils.FbAuth
import com.youngsun.mysololife.utils.FbRef
import com.youngsun.mysololife.utils.TimeUtil
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding
    private val TAG = BoardWriteActivity::class.java.simpleName

    private lateinit var boardImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView( this, R.layout.activity_board_write )

        boardImageView = binding.btnImageAdd

        // 이미지 추가 버튼 눌렀을 때
        boardImageView.setOnClickListener {
            // 이미지가 사진 추가 버튼 이미지일 때는 gallery를 실행하고
            val gallery = Intent( Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI )
            startActivityForResult( gallery, 100 )

            // 이미지가 추가되어서 추가 버튼 이미지가 아니게 되었을 때는 아무 실행도 하지 않게 하자.
        }

        // 완료 버튼 눌렀을 때 ( 글 쓰기 완료 )
        binding.btnSubmit.setOnClickListener {

            val title = binding.edtBoardTitle.text.toString()
            val contents = binding.edtContent.text.toString()

            // 게시글 제목, 내용, 사진 업로드. BoardModel
            // board \ key \ BoardModel( title, content, uid, time )

            // 게시글 이미지를 파이어베이스 storage에 저장하고 싶다.
            // 어떻게 ?

            // ***
            // 게시글 이미지를 업로드 할 때 이미지의 이름에 게시글의 key 값을 쓴다면.
            // 만약 내가 게시글을 클릭했을 때, 게시글의 key 값을 통해 이미지를 받아올 수 있다.
            // ***

            // 게시글의 key 값을 먼저 받아올 수 있다.
            val key = FbRef.boardRef.push().key.toString()

//            FbRef.boardRef.
//                    push().
//                    setValue( BoardModel( title, contents, FbAuth.getUid() , TimeUtil.getTime() ) )

            // 게시글 Firebase 에 업로드
            FbRef.boardRef.
                child(key).
                setValue( BoardModel( title, contents, FbAuth.getUid() , TimeUtil.getTime() ) )

            Toast.makeText(this, "게시글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()

            // 이미지 Firebase Storage에 업로드
            imageUpload( key )

            // images \ 게시글 key \ Image1
            // images \ 게시글 key \ Image2
            // images \ 게시글 key \ Image3
            // images \ 게시글 key \ Image4
            // images \ 게시글 key \ Image5

            finish()
        }
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

        }.addOnSuccessListener { taskSnapshot ->
            // 업로드 성공 시
        }
    }
}