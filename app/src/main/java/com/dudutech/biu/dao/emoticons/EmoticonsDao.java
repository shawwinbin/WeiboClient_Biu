package com.dudutech.biu.dao.emoticons;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dudutech.biu.R;
import com.dudutech.biu.global.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shaw on 2015/7/22.
 */
public class EmoticonsDao {

    public Map<String, String> emoticons = new LinkedHashMap<String, String>();
    public Map<String, Bitmap> bitmaps = new LinkedHashMap<String, Bitmap>();

    private static EmoticonsDao mInstance;


    public static EmoticonsDao getInstance(){

        if(mInstance==null){
            mInstance=new EmoticonsDao();
        }
        return mInstance;

    }


    private EmoticonsDao() {

        initEmoticonMap();
        bitmaps = getEmotionsTask(emoticons);
    }

    private LinkedHashMap<String, Bitmap> getEmotionsTask(Map<String, String> emotionMap) {
        List<String> index = new ArrayList<String>();
        index.addAll(emotionMap.keySet());
        LinkedHashMap<String, Bitmap> bitmapMap = new LinkedHashMap<String, Bitmap>();
        for (String str : index) {
            String name = emotionMap.get(str);
            AssetManager assetManager = MyApplication.getInstance().getAssets();
            InputStream inputStream;
            try {
                inputStream = assetManager.open(name);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    int size = (int) MyApplication.getInstance().getResources().getDimension(R.dimen.emoction_size);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                            size,
                            size,
                            true);
                    if (bitmap != scaledBitmap) {
                        bitmap.recycle();
                        bitmap = scaledBitmap;
                    }
                    bitmapMap.put(str, bitmap);
                }
            } catch (IOException ignored) {

            }
        }

        return bitmapMap;
    }

    private void initEmoticonMap() {
        emoticons.put("[挖鼻屎]", "kbsa_org.png");
        emoticons.put("[泪]", "sada_org.png");
        emoticons.put("[亲亲]", "qq_org.png");
        emoticons.put("[晕]", "dizzya_org.png");
        emoticons.put("[可爱]", "tza_org.png");
        emoticons.put("[花心]", "hsa_org.png");
        emoticons.put("[汗]", "han.png");
        emoticons.put("[衰]", "cry.png");
        emoticons.put("[偷笑]", "heia_org.png");
        emoticons.put("[打哈欠]", "k_org.png");
        emoticons.put("[睡觉]", "sleepa_org.png");
        emoticons.put("[哼]", "hatea_org.png");
        emoticons.put("[可怜]", "kl_org.png");
        emoticons.put("[右哼哼]", "yhh_org.png");
        emoticons.put("[酷]", "cool_org.png");
        emoticons.put("[生病]", "sb_org.png");
        emoticons.put("[馋嘴]", "cza_org.png");
        emoticons.put("[害羞]", "shamea_org.png");
        emoticons.put("[怒]", "angrya_org.png");
        emoticons.put("[闭嘴]", "bz_org.png");
        emoticons.put("[钱]", "money_org.png");
        emoticons.put("[嘻嘻]", "tootha_org.png");
        emoticons.put("[左哼哼]", "zhh_org.png");
        emoticons.put("[委屈]", "wq_org.png");
        emoticons.put("[鄙视]", "bs2_org.png");
        emoticons.put("[吃惊]", "cj_org.png");
        emoticons.put("[吐]", "t_org.png");
        emoticons.put("[懒得理你]", "ldln_org.png");
        emoticons.put("[思考]", "sk_org.png");
        emoticons.put("[怒骂]", "nm_org.png");
        emoticons.put("[哈哈]", "laugh.png");
        emoticons.put("[抓狂]", "crazya_org.png");
        emoticons.put("[抱抱]", "bba_org.png");
        emoticons.put("[爱你]", "lovea_org.png");
        emoticons.put("[鼓掌]", "gza_org.png");
        emoticons.put("[悲伤]", "bs_org.png");
        emoticons.put("[嘘]", "x_org.png");
        emoticons.put("[呵呵]", "smilea_org.png");
        emoticons.put("[感冒]", "gm.png");
        emoticons.put("[黑线]", "hx.png");
        emoticons.put("[愤怒]", "face335.png");
        emoticons.put("[失望]", "face032.png");
        emoticons.put("[做鬼脸]", "face290.png");
        emoticons.put("[阴险]", "face105.png");
        emoticons.put("[困]", "face059.png");
        emoticons.put("[拜拜]", "face062.png");
        emoticons.put("[疑问]", "face055.png");

        emoticons.put("[赞]", "face329.png");
        emoticons.put("[心]", "hearta_org.png");
        emoticons.put("[伤心]", "unheart.png");
        emoticons.put("[囧]", "j_org.png");
        emoticons.put("[奥特曼]", "otm_org.png");
        emoticons.put("[蜡烛]", "lazu_org.png");
        emoticons.put("[蛋糕]", "cake.png");
        emoticons.put("[弱]", "sad_org.png");
        emoticons.put("[ok]", "ok_org.png");
        emoticons.put("[威武]", "vw_org.png");
        emoticons.put("[猪头]", "face281.png");
        emoticons.put("[月亮]", "face18.png");
        emoticons.put("[浮云]", "face229.png");
        emoticons.put("[咖啡]", "face74.png");
        emoticons.put("[爱心传递]", "face221.png");
        emoticons.put("[来]", "face277.png");

        emoticons.put("[熊猫]", "face002.png");
        emoticons.put("[帅]", "face94.png");
        emoticons.put("[不要]", "face274.png");
        emoticons.put("[熊猫]", "face002.png");


        emoticons.put("[笑哈哈]", "lxh_xiaohaha.png");
        emoticons.put("[好爱哦]", "lxh_haoaio.png");
        emoticons.put("[噢耶]", "lxh_oye.png");
        emoticons.put("[偷乐]", "lxh_toule.png");
        emoticons.put("[泪流满面]", "lxh_leiliumanmian.png");
        emoticons.put("[巨汗]", "lxh_juhan.png");
        emoticons.put("[抠鼻屎]", "lxh_koubishi.png");
        emoticons.put("[求关注]", "lxh_qiuguanzhu.png");
        emoticons.put("[好喜欢]", "lxh_haoxihuan.png");
        emoticons.put("[崩溃]", "lxh_bengkui.png");
        emoticons.put("[好囧]", "lxh_haojiong.png");
        emoticons.put("[震惊]", "lxh_zhenjing.png");
        emoticons.put("[别烦我]", "lxh_biefanwo.png");
        emoticons.put("[不好意思]", "lxh_buhaoyisi.png");
        emoticons.put("[羞嗒嗒]", "lxh_xiudada.png");
        emoticons.put("[得意地笑]", "lxh_deyidexiao.png");
        emoticons.put("[纠结]", "lxh_jiujie.png");
        emoticons.put("[给劲]", "lxh_feijin.png");
        emoticons.put("[悲催]", "lxh_beicui.png");
        emoticons.put("[甩甩手]", "lxh_shuaishuaishou.png");
        emoticons.put("[好棒]", "lxh_haobang.png");
        emoticons.put("[瞧瞧]", "lxh_qiaoqiao.png");
        emoticons.put("[不想上班]", "lxh_buxiangshangban.png");
        emoticons.put("[困死了]", "lxh_kunsile.png");
        emoticons.put("[许愿]", "lxh_xuyuan.png");
        emoticons.put("[丘比特]", "lxh_qiubite.png");
        emoticons.put("[有鸭梨]", "lxh_youyali.png");
        emoticons.put("[想一想]", "lxh_xiangyixiang.png");
        emoticons.put("[躁狂症]", "lxh_kuangzaozheng.png");
        emoticons.put("[转发]", "lxh_zhuanfa.png");
        emoticons.put("[互相膜拜]", "lxh_xianghumobai.png");
        emoticons.put("[雷锋]", "lxh_leifeng.png");
        emoticons.put("[杰克逊]", "lxh_jiekexun.png");
        emoticons.put("[玫瑰]", "lxh_meigui.png");
        emoticons.put("[hold住]", "lxh_holdzhu.png");
        emoticons.put("[群体围观]", "lxh_quntiweiguan.png");
        emoticons.put("[推荐]", "lxh_tuijian.png");
        emoticons.put("[赞啊]", "lxh_zana.png");
        emoticons.put("[被电]", "lxh_beidian.png");
        emoticons.put("[霹雳]", "lxh_pili.png");
    }
}
