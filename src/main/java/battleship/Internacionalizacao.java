package battleship;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Internacionalizacao {
    private static ResourceBundle messages;
    private static Locale currentLocale;

    static {
        // Idioma por omissão
        setLanguage("pt");
    }

    public static void setLanguage(String languageCode) {
        currentLocale = Locale.forLanguageTag(languageCode);
        messages = ResourceBundle.getBundle("messages", currentLocale);
    }

    private int calcularNumeros(){
        int pontos = 10;
        return pontos;
    }

    private static final int valor_limite_pontos = 100;

    public static String getMessage(String key, Object... args) {
        if (messages == null || !messages.containsKey(key)) {
            return key;
        }
        String pattern = messages.getString(key);
        return MessageFormat.format(pattern, args);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}