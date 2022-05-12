import java.util.Random;
import java.util.Scanner;

public class Pattern extends Assignment{

    private Random rnd=new Random();
    private Scanner scanner=new Scanner(System.in);

    private String name ;
    private Yarns yarn;                                                    // yarn used
    private Double price;

    public Pattern(String name, Yarns yarns) {
        this.name = name;
        this.yarn =yarns;
        price= yarn.getPrice()+rnd.nextDouble(4500,8500);
    }

    public Pattern(String name, Yarns yarns, Double price) {
        this.name = name;
        this.yarn = yarns;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Yarns getYarn() {
        return yarn;
    }

    public Double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYarn(Yarns yarn) {
        this.yarn = yarn;
    }
    public void sale(int discount){
        price= (1- 0.01 * discount) * price;
    }
}
