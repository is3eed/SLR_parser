package slr_parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;

/*
        CPCS 302 Compiler Construction
        Group Project - Part III (SLR parser) 
        Group Members as follows: 

            Saeed Mohammed bin Khamees #1845688
            Faris Adel Alahmadi        #1845562
            Ahmad Abdulrahman Alhelali #1847506
 */
public class SLR_Parser {

    public static void main(String[] args) throws FileNotFoundException {
        Hashtable<String, String> table = new Hashtable<>(); // a hashtable to store action table
        Hashtable<String, String> cfg = new Hashtable<>(); // a hashtable to store goto table 
        File input = new File("input.txt"); // an input file contains arithmetic epxressions
        Scanner sc = new Scanner(input); //  a scanner to read from file
        Stack<String> s = new Stack<>(); // a stack
        s.push("0");
        int ip = 0, counter = 0; // ip = expression pointer, counter is length of a rhs * 2 to pop that number of elements
        String lhs, rhs, go; // store lhs, rhs and goto symbol
        String action = ""; // store action from table (hashtable) 
        String state = "0", a = ""; // 
        table.put("0_id", "s5");table.put("0_(", "s4"); table.put("0_GOTO", "1_2_3");
        table.put("1 +", "s6"); table.put("1_$", "accept");
        table.put("2_+", "r2"); table.put("2_*", "s7"); table.put("2_)", "r2"); table.put("2_$", "r2");
        table.put("3_+", "r4"); table.put("3_*", "r4"); table.put("3_)", "r4"); table.put("3_$", "r4");
        table.put("4_id", "s5");table.put("4_(", "s4"); table.put("4_GOTO", "8_2_3");
        table.put("5_+", "r6"); table.put("5_*", "r6"); table.put("5_)", "r6"); table.put("5_$", "r6");
        table.put("6_id", "s5");table.put("6_(", "s4"); table.put("6_GOTO", "~_9_3");
        table.put("7_id", "s5");table.put("7_(", "s4"); table.put("7_GOTO", "~_~_10");
        table.put("8_+", "s6"); table.put("8_)", "s11");
        table.put("9_+", "r1"); table.put("9_*", "s7"); table.put("9_)", "r1"); table.put("9_$", "r1");
        table.put("10_+", "r3");table.put("10_*", "r3");table.put("10_)", "r3");table.put("10_$", "r3");
        table.put("11_+", "r5");table.put("11_*", "r5");table.put("11_)", "r5");table.put("11_$", "r5");
        cfg.put("0", "E`_E");
        cfg.put("1", "E_E + T");
        cfg.put("2", "E_T");
        cfg.put("3", "T_T * F");
        cfg.put("4", "T_F");
        cfg.put("5", "F_( E )");
        cfg.put("6", "F_id");
        while (sc.hasNext()) {
            String exp[] = sc.nextLine().split(" ");
            System.out.print("Right most derivation for the arithmetic expression ");
            for (int i = 0; i < exp.length; i++) {
                System.out.print(exp[i] + " ");
            }
            System.out.println(":");
            while (true) {
                state = s.peek();
                a = exp[ip];
                action = table.get(state + "_" + a);
                if (action == null) {
                    System.err.println("Syntax error");
                    break;
                }
                if (action.contains("s")) {
                    s.push(a);
                    s.push(table.get(state + "_" + a).substring(1));
                    System.out.println("Shift " + table.get(state + "_" + a).substring(1));
                    ip++;
                } else if (action.contains("r")) {
                    String temp = table.get(state + "_" + a).substring(1);
                    lhs = cfg.get(temp).split("_")[0];
                    rhs = cfg.get(temp).split("_")[1];
                    counter = (cfg.get(temp).split("_")[1]).split(" ").length * 2;
                    while (counter != 0) {
                        s.pop();
                        counter--;
                    }
                    state = s.peek();
                    s.push(lhs);
                    switch (lhs) {
                        case "E":
                            go = table.get(state + "_GOTO").split("_")[0];
                            if (!go.equals("~")) {
                                s.push(go);
                            } else {
                                System.err.println("Syntax Error");
                            }
                            break;
                        case "T":
                            go = table.get(state + "_GOTO").split("_")[1];
                            if (!go.equals("~")) {
                                s.push(go);
                            } else {
                                System.err.println("Syntax Error");
                            }
                            break;
                        case "F":
                            go = table.get(state + "_GOTO").split("_")[2];
                            if (!go.equals("~")) {
                                s.push(go);
                            } else {
                                System.err.println("Syntax Error");
                            }
                            break;
                    }
                    System.out.println("Reduce by " + lhs + "-->" + rhs);

                } else if (action.equals("accept")) {
                    System.out.println("Accept");
                    break;
                } else {
                    System.err.println("Syntax Error");
                }
            }
            s.clear();
            s.push("0");
            ip = 0;
            System.out.println("");

        }
    }
}
