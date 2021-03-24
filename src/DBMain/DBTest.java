package DBMain;
import DBMain.CommandFiles.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.ParseExceptions;

import java.util.ArrayList;
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
//        testLoadAndStore(testDataModel2, testPathModel);
        testTokeniser();
        DBCommandFactory testParser = new DBCommandFactory();
//        testParse(testParser);
        CMDType command = new CMDUse();
        //This calls the separate test file within DBMain.CommandFiles
        new TestCMD(command);
        testIDCol();
        testIDRow();
    }

    //Testing the getter and setter for data
    private void testGetSetData(DBModelData testDataModel) {
        List<List<String>> testTable;
        testDataModel.setRowsFromFile("hello\ti\tam\thappy\tto\tmeet\tyou");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(0).size() == 7);
        testDataModel.setRowsFromFile("hi\tthere");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(1).size() == 2);
        assert (testTable.get(0).size() == 7);
        //this might cause a problem/need changing
        testDataModel.setRowsFromFile("");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(2).size() == 0);
        testDataModel.setRowsFromFile("100");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(3).size() == 1);
        testDataModel.setRowsFromFile("*\t*");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(4).size() == 2);
        //an empty string is +1 but a block of whitespace isn't
        testDataModel.setRowsFromFile("\t");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(5).size() == 0);
        testDataModel.setRowsFromFile("hello to\tyou too");
        testTable = testDataModel.getRowsData();
        assert (testTable.get(6).size() == 2);
        assert (testTable.get(0).get(0).equals("hello"));
        assert (testTable.get(1).get(1).equals("there"));
        assert (testTable.get(3).get(0).equals("100"));
        assert (testTable.get(6).get(1).equals("you too"));
    }

    //Testing our column functions
    private void testColumnArr(DBModelData testDataModel) {
        List<String> colArr;
        testDataModel.setColumnsFromFile("hello\tthank\tyou\tfor\tvisiting");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 5);
        testDataModel.setColumnsFromFile("*\t*\t*");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 8);
        testDataModel.setColumnsFromFile("1\t01\t03");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 11);
        testDataModel.setColumnsFromFile("\t");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 11);
        //this is a space, which should be recognised as a character
        testDataModel.setColumnsFromFile(" ");
        colArr = testDataModel.getColumnData();
        assert (colArr.size() == 12);
        assert (colArr.get(0).equals("hello"));
        assert (colArr.get(10).equals("03"));
    }

    private void testGetRowCol(DBModelData testDataModel) {
        //There should already be 12 columns set as a hang over from testColumnArr()
        assert (testDataModel.getColumnNumber() == 12);
        testDataModel.setColumnsFromFile("there\tare\t4\tthings");
        assert (testDataModel.getColumnNumber() == 16);
        testDataModel.setColumnsFromFile("*\t*\t*\t*");
        assert (testDataModel.getColumnNumber() == 20);
        testDataModel.setColumnsFromFile("1\t2\t3\t4\t5\t6");
        assert (testDataModel.getColumnNumber() == 26);
        //There should already be 7 rows set as a hang over from testGetSetData()
        assert (testDataModel.getRowNumber() == 7);
        testDataModel.setRowsFromFile("100");
        assert (testDataModel.getRowNumber() == 8);
        testDataModel.setRowsFromFile("F");
        assert (testDataModel.getRowNumber() == 9);
        testDataModel.setRowsFromFile("test\ttest\ttest");
        assert (testDataModel.getRowNumber() == 10);
    }
