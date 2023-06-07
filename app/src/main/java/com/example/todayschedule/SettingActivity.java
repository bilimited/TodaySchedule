package com.example.todayschedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;




import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.todayschedule.bean.Image;
import com.example.todayschedule.bean.Portrait;
import com.example.todayschedule.bean.User_Info;
import com.example.todayschedule.tool.Base64Coder;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 *  TODO:两个Spinner显示不正常
 */

public class SettingActivity extends AppCompatActivity {
    private ImageView avatarImageView;
    private EditText nameEditText;
    private EditText realNameEditText;
    private RadioGroup genderRadioGroup;
    private SeekBar ageSeekBar;
    private TextView tvprogress;
    private Switch privacySwitch;
    private static final int PERMISSIONS_REQUEST = 100;
    private Switch switchMemoryAccess;
    private Spinner mProvinceSpinner;
    private Spinner mUniversitySpinner;
    private List<String> mProvinceList;
    private String selectedProvince;
    private String selectedUniverse;
    private User_Info user_info;

    Uri sel_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        avatarImageView = findViewById(R.id.avatar_image_view);
        nameEditText = findViewById(R.id.name_edit_text);
        realNameEditText = findViewById(R.id.edit_realName);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        ageSeekBar = findViewById(R.id.age_seek_bar);
        tvprogress = findViewById(R.id.tvProgress);
        mUniversitySpinner = findViewById(R.id.university_spinner);
        mProvinceList = Arrays.asList("北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆", "台湾", "香港", "澳门");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);

        mProvinceSpinner = findViewById(R.id.province_spinner);
        /*
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this,
                R.array.province_array, android.R.layout.simple_spinner_item);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(provinceAdapter);*/

        // 添加代码以在ImageView上设置侦听器
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, TodaySchedule.REQUEST_SELECT_PIC);
            }
        });
        // 添加代码以在SeekBar上设置侦听器
        ageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 在TextView中更新当前进度

                // 更新 TextView 的文本
                tvprogress.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        TextView termsButton = findViewById(R.id.terms_button);
        termsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开条款和条件页面
                Intent intent = new Intent(SettingActivity.this, AgreementActivity.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> mProvinceAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mProvinceList);
        mProvinceSpinner.setAdapter(mProvinceAdapter);
        mProvinceSpinner.setSelection(0);
        mProvinceSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedProvince = parent.getItemAtPosition(position).toString();
                        updateUniversitySpinner(selectedProvince);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // do nothing
                    }
                }
        );

        Button submit = findViewById(R.id.submit_settings);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交更改
                String base64 = Base64Coder.getBase64FromFile(SettingActivity.this,sel_uri);
                if(base64!=null){
                    Toast.makeText(SettingActivity.this, "Base64 length:"+base64.length(), Toast.LENGTH_SHORT).show();
                }
                if(sel_uri!=null){
                    Portrait image = new Portrait();
                    image.setBase64(base64);
                    image.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e!=null){
                                Toast.makeText(SettingActivity.this, "图片上传失败:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }else {
                                save(image.getObjectId());
                            }
                        }
                    });
                }else {
                    save(null);
                }
            }
        });
        BmobQuery<User_Info> bmobQuery = new BmobQuery<>();

        bmobQuery.addWhereEqualTo("userID",TodaySchedule.UserID);

        bmobQuery.findObjects(new FindListener<User_Info>() {
            @Override
            public void done(List<User_Info> list, BmobException e) {
                if(e==null&&!list.isEmpty()){
                    user_info=list.get(0);
                    init();
                }
            }
        });
    }

    private void updateUniversitySpinner(String selectedProvince) {
        List<String> universities = getUniversitiesForProvince(selectedProvince); // 根据省份名获取大学列表
        ArrayAdapter<String> universityAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, universities);
        mUniversitySpinner.setAdapter(universityAdapter);
        mUniversitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUniverse = universities.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private List<String> getUniversitiesForProvince(String provinceName) {
        Map<String, List<String>> universitiesMap = new HashMap<>(); // 硬编码的哈希映射
        // 增加省份对应的大学列表
        universitiesMap.put("河南", Arrays.asList("河南大学", "开封大学", "河南财经政法大学", "河南工程学院", "河南工业大学", "河南警察学院", "河南科技大学", "河南理工大学", "河南牧业经济学院", "河南农业大学", "河南师范大学", "河南中医药大学", "洛阳师范学院", "南阳师范学院", "平顶山学院", "商丘师范学院", "新乡医学院", "新乡学院", "信阳师范学院", "许昌学院", "郑州航空工业管理学院", "郑州工程技术学院", "郑州工业应用技术学院", "郑州华信学院", "郑州科技学院", "郑州轻工业学院", "郑州师范学院"));
        universitiesMap.put("北京", Arrays.asList("清华大学", "北京大学", "中国人民大学","北京电影学院", "北京电子科技学院", "北京服装学院", "北京工商大学", "北京工业大学", "北京航空航天大学", "北京化工大学", "北京建筑大学", "北京交通大学", "北京科技大学", "北京理工大学", "北京林业大学", "北京师范大学", "北京体育大学", "北京外国语大学", "北京舞蹈学院", "北京信息科技大学", "北京印刷学院", "北京语言大学", "北京中医药大学", "对外经济贸易大学", "首都经济贸易大学", "首都师范大学", "中国农业大学", "中国地质大学（北京）", "中国传媒大学", "中国矿业大学（北京）", "中国劳动关系学院", "中国美术学院", "中国民航大学", "中国农业科学院", "中国青年政治学院", "中国人民公安大学", "中国人民大学", "中国石油大学（北京）", "中国戏曲学院", "中国音乐学院", "中华女子学院"));
        universitiesMap.put("天津", Arrays.asList("天津大学", "南开大学", "天津科技大学", "中国民航大学", "天津理工大学", "天津工业大学", "天津农学院", "天津医科大学", "天津中医药大学", "天津师范大学", "天津外国语大学", "天津财经大学", "天津商业大学", "天津体育学院", "天津音乐学院", "天津美术学院", "天津职业技术师范大学"));
        universitiesMap.put("安徽", Arrays.asList("安徽农业大学", "安徽工程大学", "安徽工业大学", "安徽建筑大学", "安徽理工大学", "安徽师范大学", "安徽医科大学", "安徽中医药大学", "合肥工业大学", "合肥学院", "皖南医学院", "中国科学技术大学"));
        universitiesMap.put("福建", Arrays.asList("福建师范大学", "福建医科大学", "福建中医药大学", "福州大学", "华侨大学", "集美大学", "闽南师范大学", "闽江学院", "厦门大学", "仰恩大学"));
        universitiesMap.put("甘肃", Arrays.asList("甘肃中医药大学", "兰州财经大学", "兰州大学", "兰州交通大学", "兰州理工大学", "兰州文理学院", "西北师范大学", "西北民族大学", "甘肃农业大学"));
        universitiesMap.put("广东", Arrays.asList("五邑大学", "东莞理工学院", "东莞南博职业技术学院", "佛山科学技术学院", "广东白云学院", "广东财经大学", "广东海洋大学", "广东技术师范大学", "广东科技学院", "广东理工学院", "广东南方职业学院", "广东商学院", "广东省外语艺术职业学院", "广东石油化工学院", "广东水利电力职业技术学院", "广东医科大学", "广东以色列理工学院", "广东岭南职业技术学院", "广东工业大学", "广州大学", "广州大学华软软件学院", "广州美术学院", "广州商学院", "广州体育学院", "广州医科大学", "广州中医药大学", "华南农业大学", "华南理工大学", "暨南大学", "南方医科大学", "南方科技大学", "南方学院", "深圳技术大学", "深圳大学", "深圳职业技术学院", "仲恺农业工程学院"));
        universitiesMap.put("广西", Arrays.asList("广西艺术学院", "广西大学", "广西交通职业技术学院", "广西科技大学", "广西师范大学", "广西中医药大学", "桂林电子科技大学", "桂林航天工业学院", "桂林理工大学", "桂林旅游学院", "桂林师范高等专科学校", "桂林医学院", "梧州学院", "玉林师范学院"));
        universitiesMap.put("贵州", Arrays.asList("贵阳中医学院", "贵阳学院", "贵阳医学院", "贵州财经大学", "贵州大学", "贵州工程应用技术学院", "贵州理工学院", "贵州民族大学", "贵州师范大学", "遵义医学院", "遵义师范学院"));
        universitiesMap.put("海南", Arrays.asList("海南大学", "海南热带海洋学院"));
        universitiesMap.put("河北", Arrays.asList("保定学院", "河北工程大学", "河北工业大学", "河北师范大学", "河北体育学院", "河北医科大学", "河北农业大学", "华北电力大学", "华北科技学院", "华北水利水电大学", "廊坊师范学院", "燕山大学", "河北地质大学", "唐山师范学院"));
        universitiesMap.put("山西", Arrays.asList("太原理工大学", "山西大学", "中北大学", "山西医科大学", "山西师范大学", "山西财经大学", "山西农业大学", "长治医学院", "晋中学院", "运城学院", "忻州师范学院", "吕梁学院", "太原学院", "山西传媒学院", "山西工程技术学院", "山西应用科技学院", "山西大同大学", "晋南学院"));
        universitiesMap.put("内蒙古", Arrays.asList("内蒙古大学", "内蒙古工业大学", "内蒙古农业大学", "内蒙古师范大学", "内蒙古民族大学", "内蒙古财经大学", "赤峰学院", "呼伦贝尔学院", "满洲里俄语师范学院", "集宁师范学院", "鄂尔多斯应用技术学院", "呼和浩特民族学院", "阿拉善职业技术学院", "内蒙古建筑职业技术学院", "内蒙古电子信息职业技术学院", "内蒙古机电职业技术学院", "包头钢铁职业技术学院"));
        universitiesMap.put("辽宁", Arrays.asList("大连理工大学", "辽宁大学", "东北大学", "大连海事大学", "辽宁工程技术大学", "沈阳工业大学", "大连工业大学", "沈阳航空航天大学", "沈阳建筑大学", "辽宁石油化工大学", "大连交通大学", "大连海洋大学", "沈阳化工大学", "辽宁科技大学", "鲁迅美术学院", "中国医科大学", "辽宁师范大学", "辽宁财贸学院", "东北财经大学", "大连民族大学", "辽宁警察学院", "沈阳体育学院", "沈阳音乐学院", "鞍山师范学院", "抚顺师范学院", "锦州医科大学", "阜新高等专科学校", "辽宁中医药大学", "辽宁传媒学院", "沈阳大学", "沈阳师范大学"));
        universitiesMap.put("吉林", Arrays.asList("吉林大学", "延边大学", "长春中医药大学", "长春大学", "长春理工大学", "吉林农业大学", "东北电力大学", "长春工程学院", "吉林师范大学", "吉林财经大学", "吉林工商学院", "吉林建筑大学", "长春师范大学", "吉林化工学院", "吉林艺术学院"));
        universitiesMap.put("黑龙江", Arrays.asList("哈尔滨工业大学", "哈尔滨工程大学", "东北农业大学", "黑龙江大学", "哈尔滨医科大学", "黑龙江八一农垦大学", "哈尔滨商业大学", "哈尔滨理工大学", "大庆师范学院", "绥化学院", "黑龙江科技大学", "齐齐哈尔大学", "牡丹江医学院", "东北石油大学", "佳木斯大学", "黑龙江中医药大学", "哈尔滨师范大学", "哈尔滨学院", "黑龙江工业学院", "黑龙江东方学院", "齐齐哈尔医学院", "黑龙江工程学院"));
        universitiesMap.put("上海", Arrays.asList("复旦大学", "上海交通大学", "同济大学", "华东师范大学", "东华大学", "上海大学", "上海财经大学", "上海外国语大学", "上海理工大学", "上海海事大学", "上海中医药大学", "上海师范大学", "上海海洋大学", "上海电机学院", "上海对外经贸大学", "上海工程技术大学", "上海立信会计金融学院", "上海体育学院", "上海音乐学院"));
        universitiesMap.put("江苏", Arrays.asList("南京大学", "东南大学", "南京航空航天大学", "苏州大学", "江南大学", "南京农业大学", "中国矿业大学（徐州）", "河海大学", "江苏大学", "南京师范大学", "南京理工大学", "南京邮电大学", "南通大学", "徐州医科大学", "南京工业大学", "盐城工学院", "南京审计大学", "南京信息工程大学", "淮阴师范学院", "常州大学", "泰州学院", "扬州大学", "南京森林警察学院", "南京医科大学", "南京中医药大学", "常熟理工学院", "江苏科技大学", "南京艺术学院", "苏州科技大学", "南京财经大学", "南京森林公安高等专科学校", "江苏警官学院"));
        universitiesMap.put("浙江", Arrays.asList("浙江大学", "杭州电子科技大学", "浙江工业大学", "浙江师范大学", "宁波大学", "温州医科大学", "浙江理工大学", "浙江海洋大学", "浙江工商大学", "中国美术学院", "浙江农林大学", "浙江中医药大学", "浙江传媒学院", "嘉兴学院", "台州学院", "湖州师范学院", "丽水学院", "浙江外国语学院", "浙江财经大学", "浙江科技学院", "绍兴文理学院", "衢州学院"));
        universitiesMap.put("江西", Arrays.asList("南昌大学", "华东交通大学", "江西财经大学", "江西农业大学", "江西师范大学", "赣南医学院", "江西理工大学", "景德镇陶瓷学院", "南昌工程学院", "江西中医药大学", "赣南师范大学", "宜春学院", "九江学院", "上饶师范学院", "井冈山大学", "江西科技师范大学", "东华理工大学"));
        universitiesMap.put("山东", Arrays.asList("山东大学", "中国海洋大学", "青岛大学", "山东科技大学", "聊城大学", "山东师范大学", "临沂大学", "烟台大学", "曲阜师范大学", "青岛理工大学", "燕山大学（青岛校区）", "济宁医学院", "山东建筑大学", "山东农业大学", "鲁东大学", "齐鲁工业大学", "山东中医药大学"));
        universitiesMap.put("湖北", Arrays.asList("华中科技大学", "武汉大学", "中国地质大学（武汉）", "中南财经政法大学", "华中师范大学", "华中农业大学", "湖北大学", "三峡大学", "武汉纺织大学", "江汉大学", "荆楚理工学院", "武汉工程大学", "鄂州职业大学", "湖北科技大学", "黄冈师范学院", "襄阳职业技术学院"));
        universitiesMap.put("湖南", Arrays.asList("中南大学", "湖南大学", "湖南师范大学", "长沙理工大学", "湖南工业大学", "湘潭大学", "湖南科技大学", "南华大学", "湖南农业大学", "湖南中医药大学", "湖南理工学院", "怀化学院", "衡阳师范学院", "邵阳学院", "永州学院", "长沙学院", "湖南工程学院", "湖南城市学院", "湖南人文科技学院"));
        universitiesMap.put("重庆", Arrays.asList("重庆大学", "西南大学", "重庆师范大学", "重庆邮电大学", "重庆医科大学", "西南政法大学", "重庆交通大学", "四川美术学院（重庆校区）", "重庆科技学院", "重庆理工大学", "重庆工程学院", "重庆文理学院", "重庆三峡学院", "四川外语学院（重庆南方翻译学院）"));
        universitiesMap.put("四川", Arrays.asList("四川大学", "电子科技大学", "西南财经大学", "四川师范大学", "成都理工大学", "西南科技大学", "西华大学", "成都中医药大学", "西南民族大学", "成都电子科技大学", "四川农业大学", "四川理工学院", "乐山师范学院", "内江师范学院", "攀枝花学院", "绵阳师范学院", "西昌学院", "川北医学院", "成都信息工程大学", "成都医学院", "成都工业学院", "成都学院", "成都农业科技职业学院"));
        universitiesMap.put("云南", Arrays.asList("云南大学", "昆明理工大学", "大理大学", "云南农业大学", "云南师范大学", "昆明医科大学", "西南林业大学", "云南财经大学", "云南民族大学", "红河学院", "曲靖师范学院", "玉溪师范学院", "楚雄师范学院", "文山学院", "保山学院", "昆明学院", "昭通学院"));
        universitiesMap.put("陕西", Arrays.asList("西北大学", "西安交通大学", "陕西师范大学", "延安大学", "长安大学", "西北工业大学", "陕西科技大学", "西安电子科技大学", "咸阳师范学院", "安康学院", "商洛学院", "西安石油大学", "西安理工大学", "陕西国际商贸学院", "西安建筑科技大学", "西安科技大学", "西安外国语大学", "西北农林科技大学", "陕西中医药大学", "陕西理工学院", "陕西文理学院"));
        universitiesMap.put("西藏", Arrays.asList("西藏大学"));
        universitiesMap.put("宁夏", Arrays.asList("宁夏大学"));
        universitiesMap.put("青海", Arrays.asList("青海大学"));
        universitiesMap.put("新疆", Arrays.asList("新疆大学", "新疆农业大学", "新疆医科大学", "新疆师范大学", "昌吉学院", "塔里木大学", "石河子大学", "新疆财经大学"));
        universitiesMap.put("香港", Arrays.asList("香港大学", "香港中文大学", "香港科技大学", "香港理工大学", "香港浸会大学", "香港城市大学", "香港岭南大学", "香港树仁大学", "香港恒生大学"));
        universitiesMap.put("澳门", Arrays.asList("澳门大学", "澳门科技大学"));
        universitiesMap.put("台湾", Arrays.asList("国立台湾大学", "国立清华大学", "国立交通大学", "国立中兴大学", "国立成功大学", "国立中山大学", "国立政治大学", "国立中央大学", "国立暨南国际大学", "国立彰化师范大学", "国立高雄师范大学", "国立嘉义大学", "中华大学", "国立台湾艺术大学", "国立台北大学", "国立宜蓝大学", "国立空中大学", "淡江大学", "辅仁大学", "真理大学", "世新大学", "东吴大学", "铭传大学", "华梵大学", "长庚大学", "元智大学", "中原大学"));
        // 省份名对应的大学列表若存在，返回该列表；否则返回空列表。
        return universitiesMap.get(provinceName);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TodaySchedule.REQUEST_SELECT_PIC) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                sel_uri = uri;
                avatarImageView.setImageURI(uri);
                //Base64Coder.getBase64FromFile(this,uri);
            }
        }
    }

    private void init(){
        if(user_info==null){
            return;
        }
        nameEditText.setText(user_info.getNickName());
        realNameEditText.setText(user_info.getRealName());
        ageSeekBar.setProgress(Integer.valueOf(user_info.getAge()));
        Base64Coder.LoadProtrait(this,user_info.getPortraitID(),avatarImageView);
        //TODO:还有一些剩下的
        if(user_info.getGender()==1){
            ((RadioButton) genderRadioGroup.getChildAt(1)).setChecked(true);
        }
    }

    private void save(String protraitID) {
        String nickname = nameEditText.getText().toString();
        String realname = realNameEditText.getText().toString();
        String prov = selectedProvince;
        String univ = selectedUniverse;

        String age = String.valueOf(ageSeekBar.getProgress());
        int gender = 0;
        if (((RadioButton) genderRadioGroup.getChildAt(1)).isChecked()) {
            gender = 1;
        }
        User_Info new_user_info = new User_Info(nickname, realname, univ, age, prov, gender, TodaySchedule.UserID);
        if (protraitID != null && !protraitID.equals("")) {
            new_user_info.setPortraitID(protraitID);
        }

        if (user_info == null) {
            /*
            用户信息不存在
            */
            new_user_info.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e != null) {
                        Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SettingActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            });
        } else {
            /*
            用户信息存在
            */
            new_user_info.update(user_info.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e != null) {
                        Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SettingActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            });
        }
    }
}