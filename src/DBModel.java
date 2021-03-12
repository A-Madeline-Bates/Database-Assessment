import java.util.ArrayList;
import java.util.List;

public class DBModel {
    protected List<List<String>> tableData = new ArrayList<List<String>>();
    protected ArrayList<String> columnNames = new ArrayList<>();
    protected String filename;
    protected String databaseName;
    protected int row = 0;
}
