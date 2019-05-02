import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int Mtotal = 50;


        System.out.println("______________________________\n" +
                "*La longitud total es de " + Mtotal +"\n" +
                "*La ventana de entrada tiene que ser menor o igual que la deslizante \n" +
                "*Y la suma de ambas ventanas no puede ser superior a " +Mtotal +
                "\n______________________________\n");

        System.out.println("Introduce la longitud de la ventana de entrada: (Tiene que ser potencia de 2)");
        int Ment = sc.nextInt();

        System.out.println("Introduce la longitud de la ventana deslizante: (Tiene que ser potencia de 2)");
        int Mdes = sc.nextInt();

        int MentLog = (int) (Math.log(Ment)/Math.log(2));
        int MdesLog = (int) (Math.log(Mdes)/Math.log(2));


        while(((((int)(Math.pow(2,MentLog))) != Ment ) || (((int)(Math.pow(2,MdesLog))) != Mdes ))
            || (Ment > Mdes) || ((Ment+Mdes) > Mtotal)){
            System.out.println("______________________________\n" +
                    "*La longitud total es de " + Mtotal +"\n" +
                    "*La ventana de entrada tiene que ser menor o igual que la deslizante \n" +
                    "*Y la suma de ambas ventanas no puede ser superior a " +Mtotal +
                    "\n______________________________\n");

            System.out.println("Introduce la longitud de la ventana de entrada: (Tiene que ser potencia de 2)");
            Ment = sc.nextInt();

            System.out.println("Introduce la longitud de la ventana deslizante: (Tiene que ser potencia de 2)");
            Mdes = sc.nextInt();

            MentLog = (int) (Math.log(Ment)/Math.log(2));
            MdesLog = (int) (Math.log(Mdes)/Math.log(2));

        }

        System.out.println("______________________________");

        LZ77 cadena = new LZ77(Ment,Mdes,Mtotal);
        System.out.println("La cadena generada aleatoriamente con longitud " + cadena.getCode().length() + " es: \n"
                + cadena.getCode());

        System.out.println("La cadena comprimida con longitud " + cadena.getCompressCode().length() + " es: \n"
                + cadena.getCompressCode());


        System.out.println("______________________________");
        System.out.println("______________________________");
        System.out.println("__________Apartado 1__________");
        System.out.println("______________________________");

        LZ77 apartado1 = new LZ77(4,8,25);
        System.out.println("Se ha creado la cadena: \n" + apartado1.getCode());
        System.out.println("La comprimimos: \n" + apartado1.getCompressCode());
        System.out.println("La descomprimimos: \n" + apartado1.decompressCode());

        System.out.println("______________________________");
        System.out.println("__________Apartado 2__________");
        System.out.println("______________________________");

        System.out.println("No es posible hacerlo en el caso de ser una cadena tan pequeña, \n" +
                "Debido a que utilizamos una codificación de este tipo, a menos que \n" +
                "compactemos mas de (log2(Mdes) + log2(Ment)) no ganamos veneficio con la compresion \n" +
                "En grandes cadenas se puede conseguir una mejora mucho mas consistente \n" +
                "Allá va una demostración: ten paciencia...");

        System.out.println("Se están generando 10000 bits con Mdes 2048 y Ment 512");

        LZ77 apartado2 = new LZ77(512,2048,10000);
        System.out.println("Longitud de cadena generada: " + apartado2.getCode().length());
        System.out.println("Longitud de cadena generada comprimida: " + apartado2.getCompressCode().length());

        System.out.println("Beneficio: " + ((float)apartado2.getCompressCode().length() / (float)apartado2.getCode().length()));



    }
}
