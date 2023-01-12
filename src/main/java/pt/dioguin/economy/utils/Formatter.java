package pt.dioguin.economy.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");

    public static final Formatter SIMPLE_FORMATTER = new Formatter();

    private final List<String> suffixes;

    public Formatter() {
        suffixes = Arrays.asList("", "K", "M", "B", "T", "Q", "QQ");
    }

    public Formatter(List<String> suffixes) {
        this.suffixes = suffixes;
    }

    public String formatNumber(double value) {
        boolean negative = value < 0;
        int index = 0;
        value = Math.abs(value);

        double tmp;
        while ((tmp = value / 1000) >= 1) {
            if (index + 1 == suffixes.size())
                break; //acabo. caso seus sufixos sejam "K, M, B" e vc der um numero tipo 1000000000000 (1T) ele vai virar 1000B
            value = tmp;
            ++index;
        }

        return (negative ? "-" : "") +  DECIMAL_FORMAT.format(value) + suffixes.get(index);
    }

    public double parseString(String value) throws Exception {
        try {
            return Double.parseDouble(value);
        } catch (Exception ignored) {
        }

        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.find())
            throw new Exception("Invalid format");

        double amount = Double.parseDouble(matcher.group(1));
        String suffix = matcher.group(2);

        int index = suffixes.indexOf(suffix.toUpperCase());

        return amount * Math.pow(1000, index);
    }
}
