package link.dayang.tjutname.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import link.dayang.tjutname.R;
import link.dayang.tjutname.datasource.GetRawKeBiaoCallback;
import link.dayang.tjutname.datasource.KeBiaoDataSource;
import link.dayang.tjutname.datasource.entity.KeBiao;

public class HomeActivity extends AppCompatActivity {

    private final String[] terms = {"上半学期", "下半学期"};
    private final String[] grades = {"大一", "大二", "大三", "大四"};
    private RecyclerView recyclerView;
    private TextView btnGrade;
    private TextView btnImport;
    private TextView btnWeek;

    private AlertDialog dialogLogin;
    private AlertDialog dialogSetting;
    private AlertDialog dialogLoading;
    private AlertDialog dialogWeek;

    private EditText loginId;
    private EditText loginPassword;

    private NumberPicker pkGrade;
    private NumberPicker pkTerm;
    private DatePicker pkStartDate;
    private NumberPicker pkWeek;

    private String curId;
    private String curPassword;
    private int curWeek;
    private int curGrade;
    private int curTerm;
    private Date curStartDate;
    private TextView thMonth;
    private TextView th1;
    private TextView th2;
    private TextView th3;
    private TextView th4;
    private TextView th5;
    private TextView th6;
    private TextView th7;
    private Gson gson = new Gson();
    private KeBiaoAdapter adapter;

    public static String getStringFromSP(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void setStringToSP(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        thMonth = (TextView) findViewById(R.id.thMonth);
        th1 = (TextView) findViewById(R.id.th1);
        th2 = (TextView) findViewById(R.id.th2);
        th3 = (TextView) findViewById(R.id.th3);
        th4 = (TextView) findViewById(R.id.th4);
        th5 = (TextView) findViewById(R.id.th5);
        th6 = (TextView) findViewById(R.id.th6);
        th7 = (TextView) findViewById(R.id.th7);

        try {
            curStartDate = new Date(getStringFromSP(this, "startDate"));
            updateTh();
        } catch (Exception e) {
            curStartDate = new Date();
            updateTh();
        }

        curId = getStringFromSP(this, "id");
        curPassword = getStringFromSP(this, "password");

        if (curId.isEmpty() || curPassword.isEmpty()) {
            toast("未导入");
        }

        initDialogs();

        btnImport = (TextView) findViewById(R.id.btn_import);
        btnImport.setOnClickListener(v -> {
            toast("OK");
            dialogLogin.show();
        });

        btnWeek = (TextView) findViewById(R.id.btn_week);
        btnWeek.setOnClickListener(v -> {
            dialogWeek.show();

        });

        btnGrade = findViewById(R.id.btn_garde);
        btnGrade.setOnClickListener(v -> {
            dialogSetting.show();
        });

        recyclerView = findViewById(R.id.home_list);
        recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 7));
        adapter = new KeBiaoAdapter(HomeActivity.this);
        recyclerView.setAdapter(adapter);

