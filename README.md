# 🔐 Huffman Coding

A **Java implementation of the Huffman Coding algorithm**, a classic lossless data compression technique based on variable-length prefix codes.

This project demonstrates the fundamental steps of Huffman coding, including **frequency analysis, priority queues, binary tree construction, code generation, and text encoding**.

The implementation is intentionally focused on the core algorithm rather than building a complete file-compression application.

---

## 📌 Overview

Huffman Coding is a **lossless compression algorithm** that assigns shorter binary codes to more frequent characters and longer codes to less frequent characters.

The main idea is:

```text
More Frequent Character
          ↓
     Shorter Code

Less Frequent Character
          ↓
      Longer Code
```

Unlike fixed-length encoding, where every character receives the same number of bits, Huffman Coding uses the frequency of each character to construct an efficient variable-length code.

For example, instead of representing every character using the same number of bits:

```text
A → 000
B → 001
C → 010
D → 011
```

Huffman Coding may produce codes such as:

```text
A → 0
B → 10
C → 110
D → 111
```

The exact codes depend on the frequency distribution of the input text.

---

# 🎯 Project Objective

The goal of this project is to implement the main stages of the Huffman Coding algorithm from scratch using Java.

The implementation demonstrates:

* Character frequency calculation
* Priority Queue usage
* Huffman Tree construction
* Binary code generation
* Recursive tree traversal
* Text encoding
* Mapping characters to binary codes

---

# 🧠 How Huffman Coding Works

The algorithm can be divided into four major stages:

```text
Input Text
    │
    ▼
Calculate Character Frequencies
    │
    ▼
Build Huffman Tree
    │
    ▼
Generate Binary Codes
    │
    ▼
Encode Input Text
    │
    ▼
Compressed Representation
```

---

# 1️⃣ Frequency Analysis

The first step is counting how frequently each character occurs in the input text.

The implementation uses a Java `HashMap`:

```java
Map<Character, Integer> frequency = new HashMap<>();
```

Each character is processed from the input string:

```java
for (char ch : text.toCharArray()) {
    frequency.put(ch, frequency.getOrDefault(ch, 0) + 1);
}
```

The result is a mapping between characters and their frequencies:

```text
Character → Frequency
```

For example:

```text
A → 10
B → 7
C → 4
D → 2
```

Characters with higher frequencies will eventually receive shorter Huffman codes.

---

# 2️⃣ Creating Huffman Nodes

Each unique character is represented by a `HuffmanNode`.

The node contains:

```java
class HuffmanNode {

    char ch;
    int freq;

    HuffmanNode left;
    HuffmanNode right;
}
```

Each node stores:

* `ch` → character
* `freq` → frequency
* `left` → left child
* `right` → right child

Internal nodes created while constructing the tree use `$` as their character value because they do not represent an actual input character.

---

# 3️⃣ Priority Queue

The implementation uses Java's `PriorityQueue` to repeatedly select the two nodes with the lowest frequencies.

```java
PriorityQueue<HuffmanNode> priorityQueue =
        new PriorityQueue<>(
            (Comparator) new HuffmanNodeComparator()
        );
```

The custom comparator sorts nodes according to their frequency:

```java
class HuffmanNodeComparator implements Comparator<HuffmanNode> {

    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.freq - y.freq;
    }
}
```

Therefore:

```text
Lowest Frequency
       ↓
Highest Priority
```

---

# 4️⃣ Building the Huffman Tree

The core of the algorithm is implemented in:

```java
buildHuffmanTree(String text)
```

The algorithm repeatedly removes the two nodes with the smallest frequencies:

```text
Priority Queue

[A:2] [B:3] [C:5] [D:8]
  │     │
  └──┬──┘
     ▼
  Merge
     │
     ▼
 [AB:5]
```

The two nodes are combined into a new internal node:

```text
merged.freq = left.freq + right.freq
```

The resulting node is then inserted back into the priority queue.

This process continues until only one node remains.

That final node becomes the root of the Huffman Tree.

---

# 🌳 Huffman Tree

A simplified Huffman Tree looks like:

```text
                 (*)
                /   \
              0/     \1
              /       \
            (*)        C
           /   \
         0/     \1
         /       \
        A         B
```

The path from the root to each character determines its binary code.

For example:

```text
A → 00
B → 01
C → 1
```

The exact tree depends on the input frequencies.

---

# 5️⃣ Generating Huffman Codes

Once the tree has been constructed, the application traverses it recursively.

This is implemented in:

```java
buildHuffmanCodes(
    HuffmanNode root,
    String code,
    Map<Character, String> huffmanCodes
)
```

The rules are:

```text
Move Left  → append "0"
Move Right → append "1"
```

For example:

```text
             Root
            /    \
          0/      \1
          A        B
```

