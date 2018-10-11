public class RationalMatrix {


    // Atributos de matriz:
    private int m;                      // # de filas
    private int n;                      // # de columnas
    private String ptrContent[];        // Apuntador al primer indice de arreglo de elementos
    private int len;                    // Tamano del arreglo

    // Constructor recibe m, n y arreglo en row-major-order
    public RationalMatrix(int m, int n, String contentParam[]){
        this.m = m;
        this.n = n;
        this.len = n*m;
        this.ptrContent = contentParam;
    }

    // Getter de m
    int getM(){
        return this.m;
    }

    // Getter de n
    int getN(){
        return this.n;
    }

    // Getter de index especifico comenzando en (0,0) hasta (n-1,m-1)
    String getIndex(int i, int j){
        return ptrContent[i * this.n + j];
    }

    // Getter de index especifico del arreglo comenzando en 0 hasta len-1
    String getIndexArreglo(int i){
        return ptrContent[i];
    }

    // Getter de index especifico del arreglo comenzando en 0 hasta len-1
    String[] getArreglo(){
        return ptrContent;
    }

    // Setter de index especifico comenzando en (0,0) hasta (n-1,m-1)
    void setIndex(int i, int j, String valor){
        ptrContent[i * this.n + j] = valor;
    }

    // Metodo recorre matrix y la imprime en formato mxn
    void printMatriz(){
        int columna = 0;
        int columnaMax = this.n - 1;

        for (int i=0; i<len; i++) {
            System.out.print(ptrContent[i] + " ");

            if(columna == columnaMax){
                System.out.println("");
                columna = 0;
            } else {
                columna++;
            }
        }
    }

    // Metodo obtiene matriz transpuesta, regresa matriz resultante
    RationalMatrix getTranspuesta(){

        String resultado[] = new String[this.len];
        int k = 0;

        for (int j=0; j<this.n; j++) {
            for (int i=0; i<this.m; i++) {
                resultado[k] = this.getIndex(i, j);
                k++;
            }
        }

        return new RationalMatrix(this.n, this.m, resultado);
    }

    // Metodo realiza operación de suma o resta, dependiendo si s es +/-, regresa matriz resultante
    RationalMatrix sumaResta(RationalMatrix matrizB, int s){

        if(this.m == matrizB.getM() && this.n == matrizB.getN()){

            String resultado[] = new String[this.len];
            int k = 0;

            int a,b,c,d, num, den;
            String tmp[];

            for (int i=0; i<this.m; i++) {
                for (int j=0; j<this.n; j++) {

                    tmp = this.getIndex(i,j).split("/");
                    a = Integer.parseInt(tmp[0]);
                    b = Integer.parseInt(tmp[1]);

                    tmp = matrizB.getIndex(i,j).split("/");
                    c = s * Integer.parseInt(tmp[0]);
                    d = Integer.parseInt(tmp[1]);

                    num = (a*d) + (c*b);
                    den = b*d;

                    resultado[k] = num + "/" + den;
                    k++;
                }
            }

            return new RationalMatrix(this.m, this.n, resultado);
        }

        return null;
    }

    // Metodo suma matrices a+b, regresa matriz resultante
    RationalMatrix sum(RationalMatrix matriz){
        return sumaResta(matriz, 1);
    }

    // Metodo resta matrices a-b, regresa matriz resultante
    RationalMatrix sub(RationalMatrix matriz){
        return  sumaResta(matriz, -1);
    }

    //////////////////

    // Metodo multiplica matriz actual por matriz B, regresa apuntador a arreglo o NULL
    RationalMatrix multiply(RationalMatrix matrizB){

        if(matrizB.getM() == this.n){

            int lenR = matrizB.getN() * this.m;
            double resultado[] = new double[lenR];

            for (int k=0; k<lenR; k++) {
                resultado[k] = 0;
            }

            int k = 0;
            for (int h=0; h<this.m; h++) {
                for (int i=0; i<matrizB.getN(); i++) {
                    for (int j=0; j<matrizB.getM(); j++) {
                        resultado[k] += this.getIndex(h, j) * matrizB.getIndex(j,i);
                    }
                    k++;
                }
            }

            return new RationalMatrix(this.m, matrizB.getN(), resultado);
        }

        return null;
    }

    void swapRows(int i, int k)  {
        double tmp;
        for (int j = 0; j < this.n; j++)
        {
            tmp = this.getIndex(k,j);
            this.setIndex(k,j,this.getIndex(i,j));
            this.setIndex(i,j,tmp);
        }
    }

    RationalMatrix getIndentidad(){

        if(this.n != this.m)
            return null;

        double resultado[] = new double[this.len];
        RationalMatrix identity = new RationalMatrix(this.m, this.n, resultado);

        for (int i = 0; i < this.m; i++)
        {
            identity.setIndex(i,i,1.0);
        }

        return identity;
    }

    boolean gaussJordanElimination(double tol) {
        double piv;

        for (int i=0; i<this.m; i++){
            piv = this.getIndex(i,i);

            if(Math.abs(piv) <= tol){
                int k = 0;

                for(k = i+1; k < this.m; k++){
                    if(Math.abs(this.getIndex(k,i)) > tol){
                        this.swapRows(i,k);
                        piv = this.getIndex(i,i);
                        break;
                    }
                }

                if(k == m){
                    return false;
                }
            }

            for (int j=0; j<this.n; j++) {
                this.setIndex(i,j,this.getIndex(i,j) / piv);
            }

            for (int k=0; k<this.m; k++) {

                if(k != i){
                    double factor = this.getIndex(k,i)*-1;

                    for (int j=0; j<this.n; j++) {
                        this.setIndex(k,j, this.getIndex(i,j) * factor + this.getIndex(k,j));
                    }
                }
            }
        }

        return true;
    }

    // No funcionaron :(

    double getDeterminante() {

        return 0.0;
    }

    RationalMatrix getInversa(){

        RationalMatrix inverse = this.getIndentidad();
        return inverse;

    }



}



