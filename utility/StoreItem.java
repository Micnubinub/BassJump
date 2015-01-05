package tbs.jumpsnew.utility;

public class StoreItem {
    public final Type type;
    public final int price;
    public final String name, description;
    public final String tag;
    public boolean bought;

    public StoreItem(Type type, String tag, String name, String description, int price, boolean bought) {
        this.type = type;
        this.tag = tag;
        this.price = price;
        this.bought = bought;
        this.name = name;
        this.description = description;
    }


    public enum Type {
        SHAPE, COLOR, SONG
    }
}