produces:

```text
A → 0
B → 1
```

For a deeper tree:

```text
               Root
              /    \
            0/      \1
            /        \
           A         (*)
                    /   \
                  0/     \1
                  C       B
```

the codes become:

```text
A → 0
C → 10
B → 11
```

The implementation detects leaf nodes using:

```java
if (root.left == null && root.right == null)
```

and stores the generated code in the `HashMap`.

---

# 6️⃣ Encoding the Text

After generating the Huffman code table, the original text is encoded using:

```java
huffmanEncoding(
    String text,
    Map<Character, String> huffmanCodes
)
```

For every character:

```java
encodedText.append(huffmanCodes.get(ch));
```

Therefore:

```text
Original Text
      ↓
Character
      ↓
Look up Huffman Code
      ↓
Append Binary Code
```

For example:

```text
Input:

ABAC

Codes:

A → 0
B → 10
C → 11

Encoded:

A B A C
↓ ↓ ↓ ↓
0 10 0 11

= 010011
```

---

# 🔄 Complete Algorithm

The complete implementation follows this process:

```text
                    Input Text
                        │
                        ▼
              ┌──────────────────┐
              │ Frequency Count   │
              └────────┬─────────┘
                       │
                       ▼
              Create Huffman Nodes
                       │
                       ▼
              Insert into PriorityQueue
                       │
                       ▼
              ┌──────────────────┐
              │  Take 2 Minimum  │
              │     Nodes        │
              └────────┬─────────┘
                       │
                       ▼
                    Merge
                       │
                       ▼
               Insert New Node
                       │
                       ▼
              Queue Size == 1 ?
                 │           │
                No          Yes
                 │           │
                 └─────┐     ▼
                       │   Root
                       │     │
                       └─────┤
                             ▼
                    Traverse Huffman Tree
                             │
                             ▼
                     Generate Codes
                             │
                             ▼
                       Encode Text
                             │
                             ▼
                       Binary Output
```

---

# 🧩 Main Components

The implementation consists of three main classes.

## `HuffmanNode`

Represents a node in the Huffman Tree.

```text
HuffmanNode
├── char ch
├── int freq
├── HuffmanNode left
└── HuffmanNode right
```

---

## `HuffmanNodeComparator`

Defines how nodes are ordered inside the priority queue.

Nodes with lower frequency receive higher priority.

```text
Lower Frequency
       ↓
Higher Priority
```

---

## `HuffmanCoding`

Contains the main Huffman algorithm:

```text
buildHuffmanTree()
        │
        ▼
buildHuffmanCodes()
        │
        ▼
huffmanEncoding()
```

The `main()` method creates the sample input, builds the tree, generates codes, encodes the text, and prints the results.

---

# 🧪 Example Input

The current implementation uses a sample string:

```text
shir aliiiiiiiiiiiiii be daaaaaaaaadooooooooooooooooomm beeeeerrrrrrreeeessss koookkkkaaaaaa bbbbbbaaaaalllllllaa abbbshaarrrrrrrr abbbbb ssefidd hhhhhhesssssommmmmm
```

The program processes this text character by character and calculates the frequency of every character.

It then constructs a Huffman Tree based on those frequencies.

---

# 📤 Program Output

The application displays three main pieces of information:

```text
Original Text:
...

Encoded Text:
...

Huffman Codes:
...
```

The output therefore allows the user to observe:

1. The original input
2. The generated binary representation
3. The Huffman code assigned to each character

---

# ⏱️ Complexity

Let:

```text
n = number of characters in the input
k = number of unique characters
```

### Frequency Calculation

The input is traversed once:

```text
O(n)
```

### Building the Huffman Tree

The algorithm performs approximately `k - 1` merge operations using a priority queue:

```text
O(k log k)
```

### Generating Codes

Every node in the Huffman tree is visited:

```text
O(k)
```

### Encoding

The input text is traversed again:

```text
O(n)
```

Therefore, the overall algorithm is dominated by:

```text
O(n + k log k)
```

assuming efficient `HashMap` and `PriorityQueue` operations.

---

# 💡 Why Huffman Coding?

Huffman Coding is a classic example of a **Greedy Algorithm**.

At every step, the algorithm chooses:

```text
Two Least-Frequent Nodes
          ↓
       Merge Them
```

This local decision eventually produces an optimal prefix-code tree for the given character frequencies.

This makes Huffman Coding an important example for studying:

* Greedy algorithms
* Binary trees
* Priority queues
* Data compression
* Recursive tree traversal

---

# 🔐 Prefix-Free Codes

One of the important properties of Huffman Coding is that the generated codes are **prefix-free**.

This means that no character's code is the prefix of another character's code.

For example, this is valid:

```text
A → 0
B → 10
C → 11
```

