package com.pjj.worktimer.worktimer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class Generate_Project_Form extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
        setContentView(R.layout.activity_generate__project__form);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(onClick());
    }

    private View.OnClickListener onClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("generate_project_form", readForm());
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }

    private ArrayList<String> readForm(){
        ArrayList<String> form = new ArrayList<String>();

        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editFirma = (EditText) findViewById(R.id.editFirma);
        EditText editStrasse = (EditText) findViewById(R.id.editStrasse);
        EditText editNr = (EditText) findViewById(R.id.editNr);
        EditText editLand = (EditText) findViewById(R.id.editLand);
        EditText editUst_idnr = (EditText) findViewById(R.id.editUst_idnr);
        EditText editStrnr = (EditText) findViewById(R.id.editSteuernummer);

        String name = editName.getText().toString();
        String firma = editFirma.getText().toString();
        String strasse = editStrasse.getText().toString();
        String nr = editNr.getText().toString();
        String land = editLand.getText().toString();
        String ust_idnr = editUst_idnr.getText().toString();
        String strnr = editStrnr.getText().toString();

        form.add(name);
        form.add(firma);
        form.add(strasse);
        form.add(nr);
        form.add(land);
        form.add(ust_idnr);
        form.add(strnr);

        for (String f: form) {
            if(f == null){
                return null;
            }
        }
        return form;
    }
}
