package com.example.alperozge.avasistani.util;
import java.util.*;

public class FormatIndexer {
    private Locale locale;

    public FormatIndexer() {

    }

    public FormatIndexer(Locale l) {
        this.locale = l;
    }

    public List<Integer> getFormatArgumentIndexes(String format, Object... args) {
        Formatter oldFormatter;
        if (locale != null) oldFormatter = new Formatter(locale);
        else oldFormatter = new Formatter();

        List<Integer> indexList = new ArrayList<>();



        String str = null;
        try {
            oldFormatter.format(format);
        } catch (MissingFormatArgumentException e) {
            str = oldFormatter.toString();
        }
        if ((null!= str) && !Objects.deepEquals(str,format)) {
            indexList.add(str.length());
        } else { // means that 'format' string has no argument placeholders.
            return indexList;
        }
        if (args != null) {
            for (int i = 0 ; i <args.length ; ++i) {

                Object copiedArgs[] = Arrays.copyOf(args,i+1);
                Formatter formatter;
                if (locale != null) formatter = new Formatter(locale);
                else formatter = new Formatter();

                try {

                    formatter.format(format,copiedArgs);

                } catch (MissingFormatArgumentException e) {
                    indexList.add(formatter.toString().length());
                }
            }

        }

        return indexList;
    }
}

