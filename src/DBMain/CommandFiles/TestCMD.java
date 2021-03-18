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
	}

	private void testIsItFinalCommand() {
		assert(command.isThisCommandEnd(null));
		assert(!command.isThisCommandEnd("hello"));
		assert(!command.isThisCommandEnd("123"));
	}

	private void testIsItAlphNumeric(){
		assert(command.isNameAlphNumeric("abc"));
		assert(command.isNameAlphNumeric("ABc"));
		assert(command.isNameAlphNumeric("ABC"));
		assert(command.isNameAlphNumeric("123"));
		assert(!command.isNameAlphNumeric("!"));
		assert(!command.isNameAlphNumeric(" "));
		assert(!command.isNameAlphNumeric("_"));
	}

	private void testIsItSemiColon(){
		assert(command.isThisSemicolon(";"));
		assert(!command.isThisSemicolon(":"));
		assert(!command.isThisSemicolon("this"));
	}
}
