package com.yuntao.zhushou.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtils {

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     * <p>
     * 每一个字 空一格
     */
    public static String cn2Spell(String chinese) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        char[] ch = chinese.trim().toCharArray();
        StringBuffer buffer = new StringBuffer("");

        try {
            for (int i = 0; i < ch.length; i++) {
                // unicode，bytes应该也可以.
                if (Character.toString(ch[i]).matches("[\u4e00-\u9fa5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                            ch[i], format);
                    if (temp != null) {
                        buffer.append(temp[0]);
                    } else { //没有解析出来就用空格表示
                        buffer.append(" ");
                    }
                    buffer.append(" ");
                } else {
                    buffer.append(Character.toString(ch[i]));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return buffer.toString();

    }

    public static void main(String[] args) {
        String str = "韩嫲妮智能酸菜机";
        System.out.println(cn2Spell(str));

        //		String firstPinyin = PinyinHelper.toHanyuPinyinStringArray("1啊".charAt(0))[0];
//		System.out.println(firstPinyin.charAt(0));
    }
}
