public class RealMatrix {


    // Atributos de matriz:
    private int m;                      // # de filas
    private int n;                      // # de columnas
    private double ptrContent[];        // Apuntador al primer indice de arreglo de elementos
    private int len;                    // Tamano del arreglo

    // Constructor recibe m, n y arreglo en row-major-order
    public RealMatrix(int m, int n, double contentParam[]){
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
    double getIndex(int i, int j){
        return ptrContent[i * this.n + j];
    }

    // Getter de index especifico del arreglo comenzando en 0 hasta len-1
    double getIndexArreglo(int i){
        return ptrContent[i];
    }

    // Getter de index especifico del arreglo comenzando en 0 hasta len-1
    double[] getArreglo(){
        return ptrContent;
    }

    // Setter de index especifico comenzando en (0,0) hasta (n-1,m-1)
    void setIndex(int i, int j, double valor){
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
    RealMatrix getTranspuesta(){

        double resultado[] = new double[this.len];
        int k = 0;

        for (int j=0; j<this.n; j++) {
            for (int i=0; i<this.m; i++) {
                resultado[k] = this.getIndex(i, j);
                k++;
            }
        }

        return new RealMatrix(this.n, this.m, resultado);
    }

    // Metodo realiza operación de suma o resta, dependiendo si s es +/-, regresa matriz resultante
    RealMatrix sumaResta(RealMatrix matrizB, int s){

        if(this.m == matrizB.getM() && this.n == matrizB.getN()){

            double resultado[] = new double[this.len];
            int k = 0;

            for (int i=0; i<this.m; i++) {
                for (int j=0; j<this.n; j++) {
                    resultado[k] = this.getIndex(i, j) + (s * matrizB.getIndex(i, j));
                    k++;
                }
            }

            return new RealMatrix(this.m, this.n, resultado);
        }

        return null;
    }

    // Metodo suma matrices a+b, regresa matriz resultante
    RealMatrix sum(RealMatrix matriz){
        return sumaResta(matriz, 1);
    }

    // Metodo resta matrices a-b, regresa matriz resultante
    RealMatrix sub(RealMatrix matriz){
        return  sumaResta(matriz, -1);
    }

    // Metodo multiplica matriz actual por matriz B, regresa apuntador a arreglo o NULL
    RealMatrix multiply(RealMatrix matrizB){

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

            return new RealMatrix(this.m, matrizB.getN(), resultado);
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

    RealMatrix getIndentidad(){

        if(this.n != this.m)
            return null;

        double resultado[] = new double[this.len];
        RealMatrix identity = new RealMatrix(this.m, this.n, resultado);

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

    RealMatrix minor(RealMatrix matriz, int row, int column) {

        RealMatrix resultado = new RealMatrix(matriz.getM(), matriz.getM(), matriz.getArreglo());

        for (int i = 0; i < matriz.getM(); i++)
            for (int j = 0; i != row && j < matriz.getN(); j++)
                if (j != column)
                    resultado.setIndex(i < row ? i : i - 1, j < column ? j : j - 1, matriz.getArreglo());
                    //minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return minor;
    }

    double getDeterminante() {

        if(this.n != this.m){
            throw new IllegalStateException("invalid dimensions");
        }

        if (this.len == 2){
            return this.getIndex(0,0) * this.getIndex(1,1) - this.getIndex(0,1) * this.getIndex(1,0);
        }

        double determinante = 0;

        for (int i = 0; i < this.n; i++){
            determinante += Math.pow(-1, i) * this.getIndex(0,i) * getDeterminante(minor(matrix, 0, i));
        }

        return det;
    }

    RealMatrix getInversa(){

        RealMatrix inverse = this.getIndentidad();

        // minors and cofactors
        for (int i = 0; i < this.len; i++)
            for (int j = 0; j < matrix[i].length; j++)
                inverse[i][j] = Math.pow(-1, i + j)
                        * determinant(minor(matrix, i, j));

        // adjugate and determinant
        double det = 1.0 / determinant(matrix);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }

        return inverse;

    }



}



