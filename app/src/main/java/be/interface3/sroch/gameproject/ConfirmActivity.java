package be.interface3.sroch.gameproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmActivity extends AppCompatActivity {

    TextView tv_confirm_question;
    TextView tv_confirm_name;
    TextView tv_confirm_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        tv_confirm_question = (TextView) findViewById(R.id.question);
        tv_confirm_name = (TextView) findViewById(R.id.name);
        tv_confirm_description = (TextView) findViewById(R.id.description);

        tv_confirm_question.setText(getIntent().getExtras().getString(Utils.EXTRA_QUESTION));
        tv_confirm_name.setText(getIntent().getExtras().getString(Utils.EXTRA_NAME));
        tv_confirm_description.setText(getIntent().getExtras().getString(Utils.EXTRA_DESCR));
    }

    public void doOnClick (View v) {
        switch (v.getId()) {
            case R.id.validate:
                Intent intent = new Intent();
                intent.putExtra(Utils.EXTRA_ID, getIntent().getExtras().getLong(Utils.EXTRA_ID));
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
