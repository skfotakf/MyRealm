package com.koreait.myrealm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    Button btnSave;
    Button btnLoad;
    Button btnDelete;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);

        // 설정 파일 처리
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .name("schooldb")
                .allowQueriesOnUiThread(true) // UI Thread(main) 에서도 realm 에 접근할 수 있도록 만들어줌
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();

        // migration이 필요한 경우 지워버리겠다 (마이그레이션 : 동기화 시켜주겠다)
        // 이름 학번 학교

        // 홍길동1, 1학번, 부산대학교
        // 홍길동2, 2학번, 부산대학교

        Realm.setDefaultConfiguration(configuration);

        // Realm 객체를 얻는 방법
        realm = Realm.getDefaultInstance();

        initData();
        addEventListener();

    }

    private void initData() {
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);
        btnDelete = findViewById(R.id.btnDelete);
    }
    private void addEventListener() {
        btnSave.setOnClickListener(view -> {
            realm.executeTransaction(transactionRealm -> {
                School school = new School("PNU");
                school.setLocation("Busan");
                transactionRealm.insert(school);
            });
        });
        btnLoad.setOnClickListener(view -> {
            realm.executeTransaction(realm1 -> {
                School school = realm1.where(School.class).findFirst();
                if(school != null) {
                    Log.d("TAG", school.toString());
                } else {
                    Log.d("TAG","null입니다");
                }
            });

        });
        btnDelete.setOnClickListener(view -> {

            realm.executeTransaction(realm1 -> {
                realm1.where(School.class).findAll().deleteAllFromRealm();
            });

        });
    }
}