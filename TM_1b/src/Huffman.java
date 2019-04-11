import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Huffman {
    private ArrayList<Data> lisValues;
    Node root;
    private HashMap<Character,String> codes;

    public Huffman( ArrayList<Data> listValues){
        this.codes = new HashMap<>();

        this.lisValues = listValues;

        int size = listValues.size();

        ArrayList<Node> list = new ArrayList<>();

        for(int i=0; i< size; i++){
            Node n = new Node(listValues.get(i).value,listValues.get(i).prob);

            n.left = null;
            n.right = null;

            list.add(n);
        }

        this.root = null;

        /*
        Building a Tree
         */

        while(list.size() > 1){

            //Extract the smallest
            Node n1 = this.getSmallNode(list);
            list.remove(n1);


            //Extract the smallest
            Node n2 = this.getSmallNode(list);
            list.remove(n2);

            //We create the node between the 2 characters
            Node pivot = new Node('*', n1.prob + n2.prob);
            pivot.left = n1;
            pivot.right = n2;

            root = pivot;

            //add it on the tree
            list.add(pivot);

        }
    }

    private Node getSmallNode(ArrayList<Node> l){
        Node n = l.get(0);
        for(int i=1; i < l.size(); i++){
            if(l.get(i).smallThan(n)){
                n = l.get(i);
            }
        }
        return n;
    }

    public Node getRoot(){
        return this.root;
    }

    public void codeHuffman(Node n, String s){


        if((n.left == null) && (n.right== null)){

            System.out.println(n.value + " - " + s);
            this.codes.put(n.value,s);

        }
        else{
            this.codeHuffman(n.left, s + "0");
            this.codeHuffman(n.right, s + "1");
        }


    }

    public boolean listContains(char k) {
        boolean result = false;
        for (Data d : this.lisValues) {
            if (d.value == k) {
                result = true;
            }
        }

        return result;
    }


    public String generateRandomMessage(int size) {

        ArrayList<Character>  list = new ArrayList<>();
        String result = "";

        for(int k=0; k < this.lisValues.size(); k++){

            int prob = this.lisValues.get(k).prob;

            while(prob > 0){
                list.add(this.lisValues.get(k).value);
                prob--;
            }

        }
        int random;

        for(int i= 0; i < size; i++){

            random = (int) (Math.random() * 100);

            result+=list.get(random);

        }

        return result;

    }



    public String getCodeHuffmanOfALetter(char c){

        return this.codes.get(c);
    }

}

