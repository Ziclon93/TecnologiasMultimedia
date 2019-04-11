import java.util.ArrayList;

public class LZ77 {

    private int Ment, Mdes, Mtotal;
    private String code;
    private String compressCode;

    public LZ77(int Ment, int Mdes, int Mtotal){
        this.Ment = Ment;
        this.Mdes = Mdes;
        this.Mtotal = Mtotal;
        this.code = "";

        int random;

        for(int i=0; i < Mtotal; i++) {
            random = (int) (Math.random() * 2);

            if (random == 0) {
                this.code += '0';
            } else {
                this.code += '1';
            }
        }

        //TESTING
        this.code = "11011100101001111010100010001";
        this.Mtotal = this.code.length();


        this.generateCompressCode();
    }

    public String getCode(){
        return this.code;
    }
    public String getCompressCode(){
        return this.compressCode;
    }

    public void generateCompressCode(){
        int i =0;
        String match;
        this.compressCode = this.code.substring(0,this.Mdes);
        while (i< (this.Mtotal -(this.Ment + this.Mdes))){
            //We fond the string that matches
            match = getMatch(code.substring(i,i+this.Mdes), code.substring(i+this.Mdes,i+this.Mdes +this.Ment));

            //Displace the slidding window
            int displacement = Integer.parseInt(match.substring(0,(int)(Math.log(this.Ment)/ Math.log(2))),2);
            if (displacement == 0){
                displacement = this.Ment;
            }
            i+= displacement;

            //Add this match to the compress code
            this.compressCode += match;
        }
        this.compressCode += this.code.substring(i);
    }

    public String getMatch(String slidingWindow , String inWindow){
        boolean found = false;
        String result = "";

        while(!found){
            //Never gonna happen but...
            if( inWindow.length() == 0){
                found = true;
            }
            else{
                int u = ((slidingWindow.length() - inWindow.length()));
                boolean match = false;
                int stepsBack= 0;
                while( (u >= 0) && (!match)){
                    if(slidingWindow.substring(u,u+inWindow.length()).equals(inWindow)){
                        match = true;
                        found = true;

                        int numberOfBits = inWindow.length();
                        int displacement = inWindow.length() + stepsBack;

                        String numberOfBitsBinary = Integer.toBinaryString(numberOfBits);
                        String displacementBinary = Integer.toBinaryString(displacement);

                        //fix if the binary is smallest that what we need
                        while (numberOfBitsBinary.length() < (int)(Math.log(this.Ment)/ Math.log(2))){
                            numberOfBitsBinary = "0" +numberOfBitsBinary;
                        }
                        while (displacementBinary.length() < (int)(Math.log(this.Mdes)/ Math.log(2))){
                            displacementBinary = "0" +displacementBinary;
                        }

                        result += numberOfBitsBinary.substring(numberOfBitsBinary.length() - (int)(Math.log(this.Ment)/ Math.log(2)));
                        result += displacementBinary.substring(displacementBinary.length() - (int)(Math.log(this.Mdes)/ Math.log(2)));


                    }
                    stepsBack +=1;
                    u--;
                }
            }
            System.out.println("Window value: "+ inWindow);
            inWindow = inWindow.substring(0,(inWindow.length()-1));
        }
        System.out.println("RESULT: " + result);
        return result;
    }
}
