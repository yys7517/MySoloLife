package com.youngsun.mysololife.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.youngsun.mysololife.R
import com.youngsun.mysololife.utils.FbAuth
import com.youngsun.mysololife.utils.FbRef

// 게시글을 가져오는 Activity
class ContentsListActivity : AppCompatActivity() {

    private val items = ArrayList<ContentModel>()
    private val itemKeyList = ArrayList<String>()       // 게시글 Key 리스트

    private val bookmarkIdList = mutableListOf<String>()

    private lateinit var rvAdapter : ContentsRVAdapter
    // Firebase database
    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents_list)

        val rv : RecyclerView = findViewById(R.id.recyclerView)

        // addItems()

        rvAdapter = ContentsRVAdapter(baseContext, items, itemKeyList, bookmarkIdList )
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager( this, 2)

        // 코드를 사용하여 item 추가를 하지 않고 Server 에서 받아오는 식으로 item들을 추가해보자.
        // initContents()   - 초기 테스트 용 컨텐츠 데이터를 삽입.

        // 꿀팁 화면에서 선택한 카테고리 별로 레퍼런스를 다르게 지정해주어 카테고리 별 컨텐츠를 가져온다.
        val category = intent.getStringExtra("category").toString()
        var getContentsRef = database.getReference( category )
        //       root \ contents \ contents1,
        //       root \ contents \ contents2,
        //       root \ contents \ contents3 ..............

        val getContentListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 1. 게시글 목록을 초기화한다.
                items.clear()
                itemKeyList.clear()

                // 2. 게시글 목록을 가져온다.
                for( dataModel in dataSnapshot.children ) {
                    Log.d("ContentsKey", dataModel.key.toString())  // 게시글의 Key 값을 Log로 확인.
                    val content = dataModel.getValue(ContentModel::class.java)
                    val contentKey = dataModel.key.toString()

                    itemKeyList.add( contentKey!! )
                    items.add( content!! )
                }

                // 3. 게시글 목록 리스트의 데이터가 바뀐 값으로 adapter 를 최신화한다.
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("GetContentList", "loadPost:onCancelled", databaseError.toException())
            }
        }
        getContentsRef.addValueEventListener(getContentListener)

        /*
        rvAdapter.itemClick = object : ContentsRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                // Toast.makeText( baseContext, items[position].title, Toast.LENGTH_SHORT).show()
                val intent = Intent( baseContext, ContentShowActivity::class.java )
                intent.putExtra("webUrl", items[position].webUrl )
                startActivity(intent)
            }
        }
         */

        getBookmarkData()
    }

    // 초기 테스트 용 컨텐츠 데이터를 리스트에 수동으로 삽입.
    private fun addItems() {
        items.add(
            ContentModel
                ("밥솥 리코타치즈 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblYPPY%2Fbtq66v0S4wu%2FRmuhpkXUO4FOcrlOmVG4G1%2Fimg.png"
            ,"https://philosopher-chan.tistory.com/1235?category=941578"
            )
        )

        items.add(
            ContentModel
                ("황금노른자장 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FznKK4%2Fbtq665AUWem%2FRUawPn5Wwb4cQ8BetEwN40%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1236?category=941578"
            )
        )

        items.add(
            ContentModel
                ("사골곰탕 파스타 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1237?category=941578"
            )
        )

        items.add(
            ContentModel
                ("아웃백 투움바 파스타 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcOYyBM%2Fbtq67Or43WW%2F17lZ3tKajnNwGPSCLtfnE1%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1238?category=941578"
            )
        )

        items.add(
            ContentModel
                ("최애 당면 찜닭 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fekn5wI%2Fbtq66UlN4bC%2F8NEzlyot7HT4PcjbdYAINk%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1239?category=941578"
            )
        )


        items.add(
            ContentModel
                ("스팸 부대 국수 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F123LP%2Fbtq65qy4hAd%2F6dgpC13wgrdsnHigepoVT1%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1240?category=941578"
            )
        )

        items.add(
            ContentModel
                ("불닭 팽이버섯 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fl2KC3%2Fbtq64lkUJIN%2FeSwUPyQOddzcj6OAkPKZuk%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1241?category=941578"
            )
        )
        items.add(
            ContentModel
                ("꿀호떡 샌드위치 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FmBh5u%2Fbtq651yYxop%2FX3idRXeJ0VQEoT1d6Hln30%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1242?category=941578"
            )
        )
        items.add(
            ContentModel
                ("굽네치킨 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlOnja%2Fbtq69Tmp7X4%2FoUvdIEteFbq4Z0ZtgCd4p0%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1243?category=941578"
            )
        )
        items.add(
            ContentModel
                ("야매 JMT 홈베이킹 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FNNrYR%2Fbtq64wsW5VN%2FqIaAsfmFtcvh4Bketug9m0%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1244?category=941578"
            )
        )
        items.add(
            ContentModel
                ("자취요리 양념장레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FK917N%2Fbtq64SP5gxj%2FNzsfNAykamW7qv1hdusp1K%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1245?category=941578"
            )
        )
        items.add(
            ContentModel
                ("디톡스주스 레시피모음",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FeEO4sy%2Fbtq69SgK8L3%2FttCUxYHx9aPNebNwkPcI21%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1246?category=941578"
            )
        )
        items.add(
            ContentModel
                ("사랑듬뿍담긴 봄소풍도시락",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbdIKDG%2Fbtq64M96JFa%2FKcJiYgKuwKuP3fIyviXm90%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1247?category=941578"
            )
        )
        items.add(
            ContentModel
                ("참치맛나니 초간단레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFtY3t%2Fbtq65q6P4Zr%2FWe64GM8KzHAlGE3xQ2nDjk%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1248?category=941578"
            )
        )
        items.add(
            ContentModel
                ("간장볶음면 마성의레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOtaMq%2Fbtq67OMpk4W%2FH1cd0mda3n2wNWgVL9Dqy0%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1249?category=941578"
            )
        )
    }

    // 초기 테스트 용 컨텐츠 데이터를 삽입.
    private fun initContents() {
        val contentsInsRef = database.getReference("contents")

        contentsInsRef.push().setValue(
            ContentModel
                ("밥솥 리코타치즈 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblYPPY%2Fbtq66v0S4wu%2FRmuhpkXUO4FOcrlOmVG4G1%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1235?category=941578"
            )
        )

        contentsInsRef.push().setValue(
            ContentModel
                ("황금노른자장 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FznKK4%2Fbtq665AUWem%2FRUawPn5Wwb4cQ8BetEwN40%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1236?category=941578"
            )
        )

        contentsInsRef.push().setValue(
            ContentModel
                ("사골곰탕 파스타 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1237?category=941578"
            )
        )

        contentsInsRef.push().setValue(
            ContentModel
                ("아웃백 투움바 파스타 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcOYyBM%2Fbtq67Or43WW%2F17lZ3tKajnNwGPSCLtfnE1%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1238?category=941578"
            )
        )

        contentsInsRef.push().setValue(
            ContentModel
                ("최애 당면 찜닭 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fekn5wI%2Fbtq66UlN4bC%2F8NEzlyot7HT4PcjbdYAINk%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1239?category=941578"
            )
        )


        contentsInsRef.push().setValue(
            ContentModel
                ("스팸 부대 국수 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F123LP%2Fbtq65qy4hAd%2F6dgpC13wgrdsnHigepoVT1%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1240?category=941578"
            )
        )

        contentsInsRef.push().setValue(
            ContentModel
                ("불닭 팽이버섯 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fl2KC3%2Fbtq64lkUJIN%2FeSwUPyQOddzcj6OAkPKZuk%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1241?category=941578"
            )
        )
        contentsInsRef.push().setValue(
            ContentModel
                ("꿀호떡 샌드위치 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FmBh5u%2Fbtq651yYxop%2FX3idRXeJ0VQEoT1d6Hln30%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1242?category=941578"
            )
        )
        contentsInsRef.push().setValue(
            ContentModel
                ("굽네치킨 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlOnja%2Fbtq69Tmp7X4%2FoUvdIEteFbq4Z0ZtgCd4p0%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1243?category=941578"
            )
        )
        contentsInsRef.push().setValue(
            ContentModel
                ("야매 JMT 홈베이킹 황금레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FNNrYR%2Fbtq64wsW5VN%2FqIaAsfmFtcvh4Bketug9m0%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1244?category=941578"
            )
        )
        contentsInsRef.push().setValue(
            ContentModel
                ("자취요리 양념장레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FK917N%2Fbtq64SP5gxj%2FNzsfNAykamW7qv1hdusp1K%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1245?category=941578"
            )
        )
        contentsInsRef.push().setValue(
            ContentModel
                ("디톡스주스 레시피모음",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FeEO4sy%2Fbtq69SgK8L3%2FttCUxYHx9aPNebNwkPcI21%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1246?category=941578"
            )
        )
        contentsInsRef.push().setValue(
            ContentModel
                ("사랑듬뿍담긴 봄소풍도시락",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbdIKDG%2Fbtq64M96JFa%2FKcJiYgKuwKuP3fIyviXm90%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1247?category=941578"
            )
        )
        contentsInsRef.push().setValue(
            ContentModel
                ("참치맛나니 초간단레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFtY3t%2Fbtq65q6P4Zr%2FWe64GM8KzHAlGE3xQ2nDjk%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1248?category=941578"
            )
        )
        contentsInsRef.push().setValue(
            ContentModel
                ("간장볶음면 마성의레시피",
                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOtaMq%2Fbtq67OMpk4W%2FH1cd0mda3n2wNWgVL9Dqy0%2Fimg.png"
                ,"https://philosopher-chan.tistory.com/1249?category=941578"
            )
        )

    }

    // 사용자 북마크 정보 가져오기
    private fun getBookmarkData() {
        val getBookmarkRef = FbRef.bookmarkRef.child( FbAuth.getUid() )

        val getBookmarkListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                bookmarkIdList.clear()

                for( dataModel in dataSnapshot.children ) {
                    Log.d("getBookmarkData", dataModel.key.toString() ) // 컨텐츠 key 값.
                    Log.d("getBookmarkData", dataModel.toString() )

                    bookmarkIdList.add( dataModel.key.toString() )
                }

                Log.d("Bookmark : ", bookmarkIdList.toString())
                rvAdapter.notifyDataSetChanged()    // 사용자의 북마크 정보를 어댑터에 넘겨준다. 북마크 버튼 최신화

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("GetContentList", "북마크 정보 가져오기 실패.", databaseError.toException())
            }
        }
        getBookmarkRef.addValueEventListener(getBookmarkListener)
    }


}