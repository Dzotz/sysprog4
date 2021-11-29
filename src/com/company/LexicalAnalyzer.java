package com.company;

import java.util.List;

public class LexicalAnalyzer {
    String line;
    char peek;
    String buf;
    List<String> keywords = List.of("program","implicit","none","real","print","end");
    List<String> logicalOperators = List.of(".and.", ".or.", ".not.");
    int currentIndex;
    int lineNumber;


    public LexicalAnalyzer(){
        this.peek = ' ';
        lineNumber = 0;
        buf="";
    }

    public void setLine(String lineOfCode) {
        this.currentIndex = 0;
        this.line = lineOfCode;
        this.peek = ' ';
        line += "\n";
        ++lineNumber;
    }

    private void getNextChar() {
        if (currentIndex < line.length()) {
            this.peek = line.charAt(currentIndex);
        }
        ++currentIndex;
    }

    public Token scan() {

        buf="";
        for (;;getNextChar()) {
            if (this.peek == ' ' || this.peek == '\t')
                continue;
            else
            if (this.peek == '\n')
                ++lineNumber;
            else
                break;
        }

        Token token;

        if(Character.isDigit(peek)){
            token = processNumber();
            return token;
        }
        if(Character.isLetter(peek)){
            token = processWord();
            return token;
        }

        switch (peek){
            case '!' ->{
                token=processComment();
                return token;
            }
            case '\'', '\"' -> {
                token = processStringLiteral(peek);
                return token;
            }
            case '.' ->{
                token = processLogicalOperator();
                return token;
            }
            case '+','-'->{
                buf+=peek;
                getNextChar();
                if(Character.isDigit(peek)){
                    token = processNumber();
                }
                else {
                    token = new Token(LexemeType.OPERATORS, buf);
                }
                return token;
            }
            case '*' ->{
                buf+=peek;
                getNextChar();
                if(peek=='*'){
                    buf+=peek;
                    getNextChar();
                }
                token = new Token(LexemeType.OPERATORS, buf);
                return token;
            }
            case '/', '=', '<', '>' ->{
                buf+=peek;
                getNextChar();
                if(peek=='='){
                    buf+=peek;
                    getNextChar();
                }
                token = new Token(LexemeType.OPERATORS, buf);
                return token;
            }
            case ',',';' ->{
                buf+=peek;
                getNextChar();
                token=new Token(LexemeType.DELIMITER, buf);
                return token;
            }
            case ':' -> {
                buf+=peek;
                getNextChar();
                if(peek==':'){
                    buf+=peek;
                    getNextChar();
                }
                token = new Token(LexemeType.DELIMITER, buf);
                return token;
            }
        }
        token = processError();
        return token;
    }

    public boolean reachEndOfTheLine() {
        return currentIndex >= line.length();
    }

    public Token processLogicalOperator(){
        Token token;
        do{
            buf+=peek;
            getNextChar();
        }while (Character.isLetter(peek));
        if(peek=='.'){
            buf+=peek;
            getNextChar();
            if (logicalOperators.contains(buf)&&(peek==' '||peek=='\n')){
                token = new Token(LexemeType.LOGICAL_OPERATOR, buf);
            }
            else {
                if(peek == ' '||peek=='\n'){
                    token = new Token(LexemeType.LEXICAL_ERROR, buf);
                }
                else {
                    token = processError();
                }
            }
            return token;
        }
        else return processError();
    }

    public Token processNumber(){
        Token token;
        do{
            buf+=peek;
            getNextChar();
        }while (Character.isDigit(peek));
        if(peek=='.'){
            buf+=peek;
            getNextChar();
            if (Character.isDigit(peek)){
                do{
                    buf+=peek;
                    getNextChar();
                }while (Character.isDigit(peek));
                if (peek==' '||peek==')'||peek=='\n'){
                    token = new Token(LexemeType.NUMBER, buf);
                }
                else {
                    token = processError();
                }
                return token;
            }
            else if(peek==' '||peek==')'||peek=='\n'){
                token = new Token(LexemeType.NUMBER, buf);
            }
            else {
                token = processError();
            }
            return token;
        }
        else if(peek==' '||peek==')'||peek=='\n'){
            token = new Token(LexemeType.NUMBER, buf);
        }
        else {
            token = processError();
        }
        return token;
    }

    public Token processStringLiteral(char sep){
        Token token;
        do{
            buf+=peek;
            getNextChar();
        }while (peek!=sep&&peek!='\n');
        if(peek==sep){
            buf+=peek;
            getNextChar();
            token = new Token(LexemeType.STRING, buf);
            return token;
        }
        else{
            token = new Token(LexemeType.LEXICAL_ERROR, buf);
            return token;
        }
    }

    public Token processWord(){
        Token token;
        do{
            buf+=peek;
            getNextChar();
        }while (Character.isLetterOrDigit(peek) || peek == '_');
        if(peek==' '||peek==')'||peek=='\n'||peek==','){
            if (keywords.contains(buf)) {
                return new Token(LexemeType.KEYWORD, buf);
            } else {
                return new Token(LexemeType.IDENTIFIER, buf);
            }
        }
       else {
           token = processError();
           return token;
        }
    }

    public Token processComment(){
        while (!reachEndOfTheLine()){
            getNextChar();
        }
        return new Token(LexemeType.ONE_LINE_COMMENT, buf);
    }

    public Token processError(){
        do{
            buf+=peek;
            getNextChar();
        }while (peek!=' '&&peek!='\n'&&peek!=','&&peek!=')');
        return new Token(LexemeType.LEXICAL_ERROR, buf);
    }

}
