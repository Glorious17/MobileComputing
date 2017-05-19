package com.pjj.worktimer.worktimer.form;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pjj.worktimer.worktimer.projectScreen.Project;
import com.pjj.worktimer.worktimer.R;

import java.util.ArrayList;

public class Generate_Project_Form extends AppCompatActivity {

    int standartColorBackground;
    int standartColorText;

    EditText editName;
    EditText editFirma;
    EditText editStrasse;
    EditText editNr;
    EditText editLand;
    EditText editUst_idnr;
    EditText editStrnr;

    TextView tvName;
    TextView tvFirma;
    TextView tvStrasse;
    TextView tvNr;
    TextView tvLand;
    TextView tvUst_idnr;
    TextView tvStrnr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
        setContentView(R.layout.activity_generate__project__form);

        editName = (EditText) findViewById(R.id.editName);
        editFirma = (EditText) findViewById(R.id.editFirma);
        editStrasse = (EditText) findViewById(R.id.editStrasse);
        editNr = (EditText) findViewById(R.id.editNr);
        editLand = (EditText) findViewById(R.id.editLand);
        editUst_idnr = (EditText) findViewById(R.id.editUst_idnr);
        editStrnr = (EditText) findViewById(R.id.editSteuernummer);

        tvName = (TextView) findViewById(R.id.textName);
        tvFirma = (TextView) findViewById(R.id.textFirma);
        tvStrasse = (TextView) findViewById(R.id.textStrasse);
        tvNr = (TextView) findViewById(R.id.textNr);
        tvLand = (TextView) findViewById(R.id.textLand);
        tvUst_idnr = (TextView) findViewById(R.id.textUst_idnr);
        tvStrnr = (TextView) findViewById(R.id.textSteuernummer);

        standartColorText = tvName.getCurrentTextColor();
        standartColorBackground = editFirma.getDrawingCacheBackgroundColor();

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(onClick());
    }

    private View.OnClickListener onClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> formData = readForm();
                if(formData != null){
                    Project p = new Project(formData);
                    Intent intent = new Intent();
                    intent.putExtra("generate_project", p.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private ArrayList<String> readForm(){
        ArrayList<String> form = new ArrayList<String>();

        tvName.setTextColor(standartColorText);
        tvFirma.setTextColor(standartColorText);
        tvStrasse.setTextColor(standartColorText);
        tvNr.setTextColor(standartColorText);
        tvLand.setTextColor(standartColorText);
        tvUst_idnr.setTextColor(standartColorText);
        tvStrnr.setTextColor(standartColorText);

        editName.setBackgroundColor(standartColorBackground);
        editFirma.setBackgroundColor(standartColorBackground);
        editStrasse.setBackgroundColor(standartColorBackground);
        editNr.setBackgroundColor(standartColorBackground);
        editLand.setBackgroundColor(standartColorBackground);
        editUst_idnr.setBackgroundColor(standartColorBackground);
        editStrnr.setBackgroundColor(standartColorBackground);

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

        int counter = 0;
        boolean pass = true;
        TextView tv;

        for (String f: form) {
            if(f.equals("")){
                switch (counter){
                    case 0:
                        tvName.setTextColor(getResources().getColor(R.color.warning));
                        editName.setBackgroundColor(getResources().getColor(R.color.warning));
                        break;
                    case 1:
                        tvFirma.setTextColor(getResources().getColor(R.color.warning));
                        editFirma.setBackgroundColor(getResources().getColor(R.color.warning));
                        break;
                    case 2:
                        tvStrasse.setTextColor(getResources().getColor(R.color.warning));
                        editStrasse.setBackgroundColor(getResources().getColor(R.color.warning));
                        break;
                    case 3:
                        tvNr.setTextColor(getResources().getColor(R.color.warning));
                        editNr.setBackgroundColor(getResources().getColor(R.color.warning));
                        break;
                    case 4:
                        tvLand.setTextColor(getResources().getColor(R.color.warning));
                        editLand.setBackgroundColor(getResources().getColor(R.color.warning));
                        break;
                    case 5:
                        tvUst_idnr.setTextColor(getResources().getColor(R.color.warning));
                        editUst_idnr.setBackgroundColor(getResources().getColor(R.color.warning));
                        break;
                    case 6:
                        tvStrnr.setTextColor(getResources().getColor(R.color.warning));
                        editStrnr.setBackgroundColor(getResources().getColor(R.color.warning));
                        break;
                }
                pass = false;
            }
            counter++;
        }
        if(pass){
            return form;
        }else{
            Toast.makeText(getBaseContext(), "Rot markierte Felder sind Pflichtfelder", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
