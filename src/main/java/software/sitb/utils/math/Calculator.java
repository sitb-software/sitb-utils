package software.sitb.utils.math;

import software.sitb.utils.StringUtils;

import java.math.BigDecimal;

/**
 * 计算器
 * author 田尘殇(Sean sean.snow@live.com)
 * date 2015/7/5
 * time 11:34
 */
public class Calculator {


    /**
     * 加法
     *
     * @param addend 加数
     * @param augend 被加数
     * @return 计算结果
     */
    public static String add(String addend, String augend) {
        if (StringUtils.isEmpty(addend)) {
            addend = "0";
        }
        if (StringUtils.isEmpty(augend)) {
            augend = "0";
        }
        BigDecimal tmpAddend = new BigDecimal(addend);
        BigDecimal tmpAugend = new BigDecimal(augend);
        return tmpAddend.add(tmpAugend).toString();
    }

    /**
     * 加法
     *
     * @param addend 加数
     * @param augend 被加数
     * @return 计算结果
     */
    public static Integer add(String addend, Integer augend) {
        if (StringUtils.isEmpty(addend)) {
            addend = "0";
        }
        if (null == augend) {
            augend = 0;
        }

        return new BigDecimal(addend).add(new BigDecimal(augend)).intValue();
    }

    /**
     * 加法
     *
     * @param addend 加数
     * @param augend 被加数
     * @return 计算结果
     */
    public static Integer add(Integer addend, Integer augend) {
        if (null == addend)
            addend = 0;
        if (null == augend)
            augend = 0;
        return new BigDecimal(addend).add(new BigDecimal(augend)).intValue();
    }

    /**
     * 加法
     *
     * @param addend 加数
     * @param augend 被加数
     * @return 计算结果
     */
    public static Long add(Long addend, Integer augend) {
        if (null == addend) {
            addend = 0L;
        }
        if (null == augend) {
            augend = 0;
        }
        return new BigDecimal(addend).add(new BigDecimal(augend)).longValue();
    }

    /**
     * 加法
     *
     * @param addend 加数
     * @param augend 被加数
     * @return 计算结果
     */
    public static BigDecimal add(BigDecimal addend, BigDecimal augend) {
        if (null == addend) {
            addend = BigDecimal.ZERO;
        }
        if (null == augend) {
            augend = BigDecimal.ZERO;
        }
        return addend.add(augend);
    }

    /**
     * 减法计算
     *
     * @param subtrahend 减数
     * @param minuend    被减数
     * @return 计算结果
     */
    public static String subtract(String subtrahend, String minuend) {
        if (StringUtils.isEmpty(subtrahend)) {
            subtrahend = "0";
        }
        if (StringUtils.isEmpty(minuend)) {
            minuend = "0";
        }
        BigDecimal tmpSub = new BigDecimal(subtrahend);
        BigDecimal tmpMin = new BigDecimal(minuend);
        return tmpSub.subtract(tmpMin).toString();
    }

    /**
     * 乘法
     *
     * @param multiplier   乘数
     * @param multiplicand 被乘数
     * @return 计算结果
     */
    public static String multiply(String multiplier, String multiplicand) {
        if (StringUtils.isEmpty(multiplier)) {
            multiplier = "0";
        }
        if (StringUtils.isEmpty(multiplicand)) {
            multiplicand = "0";
        }
        BigDecimal tmpMultiplier = new BigDecimal(multiplier);
        BigDecimal tmpMultiplicand = new BigDecimal(multiplicand);
        return tmpMultiplier.multiply(tmpMultiplicand).toString();
    }

    /**
     * 乘法
     *
     * @param multiplier   乘数
     * @param multiplicand 被乘数
     * @param scale        保留位数
     * @return 计算结果
     */
    public static String multiply(String multiplier, String multiplicand, int scale) {
        return rounding(multiply(multiplier, multiplicand), scale);
    }

    /**
     * 除法,默认四舍五入方式为银行家模式
     *
     * @param divisor  除数
     * @param dividend 被除数
     * @return 计算结果
     */
    public static String divide(String divisor, String dividend) {
        return divide(divisor, dividend, 6, BigDecimal.ROUND_HALF_EVEN);
    }


    /**
     * 除法,默认四舍五入方式为银行家模式
     *
     * @param divisor  除数
     * @param dividend 被除数
     * @param scale    舍入位数
     * @return 字符串表示的除法结果
     */
    public static String divide(String divisor, String dividend, int scale) {
        return divide(divisor, dividend, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 除法
     *
     * @param divisor      除数
     * @param dividend     被除数
     * @param scale        舍入位数
     * @param roundingMode 四舍五入模式
     * @return 字符串表示的除法结果
     */
    public static String divide(String divisor, String dividend, int scale, int roundingMode) {
        if (StringUtils.isEmpty(divisor)) {
            divisor = "0";
        }
        if (StringUtils.isEmpty(dividend)) {
            dividend = "0";
        }
        BigDecimal tmpDivisor = new BigDecimal(divisor);
        BigDecimal tmpDividend = new BigDecimal(dividend);
        return tmpDivisor.divide(tmpDividend, scale, roundingMode).toString();
    }


    /**
     * 四舍五入计算
     *
     * @param roundNum     需要四舍五入的数
     * @param scale        小数点后保留位数
     * @param roundingMode 舍入模式
     * @return 计算结果
     */
    public static String rounding(String roundNum, int scale, int roundingMode) {
        if (StringUtils.isEmpty(roundNum)) {
            roundNum = "0";
        }
        BigDecimal tmp = new BigDecimal(roundNum);
        return tmp.setScale(scale, roundingMode).toString();
    }

    /**
     * 四舍五入计算
     *
     * @param roundNum 需要四舍五入的数
     * @param scale    小数点后保留位数
     * @return 计算结果
     */
    public static String rounding(String roundNum, int scale) {
        if (StringUtils.isEmpty(roundNum)) {
            roundNum = "0";
        }
        BigDecimal tmp = new BigDecimal(roundNum);
        return tmp.setScale(scale, BigDecimal.ROUND_HALF_EVEN).toString();
    }


    /**
     * 判断指定的数字是否等于0
     *
     * @param number 需要判断的数
     * @return 等于0返回true否则返回false
     */
    public static boolean equalsZero(String number) {
        if (StringUtils.isEmpty(number)) {
            number = "0";
        }
        return new BigDecimal(number).compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 判断指定的数字是否大于等于0
     *
     * @param number 需要判断的数
     * @return boolean 大于等于返回true,否则返回false
     */
    public static boolean gtEqZero(String number) {
        if (StringUtils.isEmpty(number)) {
            number = "0";
        }
        BigDecimal tmp = new BigDecimal(number);
        boolean result = false;
        switch (tmp.compareTo(BigDecimal.ZERO)) {
            case 0:
            case 1:
                result = true;
        }
        return result;
    }


}
