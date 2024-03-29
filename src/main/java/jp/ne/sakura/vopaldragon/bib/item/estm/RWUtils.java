package jp.ne.sakura.vopaldragon.bib.item.estm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

public class RWUtils {

    public static InputStream pathToStream(Path path) throws IOException {
        return new GZIPInputStream(Files.newInputStream(path));
    }

    public static int writeStreamToFile(InputStream input, Path output) throws IOException {
        GZIPOutputStream out = new GZIPOutputStream(Files.newOutputStream(output));
        int size = IOUtils.copy(input, out);
        out.close();
        input.close();
        return size;
    }

    public static DataReader gDataReader(Path gzip) throws IOException {
        return new DataReader(gzip, true);
    }

    public static DataReader dataReader(Path gzip) throws IOException {
        return new DataReader(gzip, false);
    }

    public static Reader gReader(Path gzip) throws IOException {
        return new Reader(gzip, true);
    }

    public static Reader reader(Path gzip) throws IOException {
        return new Reader(gzip, false);
    }

    public static Map<String, String[]> readArrayMap(Path path) throws IOException {
        Map<String, String[]> map = new HashMap<>();
        try (DataReader r = dataReader(path)) {
            for (String[] s : r) {
                map.put(s[0], s);
            }
        }
        return map;
    }

    public static Map<String, String> readMap(Path path) throws IOException {
        Map<String, String> map = new HashMap<>();
        try (DataReader r = dataReader(path)) {
            for (String[] s : r) {
                map.put(s[0], s[1]);
            }
        }
        return map;
    }

    public static Writer writer(Path gzip) throws IOException {
        return new Writer(gzip, false);
    }

    public static Writer gWriter(Path gzip) throws IOException {
        return new Writer(gzip, true);
    }

    public static class Writer implements AutoCloseable {

        private BufferedWriter writer;

        public void close() throws IOException {
            writer.close();
        }

        private Writer(Path gzip, boolean g) throws IOException {
            if (g) writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(Files.newOutputStream(gzip)), StandardCharsets.UTF_8));
            else writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(gzip), StandardCharsets.UTF_8));
        }

        public void writeData(Object[] data) throws IOException {
            writeLine(Arrays.stream(data).map(o -> Objects.toString(o).replaceAll("\t", " ")).collect(Collectors.joining("\t")));
        }

        public void writeDataV(Object... data) throws IOException {
            writeLine(Arrays.stream(data).map(o -> Objects.toString(o).replaceAll("\t", " ")).collect(Collectors.joining("\t")));
        }

        public void writeLine(String line) throws IOException {
            writer.append(line).append("\n");
        }

        public void writeLines(List<String> lines) throws IOException {
            for (String line : lines) {
                writeLine(line);
            }
        }

    }

    public static class DataReader implements Iterator<String[]>, Iterable<String[]>, AutoCloseable {

        private Reader reader;

        DataReader(Path gz, boolean g) throws IOException {
            reader = new Reader(gz, g);
        }

        public boolean hasNext() {
            return reader.hasNext();
        }

        public String[] nextLine() {
            return reader.nextLine().split("\t");
        }

        public void close() throws IOException {
            reader.close();
        }

        @Override
        public Iterator<String[]> iterator() {
            return this;
        }

        @Override
        public String[] next() {
            return nextLine();
        }

        public int getLineCount() {
            return reader.getLineCount();
        }

        public boolean isFirstLine() {
            return reader.isFirstLine();
        }

    }

    public static class Reader implements Iterator<String>, Iterable<String>, AutoCloseable {

        private LineIterator it;
        private int lineCount = 0;

        private Reader(Path path, boolean gzip) throws IOException {
            if (gzip) it = new LineIterator(new BufferedReader(new InputStreamReader(new GZIPInputStream(Files.newInputStream(path)), StandardCharsets.UTF_8)));
            else it = new LineIterator(new BufferedReader(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)));
        }

        public boolean hasNext() {
            return it.hasNext();
        }

        public String nextLine() {
            lineCount++;
            return it.nextLine();
        }

        public void close() throws IOException {
            it.close();
        }

        @Override
        public Iterator<String> iterator() {
            return this;
        }

        @Override
        public String next() {
            return nextLine();
        }

        public int getLineCount() {
            return lineCount;
        }

        public boolean isFirstLine() {
            return lineCount <= 1;
        }

    }

}
