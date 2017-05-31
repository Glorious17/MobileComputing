package com.pjj.worktimer.worktimer.form;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pjj.worktimer.worktimer.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {

    private int standartColorBackground;

    private String textEmail;
    private String textPassword;
    private String textPasswordRepeat;

    private EditText email;
    private EditText password;
    private EditText passwordRepeat;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText) findViewById(R.id.editRegEmail);
        password = (EditText) findViewById(R.id.editRegPassword);
        passwordRepeat = (EditText) findViewById(R.id.editRegPasswordRepeat);

        ((Button) findViewById(R.id.btnRegister)).setOnClickListener(onClickRegister());

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

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    public View.OnClickListener onClickRegister(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundRegister br = new BackgroundRegister();
                if(checkForm()){
                    br.execute(textEmail, textPassword);
                }
            }
        };
    }
}

class BackgroundRegister extends AsyncTask<String,Void,Boolean> {

    String registerUrl;

    @Override
    protected void onPreExecute() {
        registerUrl = "http://zander-bros.de/worktimer/add_user.php";
    }

    @Override
    protected Boolean doInBackground(String... args) {
        String email, password;

        email = args[0];
        password = args[1];

        try{

            URL url = new URL(registerUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));

            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "utf-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "utf-8");

            Log.d("DOINBACKGROUND", "doInBackground: " + data);

            bufferedWriter.write(data);
            bufferedWriter.flush();

            bufferedWriter.close();
            outputStream.close();
            InputStream input = httpURLConnection.getInputStream();
            input.close();
            httpURLConnection.disconnect();

            return true;

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
