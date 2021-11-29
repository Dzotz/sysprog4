package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException {
        String InputFile = "src/com/company/input.txt";
        String OutputFile = "output.txt";

        Scanner scanner = new Scanner(new File(InputFile));
        FileWriter output = new FileWriter(new File(OutputFile));

        Vector<Token> tokens = new Vector<>();
        LexicalAnalyzer analyser = new LexicalAnalyzer();

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.isEmpty())
                continue;

            Vector<Token> lineTokens = new Vector<>();
            analyser.setLine(line);


            while (!analyser.reachEndOfTheLine()) {
                var token = analyser.scan();

                if (isComment(token)) {
                    lineTokens.add(new Token(LexemeType.ONE_LINE_COMMENT,
                            token.toRawString() + line.substring(line.indexOf("#") + 1)
                    ));
                    break;
                }
                lineTokens.add(token);
            }

            if (lineTokens.size() != 0)
                tokens.addAll(lineTokens);
        }

        for (Token token : tokens) {
            if (!isComment(token))
                output.write(token.toString() + "\n");
        }
        output.close();
    }

    static boolean isComment(Token token) {
        return token.type == LexemeType.ONE_LINE_COMMENT;
    }

}
