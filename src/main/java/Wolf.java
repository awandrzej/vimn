import interfaces.Animal;

/**
 * Created by Andrzej.Wudara on 2016-04-22.
 */
public class Wolf extends Animals {

    @Override
    void Animals() {
        super.Animals();
        System.out.println("I'am sleeping");
    }

//    public void sleep(){
//        super.sleep();
//        System.out.println("I'am sleeping");
//    }


    public static void main(String[] args) {

        Animals animals = new Animals();
        animals.sleep();

        Wolf wolf= new Wolf();
        wolf.sleep();

    }
}