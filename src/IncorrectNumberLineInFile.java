public class IncorrectNumberLineInFile extends Exception {

    IncorrectNumberLineInFile(int number){
        System.out.println("Bład pliku w pliku o nazwie: question_"+number+".txt jest niewłaśiwa liczba wierszy!");
    }

}
