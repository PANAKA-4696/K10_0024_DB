package jp.ac.neec.it.k023c0024.k10_0024_db

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    //データベースヘルパーオブジェクト
    private val _helper = DatabaseHelper(this@MainActivity)

    private var _CDList: MutableList<MutableMap<String, Any>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _CDList = createCDList()
        val lvItem = findViewById<ListView>(R.id.lvItem)

        var adapter = SimpleAdapter(
            this@MainActivity,
            _CDList,
            android.R.layout.simple_list_item_2,
            arrayOf("name", "price"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        lvItem.adapter = adapter

        val btConfig = findViewById<Button>(R.id.btConfig)
        btConfig.setOnClickListener(ClickBtConfigListener())
    }

    private fun createCDList() : MutableList<MutableMap<String, Any>> {
        //既存のリストをクリアする
        _CDList.clear()

        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        val db = _helper.writableDatabase
        //主キーによる検索SQL文字列の用意
        val sql = "SELECT * FROM cdmemos"
        //SQLの実行
        val cursor = db.rawQuery(sql, null)
        //データベースから取得した値を格納する変数の用意。データが無かった時のための初期値も用意
        var name = ""
        var price = ""

        //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得
        while(cursor.moveToNext()){
            //カラムのインデックス値を取得
            val idxname = cursor.getColumnIndex("name")
            val idxprice = cursor.getColumnIndex("price")
            //カラムのインデックス値を元に実際のデータを取得
            name = cursor.getString(idxname)
            price = cursor.getString(idxprice)
            //取得したデータをリストに追加
            _CDList.add(mutableMapOf("name" to name, "price" to price))
        }

        //カーソルを閉じる
        cursor.close()

        return _CDList

    }

    private inner class ClickBtConfigListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(this@MainActivity, ConfigActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        //最新のデータベース内容を取得
        _CDList = createCDList()

        //リストビューに新しいリストをセットし直す
        val lvItem = findViewById<ListView>(R.id.lvItem)
        var adapter = SimpleAdapter(
            this@MainActivity,
            _CDList,
            android.R.layout.simple_list_item_2,
            arrayOf("name", "price"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        lvItem.adapter = adapter
    }
}