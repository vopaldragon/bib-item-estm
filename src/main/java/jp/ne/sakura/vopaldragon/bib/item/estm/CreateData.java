package jp.ne.sakura.vopaldragon.bib.item.estm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CreateData {
//[URL, タイトル, 巻次, シリーズ, 版表示, 著者, 出版者, 出版年, ISBN, 冊数（ページ数・大きさ）, NDC分類, NDC分類（第８版）, NDC分類（第９版）, NDLC分類, 件名（NDLSH）, 公開範囲]

    public static void main(String[] args) throws IOException {

        Map<String, Integer> map = new HashMap<>();
        map.put("title", 1);
        map.put("volume", 2);
        map.put("series", 3);
        map.put("edition", 4);
        map.put("author", 5);
        map.put("publisher", 6);
        map.put("year", 7);

        Map<String, RWUtils.Writer> wmap = new HashMap<>();
        for (String s : map.keySet()) {
            wmap.put(s, RWUtils.writer(Paths.get("test_" + s + ".txt")));
            wmap.put(s + "_p", RWUtils.writer(Paths.get("predict_" + s + ".txt")));
        }

        try (RWUtils.Writer train = RWUtils.writer(Paths.get("train.txt"))) {
            for (File f : new File("data").listFiles()) {
                System.out.println(f);
                boolean first = true;
                try (RWUtils.DataReader reader = RWUtils.dataReader(f.toPath())) {
                    for (String[] data : reader) {
                        if (first) {
                            first = false;
                            continue;
                        }
                        for (String key : map.keySet()) {
                            int index = map.get(key);
                            if (!data[index].isEmpty()) {
                                String tokens = String.join(" ", Tokenize.tokenize(data[index], key));
                                if (Math.random() < 0.1) {
                                    wmap.get(key).writeLine("__label__" + key + " " + tokens);
                                    wmap.get(key + "_p").writeLine(tokens);
                                } else {
                                    train.writeLine("__label__" + key + " " + String.join(" ", Tokenize.tokenize(data[index], key)));
                                }
                            }
                        }
                    }
                }
            }
        }
        wmap.forEach((k, w) -> {
            try {
                w.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Tokenize.words.print();

    }
}
