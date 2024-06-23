import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// کلاس نود درخت هافمن
class HuffmanNode {
    char ch;
    int freq;
    HuffmanNode left;
    HuffmanNode right;

    HuffmanNode(char ch, int freq) {
        this.ch = ch;
        this.freq = freq;
        this.left = null;
        this.right = null;
    }
}

// مقایسه نود‌ها بر اساس فراوانی
class HuffmanNodeComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.freq - y.freq;
    }
}

class HuffmanCoding {

    // for creating hauffman
    public static HuffmanNode buildHuffmanTree(String text) {

        Map<Character, Integer> frequency = new HashMap<>();
        for (char ch : text.toCharArray()) {
            frequency.put(ch, frequency.getOrDefault(ch, 0) + 1);
        }


        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>((Comparator) new HuffmanNodeComparator());
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        //making the tree
        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            int leftF = left.freq;
            int rightF = right.freq;
            int fullFrequency = leftF + rightF;
            HuffmanNode merged = new HuffmanNode('$', fullFrequency);
            merged.left = left;
            merged.right = right;

            priorityQueue.add(merged);
        }

        return priorityQueue.poll();
    }

    // تولید کدهای هافمن
    public static void buildHuffmanCodes(HuffmanNode root, String code, Map<Character, String> huffmanCodes) {
        if (root == null) {
            return;
        }

        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.ch, code);
        }

        buildHuffmanCodes(root.left, code + "0", huffmanCodes);
        buildHuffmanCodes(root.right, code + "1", huffmanCodes);
    }

    // رمزگذاری متن
    public static String huffmanEncoding(String text, Map<Character, String> huffmanCodes) {
        StringBuilder encodedText = new StringBuilder();
        for (char ch : text.toCharArray()) {
            encodedText.append(huffmanCodes.get(ch));
        }
        return encodedText.toString();
    }

    public static void main(String[] args) {
        String text = "shir aliiiiiiiiiiiiii be daaaaaaaaadooooooooooooooooomm beeeeerrrrrrreeeessss koookkkkaaaaaa bbbbbbaaaaalllllllaa abbbshaarrrrrrrr abbbbb ssefidd hhhhhhesssssommmmmm";

        HuffmanNode huffmanTree = buildHuffmanTree(text);
        Map<Character, String> huffmanCodes = new HashMap<>();
        buildHuffmanCodes(huffmanTree, "", huffmanCodes);

        String encodedText = huffmanEncoding(text, huffmanCodes);

        System.out.println("Original Text: " + text);
        System.out.println("Encoded Text: " + encodedText);
        System.out.println("Huffman Codes: " + huffmanCodes);
    }
}