import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int Mtotal = 50;


        System.out.println("______________________________\n" +
                "La longitud total es de " + Mtotal +"\n" +
                "La ventana de entrada tiene que ser menor o igual que la deslizante \n" +
                "Y la suma de ambas ventanas no puede ser superior a " +Mtotal );

        System.out.println("Introduce la longitud de la ventana de entrada: (Tiene que ser potencia de 2)");
        int Ment = sc.nextInt();

        System.out.println("Introduce la longitud de la ventana deslizante: (Tiene que ser potencia de 2)");
        int Mdes = sc.nextInt();

        int MentLog = (int) (Math.log(Ment)/Math.log(2));
        int MdesLog = (int) (Math.log(Mdes)/Math.log(2));


        while(((((int)(Math.pow(2,MentLog))) != Ment ) || (((int)(Math.pow(2,MdesLog))) != Mdes ))
            || (Ment > Mdes) || ((Ment+Mdes) > Mtotal)){
            System.out.println("______________________________\n" +
                    "La longitud total es de " + Mtotal +"\n" +
                    "La ventana de entrada tiene que ser menor o igual que la deslizante \n" +
                    "Y la suma de ambas ventanas no puede ser superior a " +Mtotal );

            System.out.println("Introduce la longitud de la ventana de entrada: (Tiene que ser potencia de 2)");
            Ment = sc.nextInt();

            System.out.println("Introduce la longitud de la ventana deslizante: (Tiene que ser potencia de 2)");
            Mdes = sc.nextInt();

            MentLog = (int) (Math.log(Ment)/Math.log(2));
            MdesLog = (int) (Math.log(Mdes)/Math.log(2));

        }


        LZ77 cadena = new LZ77(Ment,Mdes,Mtotal);
        System.out.println("______________________________\n" +
                "La cadena generada ha sido: \n" + cadena.getCode());

        System.out.println("______________________________\n" +
                "La cadena comprimida es: \n" + cadena.getCompressCode());


    }
}
