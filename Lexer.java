import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private String fileName;
    public Token head = null, tail = null;
    public int position, line;

    Lexer(String fN) {
        fileName = fN;
    }

    public Token run() {

        String data = "";
        position = 0;
        line = 1;
        Pattern p;
        Matcher m;

        try {
            File myFile = new File(fileName);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }
            data = data.substring(0, data.length() - 1);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return new Token("File not found.");
        }
        while (position < data.length()) {

            String t = data.substring(position, position + 1);
            String regex = "-|[a-zA-Z0-9 {}(),:;\"<>=\n\t]";
            p = Pattern.compile(regex);
            m = p.matcher(t);

            if (m.find()) {
                if (t.equals("\n")) {
                    // System.out.println("Newline character");
                    line++;
                    position++;
                    continue;
                }

                if (t.equals(" ")) {
                    position++;
                    continue;
                }

                if (t.equals("\t")) {
                    position++;
                    continue;
                }

                switch (t) {
                    case "(":
                        position++;
                        addToken(new Token(t, "Left Parenthesis"));
                        continue;
                    case ")":
                        position++;
                        addToken(new Token(t, "Right Parenthesis"));
                        continue;
                    case "{":
                        position++;
                        addToken(new Token(t, "Left Curly Bracket"));
                        continue;
                    case "}":
                        position++;
                        addToken(new Token(t, "Right Curly Bracket"));
                        continue;
                    case ";":
                        position++;
                        addToken(new Token(t, "Semi-colon"));
                        continue;
                    case ",":
                        position++;
                        addToken(new Token(t, "Comma"));
                        continue;
                    case "[":
                        position++;
                        addToken(new Token(t, "Left Square Bracket"));
                        continue;
                    case "]":
                        position++;
                        addToken(new Token(t, "Left Square Bracket"));
                        continue;
                    case ":":
                        position++;
                        t = data.substring(position, position + 1);
                        if (t.equals("=")) {
                            position++;
                            addToken(new Token(":=", "Assign"));
                        } else {
                            System.out.println("LEXICAL ERROR");
                            System.out.println("On line " + line
                                    + " the : is not followed by an = to look as :=. In correct assign format.");
                            return new Token("LEXICAL ERROR");
                        }
                        break;

                }

                regex = "-|[0-9]";
                p = Pattern.compile(regex);
                m = p.matcher(t);
                if (m.find()) {
                    position++;
                    if (!addToken(scanForNumbers(position, data))) {
                        return new Token("LEXICAL ERROR");
                    }
                    continue;
                }

                regex = "[A-Z]";
                p = Pattern.compile(regex);
                m = p.matcher(t);
                if (m.find()) {
                    position++;
                    System.out.println("LEXICAL ERROR");
                    System.out.println("On line " + line + " the error of starting with a capital letter is not allowed. (" + t + ")");
                    return new Token("LEXICAL ERROR");
                }

                if (t.equals("\"")) {
                    position++;
                    if (!addToken(scanForShortString(position, data))) {
                        return new Token("LEXICAL ERROR");
                    }
                    continue;
                }

                regex = "-|[0-9]";
                p = Pattern.compile(regex);
                m = p.matcher(t);
                if (m.find()) {
                    position++;
                    if (!addToken(scanForNumbers(position, data))) {
                        return new Token("LEXICAL ERROR");
                    }
                    continue;
                }

                regex = "[a-z]";
                p = Pattern.compile(regex);
                m = p.matcher(t);
                if (m.find()) {
                    if (!addToken(scanForText(position, data))) {
                        return new Token("LEXICAL ERROR");
                    }
                    continue;
                }

                // use substring for the rest of the tokens and regular expressions.

            } else {
                System.out.println("LEXICAL ERROR");
                System.out.println("The character " + t + " on line " + line + " is not allowed.");
                return new Token("LEXICAL ERROR");
            }

        }

        return head;
    }

    public boolean addToken(Token t) {
        if (t == null) {
            return false;
        }
        if (head == null) {
            head = t;
            tail = t;
            return true;
        }
        tail.next = t;
        tail = t;
        return true;

    }

    public Token scanForShortString(int pos, String data) {
        Pattern p;
        Matcher m;
        int count = 0;
        while (count <= 15) {
            if (pos + count == data.length()) {
                break;
            }
            String regex = "\"";
            p = Pattern.compile(regex);
            m = p.matcher(data.substring(pos + count, pos + count + 1));

            if (m.find()) {
                break;
            }
            count++;
        }
        String regex = "^([A-Z]|[0-9]|[ ]){0,15}\"$";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count + 1));
        if (m.find()) {

            position = pos + count + 1;
            return new Token(data.substring(pos - 1, pos + count + 1), "ShortString");
        }
        System.out.println("LEXICAL ERROR");
        System.out.println("The string  on line " + line + " is not the correct format for a ShortString.");
        return null;
    }

    public Token scanForNumbers(int pos, String data) {
        Pattern p;
        Matcher m;
        int count = 0;
        String regex = "[0-9]";
        if (!(pos < data.length())) {
            if (data.substring(pos - 1, pos) == "-") {
                System.out.println("LEXICAL ERROR");
                System.out.println("On line " + line + " the minus is not followed by any value");
            } else {
                return new Token(data.substring(pos - 1, pos), "Number");
            }
        }
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + 1));
        while (m.find()) {
            if (pos + count == data.length()) {
                break;
            } else {
                m = p.matcher(data.substring(pos + count, pos + count + 1));
            }
            count++;
        }
        count--;
        regex = "^([-]?[1-9]+[0-9]*)$|^0$";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos - 1, pos + count));
        if (m.find()) {

            position = pos + count;
            return new Token(data.substring(pos - 1, pos + count), "ShortString");
        }
        if (data.substring(pos - 1, pos) == "0") {
            if (count > 0) {
                System.out.println("LEXICAL ERROR");
                System.out.println("On line " + line + " the zero cannot be a leading character in a number.");
                return null;
            }
        }
        if (data.substring(pos - 1, pos) == "-") {
            if (count == 0) {
                System.out.println("LEXICAL ERROR");
                System.out.println("On line " + line + " the negative sign needs a value.");
            } else {
                System.out.println("LEXICAL ERROR");
                System.out.println("On line " + line + " the negative sign cannot be followed by a zero.");
            }
            return null;
        }
        System.out.println("LEXICAL ERROR");
        System.out.println("On line " + line + ", it is not a number.");
        return null;
    }

    public Token scanForText(int pos, String data) {
        // pos is start of the text and not the second character

        Pattern p;
        Matcher m;
        int count = 1;
        String regex = "^[a-z]([a-z]|[0-9])*$";

        if (pos + 1 != data.length()) {
            p = Pattern.compile(regex);
            m = p.matcher(data.substring(pos, pos + count + 1));
            while (m.find()) {
                if (pos + count + 1 == data.length()) {
                    break;
                }
                count++;
                // System.out.println(count);
                m = p.matcher(data.substring(pos, pos + count + 1));
            }
        }

        regex = "(^add$)|(^sub$)|(^mult$)|(^larger$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "Number operators");
        }

        regex = "(^input$)|(^output$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "I/O character");
        }

        regex = "(^eq$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "Comparison operator");
        }

        regex = "(^and$)|(^or$)|(^not$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "Boolean operator");
        }

        regex = "(^num$)|(^bool$)|(^string$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "Variable type");
        }

        regex = "(^true$)|(^false$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "Boolean value");
        }

        regex = "(^proc$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "Procedure definition");
        }

        regex = "(^main$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "Main Procedure definition");
        }

        regex = "(^arr$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "Array");
        }

        regex = "(^halt$)|(^return$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "Algorithm end");
        }

        regex = "(^if$)|(^then$)|(^else$)|(^do$)|(^until$)|(^while$)|(^call$)";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count),
                    "Instruction " + data.substring(pos, pos + 1).toUpperCase() + data.substring(pos + 1, pos + count));
        }

        regex = "[a-z]([az]|[0-9])*";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + count));
        if (m.find()) {
            position = pos + count;
            return new Token(data.substring(pos, pos + count), "userDefinedName");
        }

        System.out.println("LEXICAL ERROR");
        System.out.println("On line " + line + ", there is no acceptable format given.");
        return null;

    }

    public void printTokens() {
        int count = 0;
        Token curr = head;
        while (curr != null) {
            System.out.println("Token: " + curr.value + "\tType: " + curr.type);
            curr = curr.next;
            count++;
        }
        System.out.println("Token count: " + count);
    }

}
