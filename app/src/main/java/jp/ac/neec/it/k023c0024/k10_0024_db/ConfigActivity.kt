package jp.ac.neec.it.k023c0024.k10_0024_db

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfigActivity : AppCompatActivity() {
    private var _CDID = -1
    private var _CDName = ""
    private var _CDPrice = ""
    //データベースヘルパーオブジェクト
    private val _helper = DatabaseHelper(this@ConfigActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val btShop = findViewById<Button>(R.id.btShop)
        btShop.setOnClickListener(ClickBtShopListener())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun onSaveButtonClick(view: View) {
        val etName = findViewById<EditText>(R.id.etName)
        val etPrice = findViewById<EditText>(R.id.etPrice)

        _CDID += 1
        _CDName = etName.text.toString()
        _CDPrice = etPrice.text.toString()

        if (_CDName.isNotEmpty() && _CDPrice.isNotEmpty()){
            val CDPrice: Int? = _CDPrice.toIntOrNull()

            if (CDPrice != null) {
                //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
                val db = _helper.writableDatabase//書き込みdb

                //インサート用SQL文字列の用意
                val sqlInsert = "INSERT INTO cdmemos (_id, name, price) VALUES (?, ?, ?)"
                //SQL文字列を元にプリペアドステートメントを取得
                var stmt = db.compileStatement(sqlInsert)
                //変数のバインド
                stmt.bindLong(1, _CDID.toLong())
                stmt.bindString(2, _CDName)
                stmt.bindLong(3, _CDPrice.toLong())
                //インサートSQLの実行
                stmt.executeInsert()
            }
            else{
                Toast.makeText(this@ConfigActivity, "整数値を入力してください", Toast.LENGTH_SHORT).show()
            }
        }
        else if (_CDName.isNotEmpty() && _CDPrice.isEmpty()){
            //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
            val db = _helper.writableDatabase//書き込みdb

            //まず、リストで選択されたカクテルのメモデータを削除。その後インサートを行う
            //削除用SQL文字列を用意
            val sqlDelete = "DELETE FROM cocktailmemos WHERE name = ?"
            //SQL文字列を元にプリペアドステートメントを取得
            var stmt = db.compileStatement(sqlDelete)
            //変数のバイド
            stmt.bindString(1, _CDName)
            //削除SQLの実行。
            stmt.executeUpdateDelete()
        }

        //入力値を消去
        etName.setText("")
        etPrice.setText("")
    }

    private inner class ClickBtShopListener : View.OnClickListener {
        override fun onClick(v: View?) {
            finish()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        if (item.itemId == android.R.id.home) {
            finish()
        }
        else {
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }
}