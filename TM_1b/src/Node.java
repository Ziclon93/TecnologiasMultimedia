public class Node {
    int prob;
    char value;

    Node left;
    Node right;

    public Node(char v, int p){
        this.value = v;
        this.prob = p;
    }

    public boolean smallThan(Node n){
        return (this.prob < n.prob);
    }

}