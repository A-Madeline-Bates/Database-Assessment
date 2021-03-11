import java.util.List;

public class DBTest {

    public DBTest() {
        //this is calling our model
        DBModel testModel = new DBModel();
        //this is calling an empty constructor
        DBController testController = new DBController(testModel);
        testGetSetData(testModel);
        testColumnArr(testModel);
        testGetRowCol(testModel);
    }

    //Testing the getter and setter for data
    private void testGetSetData(DBModel testModel) {
        List<List<String>> testTable;
        testModel.setDataArray("hello\ti\tam\thappy\tto\tmeet\tyou");
        testTable = testModel.getDataArray();
        assert (testTable.get(0).size() == 7);
        testModel.setDataArray("hi\tthere");
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
        testModel.setDataArray("*\t*");
        testTable = testModel.getDataArray();
        assert (testTable.get(4).size() == 2);
        //an empty string is +1 but a block of whitespace isn't
        testModel.setDataArray("\t");
        testTable = testModel.getDataArray();
        assert (testTable.get(5).size() == 0);
        testModel.setDataArray("hello to\tyou too");
        testTable = testModel.getDataArray();
        assert (testTable.get(6).size() == 2);
        assert(testTable.get(0).get(0).equals("hello"));
        assert(testTable.get(1).get(1).equals("there"));
        assert(testTable.get(3).get(0).equals("100"));
        assert(testTable.get(6).get(1).equals("you too"));
    }

    //Testing our column functions
    private void testColumnArr(DBModel testModel) {
        List<String> colArr;
        testModel.setColumnNames("hello\tthank\tyou\tfor\tvisiting");
        colArr = testModel.getColumnNames();
        assert (colArr.size() == 5);
        testModel.setColumnNames("*\t*\t*");
        colArr = testModel.getColumnNames();
        assert (colArr.size() == 8);
        testModel.setColumnNames("1\t01\t03");
        colArr = testModel.getColumnNames();
        assert (colArr.size() == 11);
        testModel.setColumnNames("\t");
        colArr = testModel.getColumnNames();
        assert (colArr.size() == 11);
        //this is a space, which should be recognised as a character
        testModel.setColumnNames(" ");
        colArr = testModel.getColumnNames();
        assert (colArr.size() == 12);
        assert(colArr.get(0).equals("hello"));
        assert(colArr.get(10).equals("03"));
    }

    private void testGetRowCol(DBModel testModel){
        //There should already be 12 columns set as a hang over from testColumnArr()
        assert(testModel.getColumnNumber() == 12);
        testModel.setColumnNames("there\tare\t4\tthings");
        assert(testModel.getColumnNumber() == 16);
        testModel.setColumnNames("*\t*\t*\t*");
        assert(testModel.getColumnNumber() == 20);
        testModel.setColumnNames("1\t2\t3\t4\t5\t6");
        assert(testModel.getColumnNumber() == 26);
        //There should already be 6 rows set as a hang over from testGetSetData()
        assert(testModel.getRowNumber() == 6);
        testModel.setDataArray("100");
        assert(testModel.getRowNumber() == 7);
        testModel.setDataArray("F");
        assert(testModel.getRowNumber() == 8);
        testModel.setDataArray("test\ttest\ttest");
        assert(testModel.getRowNumber() == 9);
    }
}
