package models;

public class DataUpdate {
    private int id;
    private int col;
    private Object data;

    public DataUpdate(int id, int col, Object data)
    {
        this.id = id;
        this.col = col;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getCol() {
        return col;
    }

    public Object getData() {
        return data;
    }
}
