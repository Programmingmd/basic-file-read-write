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
		//Namn på inmatningsfälten
		String[] fieldData = { "Namn","Klubb","Resultat" };
		//Type av fält: 0 = Sträng, 1 = integer
		byte[] fieldType = { 0,0,1 };

        // menu text	
		String menuText = "\n1. Lägg till ny deltagare";
		menuText += "\n2. Visa alla deltagare";
		menuText += "\n3. Visa alla deltagare ifrån en viss klubb";
		menuText += "\n4. Tag bort deltagare";
		menuText += "\n5. Visa bästa resultat(finns ej)";
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

	//Skriver ut menyn och hanterar användarens inmatning
	public static String menu(String menuText, String[] choices) {
		//Skriver ut en dialogbox och låter användaren välja ett mynt				
	    String input = (String) JOptionPane.showInputDialog(null, menuText,
	        "Register(program) tex Bouletävling", JOptionPane.DEFAULT_OPTION, null, 

	        choices, // Valet ifrån användaren
	        choices[1]); // Det första valet

	    //Om användaren trycker på avbryt(cancel) så anslutas programmet
	    if (input == null) {
	    	//Om användaren trycker på avsluta knappen så avslutas program.
			JOptionPane.showMessageDialog( null,"Du valde att avsluta!", "Programmet avslutas", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
	  //returnerar värde		
		return input;
	}

/**
 * 	
 * @param filepath
 * @param dataField
 * @param dataType
 */
	
	public static void addNewData(String filepath, String[] dataField, byte[] dataType) {

		// sträng för värdet ifrån dialogboxen
		String strDialogResponse;
		// Skapar en temporär array för att lagra information ifrån användaren
		String tmpStore[] = new String[dataField.length];
		// Låter användaren skriva in värden
		for (int i = 0; i < dataField.length; i++) {

			// Skriver ut en dialog och spara det inmatande värdet i en array
			strDialogResponse = JOptionPane.showInputDialog(dataField[i] + ": ");
			tmpStore[i] = strDialogResponse;
			// Om användaren trycker på avbryt(cancel) gå ur metoden.
			if (strDialogResponse == null) {
				return;
			}

		}
		// kollar så att inte användaren har matat in bokstäver i det inmatade värdet.
		for (int j = 0; j < dataType.length; j++) {
			if (dataType[j] == 1) {
				if (!tmpStore[j].matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Resultatet får bara innehålla heltalssiffror.", "Resultat listan",
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
 * showAllData - Öppnar en textfil och skriver ut all text som finns i text filen i en dialogbox 
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

				// Undersöker hur många fält det finns och om det når slutet
				// börjar programmet skriva ut en ny rad.
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
/** showSomeData = skriver ut deltagare ifråm en viss klubb
 * 
 * @param file_path
 */
	
	public static void showSomeData(String file_path) {

		String showText = "";
		// Skriver ut en dialogbox som frågar efter klubbnamn.
		//String strDialogResponse = JOptionPane.showInputDialog(null,"Ange klubbnamn: ");
		JFrame frame = new JFrame("Deltagare ifrån en viss klubb");
		String strDialogResponse = JOptionPane.showInputDialog(
		        frame, 
		        "klubbnamn: ", 
		        "Deltagare ifrån en viss klubb", 
		        JOptionPane.WARNING_MESSAGE
		    );
		
	    //Om användaren trycker på avbryt(cancel) gå ur metoden.
	    if (strDialogResponse == null) {
			return;
		}
				
		try {
			String[] text = openFile(file_path);

			for (int i = 0; i < text.length; i++) {

				// Undersöker så att text[i] innehåller samma typer av bokstäver
				// som "team"
				if (text[i].equalsIgnoreCase(strDialogResponse)) {
					// Undersöker så att inte en klubb har samma namn som namnet
					// för då skulle resultat komma upp istället för namnet.
					if (!text[i - 1].matches("[0-9]+")) {
						showText += text[i - 1] + " kommer ifrån klubben ";
						showText += text[i] + "\n";
					}
				}
			}
			//Skriver ut deltagare ifrån en viss klubb.
			JOptionPane.showMessageDialog( null, showText, "Deltagare ifrån en viss klubb", JOptionPane.INFORMATION_MESSAGE);
						
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println();
	}
		
/** showResult = Skriver ut resultat lista,överst är bästa resultatet och längst ner är sämsta resultatet.
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
							//Undersöker om värdet i text[i] är större än nästa post dvs. text[i + dataFieldLength]
							if (Integer.parseInt(text[row]) > Integer.parseInt(text[row + dataFieldLength])) {
								temp = Integer.parseInt(text[row + dataFieldLength]);
								text[row + dataFieldLength] = text[row];
								text[row] = Integer.toString(temp);

								//Om värdet är sant så skriv ut restan av posterna som ligger före i fältet.
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
						
			//konvertera array text[] till showText för att kunna visa det i dialog-boxen.
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
					//flyttar fram variabeln 'i' till nästa fält så att for loopen läsar av nästa plats rätt.
					i -= dataFieldLength - 1;					
				}
			
			JOptionPane.showMessageDialog(null, showText, "Resultat listan", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

/** removeData = tar bort deltagare ifrån text-filen
 * 	
 * @param dataField
 */
	
	public static void removeData(String[] dataField) {
		int doWrite = 0;

		// Skriver ut en dialogbox som frågar efter namnet som ska tas bort.
		JFrame frame = new JFrame("Ta bort deltagare.");
		String strDialogResponse = JOptionPane.showInputDialog(
		        frame, 
		        "Ange namn som ska tas bort.", 
		        "Ta bort namn i listan", 
		        JOptionPane.WARNING_MESSAGE
		    );

	    //Om användaren trycker på avbryt(cancel) gå ur metoden.
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

		// en sk. pek variabel som kontrollerar om det går att skriva file
		int doWrite = 0;
		// Lagrar index positionen när den har hittat "namn"
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
				i = i + dataFieldLength;//antalet poster som hoppar till nästa post i dataField;
			}
			// Om den har hittat namnet så skriver programmet en starttmpfil
			if (doWrite == 1) {
				// Skriver filen ifrån börjon till namnet
				for (int i = 0; i < tmpFindPos; i++) {

					try {
						writeToFile(dFile, true, text[i]);
					} catch (IOException e) {
						e.getMessage();
					}

				}
			} else {
				System.out.println("Hittade ej någon deltagare med namnet: " + name);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return doWrite;
	}

	public static void createETmp(String name,  String[] dataField, String sFile, String dFile) {

		// en sk. pek variabel som kontrollerar om det går att skriva file
		int tmpFindPos = 0;
		//antalet poster i dataField
		int dataFieldLength = dataField.length - 1;

		try {
			String[] text = openFile(sFile);

			// Hittar var namnet matchar i filen
			for (int i = 0; i < text.length; i++) {

				if (text[i].equalsIgnoreCase(name)) {

					tmpFindPos = i + dataFieldLength + 1;//hoppar till nästa post så att "name" inte finns med i filen.
					break;

				}
				i = i + dataFieldLength;//antalet poster som hopper till nästa post i dataField;
			}
			// Skriver filen ifrån den hittade index positionen till name
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
			//Skapar en text array och Öppnar textfilen med metoden openFile
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
 * writeToFile - Skriver en ny text-fil eller lägger till text i en befintlig text-fil.	
 * @param fileName - filnamnet på textfilen som ska användas.
 * @param append - true = lägger till text i en befintlig text-fil, false = skapar en ny text-fil
 * @param text - datat som skrivs in i filen
 * @throws IOException
 */
	
	// När man kommer till att skriva en text fil heter metoden i det här fallet
	// 'writeFile' och den har parametrarna 'filename' och 'append'. Filename är
	// själva filnamnet och append är en sk boolean och om den är sann,dvs true
	// skriver den ny data efter sista raden i den befintliga text-filen.
	// Använder man false istället skrivs en helt ny fil. En annan inparameter
	// är text som är själva datat som ska föras in i metoden.

	public static void writeToFile(String fileName, boolean append, String text) throws IOException {

		// deklareras objekt av FileWriter och PrintWriter. FileWriter använder
		// sig av argumenten filename och append.
		FileWriter write = new FileWriter(fileName, append);
		// För att man ska kunna få in text och inte bara bytes måste man
		// importera 'PrintWriter'
		// objekt av PrintWriter och som tar in argumentet av objektet ifrån
		// FileWriter.
		PrintWriter print = new PrintWriter(write);
		// För att skriva text i sin text-fl kan man använda sig av printf.
		// Först så måste man formatera texten och det gör man med PROCENT
		// _STRING och PROCENT_ny rad och lägger till komma med variaben text.
		print.printf("%s" + "%n", text);
		// Stäng sedan print med 'close'.
		print.close();
	}
	
/**
 * openFile - öppnar en textfil
 * @param fileName - för att läsa in själva text-filen.
 * @return
 * @throws IOException
 */
	// Metoden 'Openfile' använder parametern 'filename' för att läsa in själva
	// text-filen.
	public static String[] openFile(String fileName) throws IOException {
		// Deklarerar FileReader klassen med ett objekt och lägger till ett
		// string argument på platsen där själva textfilen ligger.
		FileReader readFile = new FileReader(fileName);
		// Sen skapas ett objekt av bufferReader och som tar in argumentet av
		// objektet ifrån FileReader.
		BufferedReader textReader = new BufferedReader(readFile);
		// deklarerar en int variabel 'numberOfRows' som håller koll på antal
		// rader för text filen.
		// För att ta reda på storleken,dvs hur många raden text filen
		// innehåller behöver man skapa en metod som i det här fallet heter
		// 'checkNumberOfLines'. Metoden kommer att returnera ett siffer värde.
		int numberOfRows = checkNumberOfLines(fileName);
		// Skapar en Sträng array där storleken eller antal index är den
		// storleken på int variabel 'numberOfLines' värde.
		String[] textData = new String[numberOfRows];
		// Skapar en for-loop som loopar tills den når variabelns värde
		// 'numberOfLines-1'. egentligen
		for (int i = 0; i < numberOfRows; i++) {
			// Innuti for-loopen skapas en tilldelning av arrayen textData som
			// får ett värde ifrån 'textReader.readerLine' som läser en rad i
			// taget ifrån textfilen.
			textData[i] = textReader.readLine();
		}
		// Stänga bufferedReader
		textReader.close();
		// Metoden 'openfile' returnerar en array som sedan kan bearbetas vidare
		// av dom andra metoderna i programmet. Varje textrad i filen kommer att
		// läsas in som en enskild String i en array som innehåller strängar.
		return textData;
	}

	/**
 * checkNumberOfLines - Undersöker hur många rader textfilen har	
 * @param filename - för att läsa in själva text-filen.
 * @return returnerar numberOfLines värde.(antal rader en textfil har)
 * @throws IOException
 */
	// För metoden 'checkNumberOfLines'som räknar antal rader som finns in en
	// text-fil. Här används också 'throws' och undantaget IOExceptions.
	// . parametern 'filename' för att läsa in själva text-filen.

	private static int checkNumberOfLines(String filename) throws IOException {
		// den skapar objekt av klasserna FileReader och BufferedRedaer.
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		// En int variabel som sedan returnerar antal rader och en

		int numberOfLines = 0;
		// while loop som avslutas när 'readLines' inte hittar något mer värde.
		while ((br.readLine()) != null) {
			// I while sk. loopen ökar int variabeln 'numberOfLines' med ett
			// varje gång.
			numberOfLines++;
		}
		// Stänger ner med close
		br.close();
		// returnerar numberOfLines värde.
		return numberOfLines;
	}
	
}
