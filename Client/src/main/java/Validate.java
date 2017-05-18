import java.util.regex.*;

public class Validate {

    private static final String key = "ctx";

    public boolean validEmail(String email) {
        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[._A-Za-z0-9]{1,20}@[A-Za-z0-9-]{1,20}.[A-Za-z]{2,3}$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean validName(String s) {

        Pattern pattern;
        Matcher matcher;

        final String NAME_PATTERN = "^[A-Za-z]{1,20}$";

        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(s);

        return matcher.matches();
    }

    public boolean validPassword(String s) {

        Pattern pattern;
        Matcher matcher;

        final String PASS_PATTERN = "^[._A-Za-z0-9]{6,20}$";

        pattern = Pattern.compile(PASS_PATTERN);
        matcher = pattern.matcher(s);

        return matcher.matches();
    }

    public boolean validUsername(String s) {

        // if (se deja afla in baza de date) return false;

        Pattern pattern;
        Matcher matcher;

        final String UNAME_PATTERN = "^[._A-Za-z0-9]{1,20}$";

        pattern = Pattern.compile(UNAME_PATTERN);
        matcher = pattern.matcher(s);

        return matcher.matches();
    }

    public static String vigenereEncryption(String s) {
        String encriptedPassword = "";
        for (int i = 0, j = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z')
                encriptedPassword += (char)((c + key.charAt(j) - 2 * 'a') % 26 + 'a');
            if (c >= 'A' && c <= 'Z')
                encriptedPassword += (char)((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
            if (c >= '0' && c <= '9')
                encriptedPassword += (char)(c + key.charAt(j));
            else
                continue;

            j = ++j % key.length();
        }
        return encriptedPassword;
    }

}
