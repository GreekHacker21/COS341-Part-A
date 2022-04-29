public class Main {
    
    public static void main(String[] args) {
        Lexer lex = new Lexer("test.txt");
        String result = lex.run();
        /*
        String test = "\n1";
        if((test.substring(0, 1).equals("\n"))){
            System.out.println("YEEET");
        }
        */
    }

}