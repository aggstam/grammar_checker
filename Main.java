// -------------------------------------------------------------
//
// This program checks if a word, given by the user, can be produced
// by a Grammar, read from a file, using Depth-First Search.
//
// Author: Aggelos Stamatiou, December 2016
//
// --------------------------------------------------------------

package def;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    // The stack node.
    public static class StackNode {
        char[] word;

        public StackNode(char[] word) {
            this.word = word;
        }

        public String toString() {
            return Arrays.toString(word);
        }

    }

    // Grammar structure.
    public static class Grammar {
        char[] non_terminal; // Non terminal symbols.
        char[] terminal;     // Terminal symbols.
        Rule[] rules;        // Rules of Grammar.
        char start;          // Starting symbol.

        public Grammar(char[] non_terminal, char[] terminal, Rule[] rules, char start) {
            this.non_terminal = non_terminal;
            this.terminal = terminal;
            this.rules = rules;
            this.start = start;
        }
    }
    
    // Rule structure.
    public static class Rule {
        char producer;        // The symbol we will change.
        char[] product;       // The symbol(s) we change it to.

        public Rule(char producer, char[] product) {
            this.producer = producer;
            this.product = product;
        }

        public String toString() {
            return producer + "-->" + Arrays.toString(product);
        }

    }
    
    // This function generates each node's children by checking every letter of the current node's word,
    // applying all the rules that can be applied to that specific letter, creating the new stack nodes
    // and pushes them into the stack.
    public static void generateChilder(Grammar grammar, Stack<StackNode> st, StackNode current, char[] userWord) {
        for(int i = 0; i < current.word.length; i++) {
            for(int j = 0; j < grammar.rules.length; j++) {
                if (grammar.rules[j].producer != current.word[i]) {
                    continue;
                }
                char[] temp= applyRule(current.word,i,grammar.rules[j]);
                // Child's word must not have more terminal symbols than the word's length we're testing.
                if (finalChars(grammar,temp) <= userWord.length) {
                    st.push(new StackNode(temp));
                }
            }
        }
    }
    
    // This function checks if current node's word is the word we are looking for.
    // We exclude 'e' symbol, which means empty.
    public static boolean isSolution(char[] current, char[] userWord) {
        char[] temp = new char[userWord.length];
        int j = 0;
        for(int i = 0; i < current.length; i++) {
            if ((j < temp.length) && (current[i] != 'e')) {
                temp[j]=current[i];
                j++;
            }
        }

        for(int i = 0; i < temp.length; i++) {
            if (temp[i] != userWord[i]) {
                return false;
            }
        }
        
        return true;
    }
    
    // Returns the amount of terminal characters a word has.
    public static int finalChars(Grammar grammar, char[] word) {
        int count = 0;
        for(int i = 0; i < grammar.terminal.length; i++) {
            for(int j = 0; j < word.length; j++) {
                if (word[j] == grammar.terminal[i]) {
                    count++;
                }
            }
        }

        return count;
    }
    
    // User's input, checking for empty words.
    public static char[] readFromUser() {
        Scanner keyboard  =new Scanner(System.in);
        System.out.println("Enter a word:");
        String word = keyboard.nextLine();
        while(word.isEmpty()) {
            System.out.println("You didn't give a word. Try again.");
            System.out.println();
            System.out.println("Enter a word:");
            word = keyboard.nextLine();
        }

        return word.toCharArray();
    }
    
    // Function that checks if the word given by the user is valid for the specified Grammar.
    public static boolean isValidWord(Grammar grammar, char[] word) {
        boolean valid = false;
        for(int i = 0; i < word.length; i++) {
            valid = false;
            for(int j = 0; j < grammar.terminal.length; j++){
                if (word[i] == grammar.terminal[j]) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                return false;
            }
        }

        return true;
    }
    
    // Apply a given rule to the word. We copy each character from the one we
    // want to change, apply the changes and then copy the rest characters.
    public static char[] applyRule(char[] word, int position, Rule rule) {
        char[] temp = rule.product;
        char[] ret = new char[word.length+temp.length - 1];
        int i, j, k;
        if (word[position] == rule.producer) {
            for(i = 0 ; i < position; i++){
                ret[i] = word[i];
            }
            for(j = 0; j < temp.length; j++) {
                ret[i + j] = temp[j];
            }
            for(k = i + j; k < ret.length; k++) {
                ret[k] = word[i + 1];
                i++;        
            }
        }

        return ret;
    }
    
    // Reads the Grammar of our program from a file given by the user.
    // The Grammar's file has a specific layout:
    // -Amount of non terminal symbols.
    // -The non terminal symbols.
    // -Starting symbol.
    // -Amount of terminal symbols.
    // -The terminal symbols.
    // -Amount of Grammar's rules.
    // -Rules
    // - ...
    // -Each symbol of the rule is separated by a space.
    // -The first one is the symbol we change.
    // -The rest are those we change it to.
    // - ...
    public static Grammar readFromFile(){
        char[] non_terminal = null;
        char[] terminal = null;
        Rule[] rules = null;
        char start = ' ';

        Scanner keyboard=new Scanner(System.in);
        Scanner input=null;
        while(input==null) {
            System.out.println("Give file name:");
            String fileName=keyboard.nextLine();
            File inputFile = new File(fileName);
            try {
                input = new Scanner(inputFile);
            } catch (FileNotFoundException e) {
                System.out.println("Error, file "+fileName+" not found.");
                System.out.println("Try using full path.");
                System.out.println();
            }
        }
        
        int count=0;
        while(input.hasNextLine()) {
            String line=input.nextLine();
            if (count == 0) {
                non_terminal = new char[Character.getNumericValue(line.charAt(0))];
            } else if (count == 1) {
                int k = 0;
                for(int i = 0; i < non_terminal.length; i++) {
                    non_terminal[i] = line.charAt(k);
                    k += 2;
                }
            } else if (count == 2) {
                start = line.charAt(0);
            } else if (count == 3) {
                terminal = new char[Character.getNumericValue(line.charAt(0))];
            } else if (count == 4){
                int k = 0;
                for(int i = 0; i < terminal.length; i++) {
                    terminal[i] = line.charAt(k);
                    k += 2;
                }
            } else if (count==5){
                rules = new Rule[Character.getNumericValue(line.charAt(0))];
            } else {
                char t1 = line.charAt(0);
                char[] t2 = new char[(line.length()-1) / 2];
                int c = 0;
                for(int i = 2; i < line.length(); i += 2) {
                    t2[c] = line.charAt(i);
                    c++;
                }
                rules[count-6] = new Rule(t1, t2);
            }
            count++;
        }

        Grammar grammar = new Grammar(non_terminal, terminal, rules, start);
        input.close();

        return grammar;
    }

    public static void main(String[] args) {
        // Initialize Grammar
        Grammar grammar = readFromFile();
        char[] userWord = readFromUser();
        while(!isValidWord(grammar, userWord)) {
            System.out.println("Word is not valid for the specific Grammar. Try again.");
            System.out.println();
            userWord=readFromUser();
        }

        char[] word = {grammar.start};

        // Head of stack
        Stack<StackNode> st = new Stack<StackNode>();
        st.add(new StackNode(word));

        // While the stack is not empty
        while(!st.isEmpty()) {
            // Get the stack head
            StackNode current = st.pop();
            // Check if it's a solution
            if (isSolution(current.word, userWord)){
                System.out.println("HOORAY!! The word can be produced by the specified Grammar.");
                return;
            }
            // ..else generate it's children using the Grammar's rules.
            generateChilder(grammar, st, current, userWord);
        }

        System.out.println("What a pity, the word can't be produced by the specified Grammar.");
    }
}
