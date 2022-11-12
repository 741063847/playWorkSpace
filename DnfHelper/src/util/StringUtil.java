package util;

import java.awt.image.BufferedImage;
import java.security.MessageDigest;

public class StringUtil {

    /**
     * StringUtil
     */
    private StringUtil() {

    }

    /**
     * 
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param str
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static boolean isNotEmpty(String str) {
        boolean result = false;
        if (str != null && !"".equals(str)) {
            result = true;
        }

        return result;
    }

    /**
     * 
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param str
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static boolean isEmpty(String str) {
        boolean result = false;
        if (str == null || "".equals(str)) {
            result = true;
        }

        return result;
    }
    /**
     * 处理字符串
     *
     * @param s
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String stringParse(Object s) {
        if ("".equals(s) || s == null) {
            return "";
        } else {
            return s.toString();
        }
    }
    
    /**
     * 字符串MD5加密
     *
     * @param s
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static final String MD5(String s) {
        char []hexDigits = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char [] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 图片转字符串
     *
     * @param s
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String imgToStr(BufferedImage img) {
		StringBuffer str = new StringBuffer();
		for(int x=0;x<img.getWidth();x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				str.append(img.getRGB(x, y));
			}
		}
		return MD5(str.toString());
	}
}
