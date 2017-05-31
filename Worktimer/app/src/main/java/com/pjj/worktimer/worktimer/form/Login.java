package com.pjj.worktimer.worktimer.form;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pjj.worktimer.worktimer.R;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    private TextView register;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (TextView) findViewById(R.id.textLogRegister);
        register.setOnClickListener(onClickRegister());
    }

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    public View.OnClickListener onClickRegister(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getBaseContext(), Register.class);
                startActivity(login);
            }
        };
    }
}
