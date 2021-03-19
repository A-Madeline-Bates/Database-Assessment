package DBMain;
import DBMain.CommandFiles.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.ParseExceptions;

import java.util.List;

public class DBTest {

    public DBTest() {
        //this is calling our model
        new DBModel();
        DBModelData testDataModel = new DBModelData();
        DBModelPath testPathModel = new DBModelPath();
        testGetSetData(testDataModel);
        testColumnArr(testDataModel);
        testGetRowCol(testDataModel);
        DBModelData testDataModel2 = new DBModelData();
        testLoadAndStore(testDataModel2, testPathModel);
        testTokeniser();
        DBCommandFactory testParser = new DBCommandFactory();
        testParse(testParser);

        CMDType command = new CMDUse();
        //This calls the separate test file within DBMain.CommandFiles
        new TestCMD(command);
    }

    //Testing the getter and setter for data
    private void testGetSetData(DBModelData testDataModel) {
        List<List<String>> testTable;
        testDataModel.setRowsDataFromLoad("hello\ti\tam\thappy\tto\tmeet\tyou");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(0).size() == 7);
        testDataModel.setRowsDataFromLoad("hi\tthere");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(1).size() == 2);
        assert (testTable.get(0).size() == 7);
        //this might cause a problem/need changing
        testDataModel.setRowsDataFromLoad("");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(2).size() == 0);
        testDataModel.setRowsDataFromLoad("100");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(3).size() == 1);
        testDataModel.setRowsDataFromLoad("*\t*");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(4).size() == 2);
        //an empty string is +1 but a block of whitespace isn't
        testDataModel.setRowsDataFromLoad("\t");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(5).size() == 0);
        testDataModel.setRowsDataFromLoad("hello to\tyou too");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(6).size() == 2);
        assert(testTable.get(0).get(0).equals("hello"));
        assert(testTable.get(1).get(1).equals("there"));
        assert(testTable.get(3).get(0).equals("100"));
        assert(testTable.get(6).get(1).equals("you too"));
    }

    //Testing our column functions
    private void testColumnArr(DBModelData testDataModel) {
        List<String> colArr;
        testDataModel.setColumnDataFromLoad("hello\tthank\tyou\tfor\tvisiting");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 5);
        testDataModel.setColumnDataFromLoad("*\t*\t*");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 8);
        testDataModel.setColumnDataFromLoad("1\t01\t03");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 11);
        testDataModel.setColumnDataFromLoad("\t");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 11);
        //this is a space, which should be recognised as a character
        testDataModel.setColumnDataFromLoad(" ");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 12);
        assert(colArr.get(0).equals("hello"));
        assert(colArr.get(10).equals("03"));
    }

    private void testGetRowCol(DBModelData testDataModel){
        //There should already be 12 columns set as a hang over from testColumnArr()
        assert(testDataModel.getColumnNumber() == 12);
        testDataModel.setColumnDataFromLoad("there\tare\t4\tthings");
        assert(testDataModel.getColumnNumber() == 16);
        testDataModel.setColumnDataFromLoad("*\t*\t*\t*");
        assert(testDataModel.getColumnNumber() == 20);
        testDataModel.setColumnDataFromLoad("1\t2\t3\t4\t5\t6");
        assert(testDataModel.getColumnNumber() == 26);
        //There should already be 7 rows set as a hang over from testGetSetData()
        assert(testDataModel.getRowNumber() == 7);
        testDataModel.setRowsDataFromLoad("100");
        assert(testDataModel.getRowNumber() == 8);
        testDataModel.setRowsDataFromLoad("F");
        assert(testDataModel.getRowNumber() == 9);
        testDataModel.setRowsDataFromLoad("test\ttest\ttest");
        assert(testDataModel.getRowNumber() == 10);
    }

    private void testLoadAndStore(DBModelData testDataModel, DBModelPath testDataPath){
        new DBLoad (testDataModel, "testFiles", "test2.txt");
        assert(testDataModel.getRowNumber() == 5);
        assert(testDataModel.getColumnNumber() == 3);
        testDataPath.setFilename("test2.txt");
        testDataPath.setDatabaseName("testFiles");
        assert(testDataPath.getFilename().equals("test2.txt"));
        assert(testDataPath.getDatabaseName().equals("testFiles"));
//        new DBStore (testDataModel, testDataPath);
    }

    private void testTokeniser(){
        DBTokeniser testT1 = new DBTokeniser("hello world");
        assert(testT1.nextToken().equals("hello"));
        assert(testT1.nextToken().equals("world"));
        assert(testT1.nextToken() == null);
        DBTokeniser testT2 = new DBTokeniser("1 2 3");
        assert(testT2.nextToken().equals("1"));
        assert(testT2.nextToken().equals("2"));
        assert(testT2.nextToken().equals("3"));
        assert(testT2.nextToken() == null);
//        DBTokeniser testT3 = new DBTokeniser("1     2. 4");
//        assert(testT3.nextToken().equals("1"));
//        assert(testT3.nextToken().equals("2."));
//        assert(testT3.nextToken().equals("4"));
//        assert(testT3.nextToken() == null);
        DBTokeniser testT5 = new DBTokeniser("VALUES (hello, true, false);");
        assert(testT5.nextToken().equals("VALUES"));
        assert(testT5.nextToken().equals("("));
        assert(testT5.nextToken().equals("hello"));
        assert(testT5.nextToken().equals(","));
        assert(testT5.nextToken().equals("true"));
        assert(testT5.nextToken().equals(","));
        assert(testT5.nextToken().equals("false"));
        assert(testT5.nextToken().equals(")"));
        assert(testT5.nextToken().equals(";"));
        assert(testT5.nextToken() == null);
        DBTokeniser testT4 = new DBTokeniser("VALUES ('hello', true, false);");
        assert(testT4.nextToken().equals("VALUES"));
        assert(testT4.nextToken().equals("("));
        assert(testT4.nextToken().equals("'hello'"));
        assert(testT4.nextToken().equals(","));
        assert(testT4.nextToken().equals("true"));
        assert(testT4.nextToken().equals(","));
        assert(testT4.nextToken().equals("false"));
        assert(testT4.nextToken().equals(")"));
        assert(testT4.nextToken().equals(";"));
        assert(testT4.nextToken() == null);
    }

    private void testParse(DBCommandFactory testParser){
//        DBTokeniser testT1 = new DBTokeniser("Use");
//        testParser.parse(testT1);
    }
}
