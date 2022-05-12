import java.awt.Color;
import java.util.Random;

public class Yarns extends Assignment{

    private static final Random rnd = new Random();

    private weightEnum weight;
    private Colors colour;
    private Double price;

    public Yarns( Colors colour, weightEnum weight, Double price) {

        this.colour = colour;
        this.weight = weight;
        this.price = price;
    }

    public Yarns() {
        this.colour=Colors.white;
        this.weight=weightEnum.Standard;
        this.price=1800.0;
    }


    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public Colors getColour() {
        return colour;
    }

    public weightEnum getWeight() {
        return weight;
    }



}
