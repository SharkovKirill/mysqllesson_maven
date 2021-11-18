import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if(args[0].equals("updateDB")){
            SQLdb.updateDB(parse(args[1]));
        } else {
            SQLdb.check(parse(args[0]), args[1]);
        }

    }
    public static String parse(String date){
        String[] subStr;
        String delimeter = "-";
        subStr = date.split(delimeter);
        String newDate = subStr[2]+"/"+subStr[1]+"/"+subStr[0];
        System.out.println(newDate);
        return newDate;
    }
}
