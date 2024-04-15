import java.io.*;

public class Gui {

    private static int numberOfQuestionsInGames=0;
    private static boolean stopGame=false;


    public static int getNumberOfQuestionsInGames (){
        return numberOfQuestionsInGames;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    static void exit(){
        if(Question.lastDecision()) System.exit(0);
    }

    static void startGame() {

        boolean corect=true;
        clearScreen();
        System.out.println("\nDzień dobry Hubert Urbański witam w kolejnym odcinku teokraterzy!");
        do {
            System.out.println("Aby rozpocząć grę naciśnij: g");
            System.out.println("Aby zobaczyć sterowanie naciśnij: s");
            System.out.println("Aby zakończyć grę naciśnij: w");
            String choice="";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                choice=reader.readLine();
            }
            catch (IOException ignored){
            }
            switch (choice) {
                case "g","G" -> corect =false;
                case "s","S" -> controlHelp();
                case "w","W" -> exit();
                default -> {
                    clearScreen();
                    System.out.println("Nie ma takiej opcji w menu");

                }
            }
        }while (corect);
    }

    static void getQuestion(int questionNumber, boolean[] usingLifeSaver){

        clearScreen();
        if (usingLifeSaver[0]) LifeSaver.generateCallToFriend();
        if (usingLifeSaver[1]) LifeSaver.generateFiftyFifty();
        if (usingLifeSaver[2]) LifeSaver.generateAudience();

        System.out.println("Pytanie za: "+walue(questionNumber) );
        System.out.print("Pozostały " + LifeSaver.howManyLifeSaver()+ " koła ratunkowe:");
        LifeSaver.whatHaveLifeSaver();
    }

    static void controlHelp (){
        clearScreen();
        System.out.println("Sterowanie:");
        System.out.println("Klawisz S pokazuje sterowanie");
        System.out.println("Klawisz W wyłącza grę");
        System.out.println("Klawisz Z zakańcza grę");
        System.out.println("Klawisz A wybiera odpowiedź A");
        System.out.println("Klawisz B wybiera odpowiedź B");
        System.out.println("Klawisz C wybiera odpowiedź C");
        System.out.println("Klawisz D wybiera odpowiedź D");
        System.out.println("Klawisz 1 wybiera koło: Zadzwoń do przyjaciela");
        System.out.println("Klawisz 2 wybiera koło: Pół na pół");
        System.out.println("Klawisz 3 wybiera koło: Pytanie do publiczności");
        System.out.println("Program nie rozróżnia wielkich i małych liter");
        System.out.println();
        System.out.println("Życzymy powodzenia");
        System.out.println("Naciśnij ENTER aby kontynuować");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            reader.readLine();
        }
        catch (IOException ignored){
        }
        clearScreen();
    }

    static void game(){

        stopGame=false;
        for (int i=0;i<12;i++){
            if (stopGame) break;
            numberOfQuestionsInGames=i;
            if(!Question.listQuestion[i].askQuestion(i)) {
                System.out.println("Niepoprawna odpowiedź. Poprawna odpowiedź to: "+
                        LifeSaver.changeAnswerToString(Question.listQuestion[i].getCorrectAnswer()));
                System.out.println("Naciśnij ENTER aby kontynuować.");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    reader.readLine();
                }
                catch (IOException ignored){
                }
                end(false);
            }
            else if(!stopGame){
                System.out.println("Poprawna odpowiedż. Naciśnij ENTER aby kontynuować.");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    reader.readLine();
                }
                catch (IOException ignored){
                }
            }
        }
        if (numberOfQuestionsInGames==11&&!stopGame){
            numberOfQuestionsInGames++;
            end(true);
        }

    }

    public static int countLineFast(String fileName) {

        int lines = 0;
        try (InputStream is = new BufferedInputStream(new FileInputStream(fileName))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars;
            boolean endsWithoutNewLine = false;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
                endsWithoutNewLine = (c[readChars - 1] != '\n');
            }
            if (endsWithoutNewLine) {
                ++count;
            }
            lines = count;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    static String walue (int questionNumber){
        switch (questionNumber){
            case 0 -> {
                return "500 zł";
            }
            case 1 -> {
                return "1 000 zł";
            }
            case 2 -> {
                return "2 000 zł";
            }
            case 3 -> {
                return "5 000 zł";
            }
            case 4 -> {
                return "10 000 zł";
            }
            case 5 -> {
                return "20 000 zł";
            }
            case 6 -> {
                return "40 000 zł";
            }
            case 7 -> {
                return "75 000 zł";
            }
            case 8 -> {
                return "125 000 zł";
            }
            case 9 -> {
                return "250 000 zł";
            }
            case 10 -> {
                return "500 000 zł";
            }
            case 11 -> {
                return "1 000 000 zł";
            }
            default -> {
                return "0 zł";
            }
        }
    }

    static void end(boolean lostWin) {
        stopGame=true;
        clearScreen();
        if(!lostWin){
            System.out.println("Przegrana");
            System.out.print("Wygrałeś: ");
            if (numberOfQuestionsInGames<2){
                System.out.println("0 zł");
            }
            else if(numberOfQuestionsInGames>6){
                System.out.println("40 000 zł");
            }
            else {
                System.out.println("1 000 zł");
            }
        }
        else{
            System.out.print("Gratulacje wygrałeś: " + walue(numberOfQuestionsInGames-1));
        }
        System.out.println();
        System.out.println("Nacisij ENTER aby zagrać ponowanie.");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            reader.readLine();
        }
        catch (IOException ignored){
        }

    }
}
