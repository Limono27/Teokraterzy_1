public class Main {

    public static void main(String[] args) {

        Gui.startGame();
        for (;;) {
            Question.questionsLoad();
            Gui.game();
        }

    }

}