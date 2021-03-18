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
		testDoesTokenExist();
	}

	private void testIsItFinalCommand() {
		assert(command.isThisCommandEnd(null));
		assert(!command.isThisCommandEnd("hello"));
		assert(!command.isThisCommandEnd("123"));
	}

	private void testIsItAlphNumeric(){
		assert(command.isItAlphNumeric("abc"));
		assert(command.isItAlphNumeric("ABc"));
		assert(command.isItAlphNumeric("ABC"));
		assert(command.isItAlphNumeric("123"));
		assert(!command.isItAlphNumeric("!"));
		assert(!command.isItAlphNumeric(" "));
		assert(!command.isItAlphNumeric("_"));
	}

	private void testDoesTokenExist(){
		assert(command.doesTokenExist("hello"));
		assert(command.doesTokenExist("hello world"));
		assert(!command.doesTokenExist(null));
	}
}
