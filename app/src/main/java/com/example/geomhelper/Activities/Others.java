package com.example.geomhelper.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.geomhelper.R;

public class Others extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);

        textView = findViewById(R.id.text_others);
        int i = getIntent().getIntExtra("num_others", -1);
        switch (i) {
            case -1:
                textView.setText("Произошла ошибка.");
                break;
            case 0:
                textView.setText("\n\nДобавлены анимации в курсах и тестах, " +
                        "а именно :\nПри пролистывании кнопочка Добавить исчезает под экран\n" +
                        "Все  элементы появляются плавно сверху,\n" +
                        "В курсах и текстах в курсах круглые кнопки\n" +
                        "Добавить, Вперед и Назад плавно увеличиваются в размере \n\n\n" +
                        "В настройках добавлены пункты\n" +
                        "'Что нового' ,\n" +
                        "'Об авторах' , \n" +
                        "'О приложении'\n");
                break;
            case 1:
                textView.setText("Здесь что-то будет.");
                break;
            case 2:
                textView.setText("Олним словом - лучшее.");
                break;
        }
    }
}