//
//    private void testLoadAndStore(DBModelData testDataModel, DBModelPath testDataPath) {
//        new DBLoad(testDataModel, "testFiles", "test2.txt");
//        assert (testDataModel.getRowNumber() == 5);
//        assert (testDataModel.getColumnNumber() == 3);
//        testDataPath.setFilename("test2.txt");
//        testDataPath.setDatabaseName("testFiles");
//        assert (testDataPath.getFilename().equals("test2.txt"));
//        assert (testDataPath.getDatabaseName().equals("testFiles"));
////        new DBStore (testDataModel, testDataPath);
//    }

    private void testTokeniser() {
        DBTokeniser testT1 = new DBTokeniser("hello world");
//        System.out.println("*" + testT1.nextToken());
        assert (testT1.nextToken().equals("hello"));
        assert (testT1.nextToken().equals("world"));
        assert (testT1.nextToken() == null);
        DBTokeniser testT2 = new DBTokeniser("1 2 3");
        assert (testT2.nextToken().equals("1"));
        assert (testT2.nextToken().equals("2"));
        assert (testT2.nextToken().equals("3"));
        assert (testT2.nextToken() == null);
        DBTokeniser testT3 = new DBTokeniser("1     2 4");
        assert (testT3.nextToken().equals("1"));
        assert (testT3.nextToken().equals("2"));
        assert (testT3.nextToken().equals("4"));
        assert (testT3.nextToken() == null);
        DBTokeniser testT6 = new DBTokeniser(";() 'hello there', ';(),'");
        assert (testT6.nextToken().equals(";"));
        assert (testT6.nextToken().equals("("));
        assert (testT6.nextToken().equals(")"));
        assert (testT6.nextToken().equals("'hello there'"));
        assert (testT6.nextToken().equals(","));
        assert (testT6.nextToken().equals("';(),'"));
        assert (testT6.nextToken() == null);
        DBTokeniser testT5 = new DBTokeniser("VALUES (hello, true, false);");
        assert (testT5.nextToken().equals("VALUES"));
        assert (testT5.nextToken().equals("("));
        assert (testT5.nextToken().equals("hello"));
        assert (testT5.nextToken().equals(","));
        assert (testT5.nextToken().equals("true"));
        assert (testT5.nextToken().equals(","));
        assert (testT5.nextToken().equals("false"));
        assert (testT5.nextToken().equals(")"));
        assert (testT5.nextToken().equals(";"));
        assert (testT5.nextToken() == null);
        DBTokeniser testT4 = new DBTokeniser("VALUES ('hello', true, false);");
        assert (testT4.nextToken().equals("VALUES"));
        assert (testT4.nextToken().equals("("));
        assert (testT4.nextToken().equals("'hello'"));
        assert (testT4.nextToken().equals(","));
        assert (testT4.nextToken().equals("true"));
        assert (testT4.nextToken().equals(","));
        assert (testT4.nextToken().equals("false"));
        assert (testT4.nextToken().equals(")"));
        assert (testT4.nextToken().equals(";"));
        assert (testT4.nextToken() == null);
        DBTokeniser testT7 = new DBTokeniser("VALUES('hello',true,false);");
        assert (testT7.nextToken().equals("VALUES"));
        assert (testT7.nextToken().equals("("));
        assert (testT7.nextToken().equals("'hello'"));
        assert (testT7.nextToken().equals(","));
        assert (testT7.nextToken().equals("true"));
        assert (testT7.nextToken().equals(","));
        assert (testT7.nextToken().equals("false"));
        assert (testT7.nextToken().equals(")"));
        assert (testT7.nextToken().equals(";"));
        assert (testT7.nextToken() == null);
        DBTokeniser testT8 = new DBTokeniser("== = <= < >= > != ! '==' *");
        assert (testT8.nextToken().equals("=="));
        assert (testT8.nextToken().equals("="));
        assert (testT8.nextToken().equals("<="));
        assert (testT8.nextToken().equals("<"));
        assert (testT8.nextToken().equals(">="));
        assert (testT8.nextToken().equals(">"));
        assert (testT8.nextToken().equals("!="));
        assert (testT8.nextToken().equals("!"));
        assert (testT8.nextToken().equals("'=='"));
        assert (testT8.nextToken().equals("*"));
        assert (testT8.nextToken() == null);
    }

