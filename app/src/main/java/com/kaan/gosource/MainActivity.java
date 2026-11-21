
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
    LinearLayout root;
    TextView results;
    Button research;

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

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(35), dp(22), dp(25));
        root.setBackgroundColor(bg);

        scroll.addView(root);

        TextView logo = text("GoSource", 32, Color.WHITE);
        logo.setTypeface(null, Typeface.BOLD);
        root.addView(logo);

        TextView subtitle = text(
            "BİLGİNİN KAYNAĞINA ULAŞ",
            11, Color.LTGRAY
        );
        root.addView(subtitle);

        TextView heading = text(
            "Bugün neyi araştırmak istiyorsun?",
            23, Color.WHITE
        );
        heading.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams hp =
            new LinearLayout.LayoutParams(-1, -2);
        hp.topMargin = dp(55);
        root.addView(heading, hp);

        TextView info = text(
            "Konunu yaz. GoSource kaynakları senin için incelesin.",
            14, Color.LTGRAY
        );

        LinearLayout.LayoutParams ip =
            new LinearLayout.LayoutParams(-1, -2);
        ip.topMargin = dp(10);
        root.addView(info, ip);

        search = new EditText(this);
        search.setSingleLine(false);
        search.setMinLines(2);
        search.setTextSize(16);
        search.setTextColor(Color.WHITE);
        search.setHintTextColor(Color.GRAY);
        search.setHint("Örn: Kara delikler nasıl oluşur?");
        search.setPadding(dp(16), dp(15), dp(16), dp(15));
        search.setBackground(shape(panel, 16));

        LinearLayout.LayoutParams sp =
            new LinearLayout.LayoutParams(-1, -2);
        sp.topMargin = dp(25);
        root.addView(search, sp);

        research = new Button(this);
        research.setText("ARAŞTIRMAYI BAŞLAT");
        research.setTextColor(Color.WHITE);
        research.setTextSize(14);
        research.setTypeface(null, Typeface.BOLD);
        research.setAllCaps(false);
        research.setBackground(shape(blue, 14));

        LinearLayout.LayoutParams bp =
            new LinearLayout.LayoutParams(-1, dp(54));
        bp.topMargin = dp(15);
        root.addView(research, bp);

        TextView section = text(
            "ARAŞTIRMA SONUÇLARI",
            13, Color.LTGRAY
        );
        section.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams rp =
            new LinearLayout.LayoutParams(-1, -2);
        rp.topMargin = dp(35);
        root.addView(section, rp);

        results = text(
            "Henüz araştırma yapılmadı.",
            14, Color.WHITE
        );
        results.setPadding(dp(16), dp(18), dp(16), dp(18));
        results.setBackground(shape(panel, 16));

        LinearLayout.LayoutParams resultParams =
            new LinearLayout.LayoutParams(-1, -2);
        resultParams.topMargin = dp(15);
        root.addView(results, resultParams);

        research.setOnClickListener(v -> startResearch());

        setContentView(scroll);
    }

    void startResearch() {
        String topic = search.getText().toString().trim();

        if (topic.isEmpty()) {
            Toast.makeText(this,
                "Önce bir konu yaz.",
                Toast.LENGTH_SHORT).show();
            return;
        }

        research.setEnabled(false);
        research.setText("KAYNAKLAR ARANIYOR...");
        results.setText("GoSource kaynakları arıyor ve okuyor...");

        ResearchEngine.search(topic,
            new ResearchEngine.Listener() {

                @Override
                public void onResult(String result) {
                    results.setText(result);
                    research.setEnabled(true);
                    research.setText("YENİDEN ARAŞTIR");
                }

                @Override
                public void onError(String error) {
                    results.setText(error);
                    research.setEnabled(true);
                    research.setText("TEKRAR DENE");
                }
            }
        );
    }
}
