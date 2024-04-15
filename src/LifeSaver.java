import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LifeSaver {

    private static boolean callToFriend = false;
    private static boolean fiftyFifty = false;
    private static boolean audience = false;
    private static final int[] percent = new int[]{-1,-1,-1,-1};
    private static String friendFilesName = null;
    private static int numberOfLineFriendFiles = 0;
    private static int[] friendAnswers;


    static boolean getCallToFriend(){
        return callToFriend;
    }
    static boolean getFiftyFifty(){
        return fiftyFifty;
    }
    static boolean getAudience(){
        return audience;
    }


    static void startGame(){

        System.out.println("Rozpoczynamy grę.");
        callToFriend = true;
        fiftyFifty = true;
        audience = true;
        for(int i =0;i<4;i++){
            percent[i]=-1;
        }

    }

    static int howManyLifeSaver() {

        int sum = 0;
        if (callToFriend) sum++;
        if (fiftyFifty) sum++;
        if (audience) sum++;
        return sum;

    }

    static void whatHaveLifeSaver(){

        if (callToFriend) {
            System.out.print(" 1. telefon do przyjaciela");
            if (fiftyFifty||audience) System.out.print(",");
        }
        if (fiftyFifty) {
            System.out.print(" 2. 50:50");
            if (audience) System.out.print(",");
        }
        if (audience) System.out.println(" 3. pytanie do publiczności");

    }

    static void callToFriend(String[] answer, int correct, Question quest) {
        if (!callToFriend) return;
        callToFriend=false;
        friendAnswers = null;
        int choice;
        do {
            choice = (int) (Math.random() * 4);
        } while (choice == 1 && quest.getUsingLifeSaverFiftyFifty());
        switch (choice) {
            case 0 -> {
                friendFilesName = "friend/friend_1.txt";
                friendAnswers = new int[]{-1};
            }
            case 1 -> {
                friendFilesName = "friend/friend_2.txt";
                friendAnswers = new int[]{-1, -1};
            }
            case 2 -> friendFilesName = "friend/friend_nAll.txt";
            case 3 -> {
                friendFilesName = "friend/friend_n1.txt";
                friendAnswers = new int[]{-1};
            }
        }
        int howManyLine = Gui.countLineFast(friendFilesName);
        numberOfLineFriendFiles = (int) (Math.random() * howManyLine);
        if (friendAnswers != null) {
            boolean isCorrect = Math.random() > (5.0 + Gui.getNumberOfQuestionsInGames() / 36.0);
            int k = 0;
            if (isCorrect) {
                if (choice==3){
                    do{
                        friendAnswers[0]=(int)(Math.random()*4);
                    }while  (answer[friendAnswers[0]].equals(" ")||friendAnswers[0]==correct);
                }
                else
                {
                    friendAnswers[0] = correct;
                    k = 1;
                }
            }
            for (; k < friendAnswers.length; k++) {
                boolean operator;
                do {
                    operator=false;
                    friendAnswers[k] = (int) (Math.random() * 4);
                    for (int j = 0; j < friendAnswers.length; j++) {
                        if (k == j) continue;
                        if (friendAnswers[k] == friendAnswers[j]) {
                            operator = true;
                            break;
                        }
                    }
                    if (answer[friendAnswers[k]].equals(" ")) operator = true;
                } while (operator);
            }
        }
    }

    static void fiftyFifty(String[] answer, int correct){
        if (!fiftyFifty) return;
        fiftyFifty=false;
        int[] num = new int[]{-1,-1};
        for(int i=0;i<2;i++){
            do{
               num[i]=(int)(Math.random()*4);
            }while ((correct==num[i])||(num[0]==num[1]));
            answer[num[i]]=" ";
        }
    }

    static void audience(String[] answer, int correct, Question quest){
        if (!audience) return;
        audience=false;
        double[] per;
        double sum=0;
        double multipleter;
        if(!fiftyFifty&&quest.getUsingLifeSaverFiftyFifty()) {
            per = new double[2];
            per[0]=Math.random()*2.25;
        }
        else {
            per = new double[4];
            per[0]=Math.random()*3;
        }
        for (int i=1; i<per.length; i++){
            per[i]=Math.random();
            sum+=per[i];
        }
        sum+=per[0];
        multipleter=1/sum;
        for (int i=0; i<per.length; i++){
            per[i]=per[i]*multipleter;
        }
        for(int i=0;i<4;i++){
            if(!answer[i].equals(" ")){
                if(i==correct){
                    percent[i]=(int)(per[0]*100);
                }
                else {
                    int temp;
                    do {
                        temp = (int) ((Math.random() * (per.length - 1)) + 1);
                    }while(per[temp]==-1);
                    percent[i]= (int) Math.round((per[temp])*100);
                    per[temp]=-1;
                }
            }
        }
        sum=100;
        for (int i=0; i<4; i++){
            if(percent[i]!=-1){
                sum -= percent[i];
            }
        }
        int temp;
        do{
            temp=(int)(Math.random()*4);
        }while(percent[temp]==-1);
        percent[temp]+=sum;
    }

    static void generateCallToFriend(){
        String bufor="";
        try (BufferedReader reader = new BufferedReader(new FileReader(friendFilesName))) {
            for (int i = 0; i < numberOfLineFriendFiles; i++) {
                reader.readLine();
            }
            bufor = reader.readLine();
        }
        catch (IOException ignored) {
        }
        int iterator=0;
        do{
            if (bufor.indexOf('#')!=-1) {
                bufor = bufor.substring(0, bufor.indexOf('#')) +
                        changeAnswerToString(friendAnswers[iterator]) +
                        bufor.substring(bufor.indexOf('#')+1);
                iterator++;
            }
            else break;
            if (iterator==friendAnswers.length) break;
        }while (true);
        System.out.println(bufor);
    }

    static void generateFiftyFifty(){
    }

    static void generateAudience(){
        System.out.println("Głosy publiczności:");
        if(percent[0]!=-1){
            System.out.println("Odpowiedź A: "+percent[0]+"%");
        }
        if(percent[1]!=-1){
            System.out.println("Odpowiedź B: "+percent[1]+"%");
        }
        if(percent[2]!=-1){
            System.out.println("Odpowiedź C: "+percent[2]+"%");
        }
        if(percent[3]!=-1){
            System.out.println("Odpowiedź D: "+percent[3]+"%");
        }
    }

    static public String changeAnswerToString (int answer){
        switch (answer){
            case 0 -> {
                return "A";
            }
            case 1 -> {
                return "B";
            }
            case 2 -> {
                return "C";
            }
            case 3 -> {
                return "D";
            }
            default -> {
                return "";
            }
        }
    }

}
