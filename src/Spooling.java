import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Queue;
/*
 * f. Spooling  class is used for loading the input file
 *    into the disk pages. It divides the segments into pages
 *    and writes them into the DISK.
 *    Before writing into DISK freeFMBV is checked and then the
 *    DISK pages are updated.
 *    Error Handling for loadfile are also handled.
 */

public class Spooling
{
  static String line;
  static boolean Stop = false;
  static int input;
  static int output;
  static int jobs = 0;
  static int lines = 1;
  static boolean ProgramEnd = false;
  static boolean InputEnd = false;
  static int word = 0;
  static int JOB_ID;
  static int ID = 0;
  static int job_page = 0;
  static int page = -1;
  static int actual_input;
  static ERROR_HANDLER ERROR = new ERROR_HANDLER();
  static int code_words;
  
  static void Input_Spooling()
  {
    if (SYSTEM.CHECK_MEMORY_SIZE() < 6) {
      return;
    }
    if (line == null) {
      return;
    }
    while (!Stop)
    {
	    ID += 1;
	    SYSTEM.METER.JOBS_PROCESSED += 1;
	    SYSTEM.createpcb(ID);
	    if (!line.contains("**JOB"))
	    {
		    ERROR.ERROR_HANDLER(12, ID);
		    Ignore();
		    MakeDefault();
	    }
	    else
	    {
		    boolean test = First();
		    if (!test)
		    {
			    ERROR.ERROR_HANDLER(16, ID);
			    Ignore();
			    MakeDefault();
		    }
		    else
		    {
			    Readline();
			    test = Second();
			    if (!test)
			    {
				    ERROR.ERROR_HANDLER(16, ID);
				    Ignore();
				    MakeDefault();
			    }
			    else
			    {
				    while (!ProgramEnd)
				    {
					    Readline();
					    if (("**INPUT".equals(line)) || ("**FIN".equals(line)) || (line.contains("**JOB")))
					    {
						    ProgramEnd = true;
					    }
					    else
					    {
						    test = Program();
						    if (!test) {
							    break;
						    }
					    }
				    }
				    if (!test)
				    {
					    ERROR.ERROR_HANDLER(16, ID);
					    Ignore();
					    MakeDefault();
				    }
				    else
				    {
					    word = 0;
					    lines = 1;
					    if ("**INPUT".equals(line))
					    {
						    while (!InputEnd)
						    {
							    Readline();
							    if(line != null)
							    {
								    if (("**FIN".equals(line)) || (line.contains("**JOB")) || ("**INPUT".equals(line)))
								    {
									    InputEnd = true;
								    }
								    else
								    {
									    test = Input_Lines();
									    if (!test) {
										    break;
									    }
								    }
							    }
							    else
							    {
								 InputEnd = true;
							    }
						    }
						    if (!test)
						    {
							    ERROR.ERROR_HANDLER(16, ID);
							    Ignore();
							    MakeDefault();
							    continue;
						    }
						    Output_Lines();
						    if ("**FIN".equals(line))
						    {
							    Readline();
							    if ((line != null) && 
									    (line.contains("**FIN")))
							    {
								    ERROR.ERROR_HANDLER(18, ID);
								    Readline();
								    MakeDefault();
							    }
						    }
						    else if (line == null || line.contains("**JOB"))
						    {
							    ERROR.ERROR_HANDLER(15, ID);
						    }
						    else if ("**INPUT".equals(line))
						    {
							    ERROR.ERROR_HANDLER(13, ID);
							    while ((!"**FIN".equals(line)) && (!line.contains("**JOB"))) {
								    Readline();
							    }
							    if ("**FIN".equals(line))
							    {
								    Readline();
								    if ((line != null) && 
										    (line.contains("**FIN")))
								    {
									    ERROR.ERROR_HANDLER(18, ID);
									    Readline();
									    MakeDefault();
									    continue;
								    }
							    }
							    MakeDefault();
						    }
					    }
					    else if ("**FIN".equals(line))
					    {
						    ERROR.ERROR_HANDLER(14, ID);
						    Readline();
						    MakeDefault();
						    continue;
					    }
					    if (actual_input != input) {
						    ERROR.ERROR_HANDLER(17, ID);
					    }
					    jobs += 1;
					    MakeDefault();
					    SYSTEM.JOB_QUEUE.add(Integer.valueOf(ID));
				    }
			    }
		    }
	    }
    }
    Stop = false;
  }
  
  static void Readline()
  {
    try
    {
      line = SYSTEM.Reader.readLine();
      if (line != null) {
        line = line.trim();
      }
    }
    catch (IOException ex)
    {
      System.out.println("FILE NOT FOUND");
      System.exit(0);
    }
  }
  
  static void MakeDefault()
  {
    if (line == null) {
      Stop = true;
    }
    if (SYSTEM.CHECK_MEMORY_SIZE() < 6) {
      Stop = true;
    }
    if (jobs >= 5)
    {
      jobs = 0;
      Stop = true;
    }
    ProgramEnd = false;
    InputEnd = false;
    word = 0;
    lines = 1;
    SYSTEM.METER.ADD_LOADER_CODE_SIZES(ID, code_words);
    code_words = 0;
    actual_input = 0;
    
    job_page = 0;
  }
  
  static void Ignore()
  {
    while (!line.equals("**FIN")) {
      Readline();
    }
    if ("**FIN".equals(line)) {
      Readline();
    }
  }
  
