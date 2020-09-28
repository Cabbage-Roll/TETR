package cabbageroll.tetrjar;

public class Bags {

    public static int bag_counter = 0;
    public static int[] bag1=new int[7];
    public static int[] bag2=new int[7];

    ///makes new second bag (7 bag buffer)
    public static void generatebag2(){
        bag_counter=0;
        for(int i=0;i<7;i++){
            bag2[i]=(int)(Math.random()*7);
            for(int j=0;j<i;j++){
                if(bag2[i]==bag2[j]){
                    i--;
                }
            }
        }
    }

    ///shifts both bags
    public static void shiftbag1(){
        if(bag_counter>6){
            generatebag2();
        }
        for(int i=0;i<6;i++){
            bag1[i]=bag1[i+1];
        }
        bag1[6]=bag2[0];
        for(int i=0;i<6;i++){
            bag2[i]=bag2[i+1];
        }
        bag_counter++;
    }

}
