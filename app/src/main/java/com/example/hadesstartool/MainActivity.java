package com.example.hadesstartool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Utils.BugRecorder;
import Utils.FileUtils;
import Utils.SpacesItemDecoration;
import Utils.StringUtils;
import Utils.ToastUtils;
import Utils.dataTools;

public class MainActivity extends AppCompatActivity {


    Button btn_leftMore;
    Button btn_onLoad;
    Button btn_toPhat;

    Button btn_newAccount;
    Button btn_Calculator;
    Button btn_recorder;
    Button btn_about;

    Button btn_author;

    RecyclerView accountRecycler;
    AccountRecyclerViewAdapter accountRecyclerViewAdapter = new AccountRecyclerViewAdapter();

    ErrorRecyclerViewAdapter errorRecyclerViewAdapter;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GP.BR.add("onCreate");
        //<<<????????????
        GP.mainActivity = this;
        GP.dataTools = new dataTools(this, GP.requestDataToolCode);
        GP.settingFile = new File(getExternalFilesDir(null), "setting");
        drawerLayout = findViewById(R.id.mainLayout);
        accountRecycler = findViewById(R.id.accountRecycler);

        btn_leftMore = findViewById(R.id.btn_leftMore);
        btn_onLoad = findViewById(R.id.btn_onLoad);
        btn_toPhat = findViewById(R.id.btn_toPhat);

        btn_newAccount = findViewById(R.id.btn_newAccount);
        btn_Calculator = findViewById(R.id.btn_Calculator);
        btn_recorder = findViewById(R.id.btn_recorder);
        btn_about = findViewById(R.id.btn_about);

        btn_author = findViewById(R.id.btn_author);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        accountRecycler.setLayoutManager(layoutManager);
        accountRecycler.addItemDecoration(new SpacesItemDecoration(25));


        //<<<button????????????
        setButton();

        //<<<??????????????????
        if (GP.firstStart) {
            readSet();
        }
        GP.BR.saveTxt(!GP.firstStart);
    }


    private void setButton() {

        btn_leftMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        btn_onLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p = StringUtils.getPackage(GP.to_path.file.toString());
                stopGame(p);
                startGame(p);
            }
        });
        btn_toPhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //???le
                choicePath();
            }
        });
        btn_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkP()) {
                    File onA = GP.to_path.file;
                    DocumentFile onADF = dataTools.getDocumentFile(GP.mainActivity, onA);
                    if (!onA.exists() && (onADF == null || !onADF.exists())) {
                        ToastUtils.toast(MainActivity.this, "???????????????");
                        return;
                    }
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
//                    .setIcon(R.mipmap.icon)//?????????????????????
                            .setTitle("??????")//????????????????????????
                            .setMessage("?????????????????????????????????????????????")//????????????????????????
                            //????????????????????????
                            .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    GP.BR.add("??????");
                                }
                            })
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String p = StringUtils.getPackage(GP.to_path.file.toString());
                                    GP.BR.add("????????????");
                                    stopGame(p);
                                    DocumentFile onADF = dataTools.getDocumentFile(GP.mainActivity, GP.to_path.file);
                                    if ( onADF == null || !onADF.delete()) {
                                        GP.BR.add("????????????");
                                        ToastUtils.toast(GP.mainActivity, "????????????");
                                        dialog.dismiss();
                                        return;
                                    }

                                    GP.BR.add("????????????");
                                    startGame(p);
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                } else {
                    Toast.makeText(MainActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_Calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //<<
                ToastUtils.toast(MainActivity.this, "????????????");
            }
        });
        btn_recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //<<
                ToastUtils.toast(MainActivity.this, "????????????");
