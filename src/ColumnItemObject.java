public class ColumnItemObject {
    private String data;
    private String occurence;
    ColumnItemObject(String data,
                     String occurence){
        this.data = data;
        this.occurence = occurence;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOccurence() {
        return occurence;
    }

    public void setOccurence(String occurence) {
        this.occurence = occurence;
    }
}
