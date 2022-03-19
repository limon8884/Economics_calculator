package com.example.calculatordb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Linear_eqilibrium extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_eqilibrium);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    public void onClickEnterText(View view) {
        EditText t1 = (EditText)findViewById(R.id.enter_1);
        EditText t2 = (EditText)findViewById(R.id.enter_2);
        String d = t1.getText().toString();
        String s = t2.getText().toString();
        TextView ansText = (TextView)findViewById(R.id.answer_text);
        String DEBUG_CONST = "";
        String error = "";
        String d_error = "";
        String s_error = "";
        String answer = "ok";
        double a_d = 0, b_d = 0, a_s = 0, b_s = 0;

        // Demand
        int d_sign_pos = d.indexOf("-");
        if (d_sign_pos != -1) {
            String begining = d.substring(0, d_sign_pos);
            String ending = "0";
            if (d_sign_pos != d.length() - 1)
                ending = d.substring(d_sign_pos + 1);
            else
                d_error = "Demand is not s function!";
            if (begining.indexOf("p") == -1 && ending.indexOf("p") == -1) {
                error = "the function has no 'p' and is not a constant!";
            }else if (begining.indexOf("p") != -1 && ending.indexOf("p") == -1){
                if (begining.indexOf("p") == begining.length() - 1 && isNumeric(begining.substring(0, begining.length() - 1))) {
                    b_d = Double.parseDouble(begining.substring(0, begining.length() - 1));
                } else if (begining.equals("p")){
                    b_d = 1.0;
                } else {
                    error = "something wrong with 'p' coefficient!'";
                }
                if (isNumeric(ending)) {
                    a_d = Double.parseDouble(ending);
                } else {
                    error = "something wrong with free coeffiicient!";
                }
            } else if (begining.indexOf("p") == -1 && ending.indexOf("p") != -1) {
                if (ending.indexOf("p") == ending.length() - 1 && isNumeric(ending.substring(0, ending.length() - 1))) {
                    b_d = Double.parseDouble(ending.substring(0, ending.length() - 1));
                } else if (ending.equals("p")){
                    b_d = 1.0;
                } else {
                    error = "something wrong with 'p' coefficient!'";
                }
                if (isNumeric(begining)) {
                    a_d = Double.parseDouble(begining);
                } else {
                    error = "something wrong with free coeffiicient!";
                }
            } else {
                error = "the function does not look like 'a-bp'";
            }
        } else {
            if (isNumeric(d)) {
                b_d = 0;
                a_d = Double.parseDouble(d);
            } else if (d.equals("p")){
                b_d = 1.0;
                a_d = 0;
            } else {
                error = "the function has no '-' and it is not a constant";
            }
        }
        if (!error.equals("") && d_error.equals("")) {
            d_error = "Demand error: " + error;
        }
        error = "";

        // Supply
        int s_sign_pos = s.indexOf("+");
        if (s_sign_pos != -1) {
            String begining = s.substring(0, s_sign_pos);
            String ending = "0";
            if (s_sign_pos != s.length() - 1)
                ending = s.substring(s_sign_pos + 1);
            else
                s_error = "Supply is not s function!";
            if (begining.indexOf("p") == -1 && ending.indexOf("p") == -1) {
                error = "the function has no 'p' and it is not a constant";
            } else if (begining.indexOf("p") != -1 && ending.indexOf("p") == -1){
                if (begining.indexOf("p") == begining.length() - 1 && isNumeric(begining.substring(0, begining.length() - 1))) {
                    b_s = Double.parseDouble(begining.substring(0, begining.length() - 1));
                } else if (begining.equals("p")){
                    b_s = 1.0;
                } else {
                    error = "something wrong with 'p' coefficient!'";
                }
                if (isNumeric(ending)) {
                    a_s = Double.parseDouble(ending);
                } else {
                    error = "something wrong with free coeffiicient!";
                }
            } else if (begining.indexOf("p") == -1 && ending.indexOf("p") != -1) {
                if (ending.indexOf("p") == ending.length() - 1 && isNumeric(ending.substring(0, ending.length() - 1))) {
                    b_s = Double.parseDouble(ending.substring(0, ending.length() - 1));
                } else if (ending.equals("p")){
                    b_s = 1.0;
                } else {
                    error = "something wrong with 'p' coefficient!'";
                }
                if (isNumeric(begining)) {
                    a_s = Double.parseDouble(begining);
                } else {
                    error = "something wrong with free coeffiicient!";
                }
            } else {
                error = "the function does not look like 'a-bp'";
            }
        } else {
            if (isNumeric(s)){
                b_s = 0;
                a_s = Double.parseDouble(s);
            } else if (s.indexOf("p") == s.length() - 1 && isNumeric(s.substring(0, s.length() - 1))) {
                a_s = 0;
                b_s = Double.parseDouble(s.substring(0, s.length() - 1));
            } else if (s.equals("p")){
                a_s = 0;
                b_s = 1.0;
            } else {
                error = "the function has no '+' and it is not a constant";
            }
        }

        // Computations
        if (!error.equals("") && s_error.equals("")) {
            s_error = "Supply error: " + error;
        }
        error = "";
        if (s_error.equals("") && d_error.equals("")) {
            double p = (a_d - a_s) / (b_s + b_d);
            double q = a_d - b_d * p;
            if (q < 0) {
                answer = "q* = 0! No equilibrium!";
            } else {
                answer = "q* = " + Double.toString(q) + " p* = " + Double.toString(p);
                if (p <= 0) {
                    answer += " Notice! p* < 0! Is it ok?";
                }
            }
        } else {
            answer = d_error + " " + s_error;
        }
        ansText.setText(DEBUG_CONST + answer);
    }
}
