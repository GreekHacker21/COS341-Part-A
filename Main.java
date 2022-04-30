public class Main {
    
    public static void main(String[] args) {
        Lexer lex = new Lexer("test.txt");
        Token result = lex.run();
        if(result.value.equals("LEXICAL ERROR")||result.value.equals("File not found.")){
            return;
        }
        /*
        String test = "\n1";
        if((test.substring(0, 1).equals("\n"))){
            System.out.println("YEEET");
        }
        */
    }

}