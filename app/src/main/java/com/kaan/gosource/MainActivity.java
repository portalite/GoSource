
package com.kaan.gosource;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;
import android.view.View;

public class MainActivity extends Activity {

    int bg = Color.rgb(12, 17, 30);
    int panel = Color.rgb(25, 33, 50);
    int blue = Color.rgb(80, 130, 255);
    EditText search;

    int dp(int value) {
        return (int) (value * getResources()
            .getDisplayMetrics().density);
    }

    GradientDrawable shape(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(bg);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(35), dp(22), dp(25));

        scroll.addView(root);

        TextView logo = text("GoSource", 32, Color.WHITE);
        logo.setTypeface(null, Typeface.BOLD);
        root.addView(logo);

        TextView subtitle = text(
            "BİLGİNİN KAYNAĞINA ULAŞ",
            11, Color.LTGRAY
        );
        subtitle.setLetterSpacing(0.15f);

        LinearLayout.LayoutParams subParams =
            new LinearLayout.LayoutParams(-2, -2);
        subParams.topMargin = dp(5);
        root.addView(subtitle, subParams);

        TextView heading = text(
            "Bugün neyi araştırmak istiyorsun?",
            23, Color.WHITE
        );
        heading.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams headParams =
            new LinearLayout.LayoutParams(-1, -2);
        headParams.topMargin = dp(55);
        root.addView(heading, headParams);

        TextView info = text(
            "Konunu yaz. GoSource senin için güvenilir kaynaklar bulsun.",
            14, Color.LTGRAY
        );

        LinearLayout.LayoutParams infoParams =
            new LinearLayout.LayoutParams(-1, -2);
        infoParams.topMargin = dp(10);
        root.addView(info, infoParams);

        search = new EditText(this);
        search.setSingleLine(false);
        search.setMinLines(2);
        search.setTextSize(16);
        search.setTextColor(Color.WHITE);
        search.setHintTextColor(Color.GRAY);
        search.setHint("Örn: Kara delikler nasıl oluşur?");
        search.setPadding(dp(16), dp(15), dp(16), dp(15));
        search.setBackground(shape(panel, 16));

        LinearLayout.LayoutParams searchParams =
            new LinearLayout.LayoutParams(-1, -2);
        searchParams.topMargin = dp(25);
        root.addView(search, searchParams);

        Button research = new Button(this);
        research.setText("ARAŞTIRMAYI BAŞLAT");
        research.setTextColor(Color.WHITE);
        research.setTextSize(14);
        research.setTypeface(null, Typeface.BOLD);
        research.setAllCaps(false);
        research.setBackground(shape(blue, 14));

        LinearLayout.LayoutParams buttonParams =
            new LinearLayout.LayoutParams(-1, dp(54));
        buttonParams.topMargin = dp(15);
        root.addView(research, buttonParams);

        research.setOnClickListener(v -> {
            String topic = search.getText().toString().trim();

            if (topic.isEmpty()) {
                Toast.makeText(this,
                    "Önce bir konu yaz.",
                    Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                    "Araştırma motoru hazırlanıyor.",
                    Toast.LENGTH_SHORT).show();
            }
        });

        TextView recent = text(
            "ARAŞTIRMA MERKEZİ",
            13, Color.LTGRAY
        );
        recent.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams recentParams =
            new LinearLayout.LayoutParams(-1, -2);
        recentParams.topMargin = dp(45);
        root.addView(recent, recentParams);

        TextView empty = text(
            "Henüz araştırma yok.\nİlk araştırmanı başlattığında burada görünecek.",
            14, Color.GRAY
        );
        empty.setGravity(Gravity.CENTER);
        empty.setPadding(dp(15), dp(30), dp(15), dp(30));
        empty.setBackground(shape(panel, 16));

        LinearLayout.LayoutParams emptyParams =
            new LinearLayout.LayoutParams(-1, -2);
        emptyParams.topMargin = dp(15);
        root.addView(empty, emptyParams);

        setContentView(scroll);
    }
}
