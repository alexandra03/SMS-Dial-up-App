package com.example.dial_up;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Website extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        String sms = null;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                sms = extras.getString("WEBSITE_HTML");
            }
        } else {
            sms = (String) savedInstanceState.getSerializable("WEBSITE_HTML");
        }

        this.loadHTML(sms);
    }

    public void loadHTML(String sms) {
        String[][] shortenedHTML = {
                {"value=\"", "_"},
                {"type=\"", "Δ"},
                {"name=\"", "æ"},
                {"=\"hidden\" ", "ß"},
                {"=\"submit\" ", "Γ"},
                {">", "Π"},
                {"\">", "Σ"},
                {"</form>", "Θ"},
                {"<form>", "Ψ"},
                {"</a>", "Ω"},
                {"<a href=\"", "Φ"},
                {"<input", "Ξ"},
        };
        String[][] shortenedExpressions = {
                {" the ", " t "},
                {" and ", " & "},
                {" that ", " h "},
                {" with ", " w "},
        };

        String[] smsInfo = sms.split(" ", 2);
        String HTML = smsInfo[1];

        for (String[] pair : shortenedHTML) {
            HTML = HTML.replaceAll(pair[1], pair[0]);
        }
        for (String[] pair : shortenedExpressions) {
            HTML = HTML.replaceAll(pair[1], pair[0]);
        }
        HTML = HTML.replaceAll("><", "> <");
        HTML = HTML.replaceAll("<form", "<br><form");
        HTML = HTML.replaceAll("<input", "<br><input");

        // Load up the website!
        WebView webView = findViewById(R.id.website);
        webView.loadData(HTML, "text/html", "UTF-8");
    }
}
