package com.pjj.worktimer.worktimer;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pjj.worktimer.worktimer.form.Generate_Project_Form;
import com.pjj.worktimer.worktimer.helpClasses.HelpFunctions;
import com.pjj.worktimer.worktimer.helpClasses.RequestCodes;
import com.pjj.worktimer.worktimer.helpClasses.Save;
import com.pjj.worktimer.worktimer.helpClasses.ViewPagerAdapter;
import com.pjj.worktimer.worktimer.form.Login;
import com.pjj.worktimer.worktimer.mainScreen.Dashboard;
import com.pjj.worktimer.worktimer.mainScreen.Einstellungen;
import com.pjj.worktimer.worktimer.mainScreen.Statistik;

import com.google.android.gms.ads.MobileAds;
import com.pjj.worktimer.worktimer.projectScreen.ProjectFolder;

import inapppurchase.util.IabHelper;
import inapppurchase.util.IabResult;
import inapppurchase.util.Inventory;
import inapppurchase.util.Purchase;

public class main extends AppCompatActivity {

    private final static String SKU_PREMIUM = "premium_zugang";

    private Dashboard dashboard;
    private FloatingActionButton fab;
    private TextView login;
    private TextView premium;
    private TabLayout tabLayout;
    private TabLayout.Tab firstTab;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter vpa;
    private IabHelper mHelper;

    private static Activity self;


    private AdView mAdView;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, "ca-app-pub-7748889875467646~9838913818");
        //TextView logo = (TextView) findViewById(R.id.Logo);
        setContentView(R.layout.activity_main);

        self = this;

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //Tablayout um zwischen den einzelnen Fragmenten (Einstellungen, Dashboard, und Statistik) gewechselt werden kann
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Der ViewPager wird für die Darstellung der Fragmente benötigt
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //Der ViewPager-Adapter ist notwendig, da mehrere Fragmente generiert werden.
        vpa = new ViewPagerAdapter(getSupportFragmentManager());

        //Jedes Fragment wird einzeln dem vpa hinzugefügt
        vpa.addFragment("Statistik", new Statistik());

        //Dashboard besitzt, anders als die anderen beiden Fragmente, keine Layout-XML. Dieses Fragment wurde per Hand gecoded.
        //Um später auf Funktionen zugreifen zu können wird hier eine globale Instanz von Dashboard erzeugt.
        dashboard = new Dashboard();
        vpa.addFragment("Dashboard", dashboard);
        vpa.addFragment("Einstellungen", new Einstellungen());

        //Das TabLayout wird mit dem ViewPager-Adapter initialisiert
        viewPager.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPager);

        //Der zweite Tab im tabLayout soll nach dem Start der App direkt ausgewählt sein
        firstTab = tabLayout.getTabAt(1);
        firstTab.select();

        //Ein FloatingActionButton wird für das Hinzufügen von Projekten verwendet
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onClickForm());

        //Ein Listener für die TextView-Resource "Login"
        login = (TextView) findViewById(R.id.Login);
        login.setOnClickListener(onClickLogin());

        premium = (TextView) findViewById(R.id.premium);
        premium.setOnClickListener(onClickPurchase());

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnvx55UKJZziI+uO6rUudShr8Hhf/Li/iXfF27y3QZ8lrxCK1Ub9RXrvVL34GTiaqA9Qe/owC410UXqMGQDlS/x3sg7OrCkwh0MatEW75joOOgkt5u7vCi6Smpx0Sb8ZUtOPVvqicNgjcB5WuRsiCx4igQaGQi/+5ZgI8aS+w8QeGVXl3FgbWVMwhiNy4H0U37ZKqKB7aR99CNvjr66Tu4p6Qy03pGyBG9CUGzOz3I9B+L2nMI/OXfxX73Hq8FMdd1PVNl8lviju+OMUj43BUlxcKbHDERWIGdWc91P5JGnOqW9YshcThsnPjL3MqM3Ph6t6XrMsS2j0Q0WDBzdIvSQIDAQAB";
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(onIabSetupFinishedListener());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    /*
    Jedes Mal, wenn die Activity gestoppt wird, sollen alle Projekte gespeichert werden.
    Die Methoden zum Speichern wurden in der Klasse "Save()" statisch angelegt.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Save.saveProjects();
    }

    /*
    Sobald der FloatingActionButton geklickt wird, startet die Activity Generate_Project_Form.
    Beim finish() dieser Activity wird ein "Result" generiert, dass hier abgefangen wird.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case (RequestCodes.GENERATE_PROJECT_FORM):
                if(resultCode == RESULT_OK){
                    writeFormData(data.getIntExtra("generate_project", 0));
                }
                break;
            case (RequestCodes.LOGIN):
                if(resultCode == RESULT_OK){
                    login.setVisibility(View.GONE);
                }
        }
    }

    @Override
    protected void onDestroy() {
        dashboard.saveOrder();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
        super.onDestroy();
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/

    private void makePremium(){
        premium.setVisibility(View.GONE);
        removeAd();
        HelpFunctions.setIsPremium(true);
    }

    private void removeAd(){
        mAdView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(fab.getLayoutParams());
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlp.setMargins(0,0,HelpFunctions.dp(20), HelpFunctions.dp(20));
        fab.setLayoutParams(rlp);
    }

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    /*
    Der OnClickListener für den FloatingActionButton. Es wird die Methode startActivityForResult() genutzt,
    um die Daten aus dem Formular zu übergeben.
     */

    private IabHelper.OnIabSetupFinishedListener onIabSetupFinishedListener(){
        return new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d("IapHelper", "Problem setting up In-app Billing: " + result);
                }else{
                    mHelper.queryInventoryAsync(mGotInventoryListener());
                }
            }
        };
    }

    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener(){
        return new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                if(!inv.hasPurchase(SKU_PREMIUM)){
                    premium.setVisibility(View.VISIBLE);
                }else{
                    makePremium();
                }
            }
        };
    }

    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener(){
        return new IabHelper.OnIabPurchaseFinishedListener() {
            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                if(result.isFailure()){
                    Log.d("PurchaseFails", "onIabPurchaseFinished: " + result.getMessage());
                }

                if(info.getSku().equals(SKU_PREMIUM)){
                    makePremium();
                }
            }
        };
    }

    public View.OnClickListener onClickPurchase(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.flagEndAsync();
                mHelper.launchPurchaseFlow(main.this, SKU_PREMIUM, RequestCodes.PURCHASE,
                        mPurchaseFinishedListener(), "");
            }
        };
    }

    private View.OnClickListener onClickForm(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HelpFunctions.getIsPremium() || ProjectFolder.getSize() < 2){
                    Intent generateProjectForm = new Intent(getBaseContext(), Generate_Project_Form.class);
                    startActivityForResult(generateProjectForm, RequestCodes.GENERATE_PROJECT_FORM);
                }else{
                    mHelper.flagEndAsync();
                    mHelper.launchPurchaseFlow(main.this, SKU_PREMIUM, RequestCodes.PURCHASE,
                            mPurchaseFinishedListener(), "");
                }
            }
        };
    }

    private View.OnClickListener onClickLogin(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getBaseContext(), Login.class);
                startActivityForResult(login, RequestCodes.LOGIN);
            }
        };
    }

    /*------------------------------------*/
    /*-----------Help functions-----------*/
    /*------------------------------------*/

    public void writeFormData(int projectId){
        firstTab.select();
        dashboard.addProject(projectId);
    }

    public static Activity getActivity(){
        return self;
    }
}
