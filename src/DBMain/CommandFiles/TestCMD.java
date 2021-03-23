package DBMain.CommandFiles;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.ParseExceptions;

public class TestCMD {
	CMDType command;

	//test all command instructions here?

	public TestCMD(CMDType command) {
		this.command = command;
		testIsItFinalCommand();
		testIsItAlphNumeric();
		isItValidValue();
	}

	private void testIsItFinalCommand() {
		assert (command.isItNullEnd(null));
		assert (!command.isItNullEnd("hello"));
		assert (!command.isItNullEnd("123"));
	}

	private void testIsItAlphNumeric() {
		assert (command.isItAlphNumeric("abc"));
		assert (command.isItAlphNumeric("ABc"));
		assert (command.isItAlphNumeric("ABC"));
		assert (command.isItAlphNumeric("123"));
		assert (!command.isItAlphNumeric("!"));
		assert (!command.isItAlphNumeric(" "));
		assert (!command.isItAlphNumeric("_"));
	}

	private void isItValidValue() {
		assert (command.isItString("'hello'"));
		assert (!command.isItString("hello"));
		assert (command.isItString("''hello'"));
		assert (command.isItString("'123'"));
		assert (command.isItString("'^&*)'"));
		assert (command.isItString("' '"));
		assert (!command.isItString("'oh no"));

		assert (command.isItBoolean("true"));
		assert (command.isItBoolean("false"));
		assert (command.isItBoolean("TRUE"));
		assert (!command.isItBoolean("truefalse"));
		assert (!command.isItBoolean("hello"));
		assert (!command.isItBoolean("1"));
		assert (!command.isItBoolean("flase"));

		assert (command.isItInteger("1"));
		assert (command.isItInteger("1000000"));
		assert (command.isItInteger("-1"));
		assert (!command.isItInteger("-"));
		assert (!command.isItInteger("--1"));
		assert (command.isItInteger("0"));
		assert (!command.isItInteger("1.5"));
		assert (!command.isItInteger("one"));
		assert (!command.isItInteger("13567abc"));

		assert (command.isItFloat("1.5680"));
		assert (!command.isItFloat("1.5.4"));
		assert (command.isItFloat("-144.5"));
		//this is not disqualified- may need to keep an eye in case this is a problem
		assert (command.isItFloat("15"));
		assert (!command.isItFloat("--7"));
		assert (!command.isItFloat("ten"));
		assert (command.isItFloat("-.89"));
		assert (command.isItFloat(".89"));
	}
}
