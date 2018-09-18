import java.util.*;
/*
 * f. Checks the given values are valid or not.
 */
class CHECK
{
  public static ERROR_HANDLER ERROR = new ERROR_HANDLER();
  
  public static boolean INTEGER_OR_NOT(String input)
  {
    try
    {
      Integer.parseInt(input);
    }
    catch (NumberFormatException e)
    {
      return false;
    }
    return true;
  }
  
  public static boolean BINARY_OR_NOT(String input)
  {
    boolean result = true;
    for (int i = 0; i < input.length(); i++)
    {
      char character = input.charAt(i);
      if ((character != '1') || (character != '0'))
      {
        result = false;
        break;
      }
    }
    return result;
  }
  
  public static void INPUT_RULES(int number)
  {
    int HIGH_RANGE = (int)Math.pow(2.0D, 15.0D) - 1;
    int LOW_RANGE = (int)Math.pow(-2.0D, 15.0D);
    if ((number > HIGH_RANGE) || (number < LOW_RANGE)) {
      ERROR.ERROR_HANDLER(3, SYSTEM.CURRENT_JOB);
    }
  }
  
  public static String DATA_RULES(String input)
  {
    int temp = CONVERSIONS.TWOs_TO_DECIMAL(input);
    INPUT_RULES(temp);
    while (input.length() > 16) {
      input = input.substring(1);
    }
    return input;
  }
  
  public static boolean POSITIVE_OR_NOT(String input)
  {
    char bit = input.charAt(0);
    boolean result = false;
    if (bit == '1') {
      result = false;
    } else if (bit == '0') {
      result = true;
    }
    return result;
  }
  
  public static String OPERAND(String input)
  {
    try
    {
      Integer.parseInt(input, 2);
    }
    catch (NumberFormatException e)
    {
      return "0";
    }
    return input;
  }
  
  public static void INFINITE_LOOP(String result, String value)
  {
    PCB JOB_pcb = SYSTEM.pcbs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    if ((JOB_pcb.stored_result.equals(result)) && (JOB_pcb.stored_value.equals(value)))
    {
      JOB_pcb.int_inf += 1;
    }
    else
    {
      JOB_pcb.stored_result = result;
      JOB_pcb.stored_value = value;
    }
    if (JOB_pcb.int_inf > 5) {
      ERROR.ERROR_HANDLER(11, SYSTEM.CURRENT_JOB);
    }
  }
}

