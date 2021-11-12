public class WrongCurrency extends Exception{
    public String toString(){
        return "\n\nВы ввели неправильные данные в параметр валюты, пример правильного ввода: USD\n\n";
    }
}