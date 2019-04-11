import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("__________________________");
        System.out.println("- Apartado a -");

        ArrayList<Data> listValues = new ArrayList<Data>();

        listValues.add(new Data('D',30)); //Diamante
        listValues.add(new Data('K',20));
        listValues.add(new Data('Q',20));
        listValues.add(new Data('J',15));
        listValues.add(new Data('X',10)); //10
        listValues.add(new Data('9',5));

        Huffman h = new Huffman(listValues);

        //imprimimos los valores de huffman para todos los valores añadidos.
        h.codeHuffman(h.getRoot(),"");

        System.out.println("__________________________");
        System.out.println("- Apartado b -");
        System.out.println("Introduce el mensaje  traducir: \n(Recordamos que diamante es igual a 'D')");
        String msg = sc.nextLine();
        String huffmanMsg = "";

        for(int i=0; i< msg.length(); i++){
            if(msg.charAt(i) == '1'){
                if (msg.length() < i){
                    System.out.println("El caracter '" + msg.charAt(i) + "' no es valido");
                }
                else{
                    if (msg.charAt(i+1) == '0'){
                        i++;
                        huffmanMsg += h.getCodeHuffmanOfALetter('X');
                    }
                    else{
                        System.out.println("El caracter '" + msg.charAt(i) + "' no es valido");
                    }
                }
            }
            else{
                if(h.listContains(msg.charAt(i))){
                    huffmanMsg += h.getCodeHuffmanOfALetter(msg.charAt(i));
                }
                else{
                    System.out.println("El caracter '" + msg.charAt(i) + "' no es valido");
                }
            }
        }
        System.out.println(huffmanMsg);

        String randomMsg = h.generateRandomMessage(20);
        String huffmanRandomMsg = "";
        int random;

        for(int k=0; k < randomMsg.length() ; k++){

            huffmanRandomMsg += h.getCodeHuffmanOfALetter(randomMsg.charAt(k));

        }

        System.out.println("__________________________");
        System.out.println("- Apartado c -");
        System.out.println("La cadena aleatoria a traducir es: ");
        System.out.println(randomMsg);
        System.out.println("La cadena aleatoria traducida es: ");
        System.out.println(huffmanRandomMsg);



        System.out.println("__________________________");
        System.out.println("- Apartado d -");

        float entropy  = 0;

        for(int u = 0; u < listValues.size(); u++){

            entropy += ((listValues.get(u).prob)/100.0) * (Math.log((listValues.get(u).prob/100.0)) / Math.log(2))    ;

        }
        entropy = entropy *(-1);

        float m1 =0;
        float m2= 0;

        for (int i=0; i < 5 ; i++) {
            randomMsg =  h.generateRandomMessage(20);
            huffmanRandomMsg = "";

            for(int k=0; k < randomMsg.length() ; k++){

                huffmanRandomMsg += h.getCodeHuffmanOfALetter(randomMsg.charAt(k));

            }

            System.out.println(randomMsg + " Nbits: " + 3*randomMsg.length());
            m1 = (m1 * (i) +((3*randomMsg.length())))/(i+1);
            System.out.println(huffmanRandomMsg + " Nbits: " + huffmanRandomMsg.length());
            m2 = (m2* (i) + huffmanRandomMsg.length())/ (i+1);
        }
        System.out.println("Nbits sin huffman es 3bits por caracter: " + m1);
        System.out.println("Nbits huffman media calculada es: "+ m2);
        System.out.println("Factor de comprension: " + (m1/m2));
        System.out.println("Entropia: " + entropy);

    }
}
