package studyone.ksy.study.database;

public class ThingsDB {
    public static final String TABLE_NAME = "thing";

    public static final String COLUMN_THING_ID = "id";
    public static final String COLUMN_THING_NAME = "name";
    public static final String COLUMN_THING_COST = "cost";

    private String name;
    private int cost;

    //create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_THING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_THING_NAME + " STRING, "
                    + COLUMN_THING_COST + " INTEGER"
                    + ");";

    public ThingsDB(){
    }

    public ThingsDB(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnThingId() {
        return COLUMN_THING_ID;
    }

    public static String getColumnThingName() {
        return COLUMN_THING_NAME;
    }

    public static String getColumnThingCost() {
        return COLUMN_THING_COST;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }
}
