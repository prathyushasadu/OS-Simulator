import java.util.*;
import java.io.*;
 /*
    f. The OneAddressInstruction CLASS is used for
       executing the instruction of type one address.
       After executing each instrction the clock is
       updated.
    */


public class OneAddressInstruction {
public static ERROR_HANDLER ERROR = new ERROR_HANDLER();
public static void ONE_ADDRESS_EXECUTION(String OPCODE)
  {
    String one;
    String two;
    String result;
    int check;
    String temp;
    switch (OPCODE)
    {
    case "00001": 
      one = CPU.stack.POP();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.OR(one, two);
      CPU.stack.PUSH(result);
      break;
    case "00010": 
      one = CPU.stack.POP();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.AND(one, two);
      CPU.stack.PUSH(result);
      break;
    case "00100": 
      one = CPU.stack.POP();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.XOR(one, two);
      CPU.stack.PUSH(result);
      break;
    case "00101": 
      one = CPU.stack.POP();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.ADD(one, two);
      CPU.stack.PUSH(result);
      break;
    case "00110": 
      one = CPU.stack.POP();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.SUB(one, two);
      CPU.stack.PUSH(result);
      break;
    case "00111": 
      one = CPU.stack.POP();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.MUL(one, two);
      CPU.stack.PUSH(result);
      break;
    case "01000": 
      one = CPU.stack.POP();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.DIV(one, two);
      CPU.stack.PUSH(result);
      break;
    case "01001": 
      one = CPU.stack.POP();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.MOD(one, two);
      CPU.stack.PUSH(result);
      break;
    case "01100": 
      one = CPU.stack.PEEK();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.CPG(one, two);
      CPU.stack.PUSH(result);
      break;
    case "01101": 
      one = CPU.stack.PEEK();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.CPL(one, two);
      CPU.stack.PUSH(result);
      break;
    case "01110": 
      one = CPU.stack.PEEK();
      two = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      result = OPERATIONS.CPE(one, two);
      CPU.stack.PUSH(result);
      break;
    case "01111": 
      SYSTEM.PC = SYSTEM.EA;
      break;
    case "10000": 
      one = CPU.stack.POP();
      check = Integer.parseInt(one, 2);
      if (check == 0) {
        SYSTEM.PC = SYSTEM.EA;
      }
      break;
    case "10001": 
      one = CPU.stack.POP();
      check = Integer.parseInt(one, 2);
      if (check == 1) {
        SYSTEM.PC = SYSTEM.EA;
      }
      break;
    case "10010": 
      CPU.stack.PUSH(SYSTEM.PC);
      SYSTEM.PC = SYSTEM.EA;
      break;
    case "10110": 
      temp = MEMORY.MEMORY("READ", SYSTEM.EA, null);
      CPU.stack.PUSH(temp);
      break;
    case "10111": 
      temp = CPU.stack.POP();
      MEMORY.MEMORY("WRITE", SYSTEM.EA, temp);
      break;
    default: 
      ERROR.ERROR_HANDLER(10, SYSTEM.CURRENT_JOB);
    }
  }
}
