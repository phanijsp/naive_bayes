import java.util.ArrayList;

public class ColumnObject {
    private String[] columnarray;
    private ArrayList<ColumnItemObject> uniqueitems;
    ColumnObject(String[] columnarray){
        this.columnarray = columnarray;
        setUniqueitems();
    }
    private void setUniqueitems(){
        uniqueitems = new ArrayList<>();
        for(int i = 0 ; i < columnarray.length ; i ++){
            String a = columnarray[i];
            int count = 0 ;
            for(int j = i+1 ; j < columnarray.length ; j++){
                if(a.equals(columnarray[j])){
                    count++;
                }else{
                    continue;
                }
            }
            if(count==0){
                uniqueitems.add(new ColumnItemObject(a,String.valueOf(countoccurences(a))));
            }
        }
    }
    private int countoccurences(String value){
        int count = 0 ;
        for(int i = 0 ; i < columnarray.length ; i ++){
            if(value.equals(columnarray[i])){
                count++;
            }
        }
        return count;
    }

    public ArrayList<ColumnItemObject> getUniqueitems(){
            return uniqueitems;
    }
}
