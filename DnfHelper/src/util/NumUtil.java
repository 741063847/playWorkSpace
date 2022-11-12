package util;

import java.text.NumberFormat;
import java.util.Random;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 19042742
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class NumUtil {

    private NumUtil() {
    }

    /**
     * 计算增长率
     *
     * @param a
     * @param b
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String calcIncreaseExponent(int a, int b) {
        String rate;
        if (0 == b) {
            rate = "-";
        } else {
            double ss = ((a - b) * 1.0) / b * 100;
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(2);
            rate = nf.format(ss) + "%";
        }
        return rate;
    }

    /**
     * 字符串转int
     *
     * @param str
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static int strToInt(String str) {
        if (StringUtil.isNotEmpty(str)) {
            int t = 0;
            try {
                t = Integer.parseInt(str);
            } catch (NumberFormatException e) {
            }
            return t;
        }
        return 0;
    }
    
    /**
	 * getRandomNum 生成min~max之前的随机整数
	 * 
	 * @param min 随机数下限
	 * @param max 随机数上限
	 */
	public static int getRandomNum(int max, int min) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}
}
