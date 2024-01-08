package com.zch.common.utils.commons;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义头像生成
 * @author Poison02
 * @date 2024/1/8
 */
public class CommonAvatarUtils {

    public static String generateImg(String name) {
        int width = 100;
        int height = 100;
        int nameLength = name.length();
        String nameWritten;
        // 如果用户输入的姓名小于等于2个字符，不需要裁剪
        if (nameLength <= 2) {
            nameWritten = name;
        } else {
            // 如果用户输入的姓名大于等于3个字符，截取后面两位
            String first = StrUtil.sub(name, 0, 1);
            if (isChinese(first)) {
                // 截取倒数两位汉字
                nameWritten = name.substring(nameLength - 2);
            } else {
                // 截取前面的两个英文字母
                nameWritten = StrUtil.sub(name, 0, 1).toUpperCase();
            }
        }
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setBackground(getRandomColor());
        g2.clearRect(0, 0, width, height);
        g2.setPaint(Color.WHITE);
        Font font;
        // 两个字及以上
        if(nameWritten.length() >= 2) {
            font = new Font("微软雅黑", Font.BOLD, 30);
            g2.setFont(font);
            String firstWritten = StrUtil.sub(nameWritten, 0, 1);
            String secondWritten = StrUtil.sub(nameWritten, 0, 2);
            // 两个中文 如 言曌
            if (isChinese(firstWritten) && isChinese(secondWritten)) {
                g2.drawString(nameWritten, 20, 60);
            }
            // 首中次英 如 罗Q
            else if (isChinese(firstWritten) && !isChinese(secondWritten)) {
                g2.drawString(nameWritten, 27, 60);
                // 首英 如 AB
            } else {
                nameWritten = nameWritten.substring(0,1);
            }
        }
        // 一个字
        if(nameWritten.length() == 1) {
            // 中文
            if(isChinese(nameWritten)) {
                font = new Font("微软雅黑", Font.PLAIN, 50);
                g2.setFont(font);
                g2.drawString(nameWritten, 25, 70);
            } else {
                font = new Font("微软雅黑", Font.PLAIN, 55);
                g2.setFont(font);
                g2.drawString(nameWritten.toUpperCase(), 33, 67);
            }
        }
        System.out.println(bufferedImage);
        return ImgUtil.toBase64DataUri(bufferedImage, "jpg");
    }

    /**
     * 获取随机颜色
     * @return
     */
    private static Color getRandomColor() {
        String[] beautifulColors =
                new String[] {"114,101,230", "255,191,0", "0,162,174", "245,106,0", "24,144,255", "96,109,128"};
        String[] color = beautifulColors[RandomUtil.randomInt(beautifulColors.length)].split(StrUtil.COMMA);
        return new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
    }

    /**
     * 判断字符串是否是中文
     * @param str
     * @return
     */
    private static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static void main(String[] args) {
        String avatar = generateImg("郑才华");
        System.out.println(avatar);
    }

}