//    private void testParse(DBCommandFactory testParser){
////        DBTokeniser testT1 = new DBTokeniser("Use");
////        testParser.parse(testT1);
//    }


    private void testIDCol() {
        DBModelData testDataModel2 = new DBModelData();
        ArrayList<String> testCol = new ArrayList<>();
        testCol.add("student");
        testCol.add("grade");
        testCol.add("mark");
        testDataModel2.setColumnsFromSQL(testCol);
        assert (testDataModel2.getColumnData().get(0).equals("ID"));
        assert (testDataModel2.getColumnData().get(1).equals("student"));
        assert (testDataModel2.getColumnData().get(2).equals("grade"));
        assert (testDataModel2.getColumnData().get(3).equals("mark"));
        assert (testDataModel2.getColumnNumber() == 4);
        DBModelData testDataModel3 = new DBModelData();
        ArrayList<String> testCol2 = new ArrayList<>();
        testCol2.add("films");
        testDataModel3.setColumnsFromSQL(testCol2);
        assert (testDataModel3.getColumnData().get(0).equals("ID"));
        assert (testDataModel3.getColumnData().get(1).equals("films"));
        assert (testDataModel3.getColumnNumber() == 2);
        DBModelData testDataModel4 = new DBModelData();
        ArrayList<String> testCol3 = new ArrayList<>();
        testCol3.add("dogs");
        testCol3.add("cats");
        testCol3.add("bears");
        testDataModel4.setColumnsFromSQL(testCol3);
        assert (testDataModel4.getColumnData().get(0).equals("ID"));
        assert (testDataModel4.getColumnData().get(1).equals("dogs"));
        assert (testDataModel4.getColumnData().get(2).equals("cats"));
        assert (testDataModel4.getColumnData().get(3).equals("bears"));
        assert (testDataModel4.getColumnNumber() == 4);
    }

    private void testIDRow() {
        DBModelData testDataModel5 = new DBModelData();
        ArrayList<String> testRow = new ArrayList<>();
        testRow.add("a");
        testRow.add("b");
        testRow.add("c");
        testDataModel5.setRowsFromSQL(testRow);
        assert (testDataModel5.getRowsData().get(0).get(0).equals("0"));
        assert (testDataModel5.getRowsData().get(0).get(1).equals("a"));
        assert (testDataModel5.getRowsData().get(0).get(2).equals("b"));
        assert (testDataModel5.getRowsData().get(0).get(3).equals("c"));
        assert (testDataModel5.getRowNumber() == 1);
        ArrayList<String> testRow2 = new ArrayList<>();
        testRow2.add("d");
        testRow2.add("e");
        testRow2.add("f");
        testDataModel5.setRowsFromSQL(testRow2);
        assert (testDataModel5.getRowsData().get(1).get(0).equals("1"));
        assert (testDataModel5.getRowsData().get(1).get(1).equals("d"));
        assert (testDataModel5.getRowsData().get(1).get(2).equals("e"));
        assert (testDataModel5.getRowsData().get(1).get(3).equals("f"));
        assert (testDataModel5.getRowNumber() == 2);
        ArrayList<String> testRow3 = new ArrayList<>();
        testRow3.add("g");
        testRow3.add("h");
        testRow3.add("i");
        testDataModel5.setRowsFromSQL(testRow3);
        assert (testDataModel5.getRowsData().get(2).get(0).equals("2"));
        assert (testDataModel5.getRowsData().get(2).get(1).equals("g"));
        assert (testDataModel5.getRowsData().get(2).get(2).equals("h"));
        assert (testDataModel5.getRowsData().get(2).get(3).equals("i"));
        assert (testDataModel5.getRowNumber() == 3);
        assert (testDataModel5.getRowsData().get(1).get(1).equals("d"));
        assert (testDataModel5.getRowsData().get(0).get(3).equals("c"));
    }
}
