public class Test {

    public static void main(String [] args){

        double arregloReales[] = {0,2,3,4,5,6,7,8,9};
        RealMatrix real = new RealMatrix(3,3, arregloReales);

        double arregloReales2[] = {1,2,3,4,5,6,7,8,9,10,11,12};
        RealMatrix real2 = new RealMatrix(3,4, arregloReales2);

        real.printMatriz();

        System.out.println("Inversa:");
        real.getInversa(1.0e-05).printMatriz();

    }

}
