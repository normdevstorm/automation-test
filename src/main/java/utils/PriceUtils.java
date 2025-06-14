package utils;

public class PriceUtils {
    public static String formatPriceWithDots(String priceStr) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (int i = priceStr.length() - 1; i >= 0; i--) {
                sb.insert(0, priceStr.charAt(i));
                count++;
                if (count % 3 == 0 && i != 0) {
                    sb.insert(0, ".");
                }
            }
            return sb.toString();
        }
}
