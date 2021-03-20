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
		testIsItSemiColon();
		isItValidValue();
	}

	private void testIsItFinalCommand() {
		assert (command.isThisCommandEnd(null));
		assert (!command.isThisCommandEnd("hello"));
		assert (!command.isThisCommandEnd("123"));
	}

	private void testIsItAlphNumeric() {
		assert (command.isNameAlphNumeric("abc"));
		assert (command.isNameAlphNumeric("ABc"));
		assert (command.isNameAlphNumeric("ABC"));
		assert (command.isNameAlphNumeric("123"));
		assert (!command.isNameAlphNumeric("!"));
		assert (!command.isNameAlphNumeric(" "));
		assert (!command.isNameAlphNumeric("_"));
	}

	private void testIsItSemiColon() {
		assert (command.isThisSemicolon(";"));
		assert (!command.isThisSemicolon(":"));
		assert (!command.isThisSemicolon("this"));
	}

	private void isItValidValue() {
		assert (command.isItStringLiteral("'hello'"));
		assert (!command.isItStringLiteral("hello"));
		assert (command.isItStringLiteral("''hello'"));
		assert (command.isItStringLiteral("'123'"));
		assert (command.isItStringLiteral("'^&*)'"));
		assert (command.isItStringLiteral("' '"));
		assert (!command.isItStringLiteral("'oh no"));

		assert (command.isItBooleanLiteral("true"));
		assert (command.isItBooleanLiteral("false"));
		assert (command.isItBooleanLiteral("TRUE"));
		assert (!command.isItBooleanLiteral("truefalse"));
		assert (!command.isItBooleanLiteral("hello"));
		assert (!command.isItBooleanLiteral("1"));
		assert (!command.isItBooleanLiteral("flase"));

		assert (command.isItIntegerLiteral("1"));
		assert (command.isItIntegerLiteral("1000000"));
		assert (command.isItIntegerLiteral("-1"));
		assert (!command.isItIntegerLiteral("-"));
		assert (!command.isItIntegerLiteral("--1"));
		assert (command.isItIntegerLiteral("0"));
		assert (!command.isItIntegerLiteral("1.5"));
		assert (!command.isItIntegerLiteral("one"));
		assert (!command.isItIntegerLiteral("13567abc"));

		assert (command.isItFloatLiteral("1.5680"));
		assert (!command.isItFloatLiteral("1.5.4"));
		assert (command.isItFloatLiteral("-144.5"));
		//this is not disqualified- may need to keep an eye in case this is a problem
		assert (command.isItFloatLiteral("15"));
		assert (!command.isItFloatLiteral("--7"));
		assert (!command.isItFloatLiteral("ten"));
		assert (command.isItFloatLiteral("-.89"));
		assert (command.isItFloatLiteral(".89"));
	}
}
