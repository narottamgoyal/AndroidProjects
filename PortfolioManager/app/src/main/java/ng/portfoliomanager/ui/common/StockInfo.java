package ng.portfoliomanager.ui.common;

public class StockInfo {
    public String Name;

    public StockInfo(String name, int count) {
        Name = name;
        Count = count;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int Count;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