//                reStart();
            }
        });
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_about();
            }
        });

        btn_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_author.setText(MainActivity.this.getString(R.string.by_author));
                ToastUtils.toast(MainActivity.this, hide());
            }
        });

    }

    private final String[] qwq = {"????????????", "qwq", "???~", "???~", "???~", "???~", "???~",
            "?????????", "???", "???", "rua!", "??????", "??????", "hentai", "????????????"};

    public String hide() {
        GP.BR.add("hide()");
        Random random = new Random();
        int ran = random.nextInt(7);
        switch (ran) {
            case 0:
                return new Sentence(random.nextInt(24)).run();
            case 1:
                return "???~";
            case 2:
                if (random.nextInt(2) == 0) {
                    if (GP.onDebug) {
                        ToastUtils.debug(MainActivity.this, "Debug???????????????");
                        GP.onDebug = false;
                    } else {
                        GP.onDebug = true;
                        ToastUtils.debug(MainActivity.this, "Debug???????????????");
                    }
                }
                return "?????????";
            case 3:
                return qwq[random.nextInt(qwq.length)];
            case 4:
                setBtn_about();
                return "?????????";
            case 5:
                if (GP.accountList == null || GP.accountList.size() < 1) {
                    return "??????";
                } else {
                    return StringUtils.getFileName(GP.accountList.get(random.nextInt(GP.accountList.size()))) + " ????????????";
                }
            default:
                reStart();
                return "?????????";
        }
    }

    public void setBtn_about() {
        GP.BR.add("??????");
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AboutInfo.class);
        startActivity(intent);
    }

    /**
     * ?????????????????????
     **/
    public void choicePath() {
        GP.BR.add("????????????");
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ChoicePath.class);
        startActivity(intent);
    }

    private void readSet() {
        if (!GP.settingFile.exists()) {
            //???????????????
            setBtn_about();
            new SettingInfo().initialize();
            return;
        }
        try {
            ObjectInputStream obis = new ObjectInputStream(new FileInputStream(GP.settingFile));
            SettingInfo o = (SettingInfo) obis.readObject();
            o.read();
            if (!o.version.equals(GP.version)) {
                //????????????
                setBtn_about();
                o.write();
            }
            obis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        GP.accountList.clear();
        GP.BR.add("onStart");
        GP.errorIntList.clear();
        if (GP.firstStart) {
            //<<<????????????>>????????????
            checkPermission();
        }
        btn_toPhat.setText(GP.to_path.fileName);
        if (checkP()) {
            GP.mainFile.mkdirs();
            GP.resAccount.mkdirs();
            GP.rubbish.mkdirs();
            File parentFile = GP.to_path.file.getParentFile();
            boolean b = parentFile.exists();
            System.out.println(b);
            DocumentFile parentDF = DocumentFile.fromTreeUri(MainActivity.this, Uri.parse(dataTools.changeToUri3(parentFile.getPath())));
//            DocumentFile parentDF = DocumentFile.fromFile(GP.to_path.file);
            boolean a = parentDF.exists();
            System.out.println(a);
            if ((parentFile == null || !parentFile.exists()) && !parentDF.exists()) {
//            if (!GP.dataTools.dirIsExist("/"+StringUtils.cutEnd(GP.Android_data,GP.to_path.file.toString()))) {
                //???????????????????????????
                ToastUtils.toast(this, "?????????????????????");
                GP.BR.add("?????????????????????");
                GP.errorIntList.add(GP.Game_Directory_Not_Exist);
                errorRecyclerViewAdapter = new ErrorRecyclerViewAdapter();
                accountRecycler.setAdapter(errorRecyclerViewAdapter);
            } else {
                //????????????
                CheckAccount.checkResError();
                CheckAccount.checkAccount();

                GP.accountList.clear();
                GP.accountList.addAll(Arrays.asList(FileUtils.Sort.sortFilesA_Z(GP.resAccount.listFiles())));
                if (GP.accountList.size() < 1) {
                    GP.errorIntList.add(GP.No_Record);
                    errorRecyclerViewAdapter = new ErrorRecyclerViewAdapter();
                    accountRecycler.setAdapter(errorRecyclerViewAdapter);
                } else {

                    //????????????
                    accountRecyclerViewAdapter.notifyDataSetChanged();
                    accountRecycler.setAdapter(accountRecyclerViewAdapter);

                }

            }
        } else {
            //????????????
            GP.errorIntList.add(GP.Insufficient_Permissions);
            errorRecyclerViewAdapter = new ErrorRecyclerViewAdapter();
            accountRecycler.setAdapter(errorRecyclerViewAdapter);

        }


        GP.BR.saveTxt(true);
        GP.firstStart = false;
    }

    public void setBtn_onLoad(String name) {

        btn_onLoad.setText(name);
    }

    public void stopGame(String packageName) {

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = am.getRunningAppProcesses();
        int pid = -1;
        for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
            if (amProcess.processName.equals(packageName)) {
                pid = amProcess.pid;
                break;
            }
        }
        if (pid != -1) {
            android.os.Process.killProcess(pid);
            GP.BR.add("??????:" + packageName);
            return;
        }
        ActivityManager mActivityManager = (ActivityManager)
                GP.mainActivity.getSystemService(Context.ACTIVITY_SERVICE);
        GP.BR.add("??????:" + packageName);
        mActivityManager.killBackgroundProcesses(packageName);
//        mActivityManager.forceStopPackage
        Method method = null;
        try {
            method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(mActivityManager, packageName);
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            GP.BR.add(e.getMessage());
        }
    }

    public void startGame(String packageName) {

        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            doStartApplicationWithPackageName(packageName);
        }

    }

    private void doStartApplicationWithPackageName(String packagename) {

// ?????????????????????APP?????????????????????Activities???services???versioncode???name??????
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            GP.BR.add(e.getMessage());
        }
        if (packageinfo == null) {
            GP.BR.add("PackageInfo == null");
            return;
        }

        // ?????????????????????CATEGORY_LAUNCHER???????????????Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

// ??????getPackageManager()???queryIntentActivities????????????
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
// packagename = ??????packname
            String packageName = resolveinfo.activityInfo.packageName;
