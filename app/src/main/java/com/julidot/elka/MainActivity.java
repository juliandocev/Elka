package com.julidot.elka;

import static com.julidot.elka.utils.UtilityFunctions.isLastDigit;
import static com.julidot.elka.utils.UtilityFunctions.isLastNumberDecimal;
import static com.julidot.elka.utils.UtilityFunctions.removeLastChar;
import static com.julidot.elka.utils.UtilityFunctions.xPowTwoFuncPlacementBrackets;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.julidot.elka.databinding.ActivityMainBinding;


import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.mXparser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityMainBinding binding;

    TextView tv_expression;
    TextView tv_result;

//    Boolean isLastNumeric = false; // is the last character numeric
 //   Boolean isStateError = false; // is there any error
//    Boolean isDot = false; // is last character a dot

//    Boolean isDecimal = false; // is the last number decimal

    private Expression expression;


    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
    private static final int CIRCLE_SIZE_DP = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Set the toolbar as an ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        initiateOnCLickListeners();

        // Slide panel to show/hide scientific btns
        binding.ivSlide.setOnTouchListener(
                new AnchorTouchListener(binding.llBtns));


    }





    // Scientific  buttons

    /** INV button toggle */
    private void onInvClick(View v) {
        if(binding.btnSin.getText().toString().equals("sin")){// It is not necessary to survey the sin. it can be cos, tan...
            binding.btnSin.setText(R.string.btn_arc_sin);
            binding.btnCos.setText(R.string.btn_arc_cos);
            binding.btnTan.setText(R.string.btn_arc_tan);
            binding.btnLn.setText(R.string.btn_ln_to_e_x);
            binding.btnLog.setText(R.string.btn_log_to_10_x);
            binding.btnSquareRoot.setText(R.string.btn_square_root_to_x_2);
        }else{
            binding.btnSin.setText(R.string.btn_sin);
            binding.btnCos.setText(R.string.btn_cos);
            binding.btnTan.setText(R.string.btn_tan);
            binding.btnLn.setText(R.string.btn_ln);
            binding.btnLog.setText(R.string.btn_log);
            binding.btnSquareRoot.setText(R.string.btn_square_root);
        }

    }

    /** Radian or Degree calculation toggle button */
    private void onDegRadClick(View v)
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if(((Button)v).getText().toString().equals(this.getString(R.string.btn_deg) )){
            binding.btnDegRad.setText(this.getString(R.string.btn_rad));
            toolbar.setTitle(R.string.btn_deg);

        }else{
            binding.btnDegRad.setText(this.getString(R.string.btn_deg));
            toolbar.setTitle(R.string.btn_rad);
        }
        calculate();
    }

    /** Change the sign of the last number or constant */
    private void onPlusMinusClick(String str) {
        String subStr="";
        String[] numbers = str.split("[^\\d.]+");

        // Take the substring of the last number
        for (int i = numbers.length - 1; i >= 0; i--) {
            String s = numbers[i];
            if(s!=""){
                subStr = numbers[i];
                break;
            }
        }

        // if there is no number(the numbers array is empty, make last index number -1

        int lastIndexNumber = -1;
        if(numbers.length != 0){
            lastIndexNumber  = str.lastIndexOf(subStr);
        }



        int lastIndexPi = str.lastIndexOf("π");

        int lastIndexE = str.lastIndexOf("e");

        // Find the last number or constant. It is the biggest index in thr sting with one of them
        int biggestIndex = Math.max(lastIndexNumber,lastIndexPi);
        biggestIndex = Math.max(biggestIndex,lastIndexE); // This is the last number or constant

        // If there are numbers or constants
        if(biggestIndex != -1){

            StringBuilder sb = new StringBuilder();
            sb.append(str);


            // if there is just one index and not an empty string
            if(biggestIndex == 0 && !str.equals("")){
                binding.tvExpression.setText("-" +str);
            }else{
                // if the sign before the last number or constant is -
                if(str.charAt(biggestIndex - 1)== "-".charAt(0)) {


                    if(str.length() == 2){
                        // erase it

                        sb.deleteCharAt(biggestIndex - 1);
                    }else{
                        // check what is it before that. if it is a number ot pi or e change it to plus
                        if( (Character.isDigit(str.charAt(biggestIndex - 2) )|| str.charAt(biggestIndex - 2) == "e".charAt(0))|| str.charAt(biggestIndex - 2) == "π".charAt(0) ){
                            // change it to +

                            sb.setCharAt(biggestIndex-1, '+');

                        }else{
                            // erase it

                            sb.deleteCharAt(biggestIndex - 1);

                        }
                    }


                    binding.tvExpression.setText(sb.toString());

                }
                else{// if it is not -

                    // check if it +
                    if(str.charAt(biggestIndex - 1)== "+".charAt(0)){
                        // if it is + change it to -
                        sb.setCharAt(biggestIndex-1, '-');
                        binding.tvExpression.setText(sb.toString());
                    }else{
                        String newString ="";
                        // if It is a constant
                        if((str.charAt(biggestIndex)== "e".charAt(0)) ||str.charAt(biggestIndex)== "π".charAt(0)){
                            // if before is a bracket
                            if((str.charAt(biggestIndex - 1)== "(".charAt(0)) ||str.charAt(biggestIndex - 1)== ")".charAt(0)){
                                newString = str.substring(0, biggestIndex ) + "-" + str.substring(biggestIndex);
                            }
                            else{
                                // if it is not a bracket
                                newString = str.substring(0, biggestIndex ) + "(-" + str.substring(biggestIndex);
                            }

                        }
                        else{
                            // if before it is a  constant before
                            if((str.charAt(biggestIndex - 1)== "e".charAt(0)) ||str.charAt(biggestIndex - 1)== "π".charAt(0)){
                                newString = str.substring(0, biggestIndex ) + "(-" + str.substring(biggestIndex);
                            }
                            else{
                                newString = str.substring(0, biggestIndex ) + "-" + str.substring(biggestIndex);
                            }

                        }
                        binding.tvExpression.setText(newString);
                    }

                }

            }

            calculate();

        }


    }

    //TODO Create Unary functions model




    // On click listeners and iterations
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_unit_converter:
                Toast.makeText(this,"btn_unit_converter",Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_currency_converter:
                Toast.makeText(this,"btn_currency_converter",Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_history:
                Toast.makeText(this,"btn_history",Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_inv:
                onInvClick(v);

                break;
            case R.id.btn_deg_rad:
                onDegRadClick(v);

                break;
            case R.id.btn_sin:
                onSpecialButtonClick(v);

                break;
            case R.id.btn_cos:
                onSpecialButtonClick(v);

                break;
            case R.id.btn_tan:
                onSpecialButtonClick(v);

                break;
            case R.id.btn_plus_minus:
                onSpecialButtonClick(v);

                break;
            case R.id.btn_ln:
                onSpecialButtonClick(v);

                break;
            case R.id.btn_log:
                onSpecialButtonClick(v);

                break;

            case R.id.btn_period:
                onDigitClick(v);

                break;
            case R.id.btn_plus_caret:
                onOperatorClick(v);

                break;
            case R.id.btn_pi:
                onDigitClick(v);

                break;
            case R.id.btn_e:
                onDigitClick(v);

                break;
            case R.id.btn_left_bracket:
                onDigitClick(v);

                break;
            case R.id.btn_right_bracket:
                onDigitClick(v);

                break;

            case R.id.btn_square_root:
                onSpecialButtonClick(v);

                break;
            case R.id.btn_c:
                onAllClearClick();

                break;
            case R.id.btn_erase:
                onEraseClick();

                break;
            case R.id.btn_percent:
                onOperatorClick(v);

                break;
            case R.id.btn_divide:
                onOperatorClick(v);

                break;
            case R.id.btn_seven:
                onDigitClick(v);

                break;
            case R.id.btn_eight:
                onDigitClick(v);

                break;
            case R.id.btn_nine:
                onDigitClick(v);

                break;
            case R.id.btn_multiply:
                onOperatorClick(v);

                break;
            case R.id.btn_four:
                onDigitClick(v);

                break;
            case R.id.btn_five:
                onDigitClick(v);

                break;
            case R.id.btn_six:
                onDigitClick(v);

                break;
            case R.id.btn_minus:
                onOperatorClick(v);

                break;
            case R.id.btn_one:
                onDigitClick(v);

                break;
            case R.id.btn_two:
                onDigitClick(v);

                break;
            case R.id.btn_three:
                onDigitClick(v);

                break;
            case R.id.btn_plus:
                onOperatorClick(v);

                break;
            case R.id.btn_zero:
                if(!binding.tvExpression.getText().toString().equals("0")){
                    onDigitClick(v);
                }

                break;
            case R.id.btn_dot:
                String str = binding.tvExpression.getText().toString();
                if(!isLastNumberDecimal(str)){
                    onOperatorClick(v);
                }

                //TODO  Make a function just for it. if dot exists in the last number in the expression do not add it. Find the last number()from nothing or charecter to end or character


                break;
            case R.id.btn_equals:
                calculate();

                break;

        }


    }

    public void onSpecialButtonClick(View view){

//        if(((Button) view).getText().toString().equals(this.getString(R.string.btn_sin))){
//            binding.tvExpression.append(this.getString(R.string.btn_sin) + "(");
//        }
//        if(((Button) view).getText().toString().equals(this.getString(R.string.btn_arc_sin))){
//            binding.tvExpression.append("arcsin\u207B\u00b9 (");
//        }

        switch (((Button)view).getText().toString()){

            case "INV":
                onInvClick(view);

                break;

            case "DEG":
                binding.tvExpression.append("sin(");

                break;

            case "RAD":
                binding.tvExpression.append("sin(");

                break;

            case "sin-1":
                binding.tvExpression.append("sin\u207B\u00b9("); // sin power-1 (arcsin)

                break;
            case "sin":
                binding.tvExpression.append("sin(");

                break;

            case "cos":
                binding.tvExpression.append("cos(");

                break;
            case "cos-1":
                binding.tvExpression.append("cos\u207B\u00b9("); // cos power-1 (arccos)

                break;

            case "tan":
                binding.tvExpression.append("tan(");

                break;

            case "tan-1":
                binding.tvExpression.append("tan\u207B\u00b9("); // tan power-1 (arctan)

                break;
            case "+/-":
                if(!binding.tvExpression.getText().toString().isEmpty()){
                    onPlusMinusClick(binding.tvExpression.getText().toString());
                }

                break;

            case "ln":
                binding.tvExpression.append("ln(");

                break;

            case "ex":
                binding.tvExpression.append("exp("); //TODO problem calculation. It doesn't work

                break;


            case "log":
                binding.tvExpression.append("log(");

                break;

            case "10x":
                binding.tvExpression.append("10^");

                break;

            case "!":
                binding.tvExpression.append("!");

                break;

            case "^":
                binding.tvExpression.append("^");

                break;

            case "π":
                binding.tvExpression.append("π");

                break;

            case "e":
                binding.tvExpression.append("e");

                break;



            case "√":
                binding.tvExpression.append("√");

                break;

            case "X2":
//                // Check first if it is a number
//                String txt = binding.tvExpression.getText().toString();
//                if(isLastDigit(txt)){
//                    ;
//                    binding.tvExpression.setText(putNumberAndPowInBrackets(txt,"\u00B2"));// putt the function here
//                }else if(txt.endsWith("\u00B2" + ")")){
//                    // find the first opening bracket put another onenext to it and finish this one with
//                }else{
//                    binding.tvExpression.append("Fuck");// putt the function here
//                }

                binding.tvExpression.append("\u00B2");

                // TODO Proveri i vzemi poslednoto kislo.slozi pred nego skoba i v apend zatariasta skoba skoba

                calculate();

                break;

        }

    }

    /** Click on any button that always adds its sign. Digit, bracket etc. */
    public void onDigitClick(View view){
        if(binding.tvExpression.getText().toString().equals("0")){
            binding.tvExpression.setText(((Button)view).getText());
        }else{
            binding.tvExpression.append(((Button)view).getText());
        }

        calculate();

    }

    /** Iterations when the operator button is clicked */
    public void onOperatorClick(View view){
        String expression = binding.tvExpression.getText().toString();
        if(!expression.isEmpty()){
            CharSequence textBtn = ((Button)view).getText();

            if(isLastDigit(expression)){
                binding.tvExpression.append(textBtn);
            }


            calculate();

        }


    }



    /** Erase the last character */
    public void onEraseClick(){
        String str = binding.tvExpression.getText().toString();


        str = removeLastChar(binding.tvExpression.getText().toString());
        binding.tvExpression.setText(str);

        try {
            Boolean isDigit = Character.isDigit(str.charAt(str.length()-1));

            if (isDigit){
                calculate();
            }


        }catch (Exception e){

            binding.tvResult.setText("");
            Log.e("Last char error", e.toString());

        }

    }


    /** Clear everything */
    public void onAllClearClick(){
        binding.tvExpression.setText("");
        binding.tvResult.setText("");

    }

    //    public void onEqualClick(){
//        calculate();
//        binding.tvExpression.setText(binding.tvResult.getText().toString().substring(1));
//    }



//TODO opishi x2 i viz zasto sled skoba ne moga da sloza znak a samo chislo dokato ne dam back buttona
    /** Calculate the expression */
    public void calculate(){

        String newExpression = binding.tvExpression.getText().toString();

        // if you do the calculation with . and % it gives NaN as an answer
        if(!newExpression.endsWith(".") && !newExpression.endsWith("%")){
            String txt = binding.tvExpression.getText().toString();
            txt = txt.replaceAll("x", "*");
            txt = txt.replaceAll("÷", "/");
            txt = txt.replaceAll("%", "/100*");
            txt = txt.replaceAll("sin\u207B\u00b9","arcsin");
            txt = txt.replaceAll("cos\u207B\u00b9","arccos");
            txt = txt.replaceAll("tan\u207B\u00b9","arctan");

            // count the last power how many
            txt = xPowTwoFuncPlacementBrackets(txt,"²" );


            txt = txt.replaceAll("\u00b2","^2");





            // expression =  new ExpressionBuilder(txt).build();
            expression =  new Expression(txt);
            boolean isDegMod = ((Toolbar)findViewById(R.id.toolbar)).getTitle().toString().equals(this.getString(R.string.btn_deg));
            if (isDegMod) {
                mXparser.setDegreesMode();
            } else {
                mXparser.setRadiansMode();
            }


            try {
                //double result = expression.evaluate();
                String result = String.valueOf(expression.calculate());

                binding.tvResult.setText("=" + result);
            }catch (ArithmeticException ex){
                Log.e("Evaluation Error", ex.toString());
                binding.tvResult.setText("Error");

            }


        }

    }



    /** Clear everything */
    public void onClearClick(){
        binding.tvExpression.setText("");

    }

    /**  Creation of an options meni for the action Bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //TODO We use it to select options in the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public void initiateOnCLickListeners(){

        // Initiate the variable for the input and output
        tv_expression = binding.tvExpression;
        tv_result = binding.tvResult;

        // Listeners Special buttons
        binding.btnUnitConverter.setOnClickListener(this);
        binding.btnCurrencyConverter.setOnClickListener(this);
        binding.btnHistory.setOnClickListener(this);

        // Listeners Scientific buttons
        binding.btnInv.setOnClickListener(this);
        binding.btnDegRad.setOnClickListener(this);
        binding.btnSin.setOnClickListener(this);
        binding.btnCos.setOnClickListener(this);
        binding.btnTan.setOnClickListener(this);
        binding.btnPlusMinus.setOnClickListener(this);
        binding.btnLn.setOnClickListener(this);
        binding.btnLog.setOnClickListener(this);
        binding.btnPeriod.setOnClickListener(this);
        binding.btnPlusCaret.setOnClickListener(this);
        binding.btnPi.setOnClickListener(this);
        binding.btnE.setOnClickListener(this);
        binding.btnLeftBracket.setOnClickListener(this);
        binding.btnRightBracket.setOnClickListener(this);
        binding.btnSquareRoot.setOnClickListener(this);

        // Listeners for regular buttons
        binding.btnZero.setOnClickListener(this);
        binding.btnOne.setOnClickListener(this);
        binding.btnTwo.setOnClickListener(this);
        binding.btnThree.setOnClickListener(this);
        binding.btnFour.setOnClickListener(this);
        binding.btnFive.setOnClickListener(this);
        binding.btnSix.setOnClickListener(this);
        binding.btnSeven.setOnClickListener(this);
        binding.btnEight.setOnClickListener(this);
        binding.btnNine.setOnClickListener(this);
        binding.btnDot.setOnClickListener(this);
        binding.btnEquals.setOnClickListener(this);
        binding.btnPlus.setOnClickListener(this);
        binding.btnMinus.setOnClickListener(this);
        binding.btnMultiply.setOnClickListener(this);
        binding.btnDivide.setOnClickListener(this);
        binding.btnPercent.setOnClickListener(this);
        binding.btnErase.setOnClickListener(this);
        binding.btnC.setOnClickListener(this);

    }


}

// TODO napravi rad/deg funcionalnostta. zapochni s debug
//TODO probvai tochkata sled vseki znak i iztrivaneto mu. ima mesta na koito ne triabva da izliza
// TODO pri iztrivane ima fukcii koito triabva da budat iztriti izcialo oste na purvia back buton
// TODO proveri isLastNumberDecimal kak reagira kak razdelia kato ima funcii