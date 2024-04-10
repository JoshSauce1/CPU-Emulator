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

The architexture docx file can be found below:
![Architecture docx](https://github.com/JoshSauce1/CPU-Emulator/blob/master/SIA32.docx)
