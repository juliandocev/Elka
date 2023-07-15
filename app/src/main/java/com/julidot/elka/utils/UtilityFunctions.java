package com.julidot.elka.utils;

import org.mariuszgromada.math.mxparser.StringUtils;

public class UtilityFunctions {


    /** Remove the last character of a string */
    public static String removeLastChar(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, str.length() - 1);
    }

    /** Get the last character of a string */
    public static String getLastChar(String str){
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(str.length() - 1);
    }

    /** Check if last number is a decimal number */
    public static Boolean isLastNumberDecimal(String str){

        // takes all the number(decimal or hole numbers)
        String[] numbers = str.split("[^\\d.]+");

        // String[] operators = str.split("[\\d.]+");

        String lastNumber = numbers[numbers.length - 1];
        return lastNumber.indexOf('.') >= 0;

    }


    /** Check if last character is a digit or operator */
    public static Boolean isLastDigit(String str){

        return !str.endsWith(".") && !str.endsWith("+") && !str.endsWith("-") && !str.endsWith("/")
                && !str.endsWith("*")&& !str.endsWith("%")&& !str.endsWith("(")&& !str.endsWith(")");
    }

    /** Takes the number before the pow and put everything in brackets*/
    public static String putNumberAndPowInBrackets(String txt, String power) {

        StringBuilder newExpression = new StringBuilder();
        //  than take the last number
        // takes all the number(decimal or hole numbers)
        String[] numbers = txt.split("[^\\d.]+");
        // take the last number from the string
        String lastNumber = numbers[numbers.length - 1];
        // make a string without the last number

        //I want to make a string without the last number
        for (int i = 0; i < numbers.length-3; i++){
            newExpression.append(numbers[i]);
        }


        // add to last number the pow and put it into brackets
        String newLastNumberAndPowInBrackets="(" + lastNumber + power +")";

        String finalExpression = String.join(newExpression,newLastNumberAndPowInBrackets);

        return finalExpression;
    }

    public static String xPowTwoFuncPlacementBrackets(String txt,String powerChar) {

        // for loop counting backwards. from one side you put every loop a bracket behind the special character

        String newString = txt;
        int count =0;
        StringBuilder newExpression = new StringBuilder();
        while(newString.endsWith(powerChar)){
            newString  = removeLastChar(newString);
            count ++;

        }
        // takes all the number(decimal or hole numbers)
        String[] numbers = newString.split("[^\\d.]+");
        // take the last number from the string
        String lastNumber = numbers[numbers.length - 1];
        int index = newString.lastIndexOf(lastNumber);

        StringBuilder startBrackets= new StringBuilder();
        StringBuilder endString = new StringBuilder();
        while(count>0){
            startBrackets.append("(");
            endString.append(powerChar).append(")");
            count--;
        }
        newString = newString.substring(0, index) + startBrackets + newString.substring(index) + endString;

//        for (int i = txt.length()-1; i >=0 ; i--) {
//            if(txt.charAt(i) == powerChar){
//                count++;
//            }
//        }


//        for (int i = txt.length()-1; i >=0 ; i--){
//            for (char currentChar : txt.toCharArray()) {
//                Character replacementChar = charReplacementMap.get(currentChar);
//                builder.append(replacementChar != null ? replacementChar : currentChar);
//            }

        return newString;
        }
        // from other side you count until you find a number
        // than you put the number of brackets before and export the result






}
