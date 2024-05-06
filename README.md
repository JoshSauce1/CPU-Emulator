# Simple CPU Emulator
This project implements a binary computing system in Java, consisting of classes to represent binary words, perform arithmetic and logical operations, and simulate a simple computing environment!

## Purpose:
 - Understand and compare assembly languages.
 - Explain how circuits are used to construct memory and processors.
 - Discuss performance techniques such as pipelining and out-of-order execution.
 - Compare and contrast different computer designs.
 - Write an assembler.
 - Emulate a simple CPU using a virtual machine.

## Architecture and Design:
The provided text outlines the architecture of the SIA32 processor, including its instruction formats, register usage, opcode composition, and instruction set. Here's a concise summary:

 - Instruction Formats: Four formats include 3 Register (3R), 2 Register (2R), Destination Only, and No Register.
 - Opcode Composition: Combines operation and format; for example, a 3R math operation has opcode 00011.
 - Registers: 32 general-purpose registers (R0 - R31) and two special-purpose registers (SP, PC).
 - Instruction Set: Includes Math, Branch, Call, Push, Load, Store, and Pop/Interrupt operations.
 - Definition Matrix: Specifies meanings of bit patterns for Math and Boolean operations.
 - This architecture enables arithmetic, logical, branching, and memory operations, facilitating various computing tasks.

The architexture docx file can be found here:
![SIA32 CPU Architecture](https://github.com/JoshSauce1/CPU-Emulator/blob/master/SIA32%20(3).pdf)

## Assembler
Handcoding machine code can be rather tedious (trust me I know), so I also made an assembler to read assembly language that converts it to the corresponding machine code. It takes a txt file as input, then using the lexer turns it into tokens, then finally parses it into executable machine code.

Here is an example assembly program and machine code!

Assembly Program:
 - COPY R1 10    
 - COPY R2 20      
 - MATH ADD R3 R1 R2
 - HALT

Machine Code:
 - "00000000000000101000000000100001",
 - "00000000000001010000000001000001",
 - "00000000000010001011100001100011",
 - "00000000000000000000000000000000"

