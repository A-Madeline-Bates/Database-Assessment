import java.util.ArrayList;
import java.util.List;

public class DBTest {

    public DBTest() {
        //this is calling our model
        DBModel testModel = new DBModel();
        //this is calling an empty constructor
        DBController testController = new DBController(testModel);
        testGetSetData(testModel);
        testGetSetCol(testModel);
    }

    private void testGetSetData(DBModel testModel) {
        List<String> testArr = new ArrayList<String>();
        testModel.setDataArray("hello i am happy to meet you");
        testArr = testModel.getDataArray();
        assert (testArr.size() == 7);
        testModel.setDataArray("hi there");
        testArr = testModel.getDataArray();
        assert (testArr.size() == 9);
        //this might cause a problem/need changing
        testModel.setDataArray("");
        testArr = testModel.getDataArray();
        assert (testArr.size() == 10);
        testModel.setDataArray("100");
        testArr = testModel.getDataArray();
        assert (testArr.size() == 11);
        testModel.setDataArray("* *");
        testArr = testModel.getDataArray();
        assert (testArr.size() == 13);
        //an empty string is +1 but a block of whitespace isn't
        testModel.setDataArray("      ");
        testArr = testModel.getDataArray();
        assert (testArr.size() == 13);
        testModel.setDataArray("hello    to you  too");
        testArr = testModel.getDataArray();
        assert (testArr.size() == 17);
    }

    private void testGetSetCol(DBModel testModel) {
        List<String> colArr = new ArrayList<String>();
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
    }
}
