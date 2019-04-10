package com.cxz.iocsample;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cxz.icolibrary.annotation.ContentView;
import com.cxz.icolibrary.annotation.InjectView;
import com.cxz.icolibrary.annotation.OnClick;
import com.cxz.icolibrary.annotation.OnLongClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @InjectView(R.id.btn)
    public Button btn;

    @InjectView(R.id.tv)
    private TextView tv;

    @Override
    public void initView() {
        tv.setText("textView");
    }

    @OnClick({R.id.btn, R.id.tv})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                tv.setText("button doClick(view)");
                Toast.makeText(this, "button doClick(view)", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv:
                tv.setText("textView doClick(view)");
                Toast.makeText(this, "textView doClick(view)", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnLongClick(R.id.btn2)
    public boolean doLongClick() {
        tv.setText("button doLongClick()");
        Toast.makeText(this, "button doLongClick()", Toast.LENGTH_SHORT).show();
        return false;
    }

}
