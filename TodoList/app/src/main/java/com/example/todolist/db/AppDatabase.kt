package com.example.todolist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoEntity::class], version = 1)  // Room 데이터베이스임을 선언(테이블 목록과 버전 명시)
abstract class AppDatabase : RoomDatabase() {   // 추상클래스로 만들고 RoomDatabase 상속

    abstract fun getTodoDao(): TodoDao  // Room이 Dao 구현체를 자동 생성( 이 메서드를 통해 Dao 접근 )

    companion object {   // DB는 앱 전체에서 하나만 생성되어야 하므로 싱글톤 패턴으로 관리
        val databaseName = "db_Todo"    // DB 이름
        var appDatabase: AppDatabase? = null    // DB객체 저장용

        fun getInstance(context: Context): AppDatabase? {   // DB 객체 불러오기
            if (appDatabase == null) {  // DB가 없으면 생성
                appDatabase = Room.databaseBuilder(
                    context,    // DB 파일 생성 위치
                    AppDatabase::class.java,    // DB 클래스
                    databaseName    // DB 이름
                )
                    .fallbackToDestructiveMigration()   // DB버전이 바뀌었는데 마이그레이션을 작성하지 않으면 기존 DB 삭제 후 새로 생성
                    .build()
            }
            return appDatabase
        }
    }
}