package com.swuos.swuassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.swuos.ALLFragment.FragmentControl;
import com.swuos.ALLFragment.swujw.TotalInfo;
import com.swuos.Service.ClassAlarmService;


public class MainActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, View.OnClickListener {
    /*账户名*/
    private static String userName;
    /*密码*/
    private static String password;
    TextView nameTextView;
    TextView swuIDTextView;
    /*保存用户信息*/
    private static TotalInfo totalInfo = new TotalInfo();

    /*用户信息的本地储存文件*/
    public static SharedPreferences sharedPreferences;

    private FragmentControl fragmentControl;
    private static int fragmentPosition = R.id.nav_main;


    private Toolbar toolbar;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        initView();
        fragmentControl = new FragmentControl(getSupportFragmentManager());
        fragmentControl.initFragment(getSupportFragmentManager());
        fragmentControl.fragmentStateCheck(
                savedInstanceState, getSupportFragmentManager(),
                fragmentPosition);

        Log.d("Mainactivity", "OnCreatview");
        Intent statrtIntent = new Intent(this, ClassAlarmService.class);
        startService(statrtIntent);
    }


    /*获得某个活动的回复信息*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    userName = data.getStringExtra("userName");
                    password = data.getStringExtra("password");

                      /*对侧边栏的姓名和学号进行配置*/
                    swuIDTextView.setText(totalInfo.getSwuID());
                    nameTextView.setText(totalInfo.getName());
                    nameTextView.setClickable(false);
                }
                break;

        }
    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);

        /*显示退出按钮*/
        ImageButton imageButtonLoginOut = (ImageButton) view.findViewById(R.id.login_out);
        imageButtonLoginOut.setOnClickListener(MainActivity.this);

        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        /*侧边栏显示姓名学号*/
        nameTextView = (TextView) view.findViewById(R.id.name);
        swuIDTextView = (TextView) view.findViewById(R.id.swuid);

        /*打开保存用户信息的文件*/
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        totalInfo.setUserName(sharedPreferences.getString("userName", ""));
        totalInfo.setName(sharedPreferences.getString("name", ""));
        totalInfo.setPassword(sharedPreferences.getString("password", ""));
        totalInfo.setSwuID(sharedPreferences.getString("swuID", ""));
        setNavigationViewHeader();
    }

    private void setNavigationViewHeader() {
        if (totalInfo.getName().equals("")) {
            nameTextView.setOnClickListener(this);
            Toast.makeText(this, R.string.not_logged_in, Toast.LENGTH_SHORT).show();
        } else {
            /*对侧边栏的姓名和学号进行配置*/
            swuIDTextView.setText(totalInfo.getSwuID());
            nameTextView.setText(totalInfo.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            return true;
        }
        if(id==R.menu.main)
        {
            Log.d("MainActivity","click_main");
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_main) {
            toolbar.setTitle(R.string.main_page_title);
            fragmentControl.fragmentSelection(id);
            fragmentPosition = id;
        } else
            if (id == R.id.nav_grades) {
                fragmentControl.fragmentSelection(id);
                toolbar.setTitle(R.string.grades_title);
                fragmentPosition = id;
            } else
                if (id == R.id.nav_schedule) {
                    fragmentControl.fragmentSelection(id);
                    toolbar.setTitle(R.string.schedule_title);
                    fragmentPosition = id;

                } else
                    if (id == R.id.nav_study_materials) {
                        fragmentControl.fragmentSelection(id);
                        toolbar.setTitle(R.string.study_materials_title);
                        fragmentPosition = id;
                    } else
                        if (id == R.id.nav_library) {
                            fragmentControl.fragmentSelection(id);
                            toolbar.setTitle(R.string.library_title);
                            fragmentPosition = id;
                        } else
                            if (id == R.id.nav_charge) {
                                fragmentControl.fragmentSelection(id);
                                toolbar.setTitle(R.string.charge_title);
                                fragmentPosition = id;
                            } else
                                if (id == R.id.nav_find_lost) {
                                    fragmentControl.fragmentSelection(id);
                                    toolbar.setTitle(R.string.find_lost_title);
                                    fragmentPosition = id;
                                } else
                                    if (id == R.id.nav_share) {

                                    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*阻止活动被销毁*/
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*点击退出按钮*/
            case R.id.login_out:

            /*退出确认框*/
                final AlertDialog.Builder dialogsQuit;
                dialogsQuit = new AlertDialog.Builder(MainActivity.this);

                dialogsQuit.setMessage("确认退出");
                dialogsQuit.setNegativeButton(
                        "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                dialogsQuit.setPositiveButton(
                        "确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                    /*确认退出,清除保存的用户信息,并退出应用*/
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                System.exit(0);
                            }
                        });
            /*显示警告框*/
                dialogsQuit.show();
                break;
            case R.id.name:
//             开启登陆活动,并要求获得回复信息
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 1);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Mainactivity", "onResume");

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Log.d("Mainactivity", "onResumeFragments");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Mainactivity", "onStart6");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Mainactivity", "destory");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Mainactivity", "onSaveInstanceState");

    }

    public Toolbar getToolbar() {
        return toolbar;
    }


}