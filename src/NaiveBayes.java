import org.json.*;
import java.io.*;
import java.util.ArrayList;


public class NaiveBayes {
    private static ArrayList<String[]> columns = new ArrayList<>();
    private static ArrayList<ColumnItemObject> doom = new ArrayList<>();
    private static String[][] dataarr;
    private static String exstring="Given array length does not match with learned data";
    private static int predictarrlength;
    public  void learn(String FileAddress,String[] headers){
        doom.clear();
        StringBuilder data= new StringBuilder();
        dataarr = null;
        FileInputStream inputStream;
        try{
            inputStream = new FileInputStream(FileAddress);
            while(inputStream.available()>0){
                data.append((char) inputStream.read());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(data.length()>0){
            try {
                JSONObject jsonObject = new JSONObject(data.toString());
                JSONArray arr = jsonObject.getJSONArray("data");
                dataarr = new String[arr.length()][arr.getJSONObject(0).length()];
                for(int i  = 0 ; i < arr.length() ; i ++){
                    JSONObject dataobj = arr.getJSONObject(i);
                    for(int j = 0 ; j < arr.getJSONObject(0).length() ; j ++){
                        dataarr[i][j] = dataobj.getString((headers[j]));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            predictarrlength=dataarr[0].length;
            for(int i = 0; i < dataarr.length ; i ++){
                System.out.print(i+1+" ");
                for(int j = 0; j < dataarr[0].length ; j ++){
                    System.out.print(dataarr[i][j]+"\t\t\t\t");
                }
                System.out.println("\n");
            }
            ArrayList<ColumnObject> columnObjects = new ArrayList<>();
            for(int i = 0; i < dataarr[0].length ; i ++){
                String[] colArray=new String[dataarr.length];
                for(int row = 0; row < dataarr.length; row++) {
                    colArray[row] = dataarr[row][i];
                }
                columns.add(colArray);
                columnObjects.add(new ColumnObject(colArray));
            }

            ArrayList<ColumnItemObject> columnItemObjects = new ArrayList<>();
            for (ColumnObject columnObject : columnObjects) {
                columnItemObjects.addAll(columnObject.getUniqueitems());
            }
            ArrayList<ColumnItemObject> coom = new ArrayList<>();
            for (ColumnItemObject object : columnItemObjects) {
                coom.add(new ColumnItemObject(object.getData(),
                        object.getOccurence()));
            }
            ArrayList<ColumnItemObject> boom = new ArrayList<>();

            for(int i = 0 ; i < columns.size() ; i ++){
                for (String[] column : columns) {
                    String[] arr1 = columns.get(i);
                    ArrayList<String> uniques_arr1 = getoccurences(arr1);
                    ArrayList<String> uniques_arr2 = getoccurences(column);
                    for (String unique_arr1_string : uniques_arr1) {
                        for (String unique_arr2_string : uniques_arr2) {
                            int xcount = 0;
                            for (int r = 0; r < arr1.length; r++) {
                                if (arr1[r].equals(unique_arr1_string) &&
                                        column[r].equals(unique_arr2_string)) {
                                    xcount++;
                                }
                            }
                            boom.add(new ColumnItemObject(unique_arr1_string +
                                    "&&" +
                                    unique_arr2_string, String.valueOf(xcount)));
                        }
                    }
                }
            }
//            System.out.println(boom);
//            System.out.println(coom);
            for (ColumnItemObject itemObject : boom) {
                String[] arr = itemObject.getData().split("&&");
                String a = arr[0];
                String b = arr[1];
                ColumnItemObject coomb = getb(coom, b);
                assert coomb != null;
                ColumnItemObject doomb = new ColumnItemObject(a + "&|&" + b,
                        String.valueOf(Double.parseDouble(itemObject.getOccurence()) /
                                Double.parseDouble(coomb.getOccurence())));
                doom.add(doomb);
            }
            for (ColumnItemObject itemObject : coom) {
                ColumnItemObject columnItemObject = new ColumnItemObject(itemObject.getData(),
                        String.valueOf(Double.parseDouble(itemObject.getOccurence()) / dataarr.length));
                doom.add(columnItemObject);
            }
/*
            for (ColumnItemObject columnItemObject : doom) {
              System.out.println(columnItemObject.getData().replaceAll("&", "") +
                       "\t" + columnItemObject.getOccurence());
            }
*/
        }
    }
    private static ColumnItemObject getb(ArrayList<ColumnItemObject> cibs,String b){
        for (ColumnItemObject cib : cibs) {
            if (cib.getData().equals(b)) {
                return cib;
            }
        }
        return null;
    }
    private static ArrayList<String> getoccurences(String[] arr){
        ArrayList<String> uniqueitems = new ArrayList<>();
        for(int i = 0 ; i < arr.length ; i ++){
            String a = arr[i];
            int count = 0 ;
            for(int j = i+1 ; j < arr.length ; j++){
                if(a.equals(arr[j])){
                    count++;
                }
            }
            if(count==0){
                uniqueitems.add(a);
            }
        }
        return uniqueitems;
    }


    public String predict(String[] predictarr) {
        ArrayList<String> predictors = new ArrayList<>();
        ArrayList<ColumnItemObject> mainpredictarr = new ArrayList<>();
        if (predictarr.length != predictarrlength) {
            try {
                throw new IncorrectLength(exstring);
            } catch (IncorrectLength incorrectLength) {
                incorrectLength.printStackTrace();
            }
        } else {
            for (String s : predictarr) {
                if (!s.equals("?")) {
                    predictors.add(s);
                }
            }
            for (int i = 0; i < predictarr.length; i++) {
                if(predictarr[i].equals("?")){
                    String[] columnarray = getColumnArray(i);
                    for (String value : columnarray) {
                        double calci = 1;
                        calci *= Double.parseDouble(getColumnItemObject(value).getOccurence());
                        for (String predictor : predictors) {
                            String r = predictor + "|" + value;
//                           System.out.println(r);
                            calci *= Double.parseDouble(getColumnItemObject(r).getOccurence());
//                            System.out.println(calci);

                        }
                        mainpredictarr.add(new ColumnItemObject(value, String.valueOf(calci)));
                    }
                }
            }
//            for(int i = 0 ; i < mainpredictarr.size() ; i ++){
//                System.out.println(mainpredictarr.get(i).getData()+" "+mainpredictarr.get(i).getOccurence());
//            }
        }
        return getLargest(mainpredictarr);
    }
    private ColumnItemObject getColumnItemObject(String r){
        ColumnItemObject cio=null;
        for (ColumnItemObject columnItemObject : doom) {
            if (columnItemObject.getData().replaceAll("&", "").equals(r)) {
                cio = columnItemObject;
                break;
            }
        }
        return cio;
    }
    private static String[] getColumnArray(int column){
        String[] colArray=new String[dataarr.length];
        for(int row = 0; row < dataarr.length; row++) {
            colArray[row] = dataarr[row][column];
        }
        ArrayList<String> uniqueitems = setUniqueitems(colArray);
        String[] arr = new String[uniqueitems.size()];
        for(int i = 0 ; i < uniqueitems.size() ; i ++){
            arr[i] = uniqueitems.get(i);
        }
        return arr;
    }
    private static ArrayList<String> setUniqueitems(String[] columnarray){
        ArrayList<String> uniqueitems = new ArrayList<>();
        for(int i = 0 ; i < columnarray.length ; i ++){
            String a = columnarray[i];
            int count = 0 ;
            for(int j = i+1 ; j < columnarray.length ; j++){
                if(a.equals(columnarray[j])){
                    count++;
                }
            }
            if(count==0){
                uniqueitems.add(a);
            }
        }
        return uniqueitems;
    }
    private static String getLargest(ArrayList<ColumnItemObject> cio){
        double one = Double.parseDouble(cio.get(0).getOccurence());
        String onex = cio.get(0).getData();
        for(int i = 1 ; i < cio.size() ; i ++){
            if(Double.parseDouble(cio.get(i).getOccurence())>one){
                one = Double.parseDouble(cio.get(i).getOccurence());
                onex = cio.get(i).getData();
            }
        }
        return onex;
    }

    private static class IncorrectLength extends Exception{
        IncorrectLength(String s){
            super(s);
        }
    }
}
