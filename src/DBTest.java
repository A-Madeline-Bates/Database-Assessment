import java.util.List;

public class DBTest {

    public DBTest() {
        //this is calling our model
        DBModel testModel = new DBModel();
        //this is calling an empty constructor
        DBController testController = new DBController(testModel);
        testGetSetData(testModel);
        testColumnFunc(testModel);
    }

    //Testing the getter and setter for data
    private void testGetSetData(DBModel testModel) {
        List<List<String>> testTable;
        testModel.setDataArray("hello i am happy to meet you");
        testTable = testModel.getDataArray();
        assert (testTable.get(0).size() == 7);
        testModel.setDataArray("hi there");
        testTable = testModel.getDataArray();
        assert (testTable.get(1).size() == 2);
        assert (testTable.get(0).size() == 7);
        //this might cause a problem/need changing
        testModel.setDataArray("");
        testTable = testModel.getDataArray();
        assert (testTable.get(2).size() == 1);
        testModel.setDataArray("100");
        testTable = testModel.getDataArray();
        assert (testTable.get(3).size() == 1);
        testModel.setDataArray("* *");
        testTable = testModel.getDataArray();
        assert (testTable.get(4).size() == 2);
        //an empty string is +1 but a block of whitespace isn't
        testModel.setDataArray("      ");
        testTable = testModel.getDataArray();
        assert (testTable.get(5).size() == 0);
        testModel.setDataArray("hello    to you  too");
        testTable = testModel.getDataArray();
        assert (testTable.get(6).size() == 4);

        assert(testTable.get(0).get(0).equals("hello"));
        assert(testTable.get(1).get(1).equals("there"));
        assert(testTable.get(3).get(0).equals("100"));
        assert(testTable.get(6).get(3).equals("too"));
    }

    //Testing our column functions
    private void testColumnFunc(DBModel testModel) {
        List<String> colArr;
        testModel.setColumnNames("hello thank you for visiting");
        colArr = testModel.getColumnNames();
        assert (colArr.size() == 5);
        testModel.setColumnNames("* * *");
        colArr = testModel.getColumnNames();
        assert (colArr.size() == 8);
        testModel.setColumnNames("1 01 03");
        colArr = testModel.getColumnNames();
        assert (colArr.size() == 11);
        testModel.setColumnNames(" ");
        colArr = testModel.getColumnNames();
        assert (colArr.size() == 11);

        assert(testModel.getColumnNumber() == 11);
        testModel.setColumnNames("there are 4 things");
        assert(testModel.getColumnNumber() == 15);
        testModel.setColumnNames("* * * *");
        assert(testModel.getColumnNumber() == 19);
    }
}
