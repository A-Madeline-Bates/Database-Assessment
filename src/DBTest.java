import java.util.List;

public class DBTest {

    public DBTest() {
        //this is calling our model
        new DBModel();
        DBModelFileData testDataModel = new DBModelFileData();
        DBModelFilePath testPathModel = new DBModelFilePath();
        //this is calling an empty constructor
        DBController testController = new DBController(testDataModel, testPathModel);
        testGetSetData(testDataModel);
        testColumnArr(testDataModel);
        testGetRowCol(testDataModel);
        DBModelFileData testDataModel2 = new DBModelFileData();
        testLoadAndStore(testDataModel2);
    }

    //Testing the getter and setter for data
    private void testGetSetData(DBModelFileData testDataModel) {
        List<List<String>> testTable;
        testDataModel.setRowsData("hello\ti\tam\thappy\tto\tmeet\tyou");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(0).size() == 7);
        testDataModel.setRowsData("hi\tthere");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(1).size() == 2);
        assert (testTable.get(0).size() == 7);
        //this might cause a problem/need changing
        testDataModel.setRowsData("");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(2).size() == 1);
        testDataModel.setRowsData("100");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(3).size() == 1);
        testDataModel.setRowsData("*\t*");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(4).size() == 2);
        //an empty string is +1 but a block of whitespace isn't
        testDataModel.setRowsData("\t");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(5).size() == 0);
        testDataModel.setRowsData("hello to\tyou too");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(6).size() == 2);
        assert(testTable.get(0).get(0).equals("hello"));
        assert(testTable.get(1).get(1).equals("there"));
        assert(testTable.get(3).get(0).equals("100"));
        assert(testTable.get(6).get(1).equals("you too"));
    }

    //Testing our column functions
    private void testColumnArr(DBModelFileData testDataModel) {
        List<String> colArr;
        testDataModel.setColumnData("hello\tthank\tyou\tfor\tvisiting");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 5);
        testDataModel.setColumnData("*\t*\t*");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 8);
        testDataModel.setColumnData("1\t01\t03");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 11);
        testDataModel.setColumnData("\t");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 11);
        //this is a space, which should be recognised as a character
        testDataModel.setColumnData(" ");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 12);
        assert(colArr.get(0).equals("hello"));
        assert(colArr.get(10).equals("03"));
    }

    private void testGetRowCol(DBModelFileData testDataModel){
        //There should already be 12 columns set as a hang over from testColumnArr()
        assert(testDataModel.getColumnNumber() == 12);
        testDataModel.setColumnData("there\tare\t4\tthings");
        assert(testDataModel.getColumnNumber() == 16);
        testDataModel.setColumnData("*\t*\t*\t*");
        assert(testDataModel.getColumnNumber() == 20);
        testDataModel.setColumnData("1\t2\t3\t4\t5\t6");
        assert(testDataModel.getColumnNumber() == 26);
        //There should already be 7 rows set as a hang over from testGetSetData()
        assert(testDataModel.getRowNumber() == 7);
        testDataModel.setRowsData("100");
        assert(testDataModel.getRowNumber() == 8);
        testDataModel.setRowsData("F");
        assert(testDataModel.getRowNumber() == 9);
        testDataModel.setRowsData("test\ttest\ttest");
        assert(testDataModel.getRowNumber() == 10);
    }

    private void testLoadAndStore(DBModelFileData testDataModel){
        new DBLoad (testDataModel, "testFiles", "test2.txt");
        assert(testDataModel.getRowNumber() == 5);
        assert(testDataModel.getColumnNumber() == 3);
        new DBStore (testDataModel, "testFiles", "test2output.txt");
//        new DBLoad (testDataModel, "testFiles", "test2.txt");
//        assert(testDataModel.getRowNumber() == 5);
//        assert(testDataModel.getColumnNumber() == 3);
//        new DBStore (testDataModel, "testFiles", "test2output.txt");
    }
}
