import java.io.*;
import java.util.Objects;

public class Question {

    private String question;
    private final String[] answers;
    private final int correctAnswer;
    private int playerAnswer;
    private final boolean[] usingLifeSaver = new boolean[] {false,false,false};
    static Question[] listQuestion = new Question[12];


    public boolean getUsingLifeSaverFiftyFifty (){
        return usingLifeSaver[1];
    }

    public int getCorrectAnswer(){
        return correctAnswer;
    }

    private Question (String address, int number){

        String[] bufor = new String[4];
        try (BufferedReader reader = new BufferedReader(new FileReader(address))) {
            for(int i=0;i<number*5;i++) {
                reader.readLine();
            }
            question = reader.readLine();
            for (int i=0;i<4;i++){
                bufor[i]=reader.readLine();
            }
        }
        catch (IOException ignored) {
        }
        answers = new String[] {null, null, null, null};
        correctAnswer = (int)(Math.random()*4);
        answers[correctAnswer]=bufor[0];
        for (int i=1; i<4; i++){
            int j;
            do{
                j = (int)(Math.random()*4);
            }while (answers[j]!=null);
            answers[j]=bufor[i];
        }
    }

    static void questionsLoad (){

        LifeSaver.startGame();
        int[] numberOfQuestions = new int[5];
        numberOfQuestions[0] = Gui.countLineFast("question/question_0.txt");
        numberOfQuestions[1] = Gui.countLineFast("question/question_1.txt");
        numberOfQuestions[2] = Gui.countLineFast("question/question_2.txt");
        numberOfQuestions[3] = Gui.countLineFast("question/question_3.txt");
        numberOfQuestions[4] = Gui.countLineFast("question/question_4.txt");
        try{
            for(int i=0;i<5;i++){
                if(numberOfQuestions[i]%5!=0) throw new IncorrectNumberLineInFile(i);
                numberOfQuestions[i] =(numberOfQuestions[i]-1)/5;
            }
        }
        catch (IncorrectNumberLineInFile e){
            System.out.println("Nie można kontynuować rozgrywki!");
            return;
        }

        double multipleter = 1;
        int[] level = new int[] {0,0,0,1,1,2,2,2,3,3,3,4};

        if (Math.random()<0.2) {
            level[1]=1;
            multipleter += 0.10;
        }
        else multipleter -= 0.10;

        if (Math.random()*multipleter<0.7) {
            level[2]=1;
            multipleter += 0.10;
        }
        else multipleter -= 0.10;

        if (Math.random()*multipleter<0.5) {
            level[4]=2;
            multipleter += 0.10;
        }
        else multipleter -= 0.10;

        if (Math.random()*multipleter<0.3) {
            level[6]=3;
            multipleter += 0.10;
        }
        else multipleter -= 0.10;

        if (Math.random()*multipleter<0.65) {
            level[7]=3;
            multipleter += 0.10;
        }
        else multipleter -= 0.10;

        if (Math.random()*multipleter<0.15) {
            level[9]=4;
            multipleter += 0.10;
        }
        else multipleter -= 0.10;

        if (Math.random()*multipleter<0.8)
            level[10]=4;
        int[][] random = new int[5][5];
        for (int i=0; i<5; i++){
            for (int j=0;j<5;j++){
                if (j==0) random[i][j]=0;
                else random[i][j]=-1;
            }
        }
        int temp;
        boolean iterator;
        for (int i=0;i<12;i++){
            do {
                iterator=false;
                temp = (int) (Math.random() * numberOfQuestions[level[i]]);
                here:{
                    for (int j = 1; j < 5; j++) {
                        if (temp == random[level[i]][j]) {
                            iterator = true;
                            break here;
                        }
                    }
                    random[level[i]][++(random[level[i]][0])]=temp;
                }
            }while (iterator);
            switch (level[i]) {
                case 0 -> listQuestion[i] = new Question("question/question_0.txt", temp);
                case 1 -> listQuestion[i] = new Question("question/question_1.txt", temp);
                case 2 -> listQuestion[i] = new Question("question/question_2.txt", temp);
                case 3 -> listQuestion[i] = new Question("question/question_3.txt", temp);
                case 4 -> listQuestion[i] = new Question("question/question_4.txt", temp);

            }
        }
    }

    boolean askQuestion (int a){

        do {
            Gui.getQuestion(a, usingLifeSaver);
            System.out.println();
            System.out.println("Pytanie: "+ question);
            System.out.println("Odp A: " + answers[0]);
            System.out.println("Odp B: " + answers[1]);
            System.out.println("Odp C: " + answers[2]);
            System.out.println("Odp D: " + answers[3]);
            String choice = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                choice = reader.readLine();
            }
            catch (IOException ignored) {
            }
            switch (choice) {
                case "a", "A" -> {
                    if(answers[0].equals(" ")) continue;
                    playerAnswer = 0;
                }
                case "b","B" -> {
                    if (answers[1].equals(" ")) continue;
                    playerAnswer = 1;
                }
                case "c","C" -> {
                    if (answers[2].equals(" ")) continue;
                    playerAnswer = 2;
                }
                case "d","D" -> {
                    if (answers[3].equals(" ")) continue;
                    playerAnswer = 3;
                }
                case "w","W" -> Gui.exit();
                case "s", "S" -> {
                    Gui.controlHelp();
                    continue;
                }
                case "z", "Z" -> {
                    if(lastDecision()){
                        Gui.end(true);
                        return true;
                    }
                    continue;
                }
                case "1" -> {
                    if (LifeSaver.getCallToFriend()) {
                        if (lastDecision()) {
                            LifeSaver.callToFriend(answers, correctAnswer, this);
                            usingLifeSaver[0] = true;
                        }
                        continue;
                    }
                    System.out.println("Nie masz już tego koła");
                    continue;
                }
                case "2" -> {
                    if (LifeSaver.getFiftyFifty()) {
                        if (lastDecision()) {
                            LifeSaver.fiftyFifty(answers, correctAnswer);
                            usingLifeSaver[1] = true;
                        }
                        continue;
                    }
                    System.out.println("Nie masz już tego koła");
                    continue;
                }
                case "3" -> {
                    if (LifeSaver.getAudience()) {
                        if (lastDecision()) {
                            LifeSaver.audience(answers, correctAnswer, this);
                            usingLifeSaver[2] = true;
                        }
                        continue;
                    }
                    System.out.println("Nie masz już tego koła");
                    continue;
                }
                default -> {
                    Gui.clearScreen();
                    System.out.println("Nie ma takiej opcji w menu");
                    continue;
                }
            }
            if (lastDecision()) break;

        }while (true);
        return correctAnswer == playerAnswer;
    }

    static boolean lastDecision(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ostateczna decyzja? (t/n)");
        String decysion=null;
        try {
            decysion = reader.readLine();
        }
        catch (IOException ignored) {
        }
        switch (Objects.requireNonNull(decysion)){

            case "t", "T" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

}