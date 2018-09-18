import java.io.*;
import java.util.*;
/*
 * f. The CONVERSIONS class contains methods
 *    for given value conversions.
 *    The methods helps to convert the values
 *    from hex to binary, hex to Decimal,
 *    Decimal to binary and binary to HEX
 */

public class CONVERSIONS
{

  public static ERROR_HANDLER ERROR = new ERROR_HANDLER();
  
  public static String HEX_TO_BINARY(String HEX)
  {
    String BINARY = "0";
    try
    {
      int temp = Integer.parseInt(HEX, 16);
      BINARY = Integer.toBinaryString(temp);
    }
    catch (NumberFormatException e)
    {
      ERROR.ERROR_HANDLER(7, SYSTEM.CURRENT_JOB);
    }
    return BINARY;
  }
  
  public static int HEX_TO_DECIMAL(String HEX)
  {
    int DECIMAL = Integer.parseInt(HEX, 16);
    
    return DECIMAL;
  }
  
  public static String DECIMAL_BINARY(int DECIMAL)
  {
    String BINARY = "";
    if (DECIMAL == 0) {
      BINARY = "0";
    }
    while (DECIMAL > 0)
    {
      int temp = DECIMAL % 2;
      BINARY = BINARY + temp;
      DECIMAL /= 2;
    }
    BINARY = new StringBuffer(BINARY).reverse().toString();
    
    return BINARY;
  }
  
  public static int TWOs_TO_DECIMAL(String input)
  {
    int int_input = 0;
    
    boolean one_pos = CHECK.POSITIVE_OR_NOT(input);
    if (one_pos == true)
    {
      int_input = Integer.parseInt(input, 2);
    }
    else if (!one_pos)
    {
      input = TWOs_COMPLEMENT(input);
      int_input = Integer.parseInt(input, 2);
      int_input = -1 * int_input;
    }
    return int_input;
  }
  
  public static String BINARY_TO_HEX(String input)
  {
    if (input == null || input == "") {
      return null;
    }
    int decimal = Integer.parseInt(input, 2);
    String output = Integer.toHexString(decimal);
    while (output.length() < 4) {
      output = "0" + output;
    }
    output = output.toUpperCase();
    return output;
  }
  
  public static String TWOs_COMPLEMENT(String input)
  {
    input = CHECK.OPERAND(input);
    String output = OPERATIONS.NOT(input);
    int temp = Integer.parseInt(output, 2);
    temp += 1;
    output = DECIMAL_BINARY(temp);
    return output;
  }
  
  public static String DECIMAL_TO_TWOs(int DECIMAL)
  {
    String result = "";
    if (DECIMAL < 0)
    {
      DECIMAL = Math.abs(DECIMAL);
      String temp = DECIMAL_BINARY(DECIMAL);
      temp = OPERATIONS.PADDING(temp, 16);
      result = TWOs_COMPLEMENT(temp);
    }
    else if (DECIMAL >= 0)
    {
      result = DECIMAL_BINARY(DECIMAL);
      result = OPERATIONS.PADDING(result, 16);
    }
    return result;
  }
}

