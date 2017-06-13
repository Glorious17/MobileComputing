package com.pjj.worktimer.worktimer.form;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pjj.worktimer.worktimer.R;
import com.pjj.worktimer.worktimer.helpClasses.HelpFunctions;
import com.pjj.worktimer.worktimer.helpClasses.URLs;
import com.pjj.worktimer.worktimer.projectScreen.Project;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private RelativeLayout errorLayout;

    private EditText emailAddress;
    private EditText password;

    private TextView register;
    private TextView errorMsg;

    private ImageView closeImg;

    private Button btnLogin;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errorLayout = (RelativeLayout) findViewById(R.id.logErrorLayout);
        errorLayout.setVisibility(View.GONE);

        register = (TextView) findViewById(R.id.textLogRegister);
        register.setOnClickListener(onClickRegister());

        errorMsg = (TextView) findViewById(R.id.logErrorMsg);

        emailAddress = (EditText) findViewById(R.id.editLogEmail);
        password = (EditText) findViewById(R.id.editLogPassword);

        closeImg = (ImageView) findViewById(R.id.logCloseImg);
        closeImg.setOnClickListener(closeErrorMsg());

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(onClickLogin());
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/

    private void complete(){
        Intent intent = new Intent();
        intent.putExtra("login", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showError(String error){
        errorMsg.setText(error);
        errorLayout.setVisibility(View.VISIBLE);
    }

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    public View.OnClickListener onClickLogin(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BackgroundLogin bl = new BackgroundLogin();
                String data = HelpFunctions.localURLEncoder("email") + "=" +
                        HelpFunctions.localURLEncoder(emailAddress.getText().toString()) + "&" +
                        HelpFunctions.localURLEncoder("password") + "=" +
                        HelpFunctions.localURLEncoder(HelpFunctions.md5(password.getText().toString()));

                bl.execute(URLs.LOGIN, data);
            }
        };
    }

    public View.OnClickListener onClickRegister(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getBaseContext(), Register.class);
                startActivity(login);
            }
        };
    }

    public View.OnClickListener closeErrorMsg(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorLayout.setVisibility(View.GONE);
            }
        };
    }


    /*------------------------------------*/
    /*-----------Backgroundtasks----------*/
    /*------------------------------------*/

    class BackgroundLogin extends AsyncTask<String,Void,ArrayList<Object>> {

        @Override
        protected ArrayList<Object> doInBackground(String... args) {

            String serverUrl = args[0];
            String data = args[1];

            try{

                URL url = new URL(serverUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                output(httpURLConnection, data);

                data = input(httpURLConnection);

                httpURLConnection.disconnect();

                return parseJSON(data);

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Object> response) {
            if((boolean) response.get(0)){
                complete();
            }else{
                showError((String) response.get(1));
            }
        }

        private void output(HttpURLConnection httpURLConnection, String data) throws IOException {

            Log.d("output, Login", "data: " + data);
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

        private ArrayList<Object> parseJSON(String data) throws JSONException {
            JSONObject jO = new JSONObject(data);
            ArrayList<Object> response = new ArrayList<Object>();
            response.add(jO.getBoolean("login"));
            response.add(jO.getString("message"));

            return response;
        }
    }
}