  static boolean First()
  {
    String[] Values = line.split("\\s");
    if (Values.length != 3) {
      return false;
    }
    boolean test = Hex_to_Decimal(Values[1]);
    if (!test) {
      return false;
    }
    test = Hex_to_Decimal(Values[2]);
    if (!test) {
      return false;
    }
    input = Integer.parseInt(Values[1], 16);
    output = Integer.parseInt(Values[2], 16);
    
    return true;
  }
  
  static boolean Second()
  {
    String[] Values = line.split("\\s");
    if (Values.length != 5) {
      return false;
    }
    boolean test = Hex_to_Decimal(Values[0]);
    if (!test) {
      return false;
    }
    JOB_ID = Integer.parseInt(Values[0], 16);
    PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(ID));
    pcb.SET_JOB_ID(JOB_ID);
    pcb.INTERNAL_JOB_ID = ID;
    pcb.SET_INPUT_WORDS(input);
    SYSTEM.METER.ADD_GIVEN_INPUT_SIZES(ID, input);
    pcb.SET_OUTPUT_WORDS(output);
    SYSTEM.METER.ADD_GIVEN_OUTPUT_SIZES(ID, output);
    pcb.BR = Values[1];
    test = Hex_to_Decimal(Values[2]);
    if (!test) {
      return false;
    }
    pcb.SET_PC(Integer.parseInt(Values[2], 16));
    test = Hex_to_Decimal(Values[3]);
    if (!test) {
      return false;
    }
    pcb.SET_JOB_WORDS(Integer.parseInt(Values[3], 16));
    SYSTEM.METER.ADD_GIVEN_CODE_SIZES(ID, pcb.job_words);
    pcb.SET_TRACE(Values[4]);
    if ("1".equals(Values[4]))
    {
      String temp = JOB_ID + "_" + ID + "";
      SYSTEM.createTraceFile(ID, temp);
    }
    return true;
  }
  
  static boolean Program()
  {
    int start = 0;int end = 4;
    int no_of_words = line.length() / 4;
    String[] Values = new String[no_of_words];
    
    int i = 0;
    if (line.length() % 4 != 0) {
      return false;
    }
    while (start < line.length())
    {
      Values[i] = line.substring(start, end);
      start += 4;
      end += 4;
      i++;
    }
    if ((lines > 2) || (lines == 1))
    {
      word = 0;
      lines = 1;
      page = DISK_MANAGER.GET_DISK_PAGE();
      SYSTEM.DISK_FMBV[page] = 1;
      PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(ID));
      pcb.ADD_PROGRAM_DISK_PAGES(job_page, page);
      
      job_page += 1;
    }
    for (int j = 0; j < Values.length; j++)
    {
      boolean test = Hex_to_Binary(Values[j]);
      if (!test) {
        return false;
      }
      int temp = Integer.parseInt(Values[j], 16);
      String value = Integer.toBinaryString(temp);
      value = PADDING(value, 16);
      SYSTEM.DISK[page][word] = value;
      code_words += 1;
      word += 1;
    }
    lines += 1;
    
    return true;
  }
  
  static boolean Input_Lines()
  {
    int c = 0;
    
    String value = "";
    if (line.length() % 4 != 0) {
      return false;
    }
    if ((word > 7) || (word == 0))
    {
      word = 0;
      
      page = DISK_MANAGER.GET_DISK_PAGE();
      SYSTEM.DISK_FMBV[page] = 1;
      PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(ID));
      job_page += 1;
      pcb.ADD_INPUT_PAGES(job_page); 
      pcb.ADD_INPUT_DISK_PAGES(job_page, page);
    }
    while (c < line.length())
    {
      for (int i = 0; i < 4; i++)
      {
        value = value + line.charAt(c);
        c++;
      }
      boolean test = Hex_to_Binary(value);
      if (!test) {
        return false;
      }
      int temp = Integer.parseInt(value, 16);
      value = Integer.toBinaryString(temp);
      value = PADDING(value, 16);
      SYSTEM.DISK[page][word] = value;
      actual_input += 1;
      word += 1;
      value = "";
    }
    return true;
  }
  
  static void Output_Lines()
  {
    int words = 8;
    int pages = output / words;
    for (int i = 0; i <= pages; i++)
    {
      job_page += 1;
      page = DISK_MANAGER.GET_DISK_PAGE();
      SYSTEM.DISK_FMBV[page] = 1;
      PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(ID));
      pcb.ADD_OUTPUT_PAGES(job_page);
      pcb.ADD_OUTPUT_DISK_PAGES(job_page, page);
    }
  }
  
  static String PADDING(String data, int length)
  {
    String result = data;
    while (result.length() < length) {
      result = "0" + result;
    }
    return result;
  }
  
  static boolean Hex_to_Decimal(String hex)
  {
      int decimal;
    try
    {
      decimal = Integer.parseInt(hex, 16);
    }
    catch (Exception e)
    {
      return false;
    }
    return true;
  }
  
  static boolean Hex_to_Binary(String hex)
  {
	  int decimal;
    try
    {
      decimal = Integer.parseInt(hex, 16);
    }
    catch (Exception e)
    {
      return false;
    }
    try
    {
     String binary = Integer.toBinaryString(decimal);
    }
    catch (Exception e)
    {
      return false;
    }
    return true;
  }
}
