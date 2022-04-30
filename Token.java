public class Token {

    public String value, type;
    public Token next;

    Token(String v, String t){
        value = v;
        type = t;
        next = null;
    }

    Token(String v){
        value = v;
        next = null;
    }
}
