package com.example.calculatordb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Simplify extends AppCompatActivity {

    String DEBUGSTR = "DB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplify);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /*
    public String open_one_bracket(String Str, String x) {
        boolean could_open_brackets = true;
        String s = Str;
        String error = "";
        boolean find = false;
        int left_bracket = 0;
        int right_bracket = 0;
        int needed_left_bracket = 0;
        int needed_right_bracket = 0;
        int[][] indexes_of_brackets = int[2][s.length()];
        int num_of_blocks_found = 0;
        int sign_index = 0;
        int delta_brackets = 0;
        // we need to make an array of left and right brackets of all blocks
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                if (delta_brackets == 0) {
                    left_bracket = i;
                }
                delta_brackets += 1;
            } else if (c == ')') {
                delta_brackets -= 1;
                if (delta_brackets == 0) {
                    right_bracket = i;
                }
            }
            if (delta_brackets < 0) {
                break;
            }
            if (delta_brackets == 0) { //if we find a block
                indexes_of_brackets[0][num_of_blocks_found] = left_bracket;
                indexes_of_brackets[1][num_of_blocks_found] = right_bracket;
                num_of_blocks_found += 1;
                if (sign_index != 0) { //if we find a sum_block
                    find = true;
                    if (needed_right_bracket == 0) {
                        needed_left_bracket = left_bracket;
                        needed_right_bracket = right_bracket;
                    }
                } else { //if we find a full block
                    left_bracket = 0;
                    right_bracket = 0;
                }
            }
            if (c == '+' && delta_brackets == 1) {
                if (sign_index == 0) {
                    sign_index = i;
                }
            }
        }
        if (find) { //WRITE
            //if find a sum_block
            return "ok";
        } else if (delta_brackets != 0) {
            return "The sequence of brackets is not correct!";
        } else { //if all blocks are full ones
            could_open_brackets = false;
            //WRITE about could_open_brackets
            return s;
        }
        return "ok";
    } */

    public String openDegreeBrackets(String Str) {
        int degPosition = findOpenOperator(Str, '^');
        String newStr = Str;
        while (degPosition != -1) {
            int nextOperator = findOpenOperator(Str, '?', degPosition + 1);
            if (nextOperator == -1)
                nextOperator = Str.length();
            if (isNumeric(Str.substring(degPosition + 1, nextOperator)) && ((Double.parseDouble(Str.substring(degPosition + 1, nextOperator))) % 1) == 0) { // check if degree is an integer
                int prevOperator = findOpenOperator(Str.substring(0, degPosition), '?', true);
                String replaceStr = "("; // make a opened-degree string
                for (int i = 0; i < Integer.parseInt(Str.substring(degPosition + 1, nextOperator)); i ++)
                    replaceStr += Str.substring(prevOperator + 1, degPosition) + "*";
                replaceStr = replaceStr.substring(0, replaceStr.length() - 1) + ")";
                newStr = newStr.replace(Str.substring(prevOperator + 1, nextOperator), replaceStr);
            }
            degPosition = findOpenOperator(Str, '^', degPosition + 1);
        }
        return newStr;
    }

    //open brackets block
    public String openBrackets(String str, String x) {
        String Str = prepare(str, x);
        String ansStr = "";
        int sum_operator_before = -1;
        int sum_operator_after = -1;
        while (sum_operator_after != Str.length()) {
            sum_operator_before = sum_operator_after;
            sum_operator_after = findOpenOperator(Str, '$', sum_operator_before + 1);
            if (sum_operator_after == -1)
                sum_operator_after = Str.length();
            String term = Str.substring(sum_operator_before + 1, sum_operator_after); // we will look at this term
            term = openDegreeBrackets(term);
            String newTerm = "";
            int delta_brackets = 0;
            int left_bracket = -1;
            int sign_index = -1;
            for (int i = 0; i <term.length(); i++) {
                char c = term.charAt(i);
                if (c == '(') {
                    delta_brackets += 1;
                    if (delta_brackets == 1)
                        left_bracket = i;
                } else if (c == ')' && delta_brackets == 1) { // if we find a block
                    delta_brackets = 0;
                    String block = term.substring(left_bracket + 1, i);
                    if (sign_index == -1) { // if it is a full block
                        block = openBrackets(block, x);
                    }
                    // now the block is a sum block
                    String s_prev = ""; // the string before the sum block
                    if (left_bracket > 0)
                        s_prev = term.substring(0, left_bracket);
                    String s_next = ""; // the string after the next sum block
                    if (i + 1 != term.length())
                        s_next = term.substring(i + 1);
                    String opened_block = "";
                    int sum_operator_of_block_before = -1;
                    int sum_operator_of_block_after = -1;
                    while (sum_operator_of_block_after != block.length()) {
                        sum_operator_of_block_before = sum_operator_of_block_after;
                        sum_operator_of_block_after = findOpenOperator(block, '$', sum_operator_of_block_before + 1);
                        if (sum_operator_of_block_after == -1)
                            sum_operator_of_block_after = block.length();
                        String term_of_block = block.substring(sum_operator_of_block_before + 1, sum_operator_of_block_after);
                        if (sum_operator_of_block_after != block.length())
                            opened_block += s_prev + term_of_block + s_next + String.valueOf(block.charAt(sum_operator_of_block_after));
                        else
                            opened_block += s_prev + term_of_block + s_next;
                    }
                    newTerm += opened_block;
                    left_bracket = -1;
                    sign_index = -1;
                } else if (delta_brackets == 0) {
                    newTerm += String.valueOf(c);
                }
                if (delta_brackets < 0) {
                    return "error_notACorrectBracketSequence_";
                }
                if ((c == '+' || c == '-') && delta_brackets == 1) {
                    sign_index = i;
                }
            }
            if (sum_operator_after != Str.length())
                ansStr += newTerm + String.valueOf(sum_operator_after);
            else
                ansStr += newTerm;
        }
        return ansStr;
    }
    
    // differentiate function block
    public int findOpenOperator(String Str, char op) {
        return findOpenOperator(Str, op, 0);
    }

    public int findOpenOperator(String Str, char op, int indexFrom) {
        int delta_brackets = 0;
        for (int i = indexFrom; i < Str.length(); i++) {
            char c = Str.charAt(i);
            if (c == '(')
                delta_brackets += 1;
            else if (c == ')')
                delta_brackets -= 1;
            else if (op != '?' && op != '$' && c == op && delta_brackets == 0)
                return i;
            else if (op == '?' && isOperator(c) && delta_brackets == 0) // if any operator from {+, -, *, /, ^}
                return i;
            else if (op == '$' && (c == '-' || c == '+') && delta_brackets == 0) // if + or -
                return i;
        }
        return -1;
    }

    public int findOpenOperator(String Str, char op, boolean fromTheEnd) {
        if (fromTheEnd) {
            int delta_brackets = 0;
            for (int i = Str.length() - 1; i >= 0; i--) {
                char c = Str.charAt(i);
                if (c == ')')
                    delta_brackets += 1;
                else if (c == '(')
                    delta_brackets -= 1;
                else if (op != '?' && op != '$' && c == op && delta_brackets == 0)
                    return i;
                else if (op == '?' && isOperator(c) && delta_brackets == 0) // if any operator from {+, -, *, /, ^}
                    return i;
                else if (op == '$' && (c == '-' || c == '+') && delta_brackets == 0) // if + or -
                    return i;
            }
            return -1;
        } else
            return findOpenOperator(Str, op);
    }

    public String unblock(String Str) {
        if (Str.length() > 1 && Str.charAt(0) == '(' && Str.charAt(Str.length() - 1) == ')') {
            return unblock(Str.substring(1, Str.length() - 1));
        }
        return Str;
    }

    public boolean isOperator(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^')
            return true;
        return false;
    }

    public boolean isBlock(String Str) {
        if (Str.length() > 1 && Str.charAt(0) == '(' && Str.charAt(Str.length() - 1) == ')') {
            int delta_brackets = 0;
            for (int i = 1; i < Str.length() - 1; i++){
                char c = Str.charAt(i);
                if (c == '(')
                    delta_brackets += 1;
                else if (c == ')')
                    delta_brackets -= 1;
                if (delta_brackets < 0)
                    return false;
            }
            return true;
        }
        return false;
    }

    public String differentiate(String str, String x) {
        String Str = prepare(str, x);
        if (Str.isEmpty())
            return "error_stringIsEmpty_";
        int op = 0;
        op = findOpenOperator(Str, '+'); // open plus
        if (op == Str.length() - 1)
            return "error_operatorIsTheLastSymbol_";
        if (op != -1) {
            return "(" + differentiate(Str.substring(0, op), x) + "+" + differentiate(Str.substring(op + 1), x) + ")";
        }
        op = findOpenOperator(Str, '-'); // open minus
        if (op == Str.length() - 1)
            return "error_operatorIsTheLastSymbol_";
        if (op != -1) {
            return "(" + differentiate(Str.substring(0, op), x) + "-" + differentiate(Str.substring(op + 1), x) + ")";
        }
        op = findOpenOperator(Str, '*'); // open multi
        if (op == Str.length() - 1)
            return "error_operatorIsTheLastSymbol_";
        if (op != -1) {
            String s1 = differentiate(Str.substring(0, op), x) + "*" + Str.substring(op + 1);
            String s2 = differentiate(Str.substring(op + 1), x) + "*" + Str.substring(0, op);
            return "(" + s1 + "+" + s2 + ")";
        }
        op = findOpenOperator(Str, '/'); // open division
        if (op == Str.length() - 1)
            return "error_operatorIsTheLastSymbol_";
        if (op != -1) {
            String s_begining = Str.substring(0, op);
            String s_ending = Str.substring(op + 1);
            String s1 = differentiate(s_begining, x) + "*" + s_ending;
            String s2 = differentiate(s_ending, x) + "*" + s_begining;
            return "((" + s1 + "+" + s2 + ")/" + s_ending + "^2)";
        }
        op = findOpenOperator(Str, '^'); // open deg
        if (op == Str.length() - 1)
            return "error_operatorIsTheLastSymbol_";
        if (op != -1) {
            String s_beginibg = Str.substring(0, op);
            String s_ending = Str.substring(op + 1);
            if (s_ending.indexOf(x) == -1) {
                return s_ending + "*" + s_beginibg + "^(" + s_ending + "-1)*(" + differentiate(s_beginibg, x) + ")";
            }
            return "error_toHardDegree_";
        }
        String s = unblock(Str);
        if (s.indexOf(x) == -1) { // if const
            return "0";
        } else if (s.equals(x)) { // if x
            return "1";
        } else if (!s.equals(Str)) { //if not unblocks
            return differentiate(s, x);
        } else {
            return "error_notBlockAtTheEnd_";
        }
    }

    // prepare function block
    public String separateKorpusWithoutBlocks(String Str, String x) {
        String checkString = Str.replaceAll(x, "");
        if (!isNumeric(checkString) && !checkString.isEmpty())
            return "_error_problemWithKorpusSeparation_" + Str + "!";
        String newK_withoutBlocks = Str.replaceAll(x, "*" + x + "*");
        if (newK_withoutBlocks.charAt(0) == '*')  // if * at the begining
            newK_withoutBlocks = newK_withoutBlocks.substring(1);
        if (newK_withoutBlocks.charAt(newK_withoutBlocks.length() - 1) == '*') // if * at the end
            newK_withoutBlocks = newK_withoutBlocks.substring(0, newK_withoutBlocks.length() - 1);
        return newK_withoutBlocks;
    }

    public String separateKorpus(String Str, String x) {
        String newStr = "";
        if (!isBlock(Str) && !isNumeric(Str) && !Str.equals(x)) { // if we need to separate the korpus
            int left_bracket = -1;
            int delta_brackets = 0;
            int endOfPrevBlock = -1;
            for (int i = 0; i < Str.length(); i++) {
                char c = Str.charAt(i);
                if (c == '(') {
                    left_bracket = i;
                    delta_brackets += 1;
                } else if (c == ')') {
                    delta_brackets -= 1;
                }
                if (c == ')' && delta_brackets == 0) { // we need to look on this part without blocks
                    if (endOfPrevBlock + 1 != left_bracket)
                        newStr += separateKorpusWithoutBlocks(Str.substring(endOfPrevBlock + 1, left_bracket), x) + "*(" + prepare(Str.substring(left_bracket + 1, i), x) + ")*";
                    else
                        newStr += "(" + prepare(Str.substring(left_bracket + 1, i), x) + ")*";
                    endOfPrevBlock = i;
                }
            }
            if (endOfPrevBlock != Str.length() - 1)
                newStr += separateKorpusWithoutBlocks(Str.substring(endOfPrevBlock + 1), x);
            else
                newStr = newStr.substring(0, newStr.length() - 1);
            if (newStr.charAt(0) == '*')
                newStr = newStr.substring(1);
            return newStr;
        }
        return Str;
    }

    public String prepare(String Str, String x) {
        String newStr = "";
        int prev_operator = -1;
        int delta_brackets = 0;
        for (int i = 0; i < Str.length(); i++) {
            char c = Str.charAt(i);
            if (c == '(')
                delta_brackets += 1;
            else if (c == ')')
                delta_brackets -= 1;
            else if (isOperator(c) && delta_brackets == 0) {
                newStr += separateKorpus(Str.substring(prev_operator + 1, i), x) + String.valueOf(c);
                prev_operator = i;
            }
        }
        newStr += separateKorpus(Str.substring(prev_operator + 1), x);
        return newStr;
    }
    //TODO
    //prepare function which makes the sequence KoKo...oK //DONE
    //debug prepare function //DONE
    //openBrackets function //DONE
    //debug openBrackets. Fail on (x+1)3+2
    //collapse function which unit the same summations
    //calcConcst function which calculate the constants
    public void onClickGetFunc(View view) {
        String s = ((EditText)findViewById(R.id.GetFunction)).getText().toString();
        TextView ansText = (TextView)findViewById(R.id.answer_text2);
        ansText.setText(openBrackets(s, "x") + "!" + DEBUGSTR);
    }

}