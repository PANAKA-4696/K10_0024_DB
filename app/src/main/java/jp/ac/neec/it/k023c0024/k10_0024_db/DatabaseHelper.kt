package jp.ac.neec.it.k023c0024.k10_0024_db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //クラス内のprivate定数を宣言するためにcompanion objectブロックとする
    companion object{
        //データベースファイル名の定数。
        private const val DATABASE_NAME = "cdmemos.db"
        //バージョン情報の定数
        private const val DATABASE_VERSION = 1
    }

    //onCreateとonUpgradeメソッドをオーバーライドしなければならないのでDatabaseHelperが赤波線の状態でalt+Enterで実装

    override fun onCreate(db: SQLiteDatabase) {
        //テーブル作成用SQL文字列の作成
        val sb = StringBuilder()
        sb.append("CREATE TABLE cdmemos (")
        sb.append("_id INTEGER PRIMARY KEY,")
        sb.append("name TEXT,")
        sb.append("price INT")
        sb.append(");")
        val sql = sb.toString()

        //SQLの実行
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}