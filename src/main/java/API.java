import kong.unirest.Unirest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


//Для получаемых данных по API требуется создать соответствующую объектную модель
//        (класс для сохранения этих данных)
public class API {
    public static String id = "";
    public static String name = "";
    public static String val = "";
    public static String nominal = "";
    public static void getResponse(String date, String currency) {
        try {

            String response = Unirest.get("https://www.cbr.ru/scripts/XML_daily.asp?date_req={date}").routeParam("date", date).asString().getBody();
//            System.out.println(response);

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = builderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(new InputSource(new StringReader(response)));

            String errorText = document.getDocumentElement().getTextContent();
//            System.out.print(errorText);
            if (errorText.replace("\n", "").equals("Error in parameters")) {
                throw new WrongData();
            }

            NodeList charCodes = document.getDocumentElement().getElementsByTagName("CharCode");
            for (int i = 0; i < charCodes.getLength(); i++) {
                Node charCode = charCodes.item(i);
                String docCurrency = charCode.getTextContent();
//                System.out.println(docCurrency);
                if (docCurrency.equals(currency)) {
//                    System.out.println(charCode.getParentNode().getAttributes().getNamedItem("ID"));
                    id = charCode.getParentNode().getAttributes().getNamedItem("ID").toString();
                    break;
                }
            }
            if (id.equals("")) throw new WrongCurrency();
            NodeList valutes = document.getDocumentElement().getElementsByTagName("Valute");
            for (int i = 0; i < valutes.getLength(); i++) {
                Node valute = valutes.item(i);
                String docValute = valute.getAttributes().getNamedItem("ID").toString();
                if (docValute.equals(id)) {
//                    System.out.println("Совпало");
//                    System.out.println(docValute);
                    NodeList list = valute.getChildNodes();
//                    System.out.println(list);
                    for (int iter = 0; iter < list.getLength(); iter++) {
                        Node parametr = list.item(iter);
                        if (parametr.getNodeName().equals("Name")) {
                            name = parametr.getTextContent();
                        }
                        if (parametr.getNodeName().equals("Value")) {
                            val = parametr.getTextContent();
                        }
                        if (parametr.getNodeName().equals("Nominal")) {
                            nominal = parametr.getTextContent();
                        }
                    }
                    break;
                }
            }
            System.out.println(nominal + " " + name + " = " + val + " Российских рубля");
            id = "";
        } catch (WrongData | WrongCurrency e) {
            System.out.println(e);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
//    public static ArrayList getAllCurrency(String date){
//        try{
//            String response = Unirest.get("https://www.cbr.ru/scripts/XML_daily.asp?date_req={date}").routeParam("date", date).asString().getBody();
////            System.out.println(response);
//
//            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder;
//            docBuilder = builderFactory.newDocumentBuilder();
//            Document document = docBuilder.parse(new InputSource(new StringReader(response)));
//
//            String errorText = document.getDocumentElement().getTextContent();
////            System.out.print(errorText);
//            if (errorText.replace("\n", "").equals("Error in parameters")) {
//                throw new WrongData();
//            }
//            NodeList valutes = document.getDocumentElement().getElementsByTagName("Valute");
//            ArrayList<ArrayList> allForAll = new ArrayList<>();
//            for (int i = 0; i < valutes.getLength(); i++) {
//                ArrayList<String> allForOne = new ArrayList<>();
//                Node valute = valutes.item(i);
//                allForOne.add(valute.getAttributes().getNamedItem("ID").toString());
//                NodeList list = valute.getChildNodes();
//                for (int iter = 0; iter < list.getLength(); iter++) {
//                    Node parametr = list.item(iter);
//                    if (!(parametr.getNodeName().equals("Nominal"))) {
//                        allForOne.add(parametr.getTextContent());
//                    }
//                }
////                System.out.println(allForOne);
//                allForAll.add(allForOne);
//            }
////            System.out.println(allForAll);
//            return allForAll;
//        }catch (WrongData e) {
//            System.out.println(e);
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            id="";
//        }
//        return null;
//    }
    public static ArrayList<Valute> getAllCurrencyTest(String date){
        try{
            String response = Unirest.get("https://www.cbr.ru/scripts/XML_daily.asp?date_req={date}").routeParam("date", date).asString().getBody();
//            System.out.println(response);

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = builderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(new InputSource(new StringReader(response)));

            String errorText = document.getDocumentElement().getTextContent();
//            System.out.print(errorText);
            if (errorText.replace("\n", "").equals("Error in parameters")) {
                throw new WrongData();
            }
            NodeList valutes = document.getDocumentElement().getElementsByTagName("Valute");
            ArrayList<Valute> allForAll = new ArrayList<>();
            for (int i = 0; i < valutes.getLength(); i++) {
                ArrayList<String> allForOne = new ArrayList<>();
                Node valute = valutes.item(i);
                Valute newValute = new Valute();
                newValute.id = valute.getAttributes().getNamedItem("ID").toString().replace("ID=","" );
                NodeList list = valute.getChildNodes();
                for (int iter = 0; iter < list.getLength(); iter++) {
                    Node parametr = list.item(iter);
                    if (parametr.getNodeName().equals("NumCode")) {
                        newValute.numcode = parametr.getTextContent();
                    }
                    if (parametr.getNodeName().equals("CharCode")) {
                        newValute.charcode = parametr.getTextContent();
                    }
                    if (parametr.getNodeName().equals("Nominal")) {
                        newValute.nominal = parametr.getTextContent();
                    }
                    if (parametr.getNodeName().equals("Name")) {
                        newValute.name = parametr.getTextContent();
                    }
                    if (parametr.getNodeName().equals("Value")) {
                        newValute.value = parametr.getTextContent();
                    }
                }
//                System.out.println(allForOne);
                allForAll.add(newValute);
            }
//            System.out.println(allForAll);
            return allForAll;
        }catch (WrongData e) {
            System.out.println(e);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } finally {
            id="";
        }
        return null;
    }
}
