import java.lang.Object;

public class LZ77 {

    private int Ment, Mdes, Mtotal, MentLog, MdesLog;
    private String code;
    private String compressCode;


    public LZ77(int Ment, int Mdes, int Mtotal, String code){
        this.Ment = Ment;
        this.Mdes = Mdes;
        this.Mtotal = Mtotal;
        this.code = "";

        this.MentLog = (int)(Math.log(this.Ment)/ Math.log(2));
        this.MdesLog = (int)(Math.log(this.Mdes)/ Math.log(2));
        /*
        int random;

        for(int i=0; i < Mtotal; i++) {
            random = (int) (Math.random() * 2);

            if (random == 0) {
                this.code += '0';
            } else {
                this.code += '1';
            }
        }
        */

        this.code = code;
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
        while (i<= (this.Mtotal -(this.Ment + this.Mdes))){
            //We fond the string that matches
            match = getMatch(code.substring(i,i+this.Mdes), code.substring(i+this.Mdes,i+this.Mdes +this.Ment));
            if (match.equals("0") || match.equals("1")){
                this.compressCode += match;
                i++;

            }else{
                //Displace the slidding window
                int displacement = Integer.parseInt(match.substring(0,this.MentLog),2);
                if (displacement == 0){
                    displacement = this.Ment;
                }
                i+= displacement;

                //Add this match to the compress code
                this.compressCode += match;
            }

        }
        this.compressCode += this.code.substring(i+this.Mdes);
    }

    public String getMatch(String slidingWindow , String inWindow){


        boolean found = false;
        String result = "";

        int count1 = this.countOccurences(slidingWindow,'1');
        int count0 = this.countOccurences(slidingWindow,'0');

        if((count0 == slidingWindow.length()) && (inWindow.charAt(0) == '1')){
            result = "1";
            found = true;
        }
        else if ((count1 == slidingWindow.length()) && (inWindow.charAt(0) == '0')){
            result = "0";
            found = true;
        }

        while(!found){
            //Never gonna happen but...
            if( inWindow.length() == 0) {
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
                        while (numberOfBitsBinary.length() < this.MentLog){
                            numberOfBitsBinary = "0" +numberOfBitsBinary;
                        }
                        while (displacementBinary.length() < this.MdesLog){
                            displacementBinary = "0" +displacementBinary;
                        }

                        result += numberOfBitsBinary.substring(numberOfBitsBinary.length() - this.MentLog);
                        result += displacementBinary.substring(displacementBinary.length() - this.MdesLog);


                    }
                    stepsBack +=1;
                    u--;
                }
            }
            //System.out.println("Window value: "+ inWindow);
            inWindow = inWindow.substring(0,(inWindow.length()-1));
        }
        //System.out.println("RESULT: " + result);
        return result;
    }

    public String decompressCode(){
        String result =this.compressCode.substring(0,this.Mdes);

        int i = this.Mdes;
        while((i+this.MentLog+this.MdesLog) < (this.compressCode.length())){

            String Lbits = this.compressCode.substring(i,i+this.MentLog);
            String Dbits = this.compressCode.substring(i+this.MentLog, i+this.MdesLog+this.MentLog);

            int L = Integer.parseInt(Lbits,2);
            int D = Integer.parseInt(Dbits,2);

            if(L==0){
                L = this.Ment;
            }
            if(D==0){
                D = this.Mdes;
            }

            String provResult = result.substring(result.length() - D, (result.length() - D) + L);

            i+= this.MentLog + this.MdesLog;
            result += provResult;
        }

        result += this.compressCode.substring(i);

        return result;
    }

    private int countOccurences(String str, char letter) {
        int count = 0;
        for(int i=0; i< str.length(); i++){
            if(str.charAt(i) == letter){
                count++;
            }
        }

        return count;
    }
}
