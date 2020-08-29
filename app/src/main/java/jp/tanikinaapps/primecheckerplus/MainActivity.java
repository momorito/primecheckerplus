package jp.tanikinaapps.primecheckerplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv,textAns,nearPrime;
    EditText ed;
    Button checkButton,isPrimeToday;
    private AsyncCheck asyncCheck;
    private String isPrimeResult,checkNumber,todayNum;
    private int searchNum;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);


        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        tv = (TextView)findViewById(R.id.textTitle);
        ed = (EditText)findViewById(R.id.editText);

        checkButton = (Button)findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);

        //今日の素数ボタンの実装
        isPrimeToday = (Button)findViewById(R.id.isPrimeToday);
        final DateFormat df = new SimpleDateFormat("yyyyMMdd");
        final Date date = new Date(System.currentTimeMillis());
        todayNum = df.format(date);
        isPrimeToday.setText("今日(" + todayNum + ")の素数チェック");
        isPrimeToday.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        switch(itemId){
            case R.id.menuPrivacy:
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.privacy_policy_URL)));
                startActivity(i);
                break;
            case R.id.menuFininsh:
                finish();
                break;
        }
        return false;
    }

    private AsyncCheck.Listener createListener(){
        return new AsyncCheck.Listener() {
            @Override
            public void onSuccess(String[] result) {
                FragmentManager resultFragmentManager = getSupportFragmentManager();
                DialogFragment resultDialogFragment = new SetDialog.ResultDialog(getSearchNum(),result);
                resultDialogFragment.show(resultFragmentManager,"result");
            }
        };

    }


    @Override
    public void onClick(View v) {
        keyboardClose(v);
        String searchNumString = ed.getText().toString();
        switch(v.getId()){
            case R.id.checkButton:
                if(searchNumString.length() == 0){
                    Toast.makeText(getApplicationContext(),"値を入力してください",Toast.LENGTH_SHORT).show();
                } else {
                    setSearchNum(Integer.valueOf(searchNumString));
                    asyncCheck = new AsyncCheck();
                    asyncCheck.setListener(createListener());
                    asyncCheck.execute(getSearchNum());
                }
                break;
            case R.id.isPrimeToday:
                ed.setText(todayNum);
                setSearchNum(Integer.valueOf(todayNum));
                asyncCheck = new AsyncCheck();
                asyncCheck.setListener(createListener());
                asyncCheck.execute(getSearchNum());
                break;
        }

    }

    public void setSearchNum(int searchNum) {
        this.searchNum = searchNum;
    }

    public int getSearchNum(){
        return  searchNum;
    }

    private void keyboardClose(View v){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}


