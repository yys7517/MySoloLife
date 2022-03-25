package com.youngsun.mysololife.board


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.ActivityBoardShowBinding
import com.youngsun.mysololife.utils.FbRef

class BoardShowActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardShowBinding

    private val TAG = BoardShowActivity::class.java.simpleName


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

        // 두 번째 방법 - 게시글 key 를 통해 Firebase 로 받아오는 방법.
        val key = intent.getStringExtra("key").toString()
        getBoardData( key )

        // 이미지가 있다면 ? -> 이미지 받아오기

    }

    private fun getBoardData( key : String ) {
        val getBoardListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                // DataSnapshot { key = -Mz-t9-fXqtI2T_sjtzm, value = {uid=FE4EMT7tIthxHrDQhke7TUyCVv02, contents=i am a boy . you are a girl., time=2022.03.25 20:44:45, title=i am a boy } }
                val board = dataSnapshot.getValue( BoardModel::class.java )

                binding.boardTitle.text = board!!.title
                binding.boardContents.text = board!!.contents
                binding.boardWriteTime.text = board!!.time
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FbRef.boardRef.child(key).addValueEventListener(getBoardListener)
    }
}