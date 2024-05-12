import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateData {
    public static void writeInsertData(int[] randomArray, RedBlackBSTree redBlackBSTree, FileWriter writerInsertData) throws IOException {
        // добавление элементов массива в дерево
        List<Long> time_insert = new ArrayList<>();
        List<Integer> operations_insert = new ArrayList<>();
        for (int j : randomArray) {
            long start = System.nanoTime();
            redBlackBSTree.insertNode(j);
            long finish = System.nanoTime();
            long duration = finish - start;
            time_insert.add(duration);
            int operations = redBlackBSTree.getOperationsCount();
            operations_insert.add(operations);
            String data = String.format("%s %s %s\n", j, duration , operations);
            writerInsertData.write(data);
        }
        double average_time_insert = time_insert.stream().mapToLong(x -> x).average().getAsDouble();
        double average_operations_insert = operations_insert.stream().mapToInt(x -> x).average().getAsDouble();
        String data1 = String.format("%-40s %-40s\n", "average_time_insert: " + average_time_insert, "average_operations_insert: " + average_operations_insert);
        System.out.println(data1);
        writerInsertData.write(data1);
        writerInsertData.close();
    }

    public static void writeSearchData(int[] randomArray, RedBlackBSTree redBlackBSTree, FileWriter writerSearchData) throws IOException {
        // поиск случайных 100 элементов массива в дереве
        List<Long> time_search = new ArrayList<>();
        List<Integer> operations_search = new ArrayList<>();
        for (int i = 0; i < 100; i ++) {
            int index = new Random().nextInt(randomArray.length);
            long start = System.nanoTime();
            redBlackBSTree.searchNode(randomArray[index]);
            long finish = System.nanoTime();
            long duration = finish - start;
            time_search.add(duration);
            int operations = redBlackBSTree.getOperationsCount();
            operations_search.add(operations);
            String data = String.format("%s %s %s\n", randomArray[i], duration , operations);
            writerSearchData.write(data);
        }
        double average_time_search = time_search.stream().mapToLong(x -> x).average().getAsDouble();
        double average_operations_search = operations_search.stream().mapToInt(x -> x).average().getAsDouble();
        String data1 = String.format("%-40s %-40s\n", "average_time_search: " + average_time_search, "average_operations_search: " + average_operations_search);
        System.out.println(data1);
        writerSearchData.write(data1);
        writerSearchData.close();
    }
    public static void writeDeleteData(int[] randomArray, RedBlackBSTree redBlackBSTree, FileWriter writerDeleteData) throws IOException {
        // удаление случайных 1000 элементов массива в дереве
        List<Long> time_delete = new ArrayList<>();
        List<Integer> operations_delete = new ArrayList<>();
        for (int i = 0; i < 1000; i ++) {
            int index = new Random().nextInt(randomArray.length);
            long start = System.nanoTime();
            redBlackBSTree.deleteNode(randomArray[index]);
            long finish = System.nanoTime();
            long duration = finish - start;
            time_delete.add(duration);
            int operations = redBlackBSTree.getOperationsCount();
            operations_delete.add(operations);
            String data = String.format("%s %s %s\n", randomArray[i], duration , operations);
            writerDeleteData.write(data);
        }
        double average_time_delete = time_delete.stream().mapToLong(x -> x).average().getAsDouble();
        double average_operations_delete = operations_delete.stream().mapToInt(x -> x).average().getAsDouble();
        String data1 = String.format("%-40s %-40s\n", "average_time_deletion: " + average_time_delete, "average_operations_deletion: " + average_operations_delete);
        System.out.println(data1);
        writerDeleteData.write(data1);
        writerDeleteData.close();
    }

    public static void generator() throws IOException {

        int[] randomArray = new int[10000];
        Random random = new Random();

        for (int i = 0; i < randomArray.length; i++) {
            randomArray[i] = random.nextInt();
        }

        File searchData = new File("C:/Users/1/IdeaProjects/semestrovka2/src/resultFiles/searchData");
        File insertData = new File("C:/Users/1/IdeaProjects/semestrovka2/src/resultFiles/insertData");
        File deleteData = new File("C:/Users/1/IdeaProjects/semestrovka2/src/resultFiles/deleteData");

        searchData.createNewFile();
        insertData.createNewFile();
        deleteData.createNewFile();

        FileWriter writeSearchData = new FileWriter(searchData);
        FileWriter writeInsertDara = new FileWriter(insertData);
        FileWriter writeDeleteData = new FileWriter(deleteData);

        String headlines = String.format("%s %s %s\n", "Array element", "Time in nanoseconds", "OperationsCount");
        writeSearchData.write(headlines);
        writeInsertDara.write(headlines);
        writeDeleteData.write(headlines);

        RedBlackBSTree redBlackBSTree = new RedBlackBSTree();
        writeInsertData(randomArray, redBlackBSTree, writeInsertDara);
        writeSearchData(randomArray, redBlackBSTree, writeSearchData);
        writeDeleteData(randomArray, redBlackBSTree, writeDeleteData);
    }
}
