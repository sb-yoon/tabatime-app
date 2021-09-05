package Unlike.tabatmie.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import Unlike.tabatmie.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        ButterKnife.bind(this);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webview.loadUrl("http://3.37.223.41/privacy_terms.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            webview.onPause();
        } catch (Exception ignored) {

        }
    }
}