import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Championship {

	public static void main(String[] args) {
		//Namn p� inmatningsf�lten
		String[] fieldData = { "Namn","Klubb","Resultat" };
		//Type av f�lt: 0 = Str�ng, 1 = integer
		byte[] fieldType = { 0,0,1 };

        // menu text	
		String menuText = "\n1. L�gg till ny deltagare";
		menuText += "\n2. Visa alla deltagare";
		menuText += "\n3. Visa alla deltagare ifr�n en viss klubb";
		menuText += "\n4. Tag bort deltagare";
		menuText += "\n5. Visa b�sta resultat(finns ej)";
		menuText += "\n6. Resultatlista";
		menuText += "\n0. Avsluta";
		menuText += "\nDitt val: ";
		//menu valen
	    String[] choices = { "1", "2", "3", "4", "5", "6","0" };		
		
		String choice = "1";

		while(choice != null)
		{
			choice = menu(menuText, choices);
			
			if (choice.equals("1")) { 
				addNewData("textfil.txt", fieldData, fieldType);
			}
			else if (choice.equals("2")) {
				showAllData("textfil.txt", fieldData);
			}
			else if (choice.equals("3")) {				
				showSomeData("textfil.txt");
			}
			else if (choice.equals("4")) {
				removeData(fieldData);
			}
			else if (choice.equals("5")) {
//				showBestResult("textfil.txt", fieldData);
			}
			else if (choice.equals("6")) {
				showResult("textfil.txt", fieldData);
			}			
			else if (choice.equals("0")) {
				JOptionPane.showMessageDialog( null,"Du valde att avsluta!", "Programmet avslutas", JOptionPane.INFORMATION_MESSAGE);
				break;
			}
		}

	}

	//Skriver ut menyn och hanterar anv�ndarens inmatning
	public static String menu(String menuText, String[] choices) {
		//Skriver ut en dialogbox och l�ter anv�ndaren v�lja ett mynt				
	    String input = (String) JOptionPane.showInputDialog(null, menuText,
	        "Register(program) tex Boulet�vling", JOptionPane.DEFAULT_OPTION, null, 

	        choices, // Valet ifr�n anv�ndaren
	        choices[1]); // Det f�rsta valet

	    //Om anv�ndaren trycker p� avbryt(cancel) s� anslutas programmet
	    if (input == null) {
	    	//Om anv�ndaren trycker p� avsluta knappen s� avslutas program.
			JOptionPane.showMessageDialog( null,"Du valde att avsluta!", "Programmet avslutas", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
	  //returnerar v�rde		
		return input;
	}

/**
 * 	
 * @param filepath
 * @param dataField
 * @param dataType
 */
	
	public static void addNewData(String filepath, String[] dataField, byte[] dataType) {

		// str�ng f�r v�rdet ifr�n dialogboxen
		String strDialogResponse;
		// Skapar en tempor�r array f�r att lagra information ifr�n anv�ndaren
		String tmpStore[] = new String[dataField.length];
		// L�ter anv�ndaren skriva in v�rden
		for (int i = 0; i < dataField.length; i++) {

			// Skriver ut en dialog och spara det inmatande v�rdet i en array
			strDialogResponse = JOptionPane.showInputDialog(dataField[i] + ": ");
			tmpStore[i] = strDialogResponse;
			// Om anv�ndaren trycker p� avbryt(cancel) g� ur metoden.
			if (strDialogResponse == null) {
				return;
			}

		}
		// kollar s� att inte anv�ndaren har matat in bokst�ver i det inmatade v�rdet.
		for (int j = 0; j < dataType.length; j++) {
			if (dataType[j] == 1) {
				if (!tmpStore[j].matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Resultatet f�r bara inneh�lla heltalssiffror.", "Resultat listan",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
		}

		try {
			// Skriver det inmatade datat till en textfil
			for (int i = 0; i < dataField.length; i++) {
				writeToFile(filepath, true, tmpStore[i]);
			}
		} catch (IOException e) {
			e.getMessage();
		}
	}
		
/**
 * showAllData - �ppnar en textfil och skriver ut all text som finns i text filen i en dialogbox 
 * @param file_path
 * @param dataField
 */
	public static void showAllData(String file_path, String[] dataField) {

		int theNewLine = 0;
		int dataFieldLength = dataField.length;
		String showText = "";

		try {
			String[] text = openFile(file_path);

			for (int i = 0; i < text.length; i++) {

				// Unders�ker hur m�nga f�lt det finns och om det n�r slutet
				// b�rjar programmet skriva ut en ny rad.
				if (theNewLine == dataFieldLength - 1) {
					showText += text[i] + "\n";
					theNewLine = 0;
				} else {
					showText += text[i] + " ";
					theNewLine++;
				}

			}
			//Skriver ut allt
			JOptionPane.showMessageDialog( null, showText, "Listan", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println();
	}
/** showSomeData = skriver ut deltagare ifr�m en viss klubb
 * 
 * @param file_path
 */
	
	public static void showSomeData(String file_path) {

		String showText = "";
		// Skriver ut en dialogbox som fr�gar efter klubbnamn.
		//String strDialogResponse = JOptionPane.showInputDialog(null,"Ange klubbnamn: ");
		JFrame frame = new JFrame("Deltagare ifr�n en viss klubb");
		String strDialogResponse = JOptionPane.showInputDialog(
		        frame, 
		        "klubbnamn: ", 
		        "Deltagare ifr�n en viss klubb", 
		        JOptionPane.WARNING_MESSAGE
		    );
		
	    //Om anv�ndaren trycker p� avbryt(cancel) g� ur metoden.
	    if (strDialogResponse == null) {
			return;
		}
				
		try {
			String[] text = openFile(file_path);

			for (int i = 0; i < text.length; i++) {

				// Unders�ker s� att text[i] inneh�ller samma typer av bokst�ver
				// som "team"
				if (text[i].equalsIgnoreCase(strDialogResponse)) {
					// Unders�ker s� att inte en klubb har samma namn som namnet
					// f�r d� skulle resultat komma upp ist�llet f�r namnet.
					if (!text[i - 1].matches("[0-9]+")) {
						showText += text[i - 1] + " kommer ifr�n klubben ";
						showText += text[i] + "\n";
					}
				}
			}
			//Skriver ut deltagare ifr�n en viss klubb.
			JOptionPane.showMessageDialog( null, showText, "Deltagare ifr�n en viss klubb", JOptionPane.INFORMATION_MESSAGE);
						
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println();
	}
		
/** showResult = Skriver ut resultat lista,�verst �r b�sta resultatet och l�ngst ner �r s�msta resultatet.
 * 	
 * @param file_path
 * @param dataField
 */
	
	public static void showResult(String file_path, String[] dataField) {

		int dataFieldLength = dataField.length;
		int temp = 0;
		String strTemp;
		boolean fixed = false;

		try {
			String[] text = openFile(file_path);

			while (fixed == false) {
				fixed = true;
				for (int row = 0; row < text.length - 1; row++) {

					try {

						for (int col = 0; col < dataFieldLength; col++) {
							//Unders�ker om v�rdet i text[i] �r st�rre �n n�sta post dvs. text[i + dataFieldLength]
							if (Integer.parseInt(text[row]) > Integer.parseInt(text[row + dataFieldLength])) {
								temp = Integer.parseInt(text[row + dataFieldLength]);
								text[row + dataFieldLength] = text[row];
								text[row] = Integer.toString(temp);

								//Om v�rdet �r sant s� skriv ut restan av posterna som ligger f�re i f�ltet.
								for(int colLeft = 1; colLeft < dataFieldLength; colLeft++) {
									strTemp = text[row + dataFieldLength - colLeft];
									text[row + dataFieldLength - colLeft] = text[row - colLeft];
									text[row - colLeft] = strTemp;								
								}	
									
								fixed = false;
							}
						}
						row += dataFieldLength - 1;

					} catch (NumberFormatException e) {

					}
				}

			}
						
			//konvertera array text[] till showText f�r att kunna visa det i dialog-boxen.
			String showText = "";
			int count = 1;
				for (int i = text.length - 1; i >= 0; i--) {
					
					//Skriver ut nummer
					showText += count + ". ";
					count++;
					
					for (int j = dataFieldLength - 1; j >= 0; j--) {

						showText += text[i - j] + " ";						
					}
					showText += "\n";
					//flyttar fram variabeln 'i' till n�sta f�lt s� att for loopen l�sar av n�sta plats r�tt.
					i -= dataFieldLength - 1;					
				}
			
			JOptionPane.showMessageDialog(null, showText, "Resultat listan", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

/** removeData = tar bort deltagare ifr�n text-filen
 * 	
 * @param dataField
 */
	
	public static void removeData(String[] dataField) {
		int doWrite = 0;

		// Skriver ut en dialogbox som fr�gar efter namnet som ska tas bort.
		JFrame frame = new JFrame("Ta bort deltagare.");
		String strDialogResponse = JOptionPane.showInputDialog(
		        frame, 
		        "Ange namn som ska tas bort.", 
		        "Ta bort namn i listan", 
		        JOptionPane.WARNING_MESSAGE
		    );

	    //Om anv�ndaren trycker p� avbryt(cancel) g� ur metoden.
	    if (strDialogResponse == null) {
			return;
		}

		String name = strDialogResponse;		
		
		doWrite = createSTmp(name, dataField, "textfil.txt", "stmp.txt");
		// Om den har hittat namnet
		if (doWrite == 1) {
			doWrite = 0;
			createETmp(name, dataField, "textfil.txt", "etmp.txt");

			delFile("textfil.txt");

			// Skriver tillbaka

			copyFile("stmp.txt","textfil.txt");
			copyFile("etmp.txt","textfil.txt");

			delFile("stmp.txt");
			delFile("etmp.txt");
		}
	}

	public static int createSTmp(String name,  String[] dataField, String sFile, String dFile) {

		// en sk. pek variabel som kontrollerar om det g�r att skriva file
		int doWrite = 0;
		// Lagrar index positionen n�r den har hittat "namn"
		int tmpFindPos = 0;

		//antalet poster i dataField
		int dataFieldLength = dataField.length - 1;
				
		try {
			String[] text = openFile(sFile);

			// Hittar var namnet matchar i filen
			for (int i = 0; i < text.length; i++) {

				if (text[i].equalsIgnoreCase(name)) {

					doWrite = 1;
					tmpFindPos = i;
					System.out.println("*Fann " + text[i]);
					break;

				}
				i = i + dataFieldLength;//antalet poster som hoppar till n�sta post i dataField;
			}
			// Om den har hittat namnet s� skriver programmet en starttmpfil
			if (doWrite == 1) {
				// Skriver filen ifr�n b�rjon till namnet
				for (int i = 0; i < tmpFindPos; i++) {

					try {
						writeToFile(dFile, true, text[i]);
					} catch (IOException e) {
						e.getMessage();
					}

				}
			} else {
				System.out.println("Hittade ej n�gon deltagare med namnet: " + name);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return doWrite;
	}

	public static void createETmp(String name,  String[] dataField, String sFile, String dFile) {

		// en sk. pek variabel som kontrollerar om det g�r att skriva file
		int tmpFindPos = 0;
		//antalet poster i dataField
		int dataFieldLength = dataField.length - 1;

		try {
			String[] text = openFile(sFile);

			// Hittar var namnet matchar i filen
			for (int i = 0; i < text.length; i++) {

				if (text[i].equalsIgnoreCase(name)) {

					tmpFindPos = i + dataFieldLength + 1;//hoppar till n�sta post s� att "name" inte finns med i filen.
					break;

				}
				i = i + dataFieldLength;//antalet poster som hopper till n�sta post i dataField;
			}
			// Skriver filen ifr�n den hittade index positionen till name
			for (int i = tmpFindPos; i < text.length; i++) {

				try {
					writeToFile(dFile, true, text[i]);
				} catch (IOException e) {
					e.getMessage();
				}

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public static void copyFile(String filepath, String dFile) {

		try {
			//Skapar en text array och �ppnar textfilen med metoden openFile
			String[] text = openFile(filepath);

			for (int i = 0; i < text.length; i++) {

				try {
					writeToFile(dFile,true,text[i]);
				} catch (IOException e) {
					e.getMessage();
				}

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void delFile(String sFile) {

		try {

			File file = new File(sFile);

			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
		
/**
 * writeToFile - Skriver en ny text-fil eller l�gger till text i en befintlig text-fil.	
 * @param fileName - filnamnet p� textfilen som ska anv�ndas.
 * @param append - true = l�gger till text i en befintlig text-fil, false = skapar en ny text-fil
 * @param text - datat som skrivs in i filen
 * @throws IOException
 */
	
	// N�r man kommer till att skriva en text fil heter metoden i det h�r fallet
	// 'writeFile' och den har parametrarna 'filename' och 'append'. Filename �r
	// sj�lva filnamnet och append �r en sk boolean och om den �r sann,dvs true
	// skriver den ny data efter sista raden i den befintliga text-filen.
	// Anv�nder man false ist�llet skrivs en helt ny fil. En annan inparameter
	// �r text som �r sj�lva datat som ska f�ras in i metoden.

	public static void writeToFile(String fileName, boolean append, String text) throws IOException {

		// deklareras objekt av FileWriter och PrintWriter. FileWriter anv�nder
		// sig av argumenten filename och append.
		FileWriter write = new FileWriter(fileName, append);
		// F�r att man ska kunna f� in text och inte bara bytes m�ste man
		// importera 'PrintWriter'
		// objekt av PrintWriter och som tar in argumentet av objektet ifr�n
		// FileWriter.
		PrintWriter print = new PrintWriter(write);
		// F�r att skriva text i sin text-fl kan man anv�nda sig av printf.
		// F�rst s� m�ste man formatera texten och det g�r man med PROCENT
		// _STRING och PROCENT_ny rad och l�gger till komma med variaben text.
		print.printf("%s" + "%n", text);
		// St�ng sedan print med 'close'.
		print.close();
	}
	
/**
 * openFile - �ppnar en textfil
 * @param fileName - f�r att l�sa in sj�lva text-filen.
 * @return
 * @throws IOException
 */
	// Metoden 'Openfile' anv�nder parametern 'filename' f�r att l�sa in sj�lva
	// text-filen.
	public static String[] openFile(String fileName) throws IOException {
		// Deklarerar FileReader klassen med ett objekt och l�gger till ett
		// string argument p� platsen d�r sj�lva textfilen ligger.
		FileReader readFile = new FileReader(fileName);
		// Sen skapas ett objekt av bufferReader och som tar in argumentet av
		// objektet ifr�n FileReader.
		BufferedReader textReader = new BufferedReader(readFile);
		// deklarerar en int variabel 'numberOfRows' som h�ller koll p� antal
		// rader f�r text filen.
		// F�r att ta reda p� storleken,dvs hur m�nga raden text filen
		// inneh�ller beh�ver man skapa en metod som i det h�r fallet heter
		// 'checkNumberOfLines'. Metoden kommer att returnera ett siffer v�rde.
		int numberOfRows = checkNumberOfLines(fileName);
		// Skapar en Str�ng array d�r storleken eller antal index �r den
		// storleken p� int variabel 'numberOfLines' v�rde.
		String[] textData = new String[numberOfRows];
		// Skapar en for-loop som loopar tills den n�r variabelns v�rde
		// 'numberOfLines-1'. egentligen
		for (int i = 0; i < numberOfRows; i++) {
			// Innuti for-loopen skapas en tilldelning av arrayen textData som
			// f�r ett v�rde ifr�n 'textReader.readerLine' som l�ser en rad i
			// taget ifr�n textfilen.
			textData[i] = textReader.readLine();
		}
		// St�nga bufferedReader
		textReader.close();
		// Metoden 'openfile' returnerar en array som sedan kan bearbetas vidare
		// av dom andra metoderna i programmet. Varje textrad i filen kommer att
		// l�sas in som en enskild String i en array som inneh�ller str�ngar.
		return textData;
	}

	/**
 * checkNumberOfLines - Unders�ker hur m�nga rader textfilen har	
 * @param filename - f�r att l�sa in sj�lva text-filen.
 * @return returnerar numberOfLines v�rde.(antal rader en textfil har)
 * @throws IOException
 */
	// F�r metoden 'checkNumberOfLines'som r�knar antal rader som finns in en
	// text-fil. H�r anv�nds ocks� 'throws' och undantaget IOExceptions.
	// . parametern 'filename' f�r att l�sa in sj�lva text-filen.

	private static int checkNumberOfLines(String filename) throws IOException {
		// den skapar objekt av klasserna FileReader och BufferedRedaer.
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		// En int variabel som sedan returnerar antal rader och en

		int numberOfLines = 0;
		// while loop som avslutas n�r 'readLines' inte hittar n�got mer v�rde.
		while ((br.readLine()) != null) {
			// I while sk. loopen �kar int variabeln 'numberOfLines' med ett
			// varje g�ng.
			numberOfLines++;
		}
		// St�nger ner med close
		br.close();
		// returnerar numberOfLines v�rde.
		return numberOfLines;
	}
	
}