        String kebiaoStr = getStringFromSP(this, "kebiao");
        if (!kebiaoStr.isEmpty()) {
            KeBiao keBiao = gson.fromJson(kebiaoStr, KeBiao.class);
            adapter.setKeBiao(keBiao);
            try {
                curWeek = Integer.parseInt(getStringFromSP(this, "week"));
                btnWeek.setText("第" + curWeek + "周");
                adapter.setWeek(curWeek);
                updateTh();
            } catch (Exception e) {
                adapter.setWeek(1);
                btnWeek.setText("第" + 1 + "周");
            }
        }
        try {
            curGrade = Integer.parseInt(getStringFromSP(this, "grade"));
            curTerm = Integer.parseInt(getStringFromSP(this, "term"));
            btnGrade.setText(grades[curGrade - 1] + "\n" + terms[curTerm - 1]);
        } catch (Exception e) {
            btnGrade.setText("未导入");
        }
    }

    private void initDialogs() {
        View loginRoot = getLayoutInflater().inflate(R.layout.dialog_import_login, null, false);
        loginId = loginRoot.findViewById(R.id.id);
        loginPassword = loginRoot.findViewById(R.id.password);

        dialogLogin = new AlertDialog.Builder(this)
                .setTitle("从教务系统导入")
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = loginId.getText().toString();
                        String password = loginPassword.getText().toString();
                        if (id.isEmpty() || password.isEmpty()) {
                            toast("账号或密码不能为空");
                            return;
                        }
                        curId = id;
                        setStringToSP(HomeActivity.this, "id", curId);

                        curPassword = password;
                        setStringToSP(HomeActivity.this, "password", curPassword);
//                        toast(id + " " + password);
                        dialogSetting.show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(loginRoot)
                .create();


        View settingRoot = getLayoutInflater().inflate(R.layout.dialog_import_setting, null, false);
        pkGrade = settingRoot.findViewById(R.id.pkGrade);
        pkGrade.setDisplayedValues(grades);
        pkGrade.setMinValue(1);
        pkGrade.setMaxValue(grades.length);
        pkGrade.setWrapSelectorWheel(false);
        pkGrade.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        pkTerm = settingRoot.findViewById(R.id.pkTerm);
        pkTerm.setDisplayedValues(terms);
        pkTerm.setMinValue(1);
        pkTerm.setMaxValue(terms.length);
        pkTerm.setWrapSelectorWheel(false);
        pkTerm.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        pkStartDate = settingRoot.findViewById(R.id.pkStartDate);
        pkStartDate.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        dialogSetting = new AlertDialog.Builder(this)
                .setTitle("请选择")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (curId.isEmpty() || curPassword.isEmpty()) {
                            toast("未导入");
                            dialog.dismiss();
                            return;
                        }
                        dialog.dismiss();
                        dialogLoading.show();

                        int grade = pkGrade.getValue();
                        curGrade = grade;
                        setStringToSP(HomeActivity.this, "grade", curGrade + "");

                        int term = pkTerm.getValue();
                        curTerm = term;
                        setStringToSP(HomeActivity.this, "term", curTerm + "");

                        btnGrade.setText(grades[curGrade - 1] + "\n" + terms[curTerm - 1]);

                        curWeek = 1;
                        btnWeek.setText("第" + curWeek + "周");
                        updateTh();

                        Date date = new Date(pkStartDate.getYear(), pkStartDate.getMonth(), pkStartDate.getDayOfMonth());
                        curStartDate = date;
                        setStringToSP(HomeActivity.this, "startDate", curStartDate.toString());

                        KeBiaoDataSource.getInstance().getKeBiao(curId, curPassword, grade, term, new GetRawKeBiaoCallback() {
                            @Override
                            public void onFinish(boolean success, KeBiao keBiao, int enrollDate) {
                                dialogLoading.dismiss();
                                if (!success) {
                                    toast("请求失败，请重试");
                                    return;
                                }
                                toast("请求成功");

                                setStringToSP(HomeActivity.this, "kebiao", gson.toJson(keBiao));

                                adapter.setKeBiao(keBiao);
                                adapter.setWeek(1);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(settingRoot)
                .create();

        dialogLoading = new AlertDialog.Builder(this)
                .setTitle("正在导入")
                .setView(R.layout.dialog_import_loading)
                .create();

        View weekRoot = getLayoutInflater().inflate(R.layout.dialog_week, null, false);

        pkWeek = weekRoot.findViewById(R.id.pkWeek);
        pkWeek.setMinValue(1);
        pkWeek.setMaxValue(20);
        pkWeek.setWrapSelectorWheel(false);
        pkWeek.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        dialogWeek = new AlertDialog.Builder(this)
                .setTitle("设置周次")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (curId.isEmpty() || curPassword.isEmpty()) {
                            toast("未导入");
                            dialog.dismiss();
                            return;
                        }
                        int weekInt = pkWeek.getValue();
                        curWeek = weekInt;
                        btnWeek.setText("第" + curWeek + "周");
                        setStringToSP(HomeActivity.this, "week", curWeek + "");

                        updateTh();
                        adapter.setWeek(weekInt);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(weekRoot)
                .create();

    }

    private void updateTh() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(curStartDate.getYear(), curStartDate.getMonth(), curStartDate.getDay());

        Log.v("dydy", calendar.get(Calendar.DAY_OF_MONTH) + "");


        calendar.add(Calendar.WEEK_OF_MONTH, curWeek-1);

        thMonth.setText((calendar.get(Calendar.MONTH) + 1) + "\n月");

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        th1.setText("周一\n" +calendar.get(Calendar.DAY_OF_MONTH) + "日");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        th2.setText("周二\n" +calendar.get(Calendar.DAY_OF_MONTH) + "日");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        th3.setText("周三\n" +calendar.get(Calendar.DAY_OF_MONTH) + "日");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        th4.setText("周四\n" +calendar.get(Calendar.DAY_OF_MONTH) + "日");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        th5.setText("周五\n" +calendar.get(Calendar.DAY_OF_MONTH) + "日");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        th6.setText("周六\n" +calendar.get(Calendar.DAY_OF_MONTH) + "日");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        th7.setText("周日\n" +calendar.get(Calendar.DAY_OF_MONTH) + "日");
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
