public class Valute {
    public String id;
    public String numcode;
    public String charcode;
    public String nominal;
    public String name;
    public String value;
    public Valute(String id, String numcode, String charcode, String nominal, String name, String value){
        this.id = id;
        this.numcode = numcode;
        this.charcode = charcode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }
    public Valute(){}

    public String getValue() {
        return value.replace(",", ".");
    }


}