But this would not be a valid prefix code:

```text
A → 0
B → 01
```

because `0` is already a prefix of `01`.

The tree structure naturally guarantees the prefix-free property.

---

# 📁 Project Structure

The repository is intentionally small and focused on the core implementation:

```text
Hauffman-code/
│
├── src/
│   └── Main.java
│
├── .idea/
│
├── out/
│   └── production/
│       └── HauffmanCode/
│
├── HauffmanCode.iml
│
└── README.md
```

The main implementation is contained in:

```text
src/Main.java
```

The repository currently contains two commits and a single Java source file under `src`.

---

# 🛠️ Technology Stack

| Technology        | Purpose                               |
| ----------------- | ------------------------------------- |
| **Java**          | Programming language                  |
| **HashMap**       | Character-frequency and code mappings |
| **PriorityQueue** | Selecting minimum-frequency nodes     |
| **Comparator**    | Ordering Huffman nodes                |
| **Binary Tree**   | Representing Huffman codes            |
| **Recursion**     | Traversing the Huffman tree           |

---

# 📚 Concepts Demonstrated

This project provides hands-on practice with several important computer science concepts.

### Data Structures

* Binary Trees
* Priority Queues
* Hash Maps
* Tree Nodes

### Algorithms

* Huffman Coding
* Greedy Algorithms
* Frequency Counting
* Recursive Tree Traversal

### Java

* Classes
* Objects
* Constructors
* Maps
* Comparators
* Priority Queues
* Recursion
* `StringBuilder`

---

# ⚠️ Current Scope

This project implements the **encoding side** of Huffman Coding.

It currently:

```text
Text
 ↓
Frequency Analysis
 ↓
Huffman Tree
 ↓
Huffman Codes
 ↓
Encoded Text
```

It does **not currently implement**:

* Binary file compression
* Saving compressed data to a file
* Decoding the encoded text back to the original text
* Persisting the Huffman Tree
* A command-line interface for arbitrary user input
* Compression/decompression of real files

Therefore, this repository should be considered an **algorithm implementation and educational exercise**, rather than a complete file-compression utility.

---

# 🚀 Possible Improvements

The project can be extended significantly.

## 1. Implement Decoding

Add a decoder that can reconstruct the original text from the encoded bit sequence.

```text
Encoded Text
     │
     ▼
Huffman Tree
     │
     ▼
Original Text
```

---

## 2. Support User Input

Instead of using a hard-coded string:

```java
String text = "...";
```

the application could accept text from the console.

---

## 3. Calculate Compression Ratio

The application could compare:

```text
Original Size
       vs
Encoded Size
```

and calculate:

```text
Compression Ratio =
Encoded Size / Original Size
```

---

## 4. File Compression

The next step would be extending the project to process actual files:

```text
Input File
    │
    ▼
Huffman Encoder
    │
    ▼
Compressed File
```

and:

```text
Compressed File
    │
    ▼
Huffman Decoder
    │
    ▼
Original File
```

---

## 5. Improve Project Structure

The current implementation keeps the classes inside `Main.java`.

A larger version could separate them:

```text
src/
├── HuffmanNode.java
├── HuffmanNodeComparator.java
├── HuffmanCoding.java
└── Main.java
```

This would make the code easier to maintain and extend.

---

# 🎯 Learning Objectives

The primary learning objectives of this project were:

* Understanding Huffman Coding
* Implementing a Greedy Algorithm
* Working with Priority Queues
* Building a binary tree dynamically
* Using recursion for tree traversal
* Generating prefix-free binary codes
* Applying frequency analysis to input data
* Encoding text using variable-length codes

---

# 📌 Project Status

**Status:** Educational / Algorithmic Implementation

The project provides a focused implementation of the core **Huffman Coding encoding algorithm** in Java.

It is particularly useful as an example of how **Greedy Algorithms + Priority Queues + Binary Trees** can be combined to solve a classic data-compression problem.

---

# 👨‍💻 Author

**Sobhan Khedry**

Computer Engineering Graduate Student
Backend Development Enthusiast

GitHub: [@Sobhankhedry](https://github.com/Sobhankhedry)

---

# ⭐ Summary

This project implements the fundamental Huffman Coding algorithm:

```text
              Input Text
                   │
                   ▼
          Character Frequencies
                   │
                   ▼
            Priority Queue
                   │
                   ▼
           Huffman Tree
                   │
                   ▼
          Binary Huffman Codes
                   │
                   ▼
           Encoded Text
```

The project demonstrates a practical combination of **Greedy Algorithms, Binary Trees, Priority Queues, Hash Maps, and Recursion** to create an efficient prefix-code representation of text.

It serves as a foundation for further development into a complete **lossless file compression and decompression system**.
