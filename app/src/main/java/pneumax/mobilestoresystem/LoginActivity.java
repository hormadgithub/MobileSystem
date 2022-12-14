package pneumax.mobilestoresystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import pneumax.mobilestoresystem.connected.ExecuteThreeParameter;
import pneumax.mobilestoresystem.connected.GlobalUtility;
import pneumax.mobilestoresystem.manager.GlobalVar;
import pneumax.mobilestoresystem.manager.MyConstant;
import pneumax.mobilestoresystem.object.StaffLogin;

import static pneumax.mobilestoresystem.manager.GlobalVar.getDataBaseName;
import static pneumax.mobilestoresystem.manager.GlobalVar.getServerName;

public class LoginActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    StaffLogin resultStaffLogin;

    View AlertDialogView;
    String strAlertMessage;

    CircularImageView miconLogin;
    EditText metUsername;
    EditText metPassword;
    Button mbtnLogin;
    Group mgpSelectDatabase;
    RadioButton mRadioButtonPneumaxDB;
    RadioButton mRadioButtonAnalysisDB;
    RadioButton  mRadioButtonDatatest;
    CheckBox mchkSelectServer;
    TextView mtvCurrentVersion;

    String strUserName;
    String strPassword;
    boolean blnSelectDatatest;
    boolean blnSelectAnalysisDB;
    boolean blnSelectPneumaxDB;
    boolean blnSelectServerLocal;

    //parameter ????????????????????????????????? Execute
    String strMainServerAddress,strMainDataBaseName, strDataBaseName,strTableName,strField,strCondition,strURL,strReturnValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        globalUtility = new GlobalUtility();


        BindWidgets();
        Animation_Logo();
        SetEvent();
        InitializeData();
    }

    private void  Animation_Logo(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                miconLogin,
                PropertyValuesHolder.ofFloat("ScaleX",1.0f),
                PropertyValuesHolder.ofFloat("ScaleX",0.9f),
                PropertyValuesHolder.ofFloat("ScaleX",0.8f),
                PropertyValuesHolder.ofFloat("ScaleX",0.7f),
                PropertyValuesHolder.ofFloat("ScaleX",0.6f),
                PropertyValuesHolder.ofFloat("ScaleX",0.5f),
                PropertyValuesHolder.ofFloat("ScaleX",0.4f),
                PropertyValuesHolder.ofFloat("ScaleX",0.3f),
                PropertyValuesHolder.ofFloat("ScaleX",0.2f),
                PropertyValuesHolder.ofFloat("ScaleX",0.1f),
                PropertyValuesHolder.ofFloat("ScaleX",0.0f),

                PropertyValuesHolder.ofFloat("ScaleX",-0.1f),
                PropertyValuesHolder.ofFloat("ScaleX",-0.2f),
                PropertyValuesHolder.ofFloat("ScaleX",-0.3f),
                PropertyValuesHolder.ofFloat("ScaleX",-0.4f),
                PropertyValuesHolder.ofFloat("ScaleX",-0.5f),
                PropertyValuesHolder.ofFloat("ScaleX",-0.6f),
                PropertyValuesHolder.ofFloat("ScaleX",-0.7f),
                PropertyValuesHolder.ofFloat("ScaleX",-0.8f),
                PropertyValuesHolder.ofFloat("ScaleX",-0.9f),
                PropertyValuesHolder.ofFloat("ScaleX",-1.0f)
        );
        //Set duration
        objectAnimator.setDuration(500);
        //Set repeat count
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //Set repeat mode
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //Start animator
        objectAnimator.start();

    }

    private void InitializeData() {
        //????????????????????????????????????????????????????????????????????????????????? PneumaxDB
        getDataBaseName=GlobalVar.getInstance().getDataBasePneumaxDB;
        strMainDataBaseName=getDataBaseName;

        strTableName="Mobile_Server";
        strField="ServerAddress";
        strCondition="ServerName='MAIN'"; //http://192.168.2.112/webservice/service.asmx/ ??????????????????????????? Database ?????????????????????????????????????????????????????? Server ?????????????????????
        // ????????? ServerAddress  ????????? Server Main ????????? ???????????????????????? PNUMAXDB
        strURL= myConstant.urlMobile_GetServerAddress();
        strMainServerAddress=globalUtility.Find_ServerAddress(getApplicationContext(), strMainDataBaseName,strTableName,strField,strCondition,strURL);

        //22-06-2021 ???????????????????????????????????? Version ????????????????????? UPdate ?????????????????????
        if (! CheckCurrentVersion()) {
            strAlertMessage="?????????????????????????????? Update Program ??????????????????????????????";
            ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
        }


        ColorStateList colorStateRadioButton = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {
                        getResources().getColor(R.color.gray_700) //disabled
                        ,getResources().getColor(R.color.orange_700) //enabled

                }
        );
        ColorStateList colorStateCheckbox = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {
                        getResources().getColor(R.color.colorWhite) //disabled
                        ,getResources().getColor(R.color.colorWhite) //enabled
                }
        );

        mRadioButtonPneumaxDB.setButtonTintList(colorStateRadioButton);//set the color tint list
        mRadioButtonAnalysisDB.setButtonTintList(colorStateRadioButton);//set the color tint list
        mRadioButtonDatatest.setButtonTintList(colorStateRadioButton);//set the color tint list
        mchkSelectServer.setButtonTintList(colorStateRadioButton);//set the color tint list

        blnSelectServerLocal=false;
        blnSelectDatatest=false;
        blnSelectAnalysisDB=false;
        blnSelectPneumaxDB=false;

        //myConstant = new MyConstant();
        //globalVar = new GlobalVar();

        mgpSelectDatabase.setVisibility(View.GONE);
        metUsername.requestFocus();
    }

    private void BindWidgets() {
        miconLogin=(CircularImageView) findViewById(R.id.iconLogin);
        mbtnLogin = (Button) findViewById(R.id.btnLogin);
        metUsername = (EditText) findViewById(R.id.etUsername);
        metPassword = (EditText) findViewById(R.id.etPassword);

        mtvCurrentVersion = (TextView) findViewById(R.id.tvCurrentVersion);

        mgpSelectDatabase = (Group)  findViewById(R.id.gpSelectDatabase);
        mRadioButtonDatatest = (RadioButton) findViewById(R.id.radBtnDataTest);
        mRadioButtonAnalysisDB = (RadioButton) findViewById(R.id.radBtnAnalysisDB);
        mRadioButtonPneumaxDB = (RadioButton) findViewById(R.id.radBtnPneumaxDB);

        mchkSelectServer = (CheckBox) findViewById(R.id.chkSelectServer);

    }


    private void SetEvent() {

        metUsername.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
                        metPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });

        metPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
                    mbtnLogin.requestFocus();
                    return true;
                }
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                    metUsername.requestFocus();
                    return true;
                }
                return false;
            }
        });



        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = "6SepV2";
                //22-06-2021 ???????????????????????????????????? Version ????????????????????? UPdate ?????????????????????
                if (! CheckCurrentVersion()) {
                    UpdateProgram();
                    finish();
                    System.exit(0);
                    return;
                }

                try {
                    mbtnLogin.setEnabled(false);
                    strUserName = metUsername.getText().toString().trim();
                    strPassword = metPassword.getText().toString().trim();
                    blnSelectDatatest = mRadioButtonDatatest.isChecked();
                    blnSelectAnalysisDB = mRadioButtonAnalysisDB.isChecked();
                    blnSelectPneumaxDB = mRadioButtonPneumaxDB.isChecked();
                    blnSelectServerLocal=mchkSelectServer.isChecked();

                    //??????????????????????????????????????????????????????????????????????????????????????????????????? Login ????????????????????????
                    if (!(blnSelectPneumaxDB || blnSelectAnalysisDB || blnSelectDatatest)) {
                        if (GlobalVar.getInstance().isEmptyString(strUserName)) {
                            strAlertMessage="??????????????????????????? User Login ???????????? !!!";
                            ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
                            metUsername.requestFocus();
                           // showKeyboard(metUsername);
                            mbtnLogin.setEnabled(true);
                        } else if (GlobalVar.getInstance().isEmptyString(strPassword)) {
                            strAlertMessage="??????????????????????????? User Login ???????????? !!!";
                            ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
                            metPassword.requestFocus();
                           // showKeyboard(metPassword);
                            mbtnLogin.setEnabled(true);
                        } else {
                           // hiddenKeyboard();
                            //?????????????????????????????????????????? Username ?????? PNEUMAXDB ????????????
                            CheckLoginMobile(strMainDataBaseName, strUserName, strPassword);
                        }
                    } else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(StaffLogin.TABLE_NAME, resultStaffLogin);

                        //Select Server
                        if (blnSelectServerLocal){
                            intent.putExtra(getServerName, GlobalVar.getInstance().getServerLocal);
                        }
                        else{
                            intent.putExtra(getServerName, GlobalVar.getInstance().getServerMain);
                        }

                        // SetDataBaseValue
                        if (blnSelectPneumaxDB) {
                            intent.putExtra(getDataBaseName, GlobalVar.getInstance().getDataBasePneumaxDB);
                        }
                        else if (blnSelectAnalysisDB) {
                            intent.putExtra(getDataBaseName, GlobalVar.getInstance().getDataBaseAnalysisDB);
                        }
                        else {
                            intent.putExtra(getDataBaseName, GlobalVar.getInstance().getDataBaseDataTest);
                        }
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    }


                } catch (Exception e) {
                    mbtnLogin.setEnabled(true);
                    //Toast.makeText(MainActivity.this, "e Check User Login ==> " + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(tag, "e Check User Login ==> " + e.toString());
                }
            }
        });

        mchkSelectServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mchkSelectServer.isChecked()){
                    globalVar.getServerName=globalVar.getServerLocal;
                    blnSelectServerLocal=true;
                }
                else {
                    globalVar.getServerName="MAIN";
                    blnSelectServerLocal=false;
                }
            }
        });
        mRadioButtonPneumaxDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioButtonPneumaxDB.isChecked()){
                    globalVar.getDataBaseName=globalVar.getDataBasePneumaxDB;
                    blnSelectPneumaxDB=true;
                }
                else {
                    globalVar.getDataBaseName="";
                }
                 mbtnLogin.setEnabled(blnSelectDatatest || blnSelectAnalysisDB || blnSelectPneumaxDB);
            }
        });
        mRadioButtonAnalysisDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioButtonAnalysisDB.isChecked()){
                    globalVar.getDataBaseName=globalVar.getDataBaseAnalysisDB;
                    blnSelectAnalysisDB=true;
                }
                else {
                    globalVar.getDataBaseName="";
                }
                mbtnLogin.setEnabled(blnSelectDatatest || blnSelectAnalysisDB || blnSelectPneumaxDB);
            }
        });

        mRadioButtonDatatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioButtonDatatest.isChecked()){
                    globalVar.getDataBaseName=globalVar.getDataBaseDataTest;
                    blnSelectDatatest=true;
                }
                else {
                    globalVar.getDataBaseName="";
                }
                mbtnLogin.setEnabled(blnSelectDatatest || blnSelectAnalysisDB || blnSelectPneumaxDB);
            }
        });
        }

        private boolean  CheckCurrentVersion() {
//        String strCurrentVersion;
//            strCurrentVersion=mtvCurrentVersion.getText().toString();
//            strTableName="ConstVar";
//            strCondition="CVCode='STORESYSTEMVERSION' and CVValue='"+strCurrentVersion+"'";
//            strURL=strMainServerAddress+myConstant.urlMobile_CountRecord();
//            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strMainDataBaseName,strTableName,strCondition,strURL);
//            if (strReturnValue.equals("0")){
//                metUsername.setEnabled(false);
//                metPassword.setEnabled(false);
//                mbtnLogin.setText("UPDATE");
//                return false;
//            }
            return true;
        }

        private void UpdateProgram() {
            DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse("http://192.168.2.112/AppAndroid/app-debug.apk");
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("Mobile Store System");
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-debug.apk");
            downloadmanager.enqueue(request);
        }

    private void CheckLoginMobile(String strDatabaseName,String strUserID,String strPassword) {
        String tag = "6SepV2";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            executeThreeParameter.execute("strDatabaseName",strDatabaseName,
                    "strUserID", strUserID,
                    "strPassword",strPassword,
                    strMainServerAddress+myConstant.urlMobile_CheckLogin());
                    //myConstant.urlMobile_CheckLogin());
                    //"strPassword",strPassword, myConstant.urlMobile_CheckLogin_Golaung());

            String resultJSON = executeThreeParameter.get();
           // resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strAlertMessage="Username ???????????? Password ?????????????????????????????? ???????????? ?????????????????????????????? Store !!!";
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);

                mbtnLogin.setEnabled(true);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                resultStaffLogin = gson.fromJson(resultJSON.toString(), StaffLogin.class);
                //?????????????????????????????????????????? Database ????????? ????????????????????????????????????????????????????????????????????????
                if (resultStaffLogin.DPCode.toUpperCase().equals("MIS") ){
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                //if (!resultStaffLogin.DPCode.toUpperCase().equals("") ){
                    mgpSelectDatabase.setVisibility(View.VISIBLE);
                    //???????????????????????????????????? Local ????????????????????? MIS
                    mchkSelectServer.setChecked(true);
                    metUsername.setEnabled(false);
                    metPassword.setEnabled(false);
                    mbtnLogin.setEnabled(false);
                }
                else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(StaffLogin.TABLE_NAME, resultStaffLogin);
                    if (resultStaffLogin.DPCode.toUpperCase().equals("MIS")) {
                        //Select Server
                        if (blnSelectServerLocal){
                            intent.putExtra(getServerName, GlobalVar.getInstance().getServerLocal);
                        }
                        else{
                            intent.putExtra(getServerName, GlobalVar.getInstance().getServerMain);
                        }
                        // SetDataBaseValue
                        if (blnSelectPneumaxDB) {
                            intent.putExtra(getDataBaseName, GlobalVar.getInstance().getDataBasePneumaxDB);
                        }
                        else if (blnSelectAnalysisDB) {
                            intent.putExtra(getDataBaseName, GlobalVar.getInstance().getDataBaseAnalysisDB);
                        }
                        else {
                            intent.putExtra(getDataBaseName, GlobalVar.getInstance().getDataBaseDataTest);
                        }
                    }
                    //??????????????????????????? MIS ????????????????????? PNEUMAXDB
                    else{
                        //Select Server
                        if (blnSelectServerLocal){
                            intent.putExtra(getServerName, GlobalVar.getInstance().getServerLocal);
                        }
                        else{
                            intent.putExtra(getServerName, GlobalVar.getInstance().getServerMain);
                        }
                        intent.putExtra(globalVar.getDataBaseName, globalVar.getDataBasePneumaxDB);
                    }

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
            }
        } catch (Exception e) {
            Log.d(tag, "e Check User Login ==> " + e.toString());
        }
    }



        private void ShowAlertDialog(int intAlertType, String strAlertTitle, String strAlertMessage, int ic_AlertDialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AlertDialog);

            if (intAlertType == R.string.alertdialog_success) {
                AlertDialogView = LayoutInflater.from(this).inflate(
                        R.layout.layout_success_dialog,
                        (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
                );
            }

            if (intAlertType == R.string.alertdialog_warning) {
                AlertDialogView = LayoutInflater.from(this).inflate(
                        R.layout.layout_warning_dialog,
                        (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
                );
            }

            if (intAlertType == R.string.alertdialog_error) {
                AlertDialogView = LayoutInflater.from(this).inflate(
                        R.layout.layout_error_dialog,
                        (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
                );
            }

            builder.setView(AlertDialogView);
            ((TextView) AlertDialogView.findViewById(R.id.textTitle)).setText(strAlertTitle);
            ((TextView) AlertDialogView.findViewById(R.id.textMessage)).setText(strAlertMessage);
            ((ImageView) AlertDialogView.findViewById(R.id.imageIcon)).setImageResource(ic_AlertDialog);

            if (intAlertType == R.string.alertdialog_success || intAlertType == R.string.alertdialog_error) {
                ((Button) AlertDialogView.findViewById(R.id.buttonAction)).setText(getResources().getString(R.string.alertdialog_okay));
                final AlertDialog alertDialog = builder.create();
                AlertDialogView.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }

            if (intAlertType == R.string.alertdialog_warning ) {
                ((Button) AlertDialogView.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.yes));
                ((Button) AlertDialogView.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.no));
                final AlertDialog alertDialog = builder.create();
                AlertDialogView.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                AlertDialogView.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }

        }


//        private void hiddenKeyboard() {
//            if (getCurrentFocus() != null) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            }
//        }//hiddenKeyboard

//        private void showKeyboard(View view) {
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            view.requestFocus();
//            inputMethodManager.showSoftInput(view, 0);
//        }//showKeyboard
}