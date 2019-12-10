public class Main {
    public static void main(String[] args){
        NaiveBayes NB = new NaiveBayes();
        String[] arr = {"outlook","temp","humidity","wind","play"};
        NB.learn("/root/Documents/x.json",arr);
        System.out.println(NB.predict(new String[]{"?", "cool","normal","weak","yes"}));
    }
}
