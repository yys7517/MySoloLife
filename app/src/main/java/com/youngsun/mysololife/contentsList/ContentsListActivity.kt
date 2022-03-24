package com.youngsun.mysololife.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youngsun.mysololife.R

class ContentsListActivity : AppCompatActivity() {
    private val items = ArrayList<ContentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents_list)

        val rv : RecyclerView = findViewById(R.id.recyclerView)

        // addItems()
        // 코드를 사용하여 item 추가를 하지 않고 Server 에서 받아오는 식으로 item들을 추가해보자.


        val rvAdapter = ContentsRVAdapter(baseContext, items)
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager( this, 2)

        rvAdapter.itemClick = object : ContentsRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                // Toast.makeText( baseContext, items[position].title, Toast.LENGTH_SHORT).show()
                val intent = Intent( baseContext, ContentShowActivity::class.java )
                intent.putExtra("webUrl", items[position].webUrl )
                startActivity(intent)
            }
        }
    }

    fun addItems() {
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

}