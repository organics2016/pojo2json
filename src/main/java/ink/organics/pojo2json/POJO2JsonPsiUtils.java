package ink.organics.pojo2json;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class POJO2JsonPsiUtils {


    public static String psiTextToString(String psiText) {
        return psiText.replace("\"", "");
    }

    public static List<String> arrayTextToList(String text) {

        text = StringUtils.deleteWhitespace(text);

        if (text.startsWith("{") &&
                text.endsWith("}") &&
                text.length() > 2) {

            return Arrays.stream(text.substring(1, text.length() - 1)
                    .replace("\"", "")
                    .split(","))
                    .collect(Collectors.toList());

        } else if (text.matches("^\"\\w+\"$")) {
            return List.of(text.replace("\"", ""));
        }

        return List.of();
    }

    public static List<String> docTextToList(String tags, String text) {

        if (!text.contains(tags)) {
            return List.of();
        }
        
        int start = text.indexOf(tags) + tags.length();
        int end = start;
        while (text.charAt(end) != '\n') {
            end++;
        }
        if (start == end) {
            return List.of();
        }

        return Arrays.stream(StringUtils.deleteWhitespace(text.substring(start, end + 1)).split(","))
                .collect(Collectors.toList());
    }
}
