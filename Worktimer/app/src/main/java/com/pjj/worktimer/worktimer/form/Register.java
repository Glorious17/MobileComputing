package com.pjj.worktimer.worktimer.form;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pjj.worktimer.worktimer.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {

    private int standartColorBackground;

    private RelativeLayout rlReg;
    private RelativeLayout rlRegComplete;

    private String textEmail;
    private String textPassword;
    private String textPasswordRepeat;

    private EditText email;
    private EditText password;
    private EditText passwordRepeat;

    private TextView userExists;
    private TextView textRegComplete;

    private ProgressBar progressBar;
    private ProgressBar waitForRegister;

    private Button btnRegister;
    private Button btnRegComplete;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rlReg = (RelativeLayout) findViewById(R.id.rlReg);
        rlRegComplete = (RelativeLayout) findViewById(R.id.rlRegComplete);

        email = (EditText) findViewById(R.id.editRegEmail);
        password = (EditText) findViewById(R.id.editRegPassword);
        passwordRepeat = (EditText) findViewById(R.id.editRegPasswordRepeat);

        email.setOnFocusChangeListener(emailOnFocusChange());

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(onClickRegister());

        btnRegComplete = (Button) findViewById(R.id.btnRegComplete);
        btnRegComplete.setOnClickListener(onClickFinish());

        userExists = (TextView) findViewById(R.id.textRegUserExists);
        textRegComplete = (TextView) findViewById(R.id.textRegComplete);

        progressBar = (ProgressBar) findViewById(R.id.progressReg);
        waitForRegister = (ProgressBar) findViewById(R.id.waitForRegister);

        standartColorBackground = email.getDrawingCacheBackgroundColor();
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/

    private boolean checkForm(){

        textEmail = email.getText().toString();
        textPassword = password.getText().toString();
        textPasswordRepeat = passwordRepeat.getText().toString();

        email.setBackgroundColor(standartColorBackground);
        password.setBackgroundColor(standartColorBackground);
        passwordRepeat.setBackgroundColor(standartColorBackground);

        if(textEmail.length() < 6 || !textEmail.contains("@")){
            Toast.makeText(getApplicationContext(), "Schreibe bitte eine gültige Email-Adresse", Toast.LENGTH_SHORT).show();
            email.setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }

        if(!textPassword.equals(textPasswordRepeat)){
            Toast.makeText(getApplicationContext(), "Die Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
            password.setBackgroundColor(getResources().getColor(R.color.warning));
            passwordRepeat.setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }

        if(textPassword.length() < 5){
            Toast.makeText(getApplicationContext(), "Das Passwort muss mindestens fünf Zeichen enthalten", Toast.LENGTH_SHORT).show();
            password.setBackgroundColor(getResources().getColor(R.color.warning));
            passwordRepeat.setBackgroundColor(getResources().getColor(R.color.warning));
            return false;
        }

        return true;
    }

    private boolean checkEmail(){
        textEmail = email.getText().toString();

        if(textEmail.length() < 6 || !textEmail.contains("@")){
            email.setBackgroundColor(getResources().getColor(R.color.warning));
            Toast.makeText(getApplicationContext(), "Schreibe bitte eine gültige Email-Adresse", Toast.LENGTH_SHORT).show();
            btnRegister.setEnabled(false);
            return false;
        }
        return true;
    }

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    public View.OnClickListener onClickRegister(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm()){
                    rlReg.setVisibility(View.GONE);
                    btnRegister.setVisibility(View.GONE);
                    waitForRegister.setVisibility(View.VISIBLE);
                    BackgroundRegister br = new BackgroundRegister();
                    String data = localURLEncoder("email") + "=" + localURLEncoder(textEmail) + "&" +
                            localURLEncoder("password") + "=" + localURLEncoder(textPassword);
                    String url = "http://zander-bros.de/worktimer/add_user.php";
                    br.execute(url, data, BackgroundRegister.ADD_USER);
                }
            }
        };
    }

    public View.OnClickListener onClickFinish(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    public View.OnFocusChangeListener emailOnFocusChange(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && checkEmail()){

                    userExists.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    p.setMargins(0,0,0,dp(16));
                    p.addRule(RelativeLayout.BELOW, R.id.progressReg);
                    password.setLayoutParams(p);

                    BackgroundRegister br = new BackgroundRegister();
                    String data = localURLEncoder("email") + "=" + localURLEncoder(textEmail);
                    String url = "http://zander-bros.de/worktimer/is_in_user.php";
                    br.execute(url, data, BackgroundRegister.CHECK_USER);
                }
            }
        };
    }

    /*------------------------------------*/
    /*------------GUI-Changes-------------*/
    /*------------------------------------*/

    private void setUserIsAvailable(){
        email.setBackgroundColor(getResources().getColor(R.color.accepted));
        userExists.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(0,0,0,dp(16));
        p.addRule(RelativeLayout.BELOW, R.id.editRegEmail);
        password.setLayoutParams(p);
        btnRegister.setEnabled(true);
    }

    private void setUserIsNotAvailable(){
        email.setBackgroundColor(getResources().getColor(R.color.warning));
        userExists.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(0,0,0,dp(16));
        p.addRule(RelativeLayout.BELOW, R.id.textRegUserExists);
        password.setLayoutParams(p);
        btnRegister.setEnabled(false);
    }

    private void complete(){
        waitForRegister.setVisibility(View.GONE);
        rlRegComplete.setVisibility(View.VISIBLE);
        textRegComplete.setText("Die Registrierung ist fast abgeschlossen. An Ihre Email-Adresse" +
                " wurde eine Verifizierungs-Email gesandt. Folgen Sie den Anweisungen. Vielen Dank!");
    }

    /*------------------------------------*/
    /*-----------Help functions-----------*/
    /*------------------------------------*/

    public int dp(int i) {
        float density = getBaseContext().getResources().getDisplayMetrics().density;
        return (int)((i * density) + 0.5);
    }

    public String localURLEncoder(String text){
        try {
            return(URLEncoder.encode(text, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*------------------------------------*/
    /*-----------Backgroundtasks----------*/
    /*------------------------------------*/

    class BackgroundRegister extends AsyncTask<String,Void,Boolean> {

        public static final String ADD_USER = "0";
        public static final String CHECK_USER = "1";

        private boolean result;
        private int operation;

        @Override
        protected Boolean doInBackground(String... args) {
            String serverUrl = args[0];
            String data = args[1];
            operation = Integer.parseInt(args[2]);

            try{

                URL url = new URL(serverUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                Log.d("TAG", "doInBackground: " + url);

                output(httpURLConnection, data);

                data = input(httpURLConnection);

                Log.d("TAG", "doInBackground: " + data);

                httpURLConnection.disconnect();

                return parseJSON(data);

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            switch (operation){
                case 0:
                    if(bool){
                        complete();
                    }
                    break;
                case 1:
                    if(bool){
                        setUserIsAvailable();
                    }else{
                        setUserIsNotAvailable();

                    }
                    break;
            }
        }

        private void output(HttpURLConnection httpURLConnection, String data) throws IOException {
            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));

            bufferedWriter.write(data);
            bufferedWriter.flush();

            bufferedWriter.close();
            outputStream.close();
        }

        private String input(HttpURLConnection httpURLConnection) throws IOException {
            String JASON_STRING;
            InputStream input = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuilder = new StringBuilder();

            while((JASON_STRING = bufferedReader.readLine())!= null){
                stringBuilder.append(JASON_STRING+"\n");
            }

            input.close();
            return stringBuilder.toString().trim();
        }

        private boolean parseJSON(String data) throws JSONException {
            JSONObject jO = new JSONObject(data);
            return jO.getBoolean("insideUser");
        }
    }

}