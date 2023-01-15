package Helper;

import java.io.*;
import java.util.ArrayList;

public class ArrayListFile {
    //CONFIG
    private String folderName = "moves/";

    public void writeArrayListToFile(String fileName, ArrayList<Integer> values) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(folderName + fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(values);

        fileOutputStream.close();
    }

    public ArrayList<Integer> ArrayListRead(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(folderName + fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ArrayList<Integer> values = (ArrayList<Integer>) objectInputStream.readObject();
        fileInputStream.close();
        return values;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ArrayListFile tmp = new ArrayListFile();
        ArrayList<Integer> moves = tmp.ArrayListRead("game_2");
        for (Integer move : moves){
            System.out.println(move);
        }
    }
}
