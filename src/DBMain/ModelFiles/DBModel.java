package DBMain.ModelFiles;
import java.util.ArrayList;
import java.util.List;

public class DBModel {
    final List<List<String>> tableData = new ArrayList<List<String>>();
    final ArrayList<String> columnNames = new ArrayList<>();
    protected String filename;
    protected String databaseName;
    protected int row = 0;
}
