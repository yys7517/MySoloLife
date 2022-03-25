package com.youngsun.mysololife.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.ActivityBoardWriteBinding
import com.youngsun.mysololife.utils.FbAuth
import com.youngsun.mysololife.utils.FbRef
import com.youngsun.mysololife.utils.TimeUtil

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding
    private val TAG = BoardWriteActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView( this, R.layout.activity_board_write )

        // 이미지 추가 버튼 눌렀을 때
        binding.btnImageAdd.setOnClickListener {
            // 이미지를 ImageList에 담아놓자.
        }

        // 완료 버튼 눌렀을 때 ( 글 쓰기 완료 )
        binding.btnSubmit.setOnClickListener {

            // 이미지 업로드 ImageList

            // images \ 게시글 key \ Image1
            // images \ 게시글 key \ Image2
            // images \ 게시글 key \ Image3
            // images \ 게시글 key \ Image4
            // images \ 게시글 key \ Image5

            val title = binding.edtBoardTitle.text.toString()
            val contents = binding.edtContent.text.toString()

            // 게시글 제목, 내용, 사진 업로드. BoardModel
            // board \ key \ BoardModel( title, content, uid, time )

            FbRef.boardRef.
                    push().
                    setValue( BoardModel( title, contents, FbAuth.getUid() , TimeUtil.getTime() ) )

            Toast.makeText(this, "게시글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            finish()

        }
    }
}