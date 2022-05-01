public class Main {
    
    public static void main(String[] args) {
        Lexer lex = new Lexer("test.txt");
        Token result = lex.run();
        if(result.value.equals("LEXICAL ERROR")||result.value.equals("File not found.")){
           return;
        }
        lex.printTokens();

        System.out.println("\nAnother test file:\n");
        Lexer test = new Lexer("yingTest.txt");
        test.run();
        test.printTokens();
    }

}