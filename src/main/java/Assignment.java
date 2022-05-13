import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public abstract class Assignment {

    private static ArrayList<Pattern> patterns=new ArrayList<>();
    private static ArrayList<Yarns> yarns=new ArrayList<>();
    private static Scanner scanner=new Scanner(System.in);
    private static ArrayList<Pattern> purchase=new ArrayList<>();
    private static Double subtotal=0.0;
    private static Random rnd=new Random();
    private static Boolean end=Boolean.FALSE;

    public static void main(String[] args) {

//  <menu>
        settingBaseYarns();
        PatternsFromXml("src/main/resources/patterns.xml");
        YarnsFromXml("src/main/resources/Yarns.xml");


            menu();


//  </menu>
    }




    private static void menu(){
        while(!end) {
            System.out.print("What do you want to do?");
            int choise = 0;
            while (choise <= 0 || choise > 8) {
                System.out.println(
                        "\n1 - Modify the pattern" +
                        "\n2 - Add a new pattern" +
                        "\n3 - Add a new yarn" +
                        "\n4 - Delete a pattern" +
                        "\n5 - Listing stored things" +
                        "\n6 - Registering a purchase" +
                        "\n7 - Close program"
                );
                choise = numberIn();
                switch (choise) {
                    case 1 -> {
                        modifyPattern();
                        break;
                    }
                    case 2 -> {
                        addPattern();
                        break;
                    }
                    case 3 -> {
                        addYarn();
                        break;
                    }
                    case 4 -> {
                        deletePattern();
                        break;
                    }
                    case 5 -> {
                        availableLists();
                        break;
                    }
                    case 6 -> {
                        purchaseDone();
                        break;
                    }
                    case 7 -> {

                        savePattern("src/main/resources/patterns.xml");
                        saveYarn("src/main/resources/Yarns.xml");
                        saveOrder("src/main/resources/order.xml");
                        end = Boolean.TRUE;
                    }
                    default -> System.out.println("You couldn't even select the right option.\nMaybe next time...");
                }
            }
        }
    }


    private static void availableLists(){
        System.out.println(
                "List out patterns - 1" +
                "\nList out yarns - 2" +
                "\nShow ordered items and subtotal - 3" +
                "\nreturn to menu - 4"
        );
        int choice=numberIn();
        switch (choice){
            case 1->{
                listPattern();
                break;
            }
            case 2->{
                listYarn();
                break;
            }
            case 3->{
                receiptShow();
                break;
            }
            case 4->{
                menu();
            }
            default->{}
        }
    }

        private static void listPattern(){
        if(patterns.size()>0) {
            System.out.println("The available patterns: ");
            for (Pattern pattern:patterns) {
                System.out.println("pattern name: " + pattern.getName() +
                        "\t color of the yarn used: " + pattern.getYarn().getColour().toString() +
                        "\t weight of the yarn used: " + pattern.getYarn().getWeight().toString() +
                        "\t pattern's price: " + pattern.getPrice());
            }
        }
        else{
            System.out.println("There are no patterns available. Try adding one.");
            menu();
        }
    }

        private static void listYarn() {
        if(yarns.size()>0) {
            System.out.println("The yarns in stock: ");
            for (int i = 0; i < patterns.size(); i++) {
                System.out.println(" color of the yarn: " + patterns.get(i).getYarn().getColour().toString() +
                        "\t weight of the yarn: " + patterns.get(i).getYarn().getWeight().toString());

            }
        }
        else{
            System.out.println("There are no Yarns available. Try adding one.");
            menu();
        }
    }

        private static void receiptShow(){
            if(patterns.size()>0) {
                System.out.println("The available patterns: ");
                for (Pattern pattern : purchase) {
                    System.out.println("pattern name: " + pattern.getName() +
                            "\t color of the yarn used: " + pattern.getYarn().getColour().toString() +
                            "\t weight of the yarn used: " + pattern.getYarn().getWeight().toString() +
                            "\t pattern's price: " + pattern.getPrice());
                }
            }
            else {
                System.out.println("There are no patterns available. Try adding one.");
                menu();
            }
            System.out.println("Your subtotal is: "+subtotal);
        }

    private static void purchaseDone(){
        System.out.println("Name the pattern that was purchased : ");
        String nameOfPatter=scanner.next();
       purchase.add(
                new Pattern(
                        patterns.get(findPattern("name",nameOfPatter)).getName(),
                        patterns.get(findPattern("name",nameOfPatter)).getYarn(),
                        patterns.get(findPattern("name",nameOfPatter)).getPrice()
                )
        );
       subtotal+=patterns.get(findPattern("name",nameOfPatter)).getPrice();
    }

    private static void addPattern(){
        System.out.println("Name the pattern you want to add : ");
        String name=scanner.next();
        System.out.println("What color yarn do you want to add? : ");
        int i=0;
        Colors colour=colorIn();
        System.out.println("What wheight yarn do you want to add? (Heavy , Standard, Light , Baby) : ");
        weightEnum weght=weightIn();
        for (Yarns yarnes : yarns){
            if(yarns.get(i).getColour().equals(colour)){
                if(yarns.get(i).getWeight().equals(weght))
                    patterns.add(new Pattern(name,yarns.get(i)));
                else
                    yarns.add(new Yarns(colour,weght,rnd.nextDouble(1800,2600)));
            }

        }

    }

    private static void deletePattern(){
        if(patterns.size()>0) {
            System.out.print("Enter name of the pattern, what you want to delete: ");
            try {
                patterns.remove(findPattern("name", scanner.next()));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("There are no patterns available.");
            menu();
        }
    }

    private static void modifyPattern(){
        System.out.println("What do you want to do?");
        int choise=0;
        while(choise<=0||choise>3) {
                System.out.println(
                "\nModify the pattern' color - 1" +
                "\nModify the pattern's name - 2" +
                "\nGive a discount on a pattern - 3" +
                "\nReturn to the menu - 4");
            choise = numberIn();
            switch (choise) {
                case 1 -> {modifyPatternColor(); break;}
                case 2 -> {modifyPatternName(); break;}
                case 3 -> {discountOnPattern(); break;}
                case 4 -> {menu();break;}
                default -> System.out.println("Please select one of these:");
            }
        }
    }

        private static void modifyPatternName(){
            if(patterns.size()>0) {
            System.out.println("Please enter the name you want to modify: ");
            String patternName = scanner.next();
            int indexOfModification = findPattern("name", patternName);
            if (findPattern("name", patternName) == -1) {
                System.out.println("A pattern under this name doesn't exist. Please try adding one ( 1 ) or try again ( 0 ).");
                int choise = numberIn();
                switch (choise) {
                    case 1 -> addPattern();
                    case 0 -> modifyPatternName();
                    default -> {
                        System.out.println("You might have mistiped. Returning you to modify menu.");
                        modifyPattern();
                    }
                }
            }
            System.out.println("Please enter the name you want to modify it to: ");
            String newPatternName = scanner.next();
            patterns.get(indexOfModification).setName(newPatternName);
            System.out.println("It has been succesfully modified. ");
            }
            else{
                System.out.println("There are no patterns available.");
                menu();
            }
        }

        private static void modifyPatternColor() {
            if (yarns.size() > 0) {
                System.out.println("please add the pattern's name you want to change");
                String patternName = scanner.next();
                if (findPattern("name", patternName) == -1) {
                    System.out.println("A pattern under this name doesn't exist. Please try adding one ( 1 ) or try again ( 2 ).");
                    int choise = numberIn();
                    switch (choise) {
                        case 1 -> addPattern();
                        case 2 -> modifyPatternColor();
                        default -> {
                            System.out.println("Returning you to modify menu");
                            modifyPattern();
                        }
                    }
                }
                System.out.println("please add the color you want to change");
                Colors patternColor = colorIn();
                if (findYarn("color", patternColor.toString()) == -1) {
                    System.out.println("A yarn with this color doesn't exist. Please try adding one ( 1 ) or try again ( 2 ).");
                    int choise = numberIn();
                    switch (choise) {
                        case 1 -> addYarn();
                        case 2 -> modifyPatternName();
                        default -> {
                            System.out.println("Returning you to modify menu.");
                            modifyPattern();
                        }
                    }
                }
                patterns.get(findPattern("name", patternName)).setYarn(yarns.get(findYarn("color", patternColor.toString())));
                System.out.println("It has been succesfully modified. ");
            } else {
                System.out.println("There are no patterns available.");
                menu();
            }
        }

        private static void discountOnPattern(){
        if(patterns.size()>0) {
            System.out.println("Name te pattern you have a discount on: ");
            String in = scanner.next();
            System.out.println("\nThe amount of the discount is: ");
            int discountAmount = numberIn();
            patterns.get(findPattern("name", in)).sale(discountAmount);
        }
        else{
                System.out.println("There are no patterns available.");
                menu();
        }
    }


    private static void addYarn(){
        System.out.println("What color do you want to add? : ");
        Colors color=colorIn();
        System.out.println("What wheight does it have? (Heavy , Standard, Light , Baby) : ");
        weightEnum weight=weightIn();
        System.out.println("What's its price? : ");
        Double price= numberIn().doubleValue();
        yarns.add(new Yarns(color,weight,price));
    }


        private static weightEnum weightIn(){
        weightEnum weight=weightEnum.Standard;
        try {
            weight = weightEnum.valueOf(scanner.next());
        }catch(IllegalArgumentException ex) {
            System.out.println("You may have misspelled the weight. Please try again.");
            weightIn();
        }
        return weight;
        }

        private static Colors colorIn(){
        Colors color=Colors.purple;
        try {
            color = Colors.valueOf(scanner.next());
        }catch(IllegalArgumentException ex) {
            System.out.println("You may have mispelled the color. Please try again");
            colorIn();
        }
        return color;
    }

        private static Integer numberIn(){
            int i=0;
            try {
                i = scanner.nextInt();
            }
            catch(InputMismatchException ex) {
                System.out.println("You have mistiped. Try again.");
                numberIn();
            }
            return i;
        }

        private static int findPattern(String byWhat,String target){
        switch(byWhat){
            case "color" ->{
                int i=0;
                Color colour=Color.getColor(target);
                for (Pattern pattern : patterns){
                    if(pattern.getYarn().getColour().equals(colour)){
                        return patterns.indexOf(pattern.getYarn().getColour());
                    }
                }
            }
            case "name" ->{
                String name=target;
                for (Pattern pattern : patterns){
                    if(pattern.getName().equals(name)){
                       return patterns.indexOf(pattern);
                    }
                }
            }
            default -> {
                return -1;
            }
        }
        return -1;
    }

        private static int findYarn(String byWhat,String target){
        switch(byWhat){
            case "color" ->{
                int i=0;
                Colors colour=Colors.valueOf(target);
                for (Yarns yarnes : yarns){
                    if(yarns.get(i).getColour().equals(colour)){
                        return i;
                    }
                }
            }
            case "weight" ->{
                int i=0;
                weightEnum weight=weightEnum.valueOf(target);
                for (Yarns yarnes : yarns){
                    if(yarns.get(i).getWeight().equals(weight)){
                        return i;
                    }
                }
            }
            case "price" ->{int i=0;
                Integer price=Integer.getInteger(target);
                for (Yarns yarnes : yarns){
                    if(yarns.get(i).getPrice().equals(price)){
                        return i;
                    }
                }
            }
            default -> {
                return -1;
            }
        }
        return -1;
    }


    private static final void settingBaseYarns(){
        yarns.add(new Yarns(Colors.black,    weightEnum.Light, rnd.nextDouble(1800,2600)));
        yarns.add(new Yarns(Colors.white,    weightEnum.Light, rnd.nextDouble(1800,2600)));
        yarns.add(new Yarns(Colors.gray,     weightEnum.Light, rnd.nextDouble(1800,2600)));
        yarns.add(new Yarns(Colors.red,      weightEnum.Light, rnd.nextDouble(1800,2600)));
        yarns.add(new Yarns(Colors.blue,     weightEnum.Light, rnd.nextDouble(1800,2600)));
        yarns.add(new Yarns(Colors.pink,     weightEnum.Light, rnd.nextDouble(1800,2600)));
        yarns.add(new Yarns(Colors.yellow,   weightEnum.Light, rnd.nextDouble(1800,2600)));
        yarns.add(new Yarns(Colors.cyan,     weightEnum.Light, rnd.nextDouble(1800,2600)));
        yarns.add(new Yarns(Colors.orange,   weightEnum.Light, rnd.nextDouble(1800,2600)));
        yarns.add(new Yarns(Colors.green,    weightEnum.Light, rnd.nextDouble(1800,2600)));

        patterns.add(new Pattern("pony",    yarns.get(5)));
        patterns.add(new Pattern("flower",  yarns.get(1)));
        patterns.add(new Pattern("heart",   yarns.get(3)));
        patterns.add(new Pattern("orange",  yarns.get(8)));
    }


    private static ArrayList<Yarns> YarnsFromXml(String filepath) {
        ArrayList<Yarns> yarn = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath);

            Element rootElement = document.getDocumentElement();

            NodeList childNodeList = rootElement.getChildNodes();
            Node node;
            for (int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfYarnTag = node.getChildNodes();
                    String color = "red", weight = "Standard", price = "0.0";
                    for (int j = 0; j < childNodesOfYarnTag.getLength(); j++) {
                        Node childNodeOfYarnTag = childNodesOfYarnTag.item(j);
                        if (childNodeOfYarnTag.getNodeType() == Node.ELEMENT_NODE) {
                            switch (childNodeOfYarnTag.getNodeName()) {
                                case "color" -> color = childNodeOfYarnTag.getTextContent();
                                case "weight" -> weight = childNodeOfYarnTag.getTextContent();
                                case "price" -> price = childNodeOfYarnTag.getTextContent();
                            }
                        }
                    }
                    yarn.add(new Yarns(Colors.valueOf(color),weightEnum.valueOf(weight),Double.valueOf(price)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yarn;
    }

    private static void PatternsFromXml(String filepath) {
        ArrayList<Yarns> yarn = new ArrayList<>();
        ArrayList<Pattern> pattern = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath);

            Element rootElement = document.getDocumentElement();

            NodeList childNodeList = rootElement.getChildNodes();
            Node node;
            for (int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfYarnsTag = node.getChildNodes();
                    String name="",color = "", weight = "", price = "",patternPrice="";
                    for (int j = 0; j < childNodesOfYarnsTag.getLength(); j++) {
                        Node childNodeOfYarnTag = childNodesOfYarnsTag.item(j);
                        if (childNodeOfYarnTag.getNodeType() == Node.ELEMENT_NODE) {
                            switch (childNodeOfYarnTag.getNodeName()) {
                                case "name" -> name = childNodeOfYarnTag.getTextContent();
                                case "yarnColor" -> color = childNodeOfYarnTag.getTextContent();
                                case "yarnWeight" -> weight = childNodeOfYarnTag.getTextContent();
                                case "yarnPrice" -> price = childNodeOfYarnTag.getTextContent();
                                case "patternPrice" -> patternPrice=childNodeOfYarnTag.getTextContent();
                            }
                        }
                    }
                    yarns.add(new Yarns(Colors.valueOf(color),weightEnum.valueOf(weight),Double.valueOf(price)));
                    pattern.add(new Pattern(name,yarns.get(yarns.size()-1),Double.valueOf(patternPrice)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void savePattern(String filepath) {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("patterns");
            document.appendChild(rootElement);

            for (Pattern pattern : patterns) {
                Element patternElement = document.createElement("pattern");
                rootElement.appendChild(patternElement);
                createChildElement(document, patternElement, "name",
                        pattern.getName());
                createChildElement(document, patternElement, "yarnColor",
                        String.valueOf(pattern.getYarn().getColour().toString()));
                createChildElement(document, patternElement, "yarnWeight",
                        String.valueOf(pattern.getYarn().getWeight().toString()));
                createChildElement(document, patternElement, "yarnPrice",
                        String.valueOf(pattern.getYarn().getPrice().toString()));
                createChildElement(document, patternElement, "patternPrice",
                        String.valueOf(pattern.getPrice()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(filepath));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveOrder(String filepath) {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("orders");
            document.appendChild(rootElement);

            for (Pattern pattern : purchase) {
                Element patternElement = document.createElement("order");
                rootElement.appendChild(patternElement);
                createChildElement(document, patternElement, "name",
                        pattern.getName());
                createChildElement(document, patternElement, "yarnColor",
                        String.valueOf(pattern.getYarn().getColour().toString()));
                createChildElement(document, patternElement, "yarnWeight",
                        String.valueOf(pattern.getYarn().getWeight().toString()));
                createChildElement(document, patternElement, "yarnPrice",
                        String.valueOf(pattern.getYarn().getPrice().toString()));
                createChildElement(document, patternElement, "patternPrice",
                        String.valueOf(pattern.getPrice()));
            }

            Element patternElement = document.createElement("total");
            rootElement.appendChild(patternElement);
            createChildElement(document, patternElement, "total",
                    String.valueOf(subtotal.toString()));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(filepath));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveYarn(String filepath) {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("yarns");
            document.appendChild(rootElement);

            for (Yarns yarn : yarns) {
                Element yarnElement = document.createElement("yarn");
                rootElement.appendChild(yarnElement);
                createChildElement(document, yarnElement, "Color",
                        String.valueOf(yarn.getColour().toString()));
                createChildElement(document, yarnElement, "Weight",
                        String.valueOf(yarn.getWeight().toString()));
                createChildElement(document, yarnElement, "Price",
                        String.valueOf(yarn.getPrice().toString()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(filepath));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        private static void createChildElement(Document document, Element parent, String tagName, String text) {
        Element element = document.createElement(tagName);
        element.setTextContent(text);
        parent.appendChild(element);
    }
}