// ??????????????????????????????APP???LAUNCHER???Activity[???????????????packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
// LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // ??????ComponentName??????1:packagename??????2:MainActivity??????
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);
        } else {
            GP.BR.add("ResolveInfo == null");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GP.BR.add("onPause");
        GP.BR.saveTxt(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GP.BR.add("onStop");
        new SettingInfo().write();
        GP.BR.saveTxt(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GP.BR.add("onDestroy");
        new SettingInfo().write();
        GP.BR.saveTxt(true);
//        GP.BR.endSave(true);
    }

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();


    // ????????????

    /**
     * ??????????????????ture
     **/
    public boolean checkP() {
        mPermissionList.clear();
        //???????????????????????????
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
//                GP.BR.add("?????????:" + permission);
            }
        }

        if (!GP.dataTools.isPermissions(GP.to_path.getFile().getParentFile().getPath())) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            return false;
        } else {
            return true;
        }
        //?????????????????????????????????????????????
        //            GP.BR.add("????????????");
//        return mPermissionList.isEmpty();
    }

    // ????????????
    public void checkPermission() {
        mPermissionList.clear();
        //???????????????????????????
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
                GP.BR.add("?????????:" + permission);
            }
        }

        /**
         * ??????????????????
         */
        if (mPermissionList.isEmpty()) {//?????????????????????????????????????????????
            GP.BR.add("????????????");
        } else {//??????????????????
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//???List????????????
            ActivityCompat.requestPermissions(MainActivity.this, permissions, GP.PERMISSION_REQUEST);
            GP.BR.add("????????????");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            String pk = StringUtils.getPackage(GP.to_path.getFile().getPath());
//            DocumentFile documentFile = dataTools.getDocumentFile(MainActivity.this, new File(GP.storage_data+pk));
//            boolean a = documentFile.exists();
            if (!GP.dataTools.isPermissions(GP.to_path.getFile().getParentFile().getPath())) {
                String pkn = StringUtils.getPackage(GP.to_path.file.getPath());
                ToastUtils.debug(this, "???" + pkn + "????????????/???????????????");
                GP.BR.add("???" + pkn + "????????????/???????????????");
                AlertDialog dialog = new AlertDialog.Builder(this)
//                    .setIcon(R.mipmap.icon)//?????????????????????
                        .setTitle("???" + pkn + "????????????/???????????????")//????????????????????????
                        .setMessage("?????????" + pkn + "??????\n??????????????????????????????\n(??????????????????" + pkn + "????????????,???????????????????????????)")//????????????????????????
                        //????????????????????????
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                GP.BR.add("????????????");
                            }
                        })
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GP.BR.add("????????????");
                                GP.dataTools.requestPermission();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            } else {
                GP.BR.add("??????data????????????");
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()) {

//                Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                GP.BR.add("?????????????????????????????????");
            } else {
                ToastUtils.debug(this, "???????????????????????????");
                GP.BR.add("???????????????????????????");
                AlertDialog dialog = new AlertDialog.Builder(this)
//                    .setIcon(R.mipmap.icon)//?????????????????????
                        .setTitle("???????????????????????????")//????????????????????????
                        .setMessage("?????????????????????????????????\n(???????????????" + GP.mainFile.toString() + ")\n????????????????????????????????????????????????")//????????????????????????
                        //????????????????????????
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                GP.BR.add("????????????");
                            }
                        })
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GP.BR.add("????????????");

                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, GP.Manage_All_File);

                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        }


    }

    /**
     * ????????????
     * ???????????????????????????????????????????????????????????????????????????
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GP.PERMISSION_REQUEST:
                GP.BR.add("????????????");
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        GP.dataTools.savePermissions(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            GP.BR.add(data.toString());
            switch (requestCode) {
                case GP.requestDataToolCode:
                    GP.dataTools.savePermissions(requestCode, resultCode, data);
                    GP.BR.add(data.toString());
                    break;
                case GP.Manage_All_File:
                    GP.BR.add("??????????????????????????????");
                    break;

                default:

                    break;
            }
        } else {
            GP.BR.add(resultCode + "????????????");
        }
    }

    /**
     * ????????????Activity
     **/
    public void reStart() {
        Intent intent = getIntent();
//        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        GP.BR.add("????????????");
        ToastUtils.toast(this, "????????????", Toast.LENGTH_SHORT);
        finish();
        startActivity(intent);
    }

    //?????? ???????????????

    public void openFileManager(int REQUEST_CODE) {
        Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
        uri1 = DocumentFile.fromTreeUri(this, uri1).getUri();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//??????????????????????????????????????????????????????????????????
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri1);
        startActivityForResult(intent, REQUEST_CODE);
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private final String[] exitString = {"??????", "????????????????????????", "????????????????????????", "????????????????????????",};

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), exitString[new Random().nextInt(exitString.length)],
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            new SettingInfo().write();
            GP.BR.endSave(true);
            finish();
            System.exit(0);
        }
    }


}