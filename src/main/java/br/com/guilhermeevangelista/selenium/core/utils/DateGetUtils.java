package br.com.guilhermeevangelista.selenium.core.utils;

import br.com.guilhermeevangelista.selenium.core.utils.enums.DateFormatUtils;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class DateGetUtils {

    private static final Random random = new Random();

    public static String getDate(DateFormatUtils pattern, boolean isUTC){
        TimeZone tz = TimeZone.getTimeZone(isUTC? ZoneId.of("UTC") : ZoneId.systemDefault());
        DateFormat df = new SimpleDateFormat(pattern.toString());
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static String getDate(DateFormatUtils pattern){
        TimeZone tz = TimeZone.getTimeZone(ZoneId.systemDefault());
        DateFormat df = new SimpleDateFormat(pattern.toString());
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static String getDate(boolean isPast, DateFormatUtils pattern){
        TimeZone tz = TimeZone.getTimeZone(ZoneId.systemDefault());
        DateFormat df = new SimpleDateFormat(pattern.toString());
        df.setTimeZone(tz);
        return df.format(new Date(isPast?  System.currentTimeMillis() - (configurarTempoAleatorio(2)): System.currentTimeMillis() + configurarTempoAleatorio(2)));
    }

    public static String getDate(boolean isPast, DateFormatUtils pattern, boolean isUTC){
        TimeZone tz = TimeZone.getTimeZone(isUTC? ZoneId.of("UTC") : ZoneId.systemDefault());
        DateFormat df = new SimpleDateFormat(pattern.toString());
        df.setTimeZone(tz);
        return df.format(new Date(isPast? System.currentTimeMillis() - configurarTempoAleatorio(2): System.currentTimeMillis() + configurarTempoAleatorio(2)));
    }

    private static long configurarTempoAleatorio(double anosMaximos){
        return (long) (random.nextFloat() * (31557600000L * anosMaximos));
    }

    public static String gerarDataNascimento(DateFormatUtils pattern, int idadeMaxima, int idadeMinima){
        TimeZone tz = TimeZone.getTimeZone(ZoneId.systemDefault());
        DateFormat df = new SimpleDateFormat(pattern.toString());
        df.setTimeZone(tz);
        return df.format(new Date(System.currentTimeMillis() - quantidadeAnosEmMS(idadeMinima) - configurarTempoAleatorio(idadeMaxima-idadeMinima)));
    }

    private static long quantidadeAnosEmMS(int anos){
        return anos * 31536000000L;
    }

    @Test
    public void testarHora(){
        DateFormatUtils[] enums = new DateFormatUtils[]{
                DateFormatUtils.DD_MM_YY, DateFormatUtils.DD_MM_YYYY, DateFormatUtils.MM_DD_YY,
                DateFormatUtils.MM_DD_YYYY, DateFormatUtils.YYYY_MM_DD, DateFormatUtils.YYYY_MM_DD_HH_MM_SS,
                DateFormatUtils.YYYY_MM_DD_HH_MM_SS_SSS, DateFormatUtils.YYYY_MM_DD_HH_MM_SS_SSSZ, DateFormatUtils.YYYY_MM_DD_T_HH_MMZ,
                DateFormatUtils.EEEEE_DD_MMMMM_YYYY, DateFormatUtils.DD_MM_YYYY_BARRA
        };

        System.out.println("==================PASSADO=====================");
        System.out.println("--------------SEM O PARAM UTC-----------------");
        for (DateFormatUtils dateFormat: enums) System.out.println(getDate(true, dateFormat));
        System.out.println("------------COM O PARAM UTC TRUE--------------");
        for (DateFormatUtils dateFormat: enums) System.out.println(getDate(true, dateFormat, true));
        System.out.println("------------COM O PARAM UTC FALSE--------------");
        for (DateFormatUtils dateFormat: enums) System.out.println(getDate(true, dateFormat, false));

        System.out.println("==================FUTURO=====================");
        System.out.println("--------------SEM O PARAM UTC-----------------");
        for (DateFormatUtils dateFormat: enums) System.out.println(getDate(false, dateFormat));

        System.out.println("------------COM O PARAM UTC TRUE--------------");
        for (DateFormatUtils dateFormat: enums) System.out.println(getDate(false, dateFormat, true));

        System.out.println("------------COM O PARAM UTC FALSE--------------");
        for (DateFormatUtils dateFormat: enums) System.out.println(getDate(false, dateFormat, false));

        System.out.println("==================ATUAL=====================");
        System.out.println("--------------SEM O PARAM UTC-----------------");
        for (DateFormatUtils dateFormat: enums) System.out.println(getDate(dateFormat));

        System.out.println("------------COM O PARAM UTC TRUE--------------");
        for (DateFormatUtils dateFormat: enums) System.out.println(getDate(dateFormat, true));

        System.out.println("------------COM O PARAM UTC FALSE--------------");
        for (DateFormatUtils dateFormat: enums) System.out.println(getDate(dateFormat, false));
    }

}
