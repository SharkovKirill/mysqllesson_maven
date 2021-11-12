public class WrongData extends Exception{
    public String toString(){
        return "\n\nВы ввели неправильные данные в параметр даты." +
                "Пример даты: 21/02/2019\n\n";
    }
}
