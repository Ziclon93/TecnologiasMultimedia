import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StringBuffer hamletTxt = txtReader.cargarTxt("hamlet_short.txt");
        StringBuffer quijoteTxt = txtReader.cargarTxt("quijote_short.txt");
        int MtotalHamlet = hamletTxt.length();
        int MtotalQuijot = quijoteTxt.length();

        /*System.out.println("______________________________\n" +
                "*La longitud total es de " + Mtotal +"\n" +
                "*La ventana de entrada tiene que ser menor o igual que la deslizante \n" +
                "*Y la suma de ambas ventanas no puede ser superior a " +Mtotal +
                "\n______________________________\n");

        System.out.println("Introduce la longitud de la ventana de entrada: (Tiene que ser potencia de 2)");
        int Ment = sc.nextInt();

        System.out.println("Introduce la longitud de la ventana deslizante: (Tiene que ser potencia de 2)");
        int Mdes = sc.nextInt();

        int MentLog = (int) (Math.log(Ment)/Math.log(2));
        int MdesLog = (int) (Math.log(Mdes)/Math.log(2));*/

        int i = 1;
        int j = i;

        ArrayList<Double> listTimesHamlet = new ArrayList<Double>(); 
        ArrayList<Double> listTimesQuijote = new ArrayList<Double>(); 
        while (i < 13) {
                j = i;
                while(j< 13) {
                        int Ment = (int) Math.pow(2, i);
                        int Mdes = (int) Math.pow(2, j);

                        System.out.println(Ment);
                        System.out.println(Mdes);

                        double t = System.nanoTime();
                        LZ77 hamletLZ77 = new LZ77(Ment, Mdes, MtotalHamlet, hamletTxt.toString());
                        String compressed = hamletLZ77.getCompressCode();
                        t = (System.nanoTime() - t) /1000000000;
                        listTimesHamlet.add(t);

                        System.out.println("(HAMLET) Tiempo transcurrido:  " + t);
                        System.out.println("(HAMLET) Factor de compresión: " + hamletLZ77.getCode().length() / (float) compressed.length());
                
                        t = System.nanoTime();
                        LZ77 quijoteLZ77 = new LZ77(Ment, Mdes, MtotalQuijot, quijoteTxt.toString());
                        compressed = quijoteLZ77.getCompressCode();
                        t = (System.nanoTime() - t) /1000000000;
                        listTimesQuijote.add(t);
                
                        System.out.println("(QUIJOT) Tiempo transcurrido:  " + t);
                        System.out.println("(QUIJOT) Factor de compresión: " + quijoteLZ77.getCode().length() / (float) compressed.length());

                        j++;
                }
        i++; 
        }

        System.out.println(listTimesHamlet);
        System.out.println(listTimesQuijote);

    }
}
