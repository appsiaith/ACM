package view.components;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 14/11/2013
 * Time: 15:23
 */
public class AccentDialog {
    public static HashMap<Character, Character> circumflexes = new HashMap<Character, Character>();
    public static HashMap<Character, Character> acutes = new HashMap<Character, Character>();
    public static HashMap<Character, Character> umlauts = new HashMap<Character, Character>();
    public static HashMap<Character, Character> graves = new HashMap<Character, Character>();
    public static HashMap<Character, Character> macrons = new HashMap<Character, Character>();

    static {
        circumflexes.put('a', 'â');
        circumflexes.put('a', 'â');
        circumflexes.put('e', 'ê');
        circumflexes.put('i', 'î');
        circumflexes.put('o', 'ô');
        circumflexes.put('u', 'û');
        circumflexes.put('w', 'ŵ');
        circumflexes.put('y', 'ŷ');

        umlauts.put('a', 'ä');
        umlauts.put('e', 'ë');
        umlauts.put('i', 'ï');
        umlauts.put('o', 'ö');
        umlauts.put('u', 'ü');
        umlauts.put('w', 'ẅ');
        umlauts.put('y', 'ÿ');

        acutes.put('a', 'á');
        acutes.put('e', 'é');
        acutes.put('i', 'í');
        acutes.put('o', 'ó');
        acutes.put('u', 'ú');
        acutes.put('w', 'ẃ');
        acutes.put('y', 'ý');

        graves.put('a', 'à');
        graves.put('e', 'è');
        graves.put('i', 'ì');
        graves.put('o', 'ò');
        graves.put('u', 'ù');
        graves.put('w', 'ẁ');
        graves.put('y', 'ỳ');

        macrons.put('a', 'ā');
        macrons.put('e', 'ē');
        macrons.put('i', 'ī');
        macrons.put('o', 'ō');
        macrons.put('u', 'ū');
        macrons.put('w', 'w');
        macrons.put('y', 'ȳ');

        circumflexes.put('A', 'Â');
        circumflexes.put('A', 'Â');
        circumflexes.put('E', 'Ê');
        circumflexes.put('I', 'Î');
        circumflexes.put('O', 'Ô');
        circumflexes.put('U', 'Û');
        circumflexes.put('W', 'Ŵ');
        circumflexes.put('Y', 'Ŷ');

        umlauts.put('A', 'Ä');
        umlauts.put('E', 'Ë');
        umlauts.put('I', 'Ï');
        umlauts.put('O', 'Ö');
        umlauts.put('U', 'Ü');
        umlauts.put('W', 'Ẅ');
        umlauts.put('Y', 'Ÿ');

        acutes.put('A', 'Á');
        acutes.put('E', 'É');
        acutes.put('I', 'Í');
        acutes.put('O', 'Ó');
        acutes.put('U', 'Ú');
        acutes.put('W', 'Ẃ');
        acutes.put('Y', 'Ý');

        graves.put('A', 'À');
        graves.put('E', 'È');
        graves.put('I', 'Ì');
        graves.put('O', 'Ò');
        graves.put('U', 'Ù');
        graves.put('W', 'Ẁ');
        graves.put('Y', 'Ỳ');

        macrons.put('A', 'Ā');
        macrons.put('E', 'Ē');
        macrons.put('I', 'Ī');
        macrons.put('O', 'Ō');
        macrons.put('U', 'Ū');
        macrons.put('W', 'W');
        macrons.put('Y', 'Ȳ');
    }
}
